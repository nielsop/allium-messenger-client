package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.persistence.IPersistence;

import javax.inject.Inject;

public class ContactStore implements IContactStore {
    public IPersistence persistence;

    @Inject
    public ContactStore(IPersistence persistence) {
        this.persistence = persistence;
        // todo: import contacts from persistence instance
    }

    @Override public void addContact(String username, String publicKey) {

    }

    @Override public void deleteContact(String username) {

    }

    @Override public Contact findContact(String username) {
        return null;
    }
}
