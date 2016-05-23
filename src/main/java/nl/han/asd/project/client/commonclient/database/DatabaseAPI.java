package nl.han.asd.project.client.commonclient.database;

import nl.han.asd.project.client.commonclient.database.model.Contact;
import nl.han.asd.project.client.commonclient.database.model.Message;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author Niels Bokmans
 * @version 1.0
 * @since 20-5-2016
 */
public class DatabaseAPI {

    private static DatabaseConnection connection;

    private DatabaseAPI() {

    }

    public static List<Message> getAllMessages() throws SQLException {
        final List<Message> messageList = new ArrayList<>();
        ResultSet selectMessagesResult = getConnection().select("SELECT * FROM Message");
        while (selectMessagesResult.next()) {
            messageList.add(Message.fromDatabase(selectMessagesResult));
        }
        return messageList;
    }

    public static Map<Contact, Message> getAllMessagesPerContact() throws SQLException {
        final Map<Contact, Message> contactMessagesHashMap = new HashMap<>();
        ResultSet selectMessagesResult = getConnection().select("SELECT * FROM Message");
        while (selectMessagesResult.next()) {
            final Message message = Message.fromDatabase(selectMessagesResult);
            contactMessagesHashMap.put(message.getSender(), message);
        }
        return contactMessagesHashMap;
    }

    public static boolean addContact(String username) throws SQLException {
        return getConnection().executeQuery(String.format("INSERT INTO Contact (username) VALUES ('%s')", username));
    }

    public static boolean deleteContact(String username) throws SQLException {
        return getConnection().executeQuery(String.format("DELETE FROM Contact WHERE username = '%s'", username));
    }

    public static List<Contact> getContacts() throws SQLException {
        final List<Contact> contactList = new ArrayList<>();
        ResultSet selectContactsResult = getConnection().select("SELECT * FROM Contact");
        while (selectContactsResult.next()) {
            contactList.add(Contact.fromDatabase(selectContactsResult.getObject(2)));
        }
        selectContactsResult.close();
        return contactList;
    }

    public static boolean isOpen() throws SQLException {
        return connection.isOpen();
    }

    public static void close() throws SQLException {
        connection.stop();
    }

    private static DatabaseConnection getConnection() throws SQLException {
        if (connection == null || !isOpen()) {
            connection = new DatabaseConnection("username", "password");
        }
        return connection;
    }

}

