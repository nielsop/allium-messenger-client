package nl.han.asd.project.client.commonclient.store;

import java.util.List;

public interface IContactStore {

    /**
     * Adds new contact into contactstore.
     *  @param username username of contact
     */
    void addContact(String username);

    /**
     * Removes contact from contactStore.
     *
     * @param username username of to be deleted contact
     */
    void removeContact(String username);

    /**
     * Get a list of all the contacts in the contactstore.
     *
     * @return a list of all the contacts
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
    void deleteAllContactsFromMemory();

    /**
     * Setter for currentUser.
     *
     * @param currentUser the current user that is logged in
     */
    void setCurrentUser(CurrentUser currentUser);

    /**
     * Getter for currentUser as contact.
     *
     * @return current user that is logged in as contact
     */
    Contact getCurrentUserAsContact();

    /**
     * Getter for currentUser.
     *
     * @return current user that is logged in
     */
    CurrentUser getCurrentUser();
}
