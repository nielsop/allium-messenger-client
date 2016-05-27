package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.persistence.IPersistence;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

/**
 * @author DDulos
 * @version 1.0
 * @since 12-May-16
 */
public class ContactStoreTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContactStoreTest.class);

    private IPersistence persistence;
    private ContactStore contactStore;

    private  static final String TEST_CONTACT1 = "testContact1";
    private  static final String TEST_CONTACT2 = "testContact2";
    private  static final String TEST_CONTACT3 = "testContact3";
    private  static final String TEST_CONTACT4 = "testContact4";
    private List<Contact> mockedSingleContactList; // Groovy must be installed for import aliasing!
    private List<Contact> mockedMultipleContactList; // Groovy must be installed for import aliasing!
    private List<Contact> mockDeletedContactList; // Groovy must be installed for import aliasing!

    @Before
    public void initialize() {
        persistence = Mockito.mock(IPersistence.class);
        contactStore = new ContactStore(persistence);

        // Mock lists
        mockedSingleContactList = new ArrayList<>();
        mockedMultipleContactList = new ArrayList<>();
        mockDeletedContactList = new ArrayList<>();
        initializeMockContacts();
    }

    private void initializeMockContacts() {

        mockedSingleContactList.add(new Contact(TEST_CONTACT1));

        mockedMultipleContactList.add(new Contact(TEST_CONTACT1));
        mockedMultipleContactList.add(new Contact(TEST_CONTACT2));
        mockedMultipleContactList.add(new Contact(TEST_CONTACT3));
        mockedMultipleContactList.add(new Contact(TEST_CONTACT4));

        mockDeletedContactList.add(new Contact(TEST_CONTACT1));
        mockDeletedContactList.add(new Contact(TEST_CONTACT3));
        mockDeletedContactList.add(new Contact(TEST_CONTACT4));
    }

    private void addTestContacts() {
        // Mock
        when(persistence.addContact(TEST_CONTACT1)).thenReturn(true);
        when(persistence.addContact(TEST_CONTACT2)).thenReturn(true);
        when(persistence.addContact(TEST_CONTACT3)).thenReturn(true);
        when(persistence.addContact(TEST_CONTACT4)).thenReturn(true);

        // Test
        contactStore.addContact(TEST_CONTACT1);
        contactStore.addContact(TEST_CONTACT2);
        contactStore.addContact(TEST_CONTACT3);
        contactStore.addContact(TEST_CONTACT4);
    }

    @Test
    public void testAddSingleContactToList() throws SQLException {
        // Mock
        when(persistence.addContact(TEST_CONTACT1)).thenReturn(true);
        when(persistence.getContacts()).thenReturn(mockedSingleContactList);

        // Test
        contactStore.addContact(TEST_CONTACT1);
        nl.han.asd.project.client.commonclient.store.Contact tempContact = contactStore.getAllContacts().get(0);

        // Assert
        assertEquals(TEST_CONTACT1, tempContact.getUsername());
    }

    @Test
    public void testAddMultipleContactsToList() throws SQLException {
        // Mock
        when(persistence.getContacts()).thenReturn(mockedMultipleContactList);

        // Test
        addTestContacts();

        // Assert
        assertEquals(4, contactStore.getAllContacts().size());
    }

    @Test
    public void testFindContactInListWithUsername() throws SQLException {
        // Mock
        when(persistence.getContacts()).thenReturn(mockedMultipleContactList);

        // Test
        addTestContacts();
        nl.han.asd.project.client.commonclient.store.Contact selectedContact = contactStore.findContact(TEST_CONTACT3);

        // Assert
        assertEquals(TEST_CONTACT3, selectedContact.getUsername());
    }

    @Test
    public void testFindContactGivesNullWhenNotExistsInList() throws SQLException {
        // Mock
        when(persistence.getContacts()).thenReturn(mockedMultipleContactList);

        // Test
        addTestContacts();
        nl.han.asd.project.client.commonclient.store.Contact selectedContact = contactStore.findContact("testContact5");

        // Assert
        assertEquals(null, selectedContact);
    }

    @Test
    public void testDeleteSingleContactFromList() throws SQLException {
        // Mock
        when(persistence.deleteContact(TEST_CONTACT2)).thenReturn(true);
        when(persistence.getContacts()).thenReturn(mockDeletedContactList);

        // Test
        addTestContacts();
        contactStore.removeContact(TEST_CONTACT2);
        Contact selectedContact = contactStore.findContact(TEST_CONTACT2);

        // Assert
        assertNull(selectedContact);
        assertEquals(3, contactStore.getAllContacts().size());
    }

    @Test
    public void testClearAllContactsFromList() {
        addTestContacts();
        contactStore.deleteAllContactsFromMemory();
        assertEquals(0, contactStore.getAllContacts().size());
    }
}
