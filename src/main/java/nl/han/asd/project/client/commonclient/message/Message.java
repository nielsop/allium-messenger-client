package nl.han.asd.project.client.commonclient.message;

import nl.han.asd.project.client.commonclient.store.Contact;

/**
 * Created by Marius on 25-04-16.
 */
public class Message {
    private String id;

    private String text;
    private Contact sender;
    private Contact receiver;
    private long messageDateTime;

    public Message(String text, Contact sender, Contact receiver) {
        this.text = text;
        this.sender = sender;
        this.receiver = receiver;
    }

    public Contact getReceiver() {
        return receiver;
    }

    public Contact getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public long getMessageDateTime() {
        return messageDateTime;
    }

    public void setMessageDateTime(long messageDateTime) {
        this.messageDateTime = messageDateTime;
    }
}
