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
import nl.han.asd.project.client.commonclient.path.IGetMessagePath;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.client.commonclient.store.IMessageStore;
import nl.han.asd.project.commonservices.encryption.EncryptionModule;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MessageBuilderService implements IMessageBuilder {
    private static final int MINIMAL_HOPS = 3;
    public IGetMessagePath getPath;
    public ISendMessage sendMessage;
    public IMessageStore messageStore;
    private ConnectionService connectionService = null;
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageBuilderService.class);
    public IEncrypt encrypt;
    public CryptographyService cryptographyService;

    @Inject
    public MessageBuilderService(IGetMessagePath getPath, IEncrypt encrypt, ISendMessage sendMessage,
            IMessageStore messageStore) {
        this.getPath = getPath;
        this.encrypt = encrypt;
        this.sendMessage = sendMessage;
        this.messageStore = messageStore;
        final Injector injector = Guice.createInjector(new EncryptionModule());
        cryptographyService = new CryptographyService(injector.getInstance(IEncryptionService.class));
    }

    public void sendMessage(String messageText, Contact contactReceiver, Contact contactSender) {
        //TODO check if contactReceiver contains latest data from master server.
        EncryptedMessage messageToSend = buildMessagePackage(messageText, contactReceiver, contactSender);

        HanRoutingProtocol.MessageWrapper.Builder builder = HanRoutingProtocol.MessageWrapper.newBuilder();

        builder.setEncryptedData(messageToSend.getEncryptedData());

        connectionService = new ConnectionService(messageToSend.getPublicKey());
        try {
            connectionService.open(messageToSend.getIp(),messageToSend.getPort());
            connectionService.write(builder);
        } catch (IOException e) {
            LOGGER.error("Message could not be send due to connection problems.");
        }
    }

    private EncryptedMessage buildMessagePackage(String messageText, Contact contactReceiver, Contact contactSender) {
        List<Node> path = getPath.getPath(MINIMAL_HOPS, contactReceiver);

        Message message = new Message(messageText, contactSender, contactReceiver);
        //TODO kijken of path of die nodes bevat anders gooi exep
        ByteString firstLayer = buildFirstMessagePackageLayer(path.get(0), message);
        path.remove(0);
        return buildLastMessagePackageLayer(path.get(path.size() - 1), buildMessagePackageLayer(firstLayer, path));
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
        builder.setIPaddress(node.getIpAddress());
        builder.setPort(node.getPort());
        builder.setEncryptedData(ByteString.copyFromUtf8(message.getText()));
        return cryptographyService.encryptData(builder.build().toByteString(), node.getPublicKey());
    }

    private EncryptedMessage buildLastMessagePackageLayer(Node node, ByteString data) {
        return new EncryptedMessage(null, node.getIpAddress(), node.getPort(),node.getPublicKey(), data);
    }

    private ByteString buildMessagePackageLayer(ByteString message, List<Node> remainingPath) {
        if (remainingPath.size() == 1) {
            return message;
        }
        HanRoutingProtocol.MessageWrapper.Builder builder = HanRoutingProtocol.MessageWrapper.newBuilder();

        Node node = remainingPath.get(0);
        builder.setIPaddress(node.getIpAddress());
        builder.setPort(node.getPort());
        builder.setEncryptedData(message);

        remainingPath.remove(0);

        ByteString encryptedMessage = cryptographyService
                .encryptData(builder.build().toByteString(), node.getPublicKey());
        return buildMessagePackageLayer(encryptedMessage, remainingPath);
    }
}
