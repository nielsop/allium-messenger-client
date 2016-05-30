package nl.han.asd.project.client.commonclient.message;

import com.google.inject.Inject;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import nl.han.asd.project.client.commonclient.node.ISendData;
import nl.han.asd.project.client.commonclient.node.ISendMessage;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.client.commonclient.store.IMessageStore;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  Processes messages by decrypting their content and storing them in a MessageStore.
 *
 *  @author Jevgeni Geurtsen
 */
public class MessageProcessingService implements IReceiveMessage, ISendMessage {

    private final IMessageStore messageStore;
    private final ISendData nodeConnectionService;
    private final IEncryptionService encryptionService;

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProcessingService.class);

    /**
     * Initialises the class.
     *
     * @param messageStore Instance of IMessageStore.
     * @param encryptionService Instance of IEncryptionService
     */
    @Inject public MessageProcessingService(IMessageStore messageStore,
            IEncryptionService encryptionService, ISendData nodeConnectionService) {
        this.messageStore = messageStore;
        this.encryptionService = encryptionService;
        this.nodeConnectionService = nodeConnectionService;
    }

    /**
     * Processes an incoming message.
     *
     * @param messageWrapper An instance of MessageWrapper which should be processed.
     */
    @Override public void processIncomingMessage(HanRoutingProtocol.MessageWrapper messageWrapper) {
        try {
            HanRoutingProtocol.Wrapper wrapper = decryptEncryptedWrapper(
                    messageWrapper);

            if (wrapper.getType()
                    == HanRoutingProtocol.Wrapper.Type.MESSAGECONFIRMATION) {

                HanRoutingProtocol.MessageConfirmation messageConfirmation = HanRoutingProtocol.MessageConfirmation
                        .parseFrom(wrapper.getData());
                messageStore.messageReceived(messageConfirmation.getConfirmationId());
            } else if (wrapper.getType()
                    == HanRoutingProtocol.Wrapper.Type.MESSAGE) {

                HanRoutingProtocol.Message message = HanRoutingProtocol.Message.parseFrom(wrapper.getData());
                messageStore.addMessage(message, messageWrapper.getUsername());

            } else {
                throw new InvalidProtocolBufferException(String.format(
                        "Packet didn't contain a MessageConfirmation nor a Message but %s.",
                        wrapper.getType().name()));
            }

        } catch (InvalidProtocolBufferException e) {
            LOGGER.error("Error unpacking received message.", e);
        }
    }

    @Override
    public void processOutgoingMessage(HanRoutingProtocol.MessageWrapper messageWrapper, Contact contactReceiver) {
        nodeConnectionService.sendData(messageWrapper.toByteArray(), contactReceiver);
    }

    private HanRoutingProtocol.Wrapper decryptEncryptedWrapper(HanRoutingProtocol.MessageWrapper encryptedMessageWrapper) throws InvalidProtocolBufferException {
        byte[] wrapperBuffer = encryptionService
                .decryptData(encryptedMessageWrapper.getData().toByteArray());
        return HanRoutingProtocol.Wrapper.parseFrom(wrapperBuffer);
    }
}
