package nl.han.asd.project.client.commonclient.message;

import com.google.inject.Inject;
import com.google.protobuf.ByteString;
import nl.han.asd.project.client.commonclient.graph.Node;
import nl.han.asd.project.client.commonclient.path.IGetMessagePath;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MessageBuilderService implements IMessageBuilder {
    private static final int MINIMAL_HOPS = 3;
    private IGetMessagePath getPath;
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageBuilderService.class);
    private IEncryptionService encryptionService;

    @Inject
    public MessageBuilderService(IGetMessagePath getPath, IEncryptionService encryptionService) {
        this.getPath = getPath;
        this.encryptionService = encryptionService;
    }

    public void sendMessage(String messageText, Contact contactReceiver, Contact contactSender) {
        //TODO check if contactReceiver contains latest data from master server.
        EncryptedMessage messageToSend = buildMessagePackage(messageText, contactReceiver, contactSender);

        HanRoutingProtocol.MessageWrapper.Builder builder = HanRoutingProtocol.MessageWrapper.newBuilder();

        builder.setData(messageToSend.getEncryptedData());
    }

    private EncryptedMessage buildMessagePackage(String messageText, Contact contactReceiver, Contact contactSender) {
        List<Node> path = getPath.getPath(MINIMAL_HOPS, contactReceiver);

        Message message = new Message(messageText, contactSender, contactReceiver);
        //TODO kijken of path of die nodes bevat anders gooi exep
        byte[] firstLayer = buildFirstMessagePackageLayer(path.get(0), message);
        path.remove(0);
        return buildLastMessagePackageLayer(path.get(path.size() - 1), buildMessagePackageLayer(firstLayer, path));
    }

    /**
     * Deepest layer in final message package
     * @param node contains information about the next hop in path
     * @param message contains information about the message typed by the client
     * @return encrypted data from the first layer that is build
     */
    private byte[] buildFirstMessagePackageLayer(Node node, Message message) {
        HanRoutingProtocol.MessageWrapper.Builder builder = HanRoutingProtocol.MessageWrapper.newBuilder();

        builder.setUsername(message.getReceiver().getUsername());
        builder.setIPaddress(node.getIpAddress());
        builder.setPort(node.getPort());
        builder.setData(ByteString.copyFromUtf8(message.getText()));
        return encryptionService.encryptData(node.getPublicKey(),builder.build().toByteArray());
    }

    private EncryptedMessage buildLastMessagePackageLayer(Node node, byte[] data) {
        return new EncryptedMessage(null, node.getIpAddress(), node.getPort(),node.getPublicKey(),ByteString.copyFrom(data));
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

        byte[] encryptedMessage = encryptionService
                .encryptData(node.getPublicKey(),builder.build().toByteArray());
        return buildMessagePackageLayer(encryptedMessage, remainingPath);
    }
}
