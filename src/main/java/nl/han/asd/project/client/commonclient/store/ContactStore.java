package nl.han.asd.project.client.commonclient.store;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import nl.han.asd.project.client.commonclient.graph.IGetVertices;
import nl.han.asd.project.client.commonclient.graph.Node;
import nl.han.asd.project.client.commonclient.persistence.IPersistence;
import nl.han.asd.project.commonservices.internal.utility.Check;

public class ContactStore implements IContactStore {
    private final IGetVertices graphManager;
    private IPersistence persistence;
    private CurrentUser currentUser;
    private List<Contact> contactList;

    /**
     * Constructor of Contactstore.
     *
     * @param persistence the layer where the store connects and communicates with the database
     */
    @Inject
    public ContactStore(IPersistence persistence, IGetVertices graphManager) {
        this.graphManager = Check.notNull(graphManager, "graphManager");
        this.persistence = Check.notNull(persistence, "persistence");
        contactList = persistence.getContacts();
        if (contactList == null) {
            contactList = new ArrayList<>();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addContact(String username) {
        contactList.add(new Contact(username));
        persistence.addContact(username);
    }

    /**
     * {@inheritDoc}
     */
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
    public void updateUserInformation(String user, byte[] publicKey,
            boolean online, List<String> connectedNodeIds) {
        Contact oldContact = findContact(user);
        if (oldContact == null) {
            return;
        }

        int position = contactList.indexOf(oldContact);
        Contact newContact = new Contact(user, publicKey, online);

        List<Node> connectedNodes = new ArrayList<>(connectedNodeIds.size());
        for (String connectedNodeId : connectedNodeIds) {
            Node node = graphManager.getVertices().get(connectedNodeId);
            connectedNodes.add(node);
        }

        newContact.setConnectedNodes((Node[]) connectedNodes.toArray());
        contactList.set(position, newContact);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCurrentUser(CurrentUser currentUser) {
        this.currentUser = currentUser;
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
        if (username == null || username.equals("")) {
            return null;
        }
        System.out.println("searching...");
        for (Contact contact : contactList) {
            System.out.println("contact: " + contact.getUsername());
            if (contact.getUsername().equals(username)) {
                System.out.println("found!");
                return contact;
            }
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

    @Override
    public void init(String username, String password) {
        persistence.init(username, password);
    }

    @Override
    public void close() throws Exception {
        persistence.close();
    }
}
