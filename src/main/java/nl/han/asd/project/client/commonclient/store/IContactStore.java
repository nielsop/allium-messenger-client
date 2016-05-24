package nl.han.asd.project.client.commonclient.store;

import java.util.List;

public interface IContactStore {
    /**
     * Adds new contactStore into contactstore.
     *  @param username username of contactStore
     *
     */
    void addContact(String username);

    /**
     * Removes contactStore from contactstore.
     *
     * @param username username of to be deleted contactStore
     */
    void deleteContact(String username);

    /**
     * Retrieves all contacts of current user.
     *
     * @return List of contacts
     */
    List<Contact> getAllContacts();

    /**
     * Finds a contactStore in the contactstore by username.
     *
     * @param username username of contactStore.
     * @return <tt>Contact</tt> if found, <tt>null</tt> otherwise.
     */
    Contact findContact(String username);

    /**
     * Deletes all the contacts in the contactstore.
     */
    void deleteAllContacts();

    /**
     * Getter for currentUser.
     *
     * @return current user that is logged in
     */
    Contact getCurrentUser();

    /**
     * Setter for currentUser.
     *
     * @param currentUser the current user that is logged in
     */
    void setCurrentUser(CurrentUser currentUser);
}
