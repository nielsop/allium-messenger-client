package nl.han.asd.project.client.commonclient.message;

import com.google.inject.Inject;
import com.google.protobuf.InvalidProtocolBufferException;
import nl.han.asd.project.client.commonclient.node.ISendData;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.client.commonclient.store.IContactStore;
import nl.han.asd.project.client.commonclient.store.IMessageStore;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *  Processes messages by decrypting their content and storing them in a MessageStore.
 *
 *  @author Jevgeni Geurtsen
 */
public class MessageProcessingService implements IReceiveMessage, ISendMessage {

    private final IMessageStore messageStore;
    private final ISendData nodeConnectionService;
    private IMessageConfirmation messageConfirmationService;
    private IContactStore contactStore;
    private IMessageBuilder messageBuilder;
    private final IEncryptionService encryptionService;

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProcessingService.class);

    /**
     * Initialises the class.
     *
     * @param messageStore Instance of IMessageStore.
     * @param encryptionService Instance of IEncryptionService
     */
    @Inject public MessageProcessingService(IMessageStore messageStore, IEncryptionService encryptionService,
                                            ISendData nodeConnectionService, IMessageConfirmation messageConfirmationService,
                                            IContactStore contactStore, IMessageBuilder messageBuilder) {
        this.messageStore = messageStore;
        this.encryptionService = encryptionService;
        this.nodeConnectionService = nodeConnectionService;
        this.messageConfirmationService = messageConfirmationService;
        this.contactStore = contactStore;
        this.messageBuilder = messageBuilder;
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

                messageConfirmationService.messageConfirmationReceived(messageConfirmation.getConfirmationId());

            } else if (wrapper.getType()
                    == HanRoutingProtocol.Wrapper.Type.MESSAGE) {

                HanRoutingProtocol.Message message = HanRoutingProtocol.Message.parseFrom(wrapper.getData());
                Message internalMessage = Message.fromProtocolMessage(message, contactStore.getCurrentUserAsContact());
                messageStore.addMessage(internalMessage);

            } else {
                throw new InvalidProtocolBufferException(String.format(
                        "Packet didn't contain a MessageConfirmation nor a Message but %s.",
                        wrapper.getType().name()));
            }
        } catch (InvalidProtocolBufferException e) {
            LOGGER.error("Error unpacking received message.", e);
        }
    }

    private HanRoutingProtocol.Wrapper decryptEncryptedWrapper(HanRoutingProtocol.MessageWrapper encryptedMessageWrapper) throws InvalidProtocolBufferException {
        byte[] wrapperBuffer = encryptionService
                .decryptData(encryptedMessageWrapper.getData().toByteArray());
        return HanRoutingProtocol.Wrapper.parseFrom(wrapperBuffer);
    }

    @Override
    public void sendMessage(Message message, Contact contact) {
        HanRoutingProtocol.Message.Builder builder = HanRoutingProtocol.Message.newBuilder();
        builder.setId(generateUniqueMessageId(contact.getUsername()));
        builder.setSender(contactStore.getCurrentUser().getCurrentUserAsContact().getUsername());
        builder.setText(message.getText());
        builder.setTimeSent(System.currentTimeMillis() / 1000L);

        HanRoutingProtocol.MessageWrapper messageWrapper = messageBuilder.buildMessage(builder.build(), contact);

        nodeConnectionService.sendData(messageWrapper);
        messageConfirmationService.messageSent(builder.getId(), message, contact);
        messageStore.addMessage(message);
    }

    private String generateUniqueMessageId(String seed) {
        String id = seed + String.valueOf(System.currentTimeMillis());

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(id.getBytes());
            byte[] digest = messageDigest.digest();
            id = String.format("%064x", new java.math.BigInteger(1, digest));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return id;
    }
}
