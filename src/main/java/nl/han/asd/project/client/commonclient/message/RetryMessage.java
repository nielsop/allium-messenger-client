package nl.han.asd.project.client.commonclient.message;

import nl.han.asd.project.client.commonclient.store.Contact;

/**
 * Created by Raoul on 31/5/2016.
 */
public class RetryMessage {
    public String id;
    public Message message;
    public Contact contact;
    public long lastAttempt;
    public int attemptCount = 1;

    public static final int TIMEOUT = 10;

    public RetryMessage(String id, Message message, Contact contact) {
        this.id = id;
        this.message = message;
        this.contact = contact;
        lastAttempt = System.currentTimeMillis() / 1000L;
    }

    public boolean shouldRetry() {
        return (System.currentTimeMillis() / 1000L - lastAttempt) > TIMEOUT * attemptCount * attemptCount;
    }
}
