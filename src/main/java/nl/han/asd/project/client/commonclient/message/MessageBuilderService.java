package nl.han.asd.project.client.commonclient.message;

import com.google.protobuf.ByteString;
import nl.han.asd.project.client.commonclient.cryptography.IDecrypt;
import nl.han.asd.project.client.commonclient.cryptography.IEncrypt;
import nl.han.asd.project.client.commonclient.node.ISendMessage;
import nl.han.asd.project.client.commonclient.node.Node;
import nl.han.asd.project.client.commonclient.path.IGetPath;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.client.commonclient.store.IMessageStore;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import javax.inject.Inject;
import java.util.ArrayList;

public class MessageBuilderService implements IMessageBuilder {
    public IGetPath getPath;
    public ISendMessage sendMessage;
    public IMessageStore messageStore;
    public IEncrypt encrypt;
    public IDecrypt decrypt;
    private int MIN_HOPS = 3;
    private IGetPath pathDeterminationService;
    private IEncrypt cryptographyService;

    @Inject
    public MessageBuilderService(IGetPath getPath, ISendMessage sendMessage, IMessageStore messageStore) {
        this.getPath = getPath;
        this.sendMessage = sendMessage;
        this.messageStore = messageStore;
    }


    public MessageBuilderService(IGetPath pathDeterminationService, IEncrypt cryptographyService) {
        this.pathDeterminationService = pathDeterminationService;
        this.cryptographyService = cryptographyService;
    }

    public void sendMessage(String messageText, Contact contactReciever, Contact contactSender) {
        EncryptedMessage messageToSend = buildMessagePackage(messageText, contactReciever, contactSender);

        HanRoutingProtocol.EncryptedMessage.Builder builder = HanRoutingProtocol.EncryptedMessage.newBuilder();

        builder.setEncryptedData(messageToSend.getEncryptedData());
    }

    private EncryptedMessage buildMessagePackage(String messageText, Contact contactReciever, Contact contactSender) {
        ArrayList<Node> path = pathDeterminationService.getPath(MIN_HOPS, contactReciever);

        Message message = new Message(messageText, contactSender, contactReciever);

        ByteString firstLayer = buildFirstMessagePackageLayer(path.get(0), message);
        path.remove(0);
        return buildLastMessagePackageLayer(path.get(path.size()), recursiveEncrypt(firstLayer, path));
    }

    private ByteString buildFirstMessagePackageLayer(Node node, Message message) {
        HanRoutingProtocol.EncryptedMessage.Builder builder = HanRoutingProtocol.EncryptedMessage.newBuilder();

        builder.setUsername(message.getReceiver().getUsername());
        builder.setIPaddress(node.getIP());
        builder.setPort(node.getPort());
        builder.setEncryptedData(ByteString.copyFromUtf8(message.getText()));

        return cryptographyService.encryptData(builder.build().toByteString(), node.getPublicKey());
    }

    private EncryptedMessage buildLastMessagePackageLayer(Node node, ByteString data) {
        return new EncryptedMessage(null, node.getIP(), node.getPort(), data);
    }

    private ByteString recursiveEncrypt(ByteString message, ArrayList<Node> path) {
        if (path.size() == 1) {
            return message;
        }
        HanRoutingProtocol.EncryptedMessage.Builder builder = HanRoutingProtocol.EncryptedMessage.newBuilder();

        Node node = path.get(0);
        builder.setIPaddress(node.getIP());
        builder.setPort(node.getPort());
        builder.setEncryptedData(message);

        path.remove(0);

        ByteString encryptedMessage = cryptographyService.encryptData(builder.build().toByteString(),node.getPublicKey());

        return recursiveEncrypt(encryptedMessage, path);
    }
}
