package nl.han.asd.project.client.commonclient.message;

import com.google.inject.Inject;
import com.google.protobuf.ByteString;
import nl.han.asd.project.client.commonclient.cryptography.IDecrypt;
import nl.han.asd.project.client.commonclient.cryptography.IEncrypt;
import nl.han.asd.project.client.commonclient.graph.Node;
import nl.han.asd.project.client.commonclient.node.ISendMessage;
import nl.han.asd.project.client.commonclient.cryptography.CryptographyService;
import nl.han.asd.project.client.commonclient.path.IGetPath;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.client.commonclient.store.IMessageStore;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.util.ArrayList;


public class MessageBuilderService implements IMessageBuilder {
    public IGetPath getPath;
    public ISendMessage sendMessage;
    public IMessageStore messageStore;
	
    public IEncrypt encrypt;
    public IDecrypt decrypt;
    public CryptographyService cryptographyService;
    private int MIN_HOPS = 3;

    @Inject
    public MessageBuilderService(IGetPath getPath, IEncrypt encrypt, ISendMessage sendMessage, IMessageStore messageStore) {
        this.getPath = getPath;
        this.encrypt = encrypt;
        this.sendMessage = sendMessage;
        this.messageStore = messageStore;
    }

	
    /*public MessageBuilderService(CryptographyService cryptographyService) {
        this.cryptographyService = cryptographyService;
    }*/

    public void sendMessage(String messageText, Contact contactReciever, Contact contactSender) {
        EncryptedMessage messageToSend = buildMessagePackage(messageText, contactReciever, contactSender);

        HanRoutingProtocol.MessageWrapper.Builder builder = HanRoutingProtocol.MessageWrapper.newBuilder();

        builder.setEncryptedData(messageToSend.getEncryptedData());

        // verzenden die handel

    }

    private EncryptedMessage buildMessagePackage(String messageText, Contact contactReciever, Contact contactSender) {
        ArrayList<Node> path = getPath.getPath(MIN_HOPS, contactReciever);

        Message message = new Message(messageText, contactSender, contactReciever);

        ByteString firstLayer = buildFirstMessagePackageLayer(path.get(0), message);
        path.remove(0);
        return buildLastMessagePackageLayer(path.get(path.size()-1),buildMessagePackageLayer(firstLayer,path));
    }

    /**
     * Deepest layer in final message package
     * @param node contains information about the next hop in path
     * @param message contains information about the message typed by the client
     * @return encrypted data from the first layer that is build
     */
    private ByteString buildFirstMessagePackageLayer(Node node, Message message) {
        HanRoutingProtocol.MessageWrapper.Builder builder = HanRoutingProtocol.MessageWrapper.newBuilder();

        builder.setUsername(message.getReceiver().getUsername());
        builder.setIPaddress(node.getIP());
        builder.setPort(node.getPort());
        builder.setEncryptedData(ByteString.copyFromUtf8(message.getText()));

        return encrypt.encryptData(builder.build().toByteString(), node.getPublicKey());
       // return cryptographyService.encryptData(builder.build().toByteString(), node.getPublicKey());
    }

    
    private EncryptedMessage buildLastMessagePackageLayer(Node node, ByteString data) {
        return new EncryptedMessage(null, node.getIP(), node.getPort(), data);
    }

    private ByteString buildMessagePackageLayer(ByteString message, ArrayList<Node> remainingPath){
        if (remainingPath.size() == 1) {
            return message;
        }
        HanRoutingProtocol.MessageWrapper.Builder builder = HanRoutingProtocol.MessageWrapper.newBuilder();

        Node node = remainingPath.get(0);
        builder.setIPaddress(node.getIP());
        builder.setPort(node.getPort());
        builder.setEncryptedData(message);

        remainingPath.remove(0);

        ByteString encryptedMessage = encrypt.encryptData(builder.build().toByteString(),node.getPublicKey());
        //ByteString encryptedMessage = cryptographyService.encryptData(builder.build().toByteString(),node.getPublicKey());

        return buildMessagePackageLayer(encryptedMessage, remainingPath);
    }
}
