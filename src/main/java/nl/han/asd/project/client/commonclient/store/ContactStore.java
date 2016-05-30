package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.persistence.IPersistence;

import javax.inject.Inject;
import java.util.ArrayList;

public class ContactStore implements IContactStore {
    public IPersistence persistence;
    private CurrentUser currentUser;
    private ArrayList<Contact> contactList = new ArrayList<>();

    @Inject
    public ContactStore(IPersistence persistence) {
        this.persistence = persistence;
        // todo: import contacts from persistence instance
    }

    // TODO remove test contacts
    @Override
    public void createTestContacts() {
        addContact("bram", "asdf4321".getBytes());
        addContact("niels", "asdf4321".getBytes());
        addContact("marius", "asdf4321".getBytes());
        addContact("kenny", "asdf4321".getBytes());
        addContact("julius", "asdf4321".getBytes());
        addContact("jevgeni", "asdf4321".getBytes());
        addContact("dennis", "asdf4321".getBytes());
    }

    @Override //TODO: Should not be possible to add already existing userNames?
    public void addContact(String username, byte[] publicKey) {
        contactList.add(new Contact(username, publicKey));
    }

    @Override
    public void removeContact(String username) {
        for (Contact contact : contactList) {
            if (contact.getUsername().equals(username)) {
                contactList.remove(contact);
                break;
            }
        }
    }

    @Override
    public void deleteAllContacts() {
        contactList.clear();
    }

    @Override
    public ArrayList<Contact> getAllContacts() {
        return contactList;
    }

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

    @Override
    public CurrentUser getCurrentUser() {
        return currentUser;
    }

    @Override
    public void setCurrentUser(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }
}
