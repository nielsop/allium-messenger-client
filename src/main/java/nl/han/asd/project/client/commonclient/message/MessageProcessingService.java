package nl.han.asd.project.client.commonclient.message;

import com.google.inject.Inject;
import com.google.protobuf.InvalidProtocolBufferException;
import nl.han.asd.project.client.commonclient.connection.MessageNotSentException;
import nl.han.asd.project.client.commonclient.graph.IUpdateGraph;
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

import static nl.han.asd.project.protocol.HanRoutingProtocol.*;

/**
 *  Processes messages by decrypting their content and storing them in a MessageStore.
 *
 *  @author Jevgeni Geurtsen
 */
public class MessageProcessingService implements IReceiveMessage, ISendMessage {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProcessingService.class);

    private final IMessageStore messageStore;
    private final ISendData nodeConnectionService;
    private IMessageConfirmation messageConfirmationService;
    private IContactStore contactStore;
    private IMessageBuilder messageBuilder;
    private IUpdateGraph updateGraph;
    private final IEncryptionService encryptionService;

    /**
     * Initialises the class.
     *
     * @param messageStore Instance of IMessageStore.
     * @param encryptionService Instance of IEncryptionService
     */
    @Inject
    public MessageProcessingService(IMessageStore messageStore, IEncryptionService encryptionService,
                                            ISendData nodeConnectionService, IMessageConfirmation messageConfirmationService,
                                            IContactStore contactStore, IMessageBuilder messageBuilder, IUpdateGraph updateGraph) {
        this.messageStore = messageStore;
        this.encryptionService = encryptionService;
        this.nodeConnectionService = nodeConnectionService;
        this.messageConfirmationService = messageConfirmationService;
        this.contactStore = contactStore;
        this.messageBuilder = messageBuilder;
        this.updateGraph = updateGraph;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processIncomingMessage(MessageWrapper messageWrapper) {
        try {
            Wrapper wrapper = decryptEncryptedWrapper(
                    messageWrapper);

            if (wrapper.getType()
                    == Wrapper.Type.MESSAGECONFIRMATION) {

                MessageConfirmation messageConfirmation = MessageConfirmation
                        .parseFrom(wrapper.getData());

                messageConfirmationService.messageConfirmationReceived(messageConfirmation.getConfirmationId());

            } else if (wrapper.getType()
                    == Wrapper.Type.MESSAGE) {

                HanRoutingProtocol.Message message = HanRoutingProtocol.Message.parseFrom(wrapper.getData());
                Message internalMessage = Message.fromProtocolMessage(message, contactStore.getCurrentUserAsContact());
                messageStore.addMessage(internalMessage);

                confirmMessage(message);
            } else {
                throw new InvalidProtocolBufferException(String.format(
                        "Packet didn't contain a MessageConfirmation nor a Message but %s.",
                        wrapper.getType().name()));
            }
        } catch (InvalidProtocolBufferException e) {
            LOGGER.error("Error unpacking received message.", e);
        }
    }

    private Wrapper decryptEncryptedWrapper(MessageWrapper encryptedMessageWrapper) throws InvalidProtocolBufferException {
        byte[] wrapperBuffer = encryptionService
                .decryptData(encryptedMessageWrapper.getData().toByteArray());
        return Wrapper.parseFrom(wrapperBuffer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage(Message message, Contact contact) {
        updateGraph.updateGraph();
        // TODO: Should update client list first

        HanRoutingProtocol.Message.Builder builder = HanRoutingProtocol.Message.newBuilder();
        builder.setId(generateUniqueMessageId(contact.getUsername()));
        builder.setSender(contactStore.getCurrentUser().getCurrentUserAsContact().getUsername());
        builder.setText(message.getText());
        builder.setTimeSent(System.currentTimeMillis() / 1000L);

        MessageWrapper messageWrapper = messageBuilder.buildMessage(builder.build(), contact);

        try {
            nodeConnectionService.sendData(messageWrapper);
            messageConfirmationService.messageSent(builder.getId(), message, contact);
            messageStore.addMessage(message);
        } catch (MessageNotSentException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private String generateUniqueMessageId(String seed) {
        String id = seed + String.valueOf(System.currentTimeMillis());

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(id.getBytes());
            byte[] digest = messageDigest.digest();
            id = String.format("%064x", new java.math.BigInteger(1, digest));
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return id;
    }

    private void confirmMessage(HanRoutingProtocol.Message message) {
        MessageConfirmation.Builder builder = MessageConfirmation.newBuilder();
        builder.setConfirmationId(message.getId());

        Contact sender = contactStore.findContact(message.getSender());

        MessageWrapper messageWrapper = messageBuilder.buildMessage(builder.build(), sender);
        try {
            nodeConnectionService.sendData(messageWrapper);
        } catch (MessageNotSentException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
