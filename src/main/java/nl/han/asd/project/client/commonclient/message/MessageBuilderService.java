package nl.han.asd.project.client.commonclient.message;

import com.google.inject.Inject;
import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessage;
import nl.han.asd.project.client.commonclient.cryptography.IEncrypt;
import nl.han.asd.project.client.commonclient.graph.Node;
import nl.han.asd.project.client.commonclient.node.ISendMessage;
import nl.han.asd.project.client.commonclient.path.IGetMessagePath;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.client.commonclient.store.IMessageStore;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * @author Julius
 * @version 2.0
 * @since 15/04/1996
 */
public class MessageBuilderService implements IMessageBuilder {
    private static final int MINIMAL_HOPS = 3;
    private final IGetMessagePath getPath;
    private final ISendMessage sendMessage;
    private final IMessageStore messageStore;
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageBuilderService.class);
    private final IEncrypt encrypt;

    @Inject
    public MessageBuilderService(IGetMessagePath getPath, IEncrypt encrypt, ISendMessage sendMessage,
                                 IMessageStore messageStore) {
        this.getPath = getPath;
        this.encrypt = encrypt;
        this.sendMessage = sendMessage;
        this.messageStore = messageStore;
    }

    public <T extends GeneratedMessage> void sendMessage(T generatedMessage , Contact contactReceiver) throws IndexOutOfBoundsException {
        //TODO check if contactReceiver contains latest data from master server.

        HanRoutingProtocol.Wrapper.Builder wrapperBuilder = HanRoutingProtocol.Wrapper.newBuilder();
        wrapperBuilder.setData(generatedMessage.toByteString());
        ArrayList<Node> path = getPath.getPath(MINIMAL_HOPS, contactReceiver);

        if(generatedMessage.getClass() == HanRoutingProtocol.Message.class){
            wrapperBuilder.setType(HanRoutingProtocol.Wrapper.Type.MESSAGE);
        }
        else if(generatedMessage.getClass() == HanRoutingProtocol.MessageConfirmation.class){
            wrapperBuilder.setType(HanRoutingProtocol.Wrapper.Type.MESSAGECONFIRMATION);
        }
        else{
            throw new IllegalArgumentException();
        }

        ByteString firstLayer = buildFirstMessagePackageLayer(path.get(0), wrapperBuilder.build(), contactReceiver);
        path.remove(0);

        HanRoutingProtocol.MessageWrapper messageToSend = buildLastMessagePackageLayer(path.get(path.size() - 1), buildMessagePackageLayer(firstLayer, path));

        sendMessage.sendMessage(messageToSend,contactReceiver);

    }

    /**
     * Deepest layer in final message package
     * @param node contains information about the next hop in the path.
     * @param wrapper contains information about the message that needs to be send.
     * @return encrypted data from the first layer that is build
     */
    private ByteString buildFirstMessagePackageLayer(Node node, HanRoutingProtocol.Wrapper wrapper, Contact contactReceiver) {
        HanRoutingProtocol.MessageWrapper.Builder messageWrapperBuilder = HanRoutingProtocol.MessageWrapper.newBuilder();

        messageWrapperBuilder.setUsername(contactReceiver.getUsername());
        messageWrapperBuilder.setIPaddress(node.getIpAddress());
        messageWrapperBuilder.setPort(node.getPort());
        messageWrapperBuilder.setEncryptedData(wrapper.toByteString());

        return encrypt.encryptData(messageWrapperBuilder.build().toByteString(), node.getPublicKey());
    }


    private HanRoutingProtocol.MessageWrapper buildLastMessagePackageLayer(Node node, ByteString data) {
        HanRoutingProtocol.MessageWrapper.Builder messageWrapperBuilder = HanRoutingProtocol.MessageWrapper.newBuilder();

        messageWrapperBuilder.setIPaddress(node.getIpAddress());
        messageWrapperBuilder.setPort(node.getPort());
        messageWrapperBuilder.setEncryptedData(data);

        return messageWrapperBuilder.build();
    }

    private ByteString buildMessagePackageLayer(ByteString message, ArrayList<Node> remainingPath) {
        if (remainingPath.size() == 1) {
            return message;
        }
        HanRoutingProtocol.MessageWrapper.Builder builder = HanRoutingProtocol.MessageWrapper.newBuilder();

        Node node = remainingPath.get(0);
        builder.setIPaddress(node.getIpAddress());
        builder.setPort(node.getPort());
        builder.setEncryptedData(message);

        remainingPath.remove(0);

        ByteString encryptedMessage = encrypt
                .encryptData(builder.build().toByteString(), node.getPublicKey());
        return buildMessagePackageLayer(encryptedMessage, remainingPath);
    }
}
