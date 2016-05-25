package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.persistence.IPersistence;
import nl.han.asd.project.client.commonclient.presentation.CommonClientGateway;
import nl.han.asd.project.commonservices.internal.utility.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactStore implements IContactStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonClientGateway.class);

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
        try {
            persistence.addContact(username);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeContact(String username) {
        for (Contact contact : contactList) {
            if (contact.getUsername().equals(username)) {
                contactList.remove(contact);
                try {
                    persistence.deleteContact(username);
                } catch (SQLException e) {
                    LOGGER.error(e.getMessage());
                }
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
        refreshContactList();
        return contactList;
    }

    /**
     * Refills the contacts in the database and stores them into the contact list.
     */
    private void refreshContactList() {
        deleteAllContacts();
        try {
            persistence.getContacts().forEach(contact -> {
                contactList.add(new Contact(contact.getUsername()));
            });
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Contact findContact(String username) {
        refreshContactList();
        for (Contact contact : contactList) {
            if (contact.getUsername().equals(username))
                return contact;
        }
        return null;
    }
}
