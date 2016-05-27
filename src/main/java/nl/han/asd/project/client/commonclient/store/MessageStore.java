package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.persistence.IPersistence;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

public class MessageStore implements IMessageStore, IMessageStoreObserver {
<<<<<<< HEAD
    private IPersistence persistenceService;
=======
    List<Message> messages = new ArrayList<>();
    private IPersistence persistence;
>>>>>>> hotfix/sonar-issues

    @Inject
    public MessageStore(IPersistence persistence) {
        this.persistenceService = persistence;
    }

    @Override
    public void addMessage(Message message) {
        persistenceService.saveMessage(message);
    }

    @Override
    public void findMessage(Message message) {
        //TODO: implement!
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<Contact, List<Message>> getAllMessagesFromAllUsers() {
        return persistenceService.getAllMessagesPerContact();
    }

    @Override
    public List<Message> getMessagesFromUser(String contact) {
<<<<<<< HEAD
        return getAllMessagesFromAllUsers().get(new Contact(contact));
=======
        List<Message> msgs = new ArrayList<>();
        for (Message msg : messages) {
            if (msg.getSender().getUsername().equals(contact)) {
                msgs.add(msg);
            }
        }
        return msgs;
>>>>>>> hotfix/sonar-issues
    }
}
