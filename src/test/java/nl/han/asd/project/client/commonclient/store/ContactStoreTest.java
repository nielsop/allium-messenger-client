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
        assertEquals(1, contactStore.getAllContacts().size());
    }

    @Test
    public void testAddMultipleContactsToList() {
        contactStore.addContact(usernameContact1, publicKeyContact1);
        contactStore.addContact(usernameContact2, publicKeyContact2);
        contactStore.addContact(usernameContact3, publicKeyContact3);
        contactStore.addContact(usernameContact4, publicKeyContact4);
        ArrayList<Contact> contactList = contactStore.getAllContacts();
        for (int i = 0; i < multipleContactArrayList.size(); i++) {
            Contact testContact = multipleContactArrayList.get(i);
            Contact tempContact = contactList.get(i);
            assertEquals(testContact.getUsername(), tempContact.getUsername());
            assertEquals(testContact.getPublicKey(), tempContact.getPublicKey());
        }
        assertEquals(4, contactList.size());
    }

    @Test
    public void testFindContactInListWithUsername() {
        contactStore.addContact(usernameContact1, publicKeyContact1);
        contactStore.addContact(usernameContact2, publicKeyContact2);
        contactStore.addContact(usernameContact3, publicKeyContact3);
        contactStore.addContact(usernameContact4, publicKeyContact4);
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
        contactStore.addContact(usernameContact1, publicKeyContact1);
        contactStore.addContact(usernameContact2, publicKeyContact2);
        contactStore.addContact(usernameContact3, publicKeyContact3);
        contactStore.addContact(usernameContact4, publicKeyContact4);
        contactStore.deleteContact(usernameContact2);
        Contact selectedContact = contactStore.findContact(usernameContact2);
        assertEquals(null, selectedContact);
        assertEquals(3, contactStore.getAllContacts().size());
    }

    @Test
    public void testClearAllContactsFromList() {
        contactStore.addContact(usernameContact1, publicKeyContact1);
        contactStore.addContact(usernameContact2, publicKeyContact2);
        contactStore.addContact(usernameContact3, publicKeyContact3);
        contactStore.addContact(usernameContact4, publicKeyContact4);
        contactStore.clearAllContacts();
        assertEquals(0, contactStore.getAllContacts().size());
    }
}