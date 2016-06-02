package nl.han.asd.project.client.commonclient.persistence;

import nl.han.asd.project.client.commonclient.database.IDatabase;
import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.commonservices.scripting.Script;

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
    boolean saveMessage(final Message message) throws SQLException;

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

    /**
     * Returns a map of scripts stored in the database. The has build is as follow:
     * Key = ScriptName. Value = ScriptContent.
     *
     * @return A hashmap of all scripts.
     */
    Map<String, String> getScripts();

    /**
     * Deletes a script from the database.
     *
     * @param scriptName The name of the script to be deleted from the database.
     * @return <tt>true</tt> if the script was deleted successfully, <tt>false</tt> otherwise.
     */
    boolean deleteScript(final String scriptName);

    /**
     * Adds a script to the database.
     *
     * @param scriptName The name of the script to be added to the database.
     * @return <tt>true</tt> if the script was added successfully, <tt>false</tt> otherwise.
     */
    boolean addScript(final String scriptName, final String scriptContent);

    /**
     * Gets a list containing the names of all saved scripts.
     *
     * @return <tt>List<String></tt> containing the names of all saved scripts.
     */
    List<String> getAllScriptNames();

    /**
     * Gets the content of a script.
     *
     * @param scriptName The name of the script of which the content will be fetched.
     * @return <tt>String</tt> containing the content of a script.
     */
    String getScriptContent(String scriptName);

    /**
     * Updates the content of a script.
     *
     * @return <tt>String</tt> containing the content of a script.
     */
    void updateScript(String scriptName, String scriptContent);
}
