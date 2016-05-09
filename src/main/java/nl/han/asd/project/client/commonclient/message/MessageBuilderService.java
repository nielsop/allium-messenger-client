package nl.han.asd.project.client.commonclient.message;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.protobuf.ByteString;
import nl.han.asd.project.client.commonclient.connection.ConnectionService;
import nl.han.asd.project.client.commonclient.cryptography.CryptographyService;
import nl.han.asd.project.client.commonclient.cryptography.IEncrypt;
import nl.han.asd.project.client.commonclient.graph.Node;
import nl.han.asd.project.client.commonclient.node.ISendMessage;
import nl.han.asd.project.client.commonclient.path.IGetPath;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.client.commonclient.store.IMessageStore;
import nl.han.asd.project.commonservices.encryption.EncryptionModule;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.io.IOException;
import java.util.ArrayList;


public class MessageBuilderService implements IMessageBuilder {
    public IGetPath getPath;
    public ISendMessage sendMessage;
    public IMessageStore messageStore;
    protected ConnectionService connectionService = null;

    public IEncrypt encrypt;
    public CryptographyService cryptographyService;
    private int MIN_HOPS = 3;

    @Inject
    public MessageBuilderService(IGetPath getPath, IEncrypt encrypt, ISendMessage sendMessage, IMessageStore messageStore) {
        this.getPath = getPath;
        this.encrypt = encrypt;
        this.sendMessage = sendMessage;
        this.messageStore = messageStore;
        final Injector injector = Guice.createInjector(new EncryptionModule());
        cryptographyService = new CryptographyService(injector.getInstance(IEncryptionService.class));
    }

    public void sendMessage(String messageText, Contact contactReciever, Contact contactSender) {
        //check if contactReciever contains latest data from master server.
        EncryptedMessage messageToSend = buildMessagePackage(messageText, contactReciever, contactSender);

        HanRoutingProtocol.EncryptedMessage.Builder builder = HanRoutingProtocol.EncryptedMessage.newBuilder();

        builder.setEncryptedData(messageToSend.getEncryptedData());

        connectionService = new ConnectionService(messageToSend.getPublicKey());
        try {
            connectionService.open(messageToSend.getIP(),messageToSend.getPort());
            connectionService.write(builder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private EncryptedMessage buildMessagePackage(String messageText, Contact contactReciever, Contact contactSender) {
        ArrayList<Node> path = getPath.getPath(MIN_HOPS, contactReciever);

        Message message = new Message(messageText, contactSender, contactReciever);
        //kijken of path of die nodes bevat anders gooi exep
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
        HanRoutingProtocol.EncryptedMessage.Builder builder = HanRoutingProtocol.EncryptedMessage.newBuilder();

        builder.setUsername(message.getReceiver().getUsername());
        builder.setIPaddress(node.getIP());
        builder.setPort(node.getPort());
        builder.setEncryptedData(ByteString.copyFromUtf8(message.getText()));
        return cryptographyService.encryptData(builder.build().toByteString(), node.getPublicKey());
    }

    
    private EncryptedMessage buildLastMessagePackageLayer(Node node, ByteString data) {
        return new EncryptedMessage(null, node.getIP(), node.getPort(),node.getPublicKey(), data);
    }

    private ByteString buildMessagePackageLayer(ByteString message, ArrayList<Node> remainingPath){
        if (remainingPath.size() == 1) {
            return message;
        }
        HanRoutingProtocol.EncryptedMessage.Builder builder = HanRoutingProtocol.EncryptedMessage.newBuilder();

        Node node = remainingPath.get(0);
        builder.setIPaddress(node.getIP());
        builder.setPort(node.getPort());
        builder.setEncryptedData(message);

        remainingPath.remove(0);

        ByteString encryptedMessage = cryptographyService.encryptData(builder.build().toByteString(),node.getPublicKey());
        //ByteString encryptedMessage = cryptographyService.encryptData(builder.build().toByteString(),node.getPublicKey());
        return buildMessagePackageLayer(encryptedMessage, remainingPath);
    }
}
