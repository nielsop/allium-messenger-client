package nl.han.asd.project.client.commonclient.persistence;

import nl.han.asd.project.client.commonclient.database.IDatabase;
import nl.han.asd.project.client.commonclient.database.model.Contact;
import nl.han.asd.project.client.commonclient.database.model.Message;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Defines an interface for persistence functions.
 *
 * @author Niels Bokmans
 * @version 1.0
 * @since 24-05-2016
 */
public interface IPersistence {

    public static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Deletes a message from the database.
     * @param id The id of the message to remove from the database.
     * @return <tt>true</tt> if the message was deleted successfully, <tt>false</tt> otherwise.
     * @throws SQLException if a database access error occurs.
     */
    boolean deleteMessage(final int id) throws SQLException;

    /**
     * Saves a message in the database.
     * @param message The message to save.
     * @return <tt>true</tt> if the message was saved successfully, <tt>false</tt> otherwise.
     * @throws SQLException if a database access error occurs.
     */
    boolean saveMessage(final Message message) throws SQLException;

    /**
     * Returns a list of all messages.
     * @return A list of all messages.
     * @throws SQLException if a database access error occurs.
     */
    List<Message> getAllMessages() throws SQLException;

    /**
     * Returns a map of all messages, separated per contact.
     * @return A map of all messages, separated per contact.
     * @throws SQLException if a database access error occurs.
     */
    Map<Contact, List<Message>> getAllMessagesPerContact() throws SQLException;

    /**
     * Adds a contact to the database.
     * @param username The username of the contact to add to the database.
     * @return <tt>true</tt> if the contact was added successfully, <tt>false</tt> otherwise.
     * @throws SQLException if a database access error occurs.
     */
    boolean addContact(final String username) throws SQLException;

    /**
     * Deletes a contact from the database.
     * @param username The username of the contact to delete from the database.
     * @return <tt>true</tt> if the contact was deleted successfully, <tt>false</tt> otherwise.
     * @throws SQLException if a database access error occurs.
     */
    boolean deleteContact(final String username) throws SQLException;

    /**
     * Returns a list of all contacts.
     * @return A list of all contacts.
     * @throws SQLException if a database access error occurs.
     */
    List<Contact> getContacts() throws SQLException;

    /**
     * Retuns the currently open database connection.
     * @return The currently open database connection.
     */
    IDatabase getDatabase();
}
