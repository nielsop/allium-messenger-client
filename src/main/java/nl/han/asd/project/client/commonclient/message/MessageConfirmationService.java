package nl.han.asd.project.client.commonclient.message;

import com.google.inject.Inject;
import nl.han.asd.project.client.commonclient.connection.MessageNotSentException;
import nl.han.asd.project.client.commonclient.graph.IUpdateGraph;
import nl.han.asd.project.client.commonclient.node.ISendData;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.client.commonclient.store.IContactManager;
import nl.han.asd.project.client.commonclient.store.IContactStore;
import nl.han.asd.project.client.commonclient.store.NoConnectedNodesException;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class MessageConfirmationService implements IMessageConfirmation {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConfirmationService.class);

    private Map<String, RetryMessage> waitingMessages = new HashMap<>();
    private volatile boolean isRunning = true;
    private IMessageBuilder messageBuilder;
    private IContactStore contactStore;
    private ISendData sendData;
    private IUpdateGraph updateGraph;
    private IContactManager contactManager;

    public static final int TIMEOUT = 5000;

    @Inject
    public MessageConfirmationService(IMessageBuilder messageBuilder, IContactStore contactStore, ISendData sendData,
                                      IUpdateGraph updateGraph, IContactManager contactManager) {
        this.messageBuilder = messageBuilder;
        this.contactStore = contactStore;
        this.sendData = sendData;
        this.updateGraph = updateGraph;
        this.contactManager = contactManager;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    try {
                        checkAllMessages();
                        Thread.sleep(TIMEOUT);
                    } catch (InterruptedException e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                }
            }
        }).start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void messageSent(String id, Message message, Contact contact) {
        waitingMessages.put(id, new RetryMessage(id, message, contact));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void messageConfirmationReceived(String id) {
        waitingMessages.remove(id);
    }

    private void checkAllMessages() {
        for (RetryMessage retryMessage : waitingMessages.values()) {
            if (retryMessage.shouldRetry()) {
                updateGraph.updateGraph();
                contactManager.updateAllContactInformation();

                HanRoutingProtocol.Message.Builder builder = HanRoutingProtocol.Message.newBuilder();
                builder.setId(retryMessage.id);
                builder.setSender(contactStore.getCurrentUser().asContact().getUsername());
                builder.setText(retryMessage.message.getText());
                builder.setTimeSent(System.currentTimeMillis() / 1000L);

                HanRoutingProtocol.MessageWrapper messageWrapper = messageBuilder.buildMessage(builder.build(),
                        contactStore.findContact(retryMessage.contact.getUsername()));

                try {
                    sendData.sendData(messageWrapper);
                } catch (MessageNotSentException e) {
                    LOGGER.error(e.getMessage(), e);
                }

                retryMessage.attemptCount++;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        isRunning = false;
    }
}
