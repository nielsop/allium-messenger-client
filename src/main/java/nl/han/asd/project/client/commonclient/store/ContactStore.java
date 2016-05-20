package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.persistence.IPersistence;

import javax.inject.Inject;
import java.util.ArrayList;

public class ContactStore {
    private IPersistence persistence;
    private static ArrayList<Contact> contacts = new ArrayList<>();
    private static Contact currentUser;

    @Inject
    public ContactStore(IPersistence persistence) {
        this.persistence = persistence;
    }

    public static Contact findContact(String username) {
        for (Contact contact : contacts) {
            if (contact.getUsername().equals(username)) {
                return contact;
            }
        }
        return null;
    }

    public static Contact getCurrentUser() {
        return currentUser;
    }
    
    public static void setCurrentUser(Contact contact) {
        currentUser = contact;
    }
}
