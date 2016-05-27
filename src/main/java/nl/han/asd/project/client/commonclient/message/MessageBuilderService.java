package nl.han.asd.project.client.commonclient.message;

import com.google.inject.Inject;
import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessage;
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


/**
 * @author Julius
 * @version 2.0
 * @since 15/04/1996
 */
public class MessageBuilderService implements IMessageBuilder {
    private static final int MINIMAL_HOPS = 3;

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageBuilderService.class);
    private IGetMessagePath getPath;
    private IEncryptionService encryptionService;
    private ConnectionService connectionService = null;
    private IContactStore contactStore = null;

    @Inject
    public MessageBuilderService(IGetMessagePath getPath, IEncryptionService encryptionService, IContactStore contactStore) {
        this.getPath = getPath;
        this.encryptionService = encryptionService;
        this.contactStore = contactStore;
    }

    public <T extends GeneratedMessage> HanRoutingProtocol.MessageWrapper buildMessage(T generatedMessage , Contact contactReceiver) throws IndexOutOfBoundsException {
        //TODO check if contactReceiver contains latest data from master server.

        HanRoutingProtocol.Wrapper.Builder wrapperBuilder = HanRoutingProtocol.Wrapper.newBuilder();
        wrapperBuilder.setData(generatedMessage.toByteString());
        List<Node> path = getPath.getPath(MINIMAL_HOPS, contactReceiver);

        if(generatedMessage.getClass() == HanRoutingProtocol.Message.class){
            wrapperBuilder.setType(HanRoutingProtocol.Wrapper.Type.MESSAGE);
        }
        else if(generatedMessage.getClass() == HanRoutingProtocol.MessageConfirmation.class){
            wrapperBuilder.setType(HanRoutingProtocol.Wrapper.Type.MESSAGECONFIRMATION);
        }
        else{
            throw new IllegalArgumentException();
        }

        byte[] firstLayer = buildFirstMessagePackageLayer(path.get(0), wrapperBuilder.build(), contactReceiver);

        path.remove(0);

        return buildLastMessagePackageLayer(path.get(path.size() - 1), buildMessagePackageLayer(firstLayer, path));

    }

    /**
     * Deepest layer in final message package
     * @param node contains information about the next hop in the path.
     * @param wrapper contains information about the message that needs to be send.
     * @return encrypted data from the first layer that is build
     */
    private byte[] buildFirstMessagePackageLayer(Node node, HanRoutingProtocol.Wrapper wrapper, Contact contactReceiver) {
        HanRoutingProtocol.MessageWrapper.Builder messageWrapperBuilder = HanRoutingProtocol.MessageWrapper.newBuilder();

        messageWrapperBuilder.setUsername(contactReceiver.getUsername());
        messageWrapperBuilder.setIPaddress(node.getIpAddress());
        messageWrapperBuilder.setPort(node.getPort());
        messageWrapperBuilder.setData(wrapper.toByteString());
        return encryptionService.encryptData(messageWrapperBuilder.build().toByteArray(), node.getPublicKey());
    }


    private HanRoutingProtocol.MessageWrapper buildLastMessagePackageLayer(Node node, byte[] data) {
        HanRoutingProtocol.MessageWrapper.Builder messageWrapperBuilder = HanRoutingProtocol.MessageWrapper.newBuilder();

        messageWrapperBuilder.setIPaddress(node.getIpAddress());
        messageWrapperBuilder.setPort(node.getPort());
        messageWrapperBuilder.setData(ByteString.copyFrom(data));

        return messageWrapperBuilder.build();
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
