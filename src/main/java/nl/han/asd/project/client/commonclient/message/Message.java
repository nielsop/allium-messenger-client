package nl.han.asd.project.client.commonclient.message;

import nl.han.asd.project.client.commonclient.store.Contact;

/**
 * Created by Julius on 18/04/16.
 */
public class Message {
    private String ID;

    private String text;
    private Contact sender;
    private Contact reciever;

    public Contact getReciever() {
        return reciever;
    }
    public Contact getSender() {
        return sender;
    }
    public String getText() {
        return text;
    }

    public Message(String text, Contact sender, Contact reciever){
        this.text = text;
        this.sender = sender;
        this.reciever = reciever;
    }
}
