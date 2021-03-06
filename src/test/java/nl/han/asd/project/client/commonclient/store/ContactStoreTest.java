package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.graph.IGetVertices;
import nl.han.asd.project.client.commonclient.persistence.IPersistence;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class ContactStoreTest {

    private IPersistence persistence;
    private IGetVertices getVertices;
    private ContactStore contactStore;

    private  static final String TEST_CONTACT1 = "testContact1";
    private  static final String TEST_CONTACT2 = "testContact2";
    private  static final String TEST_CONTACT3 = "testContact3";
    private  static final String TEST_CONTACT4 = "testContact4";
    private Map<String, Contact> mockedSingleContactList;
    private Map<String, Contact> mockedMultipleContactList;
    private Map<String, Contact> mockDeletedContactList;

    @Before
    public void initialize() {
        persistence = Mockito.mock(IPersistence.class);
        getVertices = Mockito.mock(IGetVertices.class);
        contactStore = new ContactStore(persistence, getVertices);

        mockedSingleContactList = new HashMap<>();
        mockedMultipleContactList = new HashMap<>();
        mockDeletedContactList = new HashMap<>();
        initializeMockContacts();
    }

    private void initializeMockContacts() {
        mockedSingleContactList.put(TEST_CONTACT1, new Contact(TEST_CONTACT1));

        mockedMultipleContactList.put(TEST_CONTACT1, new Contact(TEST_CONTACT1));
        mockedMultipleContactList.put(TEST_CONTACT2, new Contact(TEST_CONTACT2));
        mockedMultipleContactList.put(TEST_CONTACT3, new Contact(TEST_CONTACT3));
        mockedMultipleContactList.put(TEST_CONTACT4, new Contact(TEST_CONTACT4));

        mockDeletedContactList.put(TEST_CONTACT1, new Contact(TEST_CONTACT1));
        mockDeletedContactList.put(TEST_CONTACT3, new Contact(TEST_CONTACT3));
        mockDeletedContactList.put(TEST_CONTACT4, new Contact(TEST_CONTACT4));
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
        when(persistence.getContacts()).thenReturn(mockedMultipleContactList);

        // Test
        addTestContacts();

        // Assert
        assertNotNull(contactStore.findContact(TEST_CONTACT2));
        assertEquals(4, contactStore.getAllContacts().size());

        contactStore.removeContact(TEST_CONTACT2);
        when(persistence.getContacts()).thenReturn(mockDeletedContactList);

        assertNull(contactStore.findContact(TEST_CONTACT2));
        assertEquals(3, contactStore.getAllContacts().size());
    }
}
