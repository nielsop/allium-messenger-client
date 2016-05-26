package nl.han.asd.project.client.commonclient.message;

import nl.han.asd.project.client.commonclient.store.Contact;

public class Message {
    private String id;

    private String text;
    private Contact sender;
    private Contact receiver;
    private long messageTimestamp;

    public Message(String text, Contact sender, Contact receiver, long messageTimestamp) {
        this.text = text;
        this.sender = sender;
        this.receiver = receiver;
        this.messageTimestamp = messageTimestamp;
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

    public String getId() {
        return id;
    }

    public long getMessageTimestamp() {
        return messageTimestamp;
    }
}
