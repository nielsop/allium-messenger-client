package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.persistence.IPersistence;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * @author DDulos
 * @version 1.0
 * @since 12-May-16
 */
public class ContactStoreTest {
    private IPersistence persistence;
    private ContactStore contactStore;
    private ArrayList<Contact> singleContactArrayList;
    private ArrayList<Contact> multipleContactArrayList;

    // Test contactStore 1
    private String usernameContact1 = "testContact1";
    private String publicKeyContact1 = "asdfTest1";

    // Test contactStore 2
    private String usernameContact2 = "testContact2";
    private String publicKeyContact2 = "asdfTest2";

    // Test contactStore 3
    private String usernameContact3 = "testContact3";
    private String publicKeyContact3 = "asdfTest3";

    // Test contactStore 4
    private String usernameContact4 = "testContact4";
    private String publicKeyContact4 = "asdfTest4";

    @Before
    public void initialize() {
        persistence = Mockito.mock(IPersistence.class);
        contactStore = new ContactStore(persistence);
        singleContactArrayList = new ArrayList<>();
        multipleContactArrayList = new ArrayList<>();

        initializeTestContacts();
    }

    private void addTestContacts() {
        contactStore.addContact(usernameContact1, publicKeyContact1);
        contactStore.addContact(usernameContact2, publicKeyContact2);
        contactStore.addContact(usernameContact3, publicKeyContact3);
        contactStore.addContact(usernameContact4, publicKeyContact4);
    }

    private void initializeTestContacts() {
        // Single ContactStore in list
        singleContactArrayList.add(new Contact(usernameContact1, publicKeyContact1));

        // Multiple contacts in list
        multipleContactArrayList.add(new Contact(usernameContact1, publicKeyContact1));
        multipleContactArrayList.add(new Contact(usernameContact2, publicKeyContact2));
        multipleContactArrayList.add(new Contact(usernameContact3, publicKeyContact3));
        multipleContactArrayList.add(new Contact(usernameContact4, publicKeyContact4));
    }

    @Test
    public void testAddSingleContactToList() {
        contactStore.addContact(usernameContact1, publicKeyContact1);
        Contact tempContact = contactStore.getAllContacts().get(0);
        assertEquals(usernameContact1, tempContact.getUsername());
        assertEquals(publicKeyContact1, tempContact.getPublicKey());
    }

    @Test
    public void testAddMultipleContactsToList() {
        addTestContacts();
        assertEquals(4, contactStore.getAllContacts().size());
    }

    @Test
    public void testFindContactInListWithUsername() {
        addTestContacts();
        Contact selectedContact = contactStore.findContact(usernameContact3);
        assertEquals(usernameContact3, selectedContact.getUsername());
        assertEquals(publicKeyContact3, selectedContact.getPublicKey());
    }

    @Test
    public void testFindContactGivesNullWhenNotExistsInList() {
        contactStore.addContact(usernameContact1, publicKeyContact1);
        contactStore.addContact(usernameContact2, publicKeyContact2);
        contactStore.addContact(usernameContact3, publicKeyContact3);
        contactStore.addContact(usernameContact4, publicKeyContact4);
        Contact selectedContact = contactStore.findContact("testContact5");
        assertEquals(null, selectedContact);
    }

    @Test
    public void testDeleteSingleContactFromList() {
        addTestContacts();
        contactStore.removeContact(usernameContact2);
        Contact selectedContact = contactStore.findContact(usernameContact2);
        assertEquals(null, selectedContact);
        assertEquals(3, contactStore.getAllContacts().size());
    }

    @Test
    public void testClearAllContactsFromList() {
        addTestContacts();
        contactStore.deleteAllContacts();
        assertEquals(0, contactStore.getAllContacts().size());
    }
}
