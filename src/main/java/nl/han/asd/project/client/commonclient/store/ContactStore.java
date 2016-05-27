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
    public void addContact(String username) throws SQLException {
        persistence.addContact(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeContact(String username) throws SQLException {
        persistence.deleteContact(username);
    }

    /**
     * {@inheritDoc}
     */
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
    public List<Contact> getAllContacts() throws SQLException {
        List<Contact> tempContactList = new ArrayList<>();
        persistence.getContacts().forEach(contact -> tempContactList.add(new Contact(contact.getUsername())));
        return tempContactList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Contact findContact(String username) throws SQLException {
        for (Contact contact : getAllContacts()) {
            if (contact.getUsername().equals(username))
                return contact;
        }
        return null;
    }
}
