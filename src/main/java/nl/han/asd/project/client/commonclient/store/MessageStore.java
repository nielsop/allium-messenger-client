package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.persistence.IPersistence;
import nl.han.asd.project.commonservices.internal.utility.Check;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageStore implements IMessageStore {
    private HashMap<Contact, List<Message>> messagesPerContact = new HashMap<>();
    private IPersistence persistenceService;

    @Inject
    public MessageStore(final IPersistence persistenceService) {
        this.persistenceService = Check.notNull(persistenceService, "persistenceService");
        updateFromDatabase();
    }

    @Override
    public void addMessage(final Message message) {
        if (!messagesPerContact.containsKey(message.getSender())) {
            final List<Message> newMessageList = new ArrayList<>();
            newMessageList.add(message);
            messagesPerContact.put(message.getSender(), newMessageList);
        } else {
            messagesPerContact.get(message.getSender()).add(message);
        }
    }

    @Override
    public Map<Contact, List<Message>> getAllMessagesFromAllUsers() {
        return persistenceService.getAllMessagesPerContact();
    }

    @Override
    public List<Message> getMessagesFromUser(String contact) {
        return getAllMessagesFromAllUsers().get(new Contact(contact));
    }

    @Override
    public void saveToDatabase() throws SQLException {
        for (final Map.Entry<Contact, List<Message>> mapEntry : messagesPerContact.entrySet()) {
            for (final Message message : mapEntry.getValue()) {
                persistenceService.saveMessage(message);
            }
        }
    }

    @Override
    public void updateFromDatabase() {
        messagesPerContact = persistenceService.getAllMessagesPerContact();
    }

    @Override
    public void clear() {
        messagesPerContact.clear();
    }

    /**
     * Returns all messages for a certain user after a certain dateTime.
     *
     * @param dateTime unix time stamp.
     * @return an arrayList of messages.
     */
    @Override
    public Message[] getMessagesAfterDate(long dateTime) {
        //ToDo implement
        return new Message[0];
    }
}
