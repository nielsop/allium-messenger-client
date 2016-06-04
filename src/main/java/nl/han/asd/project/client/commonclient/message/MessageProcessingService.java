package nl.han.asd.project.client.commonclient.message;

import com.google.inject.Inject;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import nl.han.asd.project.client.commonclient.connection.MessageNotSentException;
import nl.han.asd.project.client.commonclient.graph.IUpdateGraph;
import nl.han.asd.project.client.commonclient.node.ISendData;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.client.commonclient.store.IContactManager;
import nl.han.asd.project.client.commonclient.store.IContactStore;
import nl.han.asd.project.client.commonclient.store.IMessageStore;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static nl.han.asd.project.protocol.HanRoutingProtocol.*;

/**
 *  Processes messages by decrypting their content and storing them in a MessageStore.
 *
 *  @author Jevgeni Geurtsen
 */
public class MessageProcessingService implements IReceiveMessage, ISendMessage, ISubscribeMessageReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProcessingService.class);

    private final IMessageStore messageStore;
    private final ISendData nodeConnectionService;
    private IMessageConfirmation messageConfirmationService;
    private IContactStore contactStore;
    private IMessageBuilder messageBuilder;
    private IUpdateGraph updateGraph;
    private IContactManager contactManager;
    private final IEncryptionService encryptionService;

    private static List<IMessageReceiver> receivers = new ArrayList<>();

    /**
     * Initialises the class.
     *
     * @param messageStore Instance of IMessageStore.
     * @param encryptionService Instance of IEncryptionService
     */
    @Inject
    public MessageProcessingService(IMessageStore messageStore, IEncryptionService encryptionService,
                                            ISendData nodeConnectionService, IMessageConfirmation messageConfirmationService,
                                            IContactStore contactStore, IMessageBuilder messageBuilder, IUpdateGraph updateGraph,
                                            IContactManager contactManager) {
        this.messageStore = messageStore;
        this.encryptionService = encryptionService;
        this.nodeConnectionService = nodeConnectionService;
        this.messageConfirmationService = messageConfirmationService;
        this.contactStore = contactStore;
        this.messageBuilder = messageBuilder;
        this.updateGraph = updateGraph;
        this.contactManager = contactManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processIncomingMessage(GeneratedMessage messageWrapper) {
        try {
            if (messageWrapper instanceof HanRoutingProtocol.MessageConfirmation) {
                MessageConfirmation messageConfirmation = (MessageConfirmation) messageWrapper;
                messageConfirmationService.messageConfirmationReceived(messageConfirmation.getConfirmationId());

                for (IMessageReceiver receiver : receivers) {
                    if (receiver != null) {
                        receiver.confirmedMessage(messageConfirmation.getConfirmationId());
                    }
                }
            } else if (messageWrapper instanceof HanRoutingProtocol.Message) {
                HanRoutingProtocol.Message message = (HanRoutingProtocol.Message) messageWrapper;
                Message internalMessage = Message.fromProtocolMessage(message, contactStore.getCurrentUser().asContact());
                messageStore.addMessage(internalMessage);

                for (IMessageReceiver receiver : receivers) {
                    if (receiver != null) {
                        receiver.receivedMessage(internalMessage);
                    }
                }

                confirmMessage(message);
            } else {
                throw new InvalidProtocolBufferException(String.format(
                        "Packet didn't contain a MessageConfirmation nor a Message but %s.",
                        messageWrapper));
            }
        } catch (InvalidProtocolBufferException e) {
            LOGGER.error("Error unpacking received message.", e);
        }
    }

    private Wrapper decryptEncryptedWrapper(MessageWrapper encryptedMessageWrapper) throws InvalidProtocolBufferException {
//        byte[] wrapperBuffer = encryptionService
//                .decryptData(encryptedMessageWrapper.getData().toByteArray());
        return Wrapper.parseFrom(encryptedMessageWrapper.getData());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage(Message message, Contact contact) {
        updateGraph.updateGraph();
        contactManager.updateAllContactInformation();

        contact = contactStore.findContact(contact.getUsername());

        HanRoutingProtocol.Message.Builder builder = HanRoutingProtocol.Message.newBuilder();
        builder.setId(generateUniqueMessageId(contact.getUsername()));
        builder.setSender(contactStore.getCurrentUser().asContact().getUsername());
        builder.setText(message.getText());
        builder.setTimeSent(System.currentTimeMillis() / 1000L);

        messageConfirmationService.messageSent(builder.getId(), message, contact);

        MessageWrapper messageWrapper = messageBuilder.buildMessage(builder.build(), contact);

        try {
            nodeConnectionService.sendData(messageWrapper);
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
        Contact sender = contactStore.findContact(message.getSender());

        if (sender == null) {
            contactStore.addContact(message.getSender());
        }

        updateGraph.updateGraph();
        contactManager.updateAllContactInformation();

        sender = contactStore.findContact(message.getSender());

        MessageConfirmation.Builder builder = MessageConfirmation.newBuilder();
        builder.setConfirmationId(message.getId());

        MessageWrapper messageWrapper = messageBuilder.buildMessage(builder.build(), sender);
        try {
            nodeConnectionService.sendData(messageWrapper);
        } catch (MessageNotSentException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void subscribe(IMessageReceiver receiver) {
        receivers.add(receiver);
    }
}
