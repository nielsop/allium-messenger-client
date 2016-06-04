package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.persistence.IPersistence;
import nl.han.asd.project.commonservices.internal.utility.Check;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.*;

/**
 * Manage stored messages.
 *
 * @version 1.0
 */
public class MessageStore implements IMessageStore {
    private IPersistence persistenceService;

    /**
     * Construct a new MessageStore instance.
     *
     * @param persistenceService persistence service used to
     *          write the messages to persistent storage
     */
    @Inject
    public MessageStore(final IPersistence persistenceService) {
        this.persistenceService = Check.notNull(persistenceService, "persistenceService");
    }

    @Override
    public void init(String username, String password) throws SQLException {
        persistenceService.init(username, password);
    }

    @Override
    public void addMessage(final Message message) {
        persistenceService.saveMessage(message);
    }

    @Override
    public List<Message> getMessagesFromUser(String username) {
        Map<Contact, List<Message>> messagesPerContact = persistenceService.getAllMessagesPerContact();

        return messagesPerContact.get(new Contact(username));
    }

    @Override
    public Message[] getMessagesAfterDate(long dateTimeSince) {
        Date dateSince = new Date(dateTimeSince);

        Map<Contact, List<Message>> messagesPerContact = persistenceService.getAllMessagesPerContact();

        List<Message> messagesAfterDate = new ArrayList<>();

        for (List<Message> messages : messagesPerContact.values()) {
            for (Message message : messages) {
                if (message.getMessageTimestamp().compareTo(dateSince) > 0) {
                    messagesAfterDate.add(message);
                }
            }
        }

        return (Message[]) messagesAfterDate.toArray();
    }

    @Override
    public void close() throws Exception {
        persistenceService.close();
    }
}
