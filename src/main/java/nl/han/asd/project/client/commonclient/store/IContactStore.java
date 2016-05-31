package nl.han.asd.project.client.commonclient.store;

/**
 * Created by Jevgeni on 12-5-2016.
 */
import java.util.List;

public interface IContactStore {
    // TODO remove test method
    void createTestContacts();

    /**
     * Adds new contact into contactstore.
     *
     * @param username  username of contact
     * @param publicKey publicKey.
     */
    void addContact(String username, byte[] publicKey);

    /**
     * Removes contact from contactStore.
     *
     * @param username username of to be deleted contact
     */
    void removeContact(String username);

    /**
     * Retrieves all contacts of current user.
     *
     * @return List of contacts
     */
    List<Contact> getAllContacts();

    /**
     * Finds a contact in the contactstore by username.
     *
     * @param username username of contact.
     * @return <tt>Contact</tt> if found, <tt>null</tt> otherwise.
     */
    Contact findContact(String username);

    /**
     * Deletes all the contacts in the contactstore memory.
     */
    void deleteAllContacts();

    /**
     * Getter for currentUser.
     *
     * @return current user that is logged in
     */
    CurrentUser getCurrentUser();

    /**
     * Setter for currentUser.
     *
     * @param currentUser the current user that is logged in
     */
    void setCurrentUser(CurrentUser currentUser);

    /**
     * Getter for currentUser as a Contact.
     *
     * @return current user that is logged in as a Contact object.
     */
    Contact getCurrentUserAsContact();
}
