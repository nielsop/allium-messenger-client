package nl.han.asd.project.client.commonclient.persistence;

import nl.han.asd.project.client.commonclient.Configuration;
import nl.han.asd.project.client.commonclient.database.IDatabase;
import nl.han.asd.project.client.commonclient.database.model.Contact;
import nl.han.asd.project.client.commonclient.database.model.Message;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides a way to communicate with the database.
 */
public class PersistenceService implements IPersistence {
    private IDatabase database;

    @Inject
    public PersistenceService(IDatabase database) {
        this.database = database;
    }

    @Override
    public boolean deleteMessage(int id) throws SQLException {
        return getDatabase().query(String.format("DELETE FROM Message WHERE id = %d", id));
    }

    @Override
    public boolean saveMessage(Message message) throws SQLException {
        final String messageTimestampInDatabaseFormat = Configuration.TIMESTAMP_FORMAT.format(message.getTimestamp());
        return getDatabase().query(String
                .format("INSERT INTO Message (sender, message, timestamp) VALUES ('%s', '%s', '%s')",
                        message.getSender().getUsername(), message.getText(), messageTimestampInDatabaseFormat));
    }

    @Override
    public List<Message> getAllMessages() throws SQLException {
        final List<Message> messageList = new ArrayList<>();
        ResultSet selectMessagesResult = getDatabase().select("SELECT * FROM Message");
        while (selectMessagesResult.next()) {
            messageList.add(Message.fromDatabase(selectMessagesResult));
        }
        return messageList;
    }

    @Override
    public Map<Contact, ArrayList<Message>> getAllMessagesPerContact() throws SQLException {
        final Map<Contact, ArrayList<Message>> contactMessagesHashMap = new HashMap<>();
        ResultSet selectMessagesResult = getDatabase().select("SELECT * FROM Message");
        while (selectMessagesResult.next()) {
            final Message message = Message.fromDatabase(selectMessagesResult);
            if (!contactMessagesHashMap.containsKey(message.getSender())) {
                contactMessagesHashMap.put(message.getSender(), new ArrayList<>());
            }
            contactMessagesHashMap.get(message.getSender()).add(message);
        }
        return contactMessagesHashMap;
    }

    @Override
    public boolean addContact(String username) throws SQLException {
        return getDatabase().query(String.format("INSERT INTO Contact (username) VALUES ('%s')", username));
    }

    @Override
    public boolean deleteContact(String username) throws SQLException {
        return getDatabase().query(String.format("DELETE FROM Contact WHERE username = '%s'", username));
    }

    @Override
    public List<Contact> getContacts() throws SQLException {
        final List<Contact> contactList = new ArrayList<>();
        ResultSet selectContactsResult = getDatabase().select("SELECT * FROM Contact");
        while (selectContactsResult.next()) {
            contactList.add(Contact.fromDatabase(selectContactsResult.getObject(2)));
        }
        selectContactsResult.close();
        return contactList;
    }

    @Override
    public IDatabase getDatabase() {
        return database;
    }
}
