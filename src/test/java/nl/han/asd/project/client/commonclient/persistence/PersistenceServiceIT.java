package nl.han.asd.project.client.commonclient.persistence;

import nl.han.asd.project.client.commonclient.database.HyperSQLDatabase;
import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.store.Contact;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 24-5-2016
 */
@RunWith(MockitoJUnitRunner.class)
public class PersistenceServiceIT {

    private static final Contact CONTACT_1 = new Contact("Testcontact");
    private static final Contact CONTACT_2 = new Contact("Testcontact2");
    private static final Message TEST_MESSAGE_1 = new Message(-1, CONTACT_1, new Date(), "Testmessage");
    private static final Message TEST_MESSAGE_2 = new Message(-1, CONTACT_1, new Date(), "Testmessage2");
    private static final Message TEST_MESSAGE_3 = new Message(-1, CONTACT_2, new Date(), "Testmessage3");
    private IPersistence persistenceService;

    @Before
    public void setupTest() throws SQLException {
        final HyperSQLDatabase database = new HyperSQLDatabase("test", "test123");
        database.resetDatabase();
        persistenceService = new PersistenceService(database);
    }

    @Test
    public void testSaveMessageSuccessful() throws SQLException {
        assertTrue(persistenceService.saveMessage(TEST_MESSAGE_1));
    }

    @Test
    public void testSaveMessageThenDeleteMessageSuccessful() throws SQLException {
        persistenceService.saveMessage(TEST_MESSAGE_1);
        persistenceService.saveMessage(TEST_MESSAGE_2);
        persistenceService.deleteMessage(1);
        assertEquals(2, persistenceService.getAllMessages().get(0).getDatabaseId());
    }

    @Test
    public void testSaveTwoMessagesAutoIncrementIdSuccessful() throws SQLException {
        persistenceService.saveMessage(TEST_MESSAGE_1);
        persistenceService.saveMessage(TEST_MESSAGE_2);
        final List<Message> messages = persistenceService.getAllMessages();
        assertEquals(2, messages.size());
        assertEquals(2, messages.get(messages.size() - 1).getDatabaseId());
    }

    @Test
    public void testAutoIncrementIdSuccessfulAfterDelete() throws SQLException {
        persistenceService.saveMessage(TEST_MESSAGE_1);
        persistenceService.saveMessage(TEST_MESSAGE_2);
        persistenceService.deleteMessage(1);
        persistenceService.saveMessage(TEST_MESSAGE_3);
        final List<Message> messages = persistenceService.getAllMessages();
        assertEquals(3, messages.get(messages.size() - 1).getDatabaseId());
    }

    @Test
    public void testGetAllMessagesIsEmptyWithoutAdding() throws SQLException {
        assertTrue(persistenceService.getAllMessages().size() == 0);
    }

    @Test
    public void testGetAllMessagesReturnsOneMessageAfterSavingAMessage() throws SQLException {
        persistenceService.saveMessage(TEST_MESSAGE_1);
        assertTrue(persistenceService.getAllMessages().size() == 1);
    }

    @Test
    public void testGetAllMessagesPerContactSuccessfullyMapsContactsToMessages() throws SQLException {
        persistenceService.saveMessage(TEST_MESSAGE_1);
        persistenceService.saveMessage(TEST_MESSAGE_2);
        persistenceService.saveMessage(TEST_MESSAGE_3);
        final Map<Contact, List<Message>> contactMessagesMap = persistenceService.getAllMessagesPerContact();
        assertEquals(2, contactMessagesMap.size());
        assertEquals(2, contactMessagesMap.get(CONTACT_1).size());
    }

    @Test
    public void testAddContactSuccessful() throws SQLException {
        assertTrue(persistenceService.addContact(CONTACT_1.getUsername()));
        assertTrue(persistenceService.getContacts().contains(CONTACT_1));
    }

    @Test
    public void testDeleteContactSuccessfulIfContactExists() throws SQLException {
        assertTrue(persistenceService.addContact("Testcontact"));
        assertTrue(persistenceService.deleteContact("Testcontact"));
    }

    @Test
    public void testGetContactsEmptyUponStart() throws SQLException {
        assertTrue(persistenceService.getContacts().isEmpty());
    }

    @Test
    public void testGetContactsSuccessfulAfterAdd() throws SQLException {
        persistenceService.addContact("Testcontact");
        assertEquals(1, persistenceService.getContacts().size());
    }

    @Test
    public void testGetContactsSuccessfulAfterDeleteThenAdd() throws SQLException {
        persistenceService.addContact("Testcontact");
        persistenceService.addContact("Testcontact2");
        persistenceService.addContact("Testcontact3");
        persistenceService.deleteContact("Testcontact2");
        assertEquals(2, persistenceService.getContacts().size());
    }

    @Test
    public void testGetScriptSuccessAfterAdd()
    {
        persistenceService.addScript("name1", "Script");
        assertEquals(1, persistenceService.getScripts().size());
        persistenceService.addScript("name2", "Script");
        persistenceService.addScript("name3", "Script");
        assertEquals(3, persistenceService.getScripts().size());
    }

    @Test
    public void testGetScriptSuccessAfterAddAndDelete()
    {
        persistenceService.addScript("name1", "Script");
        persistenceService.addScript("name2", "Script");
        persistenceService.deleteScript("name1");
        assertEquals(1, persistenceService.getScripts().size());
        persistenceService.addScript("name3", "Script");
        persistenceService.addScript("name4", "Script");
        persistenceService.deleteScript("name3");
        assertEquals(2, persistenceService.getScripts().size());
        persistenceService.deleteScript("name3");
        assertEquals(2, persistenceService.getScripts().size());
    }

    @Test
    public void testGetScriptDontDeleteDuplication()
    {
        persistenceService.addScript("name1", "Script");
        persistenceService.addScript("name2", "Script");
        persistenceService.deleteScript("name1");
        assertEquals(2, persistenceService.getScripts().size());
        persistenceService.deleteScript("name1");
        assertEquals(2, persistenceService.getScripts().size());
    }







}
