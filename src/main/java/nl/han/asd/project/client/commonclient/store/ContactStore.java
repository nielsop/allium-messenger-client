package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.persistence.IPersistence;
import nl.han.asd.project.commonservices.internal.utility.Check;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class ContactStore implements IContactStore {
    private IPersistence persistence;
    private List<Contact> contactList = new ArrayList<>();
    private CurrentUser currentUser;

    /**
     * Constructor of ContactStore
     *
     * @param persistence used to connect to local database.
     */
    @Inject
    public ContactStore(IPersistence persistence) {
        this.persistence = persistence;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addContact(String username) {
        contactList.add(new Contact(username));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteContact(String username) {
        for (Contact contact : contactList) {
            if (contact.getUsername().equals(username)) {
                contactList.remove(contact);
                break;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAllContacts() {
        contactList.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Contact getCurrentUser() {
        return currentUser.getAsContact();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCurrentUser(CurrentUser currentUser) {
        this.currentUser = Check.notNull(currentUser, "currentUser");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Contact> getAllContacts() {
        return contactList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Contact findContact(String username) {
        for (Contact contact : contactList) {
            if (contact.getUsername().equals(username))
                return contact;
        }
        return null;
    }
}
