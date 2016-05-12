package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.persistence.IPersistence;

import javax.inject.Inject;
import java.util.ArrayList;

public class ContactStore implements IContactStore {
    public IPersistence persistence;

    private ArrayList<Contact> contactList = new ArrayList<>();

    @Inject
    public ContactStore(IPersistence persistence) {
        this.persistence = persistence;
    }

    // TODO remove test contacts
    public void createTestContacts() {
        addContact("bram", "asdf4321");
        addContact("niels", "asdf4321");
        addContact("marius", "asdf4321");
        addContact("kenny", "asdf4321");
        addContact("julius", "asdf4321");
        addContact("jevgeni", "asdf4321");
        addContact("dennis", "asdf4321");
    }

    @Override
    public void addContact(String username, String publicKey) {
        contactList.add(new Contact(username, publicKey));
    }

    @Override
    public void deleteContact(String username) {
        for (int i = 0; i < contactList.size(); i++) {
            Contact contact = contactList.get(i);
            if (contact.getUsername().equals(username)) {
                contactList.remove(i);
                break;
            }
        }
    }

    @Override
    public void clearAllContacts() {
        contactList.clear();
    }

    public ArrayList<Contact> getAllContacts() {
        return contactList;
    }

    @Override
    public Contact findContact(String username) {
        for (Contact contact : contactList) {
            if (contact.getUsername().equals(username))
                return contact;
        }
        return null;
    }
}
