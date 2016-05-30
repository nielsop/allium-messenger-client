package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.CommonClientGateway;
import nl.han.asd.project.client.commonclient.persistence.IPersistence;
import nl.han.asd.project.commonservices.internal.utility.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class ContactStore implements IContactStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonClientGateway.class);

    private IPersistence persistence;
    private CurrentUser currentUser;
    private List<Contact> contactList;

    @Inject
    public ContactStore(IPersistence persistence) {
        this.persistence = persistence;
        this.contactList = new ArrayList<>();
        contactList = persistence.getContacts();
    }

    @Override
    public void addContact(String username) {
        contactList.add(new Contact(username));
        persistence.addContact(username);
    }

    @Override
    public void removeContact(String username) {
        for (int i = 0; i < contactList.size(); i++) {
            Contact toBeDeletedContact = contactList.get(i);
            if (toBeDeletedContact.getUsername().equals(username)) {
                contactList.remove(i);
                break;
            }
        }
        persistence.deleteContact(username);
    }

    @Override
    public Contact getCurrentUserAsContact() {
        return currentUser.getCurrentUserAsContact();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CurrentUser getCurrentUser() {
        return currentUser;
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
        if (username == null) {
            return null;
        }
        for (Contact contact : contactList) {
            if (contact.getUsername().equals(username))
                return contact;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAllContactsFromMemory() {
        contactList.clear();
    }
}
