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
    private ArrayList<Contact> contactList = new ArrayList<>();

    @Inject
    public ContactStore(IPersistence persistence) {
        this.persistence = persistence;
    }

    @Override
    public void addContact(String username) {
        contactList.add(new Contact(username));
        persistence.addContact(username);
    }

    @Override
    public void removeContact(String username) {
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
        List<Contact> tempContactList = new ArrayList<>();
        persistence.getContacts().forEach(contact -> tempContactList.add(new Contact(contact.getUsername())));
        return tempContactList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Contact findContact(String username) {
        if (username == null) {
            return null;
        }
        for (Contact contact : getAllContacts()) {
            if (contact.getUsername().equals(username))
                return contact;
        } return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAllContactsFromMemory() {
        contactList.clear();
    }
}
