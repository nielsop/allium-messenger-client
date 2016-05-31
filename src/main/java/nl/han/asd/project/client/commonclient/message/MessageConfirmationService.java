package nl.han.asd.project.client.commonclient.message;

import com.google.inject.Inject;
import nl.han.asd.project.client.commonclient.connection.IConnectionService;
import nl.han.asd.project.client.commonclient.node.ISendData;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.client.commonclient.store.IContactStore;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.util.HashMap;

/**
 * Created by Raoul on 31/5/2016.
 */
public class MessageConfirmationService implements IMessageConfirmation {

    private HashMap<String, RetryMessage> waitingMessages = new HashMap<>();
    private volatile boolean isRunning = true;
    private IMessageBuilder messageBuilder;
    private IContactStore contactStore;
    private ISendData sendData;

    public static final int TIMEOUT = 5000;

    @Inject
    public MessageConfirmationService(IMessageBuilder messageBuilder, IContactStore contactStore, ISendData sendData) {
        this.messageBuilder = messageBuilder;
        this.contactStore = contactStore;
        this.sendData = sendData;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    try {
                        checkAllMessages();
                        Thread.sleep(TIMEOUT);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void messageSent(String id, Message message, Contact contact) {
        waitingMessages.put(id, new RetryMessage(id, message, contact));
    }

    @Override
    public void messageConfirmationReceived(String id) {
        waitingMessages.remove(id);
    }

    private void checkAllMessages() {
        for (RetryMessage retryMessage : waitingMessages.values()) {
            if (retryMessage.shouldRetry()) {
                HanRoutingProtocol.Message.Builder builder = HanRoutingProtocol.Message.newBuilder();
                builder.setId(retryMessage.id);
                builder.setSender(contactStore.getCurrentUser().getCurrentUserAsContact().getUsername());
                builder.setText(retryMessage.message.getText());
                builder.setTimeSent(System.currentTimeMillis() / 1000L);

                HanRoutingProtocol.MessageWrapper messageWrapper = messageBuilder.buildMessage(builder.build(), retryMessage.contact);

                sendData.sendData(messageWrapper);

                retryMessage.attemptCount++;
            }
        }
    }

    public void stop() {
        isRunning = false;
    }
}
