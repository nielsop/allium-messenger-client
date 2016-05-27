package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.persistence.IPersistence;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

public class MessageStore implements IMessageStore, IMessageStoreObserver {
    private IPersistence persistenceService;

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
    }

    @Override
    public Map<Contact, List<Message>> getAllMessagesFromAllUsers() {
        return persistenceService.getAllMessagesPerContact();
    }

    @Override
    public List<Message> getMessagesFromUser(String contact) {
        return getAllMessagesFromAllUsers().get(new Contact(contact));
    }
}
