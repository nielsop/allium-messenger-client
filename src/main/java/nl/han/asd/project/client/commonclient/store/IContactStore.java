package nl.han.asd.project.client.commonclient.store;

import java.util.ArrayList;

public interface IContactStore {
    // TODO remove test method
    void createTestContacts();

    void addContact(String username, String publicKey);

    void deleteContact(String username);

    ArrayList<Contact> getAllContacts();

    Contact findContact(String username);

    Contact getCurrentUser();

    void setCurrentUser(Contact currentUser);

    void deleteAllContacts();
}
