package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.persistence.IPersistence;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class MessageStore implements IMessageStore, IMessageObserver {
    public IPersistence persistence;

    //for testing purposes
    ArrayList<Message> messages = new ArrayList<>();

    @Inject
    public MessageStore(IPersistence persistence) {
        this.persistence = persistence;
    }

    @Override
    public void addMessage(Message message) {
        messages.add(message);
    }

    @Override
    public void findMessage(Message message) {
        //TODO: implement!
    }

    @Override
    public List<Message> getMessages(String contact) {
        ArrayList<Message> msgs = new ArrayList<>();
        for (Message msg : messages) {
            if (msg.getReceiver().getUsername().equals(contact)) {
                msgs.add(msg);
            }
            else if (msg.getSender().getUsername().equals(contact)) {
                msgs.add(msg);
            }
        }
        return msgs;
    }
}
