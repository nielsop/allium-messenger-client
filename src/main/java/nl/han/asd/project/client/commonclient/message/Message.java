package nl.han.asd.project.client.commonclient.message;

import nl.han.asd.project.client.commonclient.persistence.PersistenceObject;
import nl.han.asd.project.client.commonclient.store.Contact;

/**
 * Created by Marius on 25-04-16.
 */
public class Message extends PersistenceObject {
    private String id;

    private String text;
    private Contact sender;
    private Contact receiver;
    private long messageDateTime;

    public Message() {}

    public Message(String text, Contact sender, Contact receiver) {
        setText(text);
        setSender(sender);
        setReceiver(receiver);
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

    public void setText(String value) {
        this.text = value;
    }

    public void setSender(Contact sender) {
        this.sender = sender;
    }

    public void setReceiver(Contact receiver) {
        this.receiver = receiver;
    }
}
