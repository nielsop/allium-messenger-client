package nl.han.asd.project.client.commonclient.persistence;

import nl.han.asd.project.client.commonclient.database.IDatabase;
import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.commonservices.internal.utility.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Provides a way to communicate with the database.
 */
public class PersistenceService implements IPersistence {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersistenceService.class);
    private IDatabase database;

    @Inject
    public PersistenceService(IDatabase database) {
        this.database = Check.notNull(database, "database");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteMessage(int id) {
        try {
            return getDatabase().query(String.format("DELETE FROM Message WHERE id = %d", id));
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean saveMessage(Message message) {
        final String messageTimestampInDatabaseFormat = IPersistence.TIMESTAMP_FORMAT.format(message.getMessageTimestamp());
        try {
            return getDatabase().query(String
                    .format("INSERT INTO Message (sender, receiver, timestamp, message) VALUES ('%s', '%s', '%s', '%s')", message.getSender().getUsername(),
                            message.getReceiver().getUsername(), messageTimestampInDatabaseFormat, message.getText()));
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Message> getAllMessages() {
        final List<Message> messageList = new ArrayList<>();
        try {
            ResultSet selectMessagesResult = getDatabase().select("SELECT * FROM Message");
            while (selectMessagesResult.next()) {
                messageList.add(Message.fromDatabase(selectMessagesResult));
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return messageList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Contact, List<Message>> getAllMessagesPerContact() {
        final Map<Contact, List<Message>> contactMessagesHashMap = new HashMap<>();
        try {
            ResultSet selectMessagesResult = getDatabase().select("SELECT * FROM Message");
            if (selectMessagesResult == null) {
                return Collections.emptyMap();
            }
            while (selectMessagesResult.next()) {
                final Message message = Message.fromDatabase(selectMessagesResult);
                if (!contactMessagesHashMap.containsKey(message.getSender())) {
                    contactMessagesHashMap.put(message.getSender(), new ArrayList<Message>());
                }
                contactMessagesHashMap.get(message.getSender()).add(message);
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return contactMessagesHashMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addContact(String username) {
        try {
            return getDatabase().query(String.format("INSERT INTO Contact (username) VALUES ('%s')", username));
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteContact(String username) {
        try {
            return getDatabase().query(String.format("DELETE FROM Contact WHERE username = '%s'", username));
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteAllContacts() {
        try {
            return getDatabase().query(String.format("DELETE FROM Contact"));
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Contact> getContacts() {
        final Map<String, Contact> contactList = new HashMap<>();
        try {
            ResultSet selectContactsResult = getDatabase().select("SELECT * FROM Contact");

            if (selectContactsResult == null)
                return new HashMap<>();

            while (selectContactsResult.next()) {
                contactList.put((String) selectContactsResult.getObject(2),
                        Contact.fromDatabase((String) selectContactsResult.getObject(2)));
            }
            selectContactsResult.close();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return contactList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IDatabase getDatabase() {
        return database;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getScripts() {
        Map<String, String> scripts = new HashMap<>();
        try {
            ResultSet selectScriptsResult = getDatabase().select("SELECT * FROM Script");
            while (selectScriptsResult.next()) {
                String scriptName = (String) selectScriptsResult.getObject(2);
                String scriptContent = (String) selectScriptsResult.getObject(3);
                scripts.put(scriptName, scriptContent);
            }
            selectScriptsResult.close();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return scripts;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteScript(String scriptName) {
        try {
            return getDatabase().query(String.format("DELETE FROM Script WHERE scriptName = '%s'", scriptName));
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addScript(String scriptName, String scriptContent) {
        try {
            return getDatabase().query(String.format("INSERT INTO Script (scriptName, scriptContent) VALUES ('%s', '%s')", scriptName, scriptContent));
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean init(String username, String password) {
        try {
            database.init(username, password);
        } catch (SQLException e) {
            return false;
        }
        
        return true;
    }

    @Override
    public void close() throws Exception {
        database.close();
    }
}
