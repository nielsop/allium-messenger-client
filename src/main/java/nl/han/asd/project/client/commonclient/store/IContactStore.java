package nl.han.asd.project.client.commonclient.store;

import java.util.List;

public interface IContactStore {
    /**
     * Adds new contact into contactstore.
     *  @param username username of contact
     *
     */
    void addContact(String username);

    /**
     * Removes contact from contactstore.
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
     * Deletes all the contacts in the contactstore.
     */
    void deleteAllContacts();

    /**
     * Setter for currentUser.
     *
     * @param currentUser the current user that is logged in
     */
    void setCurrentUser(CurrentUser currentUser);

    /**
     * Getter for currentUser.
     *
     * @return current user that is logged in
     */
    Contact getCurrentUser();
}
