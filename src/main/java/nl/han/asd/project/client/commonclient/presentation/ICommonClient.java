package nl.han.asd.project.client.commonclient.presentation;

import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.client.commonclient.store.StorageException;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Dennis
 * @version 1.0
 * @since 25/05/16
 */
public interface ICommonClient {
    /**
     * Registers a user on the master application with the given credentials.
     * Use the MasterGateway to register a user.
     *
     * @param username username given by user.
     * @param password password given by user.
     * @return the register status, received from the registerResponseWrapper.
     */
    HanRoutingProtocol.ClientRegisterResponse.Status register(String username, String password);

    /**
     * Logs in and returns a login status. This login status is wrapped inside a loginResponseWrapper.
     *
     * @param username the username to log in.
     * @param password the password belonging to the username.
     * @return the login status, received from the loginResponseWrapper.
     */
    HanRoutingProtocol.ClientLoginResponse.Status login(String username, String password);

    /**
     * Returns the current user that is logged in.
     *
     * @return the current user
     */
    Contact getCurrentUser();

    /**
     * Returns a list of contacts of the current user.
     *
     * @return list of contacts of the current user
     */
    List<Contact> getContacts() throws SQLException, StorageException;

    /**
     * Removes contact from contactstore.
     *
     * @param username username of contact
     */
    void removeContact(String username) throws SQLException, StorageException;

    /**
     * Adds contact to contactstore.
     *
     * @param username username of contact
     */
    void addContact(String username) throws SQLException, StorageException;

    /**
     * Logs out the user and deletes all user data in memory
     */
    //TODO: Implement method. Delete all in memory user data.
    void logout();
}
