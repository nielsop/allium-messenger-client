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

    @Override
    public boolean deleteMessage(int id) {
        try {
            return getDatabase().query(String.format("DELETE FROM Message WHERE id = %d", id));
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean saveMessage(Message message) throws SQLException {
        final String messageTimestampInDatabaseFormat = IPersistence.TIMESTAMP_FORMAT.format(message.getTimestamp());
        return getDatabase()
                .query(String.format("INSERT INTO Message (sender, message, timestamp) VALUES ('%s', '%s', '%s')",
                        message.getSender().getUsername(), message.getText(), messageTimestampInDatabaseFormat));
    }

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
                    contactMessagesHashMap.put(message.getSender(), new ArrayList<>());
                }
                contactMessagesHashMap.get(message.getSender()).add(message);
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return contactMessagesHashMap;
    }

    @Override
    public boolean addContact(String username) {
        try {
            return getDatabase().query(String.format("INSERT INTO Contact (username) VALUES ('%s')", username));
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean deleteContact(String username) {
        try {
            return getDatabase().query(String.format("DELETE FROM Contact WHERE username = '%s'", username));
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean deleteAllContacts() {
        try {
            return getDatabase().query(String.format("DELETE FROM Contact"));
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public List<Contact> getContacts() {
        final List<Contact> contactList = new ArrayList<>();
        try {
            ResultSet selectContactsResult = getDatabase().select("SELECT * FROM Contact");
            while (selectContactsResult.next()) {
                contactList.add(Contact.fromDatabase((String) selectContactsResult.getObject(2)));
            }
            selectContactsResult.close();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return contactList;
    }

    @Override
    public IDatabase getDatabase() {
        return database;
    }

    @Override
    public Map<String, String> getScripts() {
        Map<String, String> scripts = new HashMap<>();
        try {
            ResultSet selectScriptsResult = getDatabase().select("SELECT * FROM Script");
            while (selectScriptsResult.next()) {
                String scriptName = (String)selectScriptsResult.getObject(2);
                String scriptContent = (String)selectScriptsResult.getObject(3);
                scripts.put(scriptName, scriptContent);
            }
            selectScriptsResult.close();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return scripts;
    }

    @Override
    public boolean deleteScript(String scriptName) {
        try {
            return getDatabase().query(String.format("DELETE FROM Script WHERE scriptname = '%s'", scriptName));
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean addScript(String scriptName, String scriptContent) {
        try {
            return getDatabase().query(String.format("INSERT INTO Script (scriptName, scriptContent) VALUES ('%s, %s')", scriptName, scriptContent));
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }
}
