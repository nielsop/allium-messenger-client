package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.persistence.IPersistence;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class MessageStore implements IMessageStore, IMessageStoreObserver {
    List<Message> messages = new ArrayList<>();
    private IPersistence persistence;
    private IContactStore contactStore;

    @Inject
    public MessageStore(IPersistence persistence, IContactStore contactStore) {
        this.persistence = persistence;
        this.contactStore = contactStore;
    }

    @Override public void messageReceived(String confirmationId) {
        //TODO: implement persistence!
    }


    public void addMessage(Message message) {
        messages.add(message);
    }

    @Override
    public void findMessage(Message message) {
        //TODO: implement!
        throw new UnsupportedOperationException();
    }

    @Override
    public HanRoutingProtocol.Message findMessageByID(String identifier) {
        //TODO: implement!
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Message> getMessagesFromUser(String contact) {
        List<Message> msgs = new ArrayList<>();
        for (Message msg : messages) {
            if (msg.getSender().getUsername().equals(contact)) {
                msgs.add(msg);
            }
        }
        return msgs;
    }

}
