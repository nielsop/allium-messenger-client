package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.persistence.IPersistence;

import javax.inject.Inject;
import java.util.ArrayList;

public class ContactStore implements IContact {
    private static ContactStore instance = null;
    private ArrayList<Contact> contacts = new ArrayList<>();
    private Contact currentUser;
    private IPersistence persistence;

    @Inject
    public ContactStore(IPersistence persistence) {
        this.persistence = persistence;
        instance = this;
    }

    private ContactStore() {

    }

    public static ContactStore getInstance() {
        if (instance == null) {
            instance = new ContactStore();
        }
        return instance;
    }

    public Contact findContact(String username) {
        for (Contact contact : contacts) {
            if (contact.getUsername().equals(username)) {
                return contact;
            }
        }
        return null;
    }

    public Contact getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Contact contact) {
        currentUser = contact;
    }
}