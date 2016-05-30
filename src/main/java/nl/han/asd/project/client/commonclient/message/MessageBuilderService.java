package nl.han.asd.project.client.commonclient.message;

import com.google.inject.Inject;
import com.google.protobuf.ByteString;
import nl.han.asd.project.client.commonclient.connection.ConnectionService;
import nl.han.asd.project.client.commonclient.graph.Node;
import nl.han.asd.project.client.commonclient.path.IGetMessagePath;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.client.commonclient.store.IContactStore;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class MessageBuilderService implements IMessageBuilder {
    private static final int MINIMAL_HOPS = 3;
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageBuilderService.class);
    private static MessageBuilderService instance = null;
    private IGetMessagePath getPath;
    private IEncryptionService encryptionService;
    private IContactStore contactStore;
    private ConnectionService connectionService;

    @Inject
    public MessageBuilderService(IGetMessagePath getPath, IEncryptionService encryptionService, IContactStore contactStore) {
        this.getPath = getPath;
        this.encryptionService = encryptionService;
        this.contactStore = contactStore;
    }

    private MessageBuilderService() {

    }

    public static MessageBuilderService getInstance() {
        if (instance == null) {
            instance = new MessageBuilderService();
        }
        return instance;
    }

    public void sendMessage(String messageText, Contact contactReceiver, Contact contactSender) {
        //TODO check if contactReceiver contains latest data from master server.
        EncryptedMessage messageToSend = buildMessagePackage(messageText, contactReceiver, contactSender);

        HanRoutingProtocol.MessageWrapper.Builder builder = HanRoutingProtocol.MessageWrapper.newBuilder();

        builder.setData(messageToSend.getEncryptedData());
        connectionService = new ConnectionService(messageToSend.getPublicKey());
        try {
            connectionService.open(messageToSend.getIp(), messageToSend.getPort());
            connectionService.write(builder);
        } catch (IOException e) {
            LOGGER.error("Message could not be send due to connection problems.");
        }
    }

    private EncryptedMessage buildMessagePackage(String messageText, Contact contactReceiver, Contact contactSender) {
        List<Node> path = getPath.getPath(MINIMAL_HOPS, contactReceiver);

        Message message = new Message(contactSender, new Date(), messageText);
        //TODO kijken of path of die nodes bevat anders gooi exep
        byte[] firstLayer = buildFirstMessagePackageLayer(path.get(0), message);
        path.remove(0);
        return buildLastMessagePackageLayer(path.get(path.size() - 1), buildMessagePackageLayer(firstLayer, path));
    }

    /**
     * Deepest layer in final message package
     *
     * @param node    contains information about the next hop in path
     * @param message contains information about the message typed by the client
     * @return encrypted data from the first layer that is build
     */
    private byte[] buildFirstMessagePackageLayer(Node node, Message message) {
        HanRoutingProtocol.MessageWrapper.Builder builder = HanRoutingProtocol.MessageWrapper.newBuilder();

        builder.setUsername(contactStore.getCurrentUser().getCurrentUserAsContact().getUsername());
        builder.setIPaddress(node.getIpAddress());
        builder.setPort(node.getPort());
        builder.setData(ByteString.copyFromUtf8(message.getText()));
        return encryptionService.encryptData(node.getPublicKey(), builder.build().toByteArray());
    }

    private EncryptedMessage buildLastMessagePackageLayer(Node node, byte[] data) {
        return new EncryptedMessage(null, node.getIpAddress(), node.getPort(), node.getPublicKey(), ByteString.copyFrom(data));
    }

    private byte[] buildMessagePackageLayer(byte[] message, List<Node> remainingPath) {
        if (remainingPath.size() == 1) {
            return message;
        }
        HanRoutingProtocol.MessageWrapper.Builder builder = HanRoutingProtocol.MessageWrapper.newBuilder();

        Node node = remainingPath.get(0);
        builder.setIPaddress(node.getIpAddress());
        builder.setPort(node.getPort());
        builder.setData(ByteString.copyFrom(message));

        remainingPath.remove(0);

        byte[] encryptedMessage = encryptionService.encryptData(node.getPublicKey(), builder.build().toByteArray());
        return buildMessagePackageLayer(encryptedMessage, remainingPath);
    }
}
