package nl.han.asd.project.client.commonclient.message;

import nl.han.asd.project.client.commonclient.store.Contact;

public class Message {
    private String ID;

    private String text;
    private Contact sender;
    private Contact receiver;

    public Message(String text, Contact sender, Contact reciever) {
        this.text = text;
        this.sender = sender;
        this.receiver = reciever;
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
}
