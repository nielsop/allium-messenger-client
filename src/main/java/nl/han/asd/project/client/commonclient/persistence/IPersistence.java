package nl.han.asd.project.client.commonclient.persistence;

import nl.han.asd.project.client.commonclient.database.IDatabase;

import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.store.Contact;

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

    SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Deletes a message from the database.
     *
     * @param id The id of the message to remove from the database.
     * @return <tt>true</tt> if the message was deleted successfully, <tt>false</tt> otherwise.
     */
    boolean deleteMessage(final int id);

    /**
     * Saves a message in the database.
     *
     * @param message The message to save.
     * @return <tt>true</tt> if the message was saved successfully, <tt>false</tt> otherwise.
     */
    boolean saveMessage(final Message message);

    /**
     * Returns a list of all messages.
     *
     * @return A list of all messages.
     */
    List<Message> getAllMessages();

    /**
     * Returns a map of all messages, separated per contact.
     *
     * @return A map of all messages, separated per contact.
     */
    Map<Contact, List<Message>> getAllMessagesPerContact();

    /**
     * Adds a contact to the database.
     *
     * @param username The username of the contact to add to the database.
     * @return <tt>true</tt> if the contact was added successfully, <tt>false</tt> otherwise.
     */
    boolean addContact(final String username);

    /**
     * Deletes a contact from the database.
     *
     * @param username The username of the contact to delete from the database.
     * @return <tt>true</tt> if the contact was deleted successfully, <tt>false</tt> otherwise.
     */
    boolean deleteContact(final String username);

    boolean deleteAllContacts();

    /**
     * Returns a list of all contacts.
     *
     * @return A list of all contacts.
     */
    List<Contact> getContacts();

    /**
     * Retuns the currently open database connection.
     *
     * @return The currently open database connection.
     */
    IDatabase getDatabase();
}