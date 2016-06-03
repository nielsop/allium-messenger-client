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

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 24-5-2016
 */
@RunWith(MockitoJUnitRunner.class)
public class PersistenceServiceIT {

    private static final Contact CONTACT_1 = new Contact("Testcontact");
    private static final Contact CONTACT_2 = new Contact("Testcontact2");
    private static final Message TEST_MESSAGE_1 = new Message(-1, CONTACT_1, CONTACT_2, new Date(), "Testmessage");
    private static final Message TEST_MESSAGE_2 = new Message(-1, CONTACT_1, CONTACT_2, new Date(), "Testmessage2");
    private static final Message TEST_MESSAGE_3 = new Message(-1, CONTACT_2, CONTACT_1, new Date(), "Testmessage3");
    private static final String SCRIPT_1_NAME = "TestScript";
    private static final String SCRIPT_1_CONTENT = "segment data begin " + "     text testMessage value \"If you receive this message, I did not delay it and I am in trouble. Send help! \" "
            + "     text reactionMessage value \"delayMessage abq1\" " + "     datetime testMessageSendTime value 2020-05-18 " + "segment end "
            + "segment contact begin " + "     person mark value \"MarkVaessen\" " + "segment end " + "segment main begin " + "     schedule testMessageSendTime begin "
            + "         send using testMessage as message mark as contact " + "     end " + "     react reactionMessage begin "
            + "         set testMessageSendTime value 2040-05-18 " + "     end " + "segment end ";
    private IPersistence persistenceService;

    @Before
    public void setupTest() throws SQLException {
        final HyperSQLDatabase database = new HyperSQLDatabase();
        database.init("test", "test123");
        database.resetDatabase();
        persistenceService = new PersistenceService(database);
    }

    @Test
    public void testSaveMessageSuccessful() throws SQLException {
        Assert.assertTrue(persistenceService.saveMessage(TEST_MESSAGE_1));
    }

    @Test
    public void testSaveMessageThenDeleteMessageSuccessful() throws SQLException {
        persistenceService.saveMessage(TEST_MESSAGE_1);
        persistenceService.saveMessage(TEST_MESSAGE_2);
        List<Message> allMessages = persistenceService.getAllMessages();
        persistenceService.deleteMessage(1);
        Assert.assertEquals(2, persistenceService.getAllMessages().get(0).getDatabaseId());
    }

    @Test
    public void testSaveTwoMessagesAutoIncrementIdSuccessful() throws SQLException {
        persistenceService.saveMessage(TEST_MESSAGE_1);
        persistenceService.saveMessage(TEST_MESSAGE_2);
        final List<Message> messages = persistenceService.getAllMessages();
        Assert.assertEquals(2, messages.size());
        Assert.assertEquals(2, messages.get(messages.size() - 1).getDatabaseId());
    }

    @Test
    public void testAutoIncrementIdSuccessfulAfterDelete() throws SQLException {
        persistenceService.saveMessage(TEST_MESSAGE_1);
        persistenceService.saveMessage(TEST_MESSAGE_2);
        persistenceService.deleteMessage(1);
        persistenceService.saveMessage(TEST_MESSAGE_3);
        final List<Message> messages = persistenceService.getAllMessages();
        Assert.assertEquals(3, messages.get(messages.size() - 1).getDatabaseId());
    }

    @Test
    public void testGetAllMessagesIsEmptyWithoutAdding() throws SQLException {
        Assert.assertTrue(persistenceService.getAllMessages().size() == 0);
    }

    @Test
    public void testGetAllMessagesReturnsOneMessageAfterSavingAMessage() throws SQLException {
        persistenceService.saveMessage(TEST_MESSAGE_1);
        Assert.assertTrue(persistenceService.getAllMessages().size() == 1);
    }

    @Test
    public void testGetAllMessagesPerContactSuccessfullyMapsContactsToMessages() throws SQLException {
        persistenceService.saveMessage(TEST_MESSAGE_1);
        persistenceService.saveMessage(TEST_MESSAGE_2);
        persistenceService.saveMessage(TEST_MESSAGE_3);
        final Map<Contact, List<Message>> contactMessagesMap = persistenceService.getAllMessagesPerContact();
        Assert.assertEquals(2, contactMessagesMap.size());
        Assert.assertEquals(2, contactMessagesMap.get(CONTACT_1).size());
    }

    @Test
    public void testAddContactSuccessful() throws SQLException {
        Assert.assertTrue(persistenceService.addContact(CONTACT_1.getUsername()));
        Assert.assertTrue(persistenceService.getContacts().contains(CONTACT_1));
    }

    @Test
    public void testDeleteContactSuccessfulIfContactExists() throws SQLException {
        Assert.assertTrue(persistenceService.addContact("Testcontact"));
        Assert.assertTrue(persistenceService.deleteContact("Testcontact"));
    }

    @Test
    public void testGetContactsEmptyUponStart() throws SQLException {
        Assert.assertTrue(persistenceService.getContacts().isEmpty());
    }

    @Test
    public void testGetContactsSuccessfulAfterAdd() throws SQLException {
        persistenceService.addContact("Testcontact");
        Assert.assertEquals(1, persistenceService.getContacts().size());
    }

    @Test
    public void testGetContactsSuccessfulAfterDeleteThenAdd() throws SQLException {
        persistenceService.addContact("Testcontact");
        persistenceService.addContact("Testcontact2");
        persistenceService.addContact("Testcontact3");
        persistenceService.deleteContact("Testcontact2");
        Assert.assertEquals(2, persistenceService.getContacts().size());
    }

    @Test
    public void testGetAllScriptsSuccessful() {
        persistenceService.addScript(SCRIPT_1_NAME, SCRIPT_1_CONTENT);
        final Map<String, String> scripts = persistenceService.getScripts();
        Assert.assertEquals(1, scripts.size());
        final Map.Entry<String, String> firstScript = scripts.entrySet().iterator().next();
        Assert.assertEquals(SCRIPT_1_NAME, firstScript.getKey());
        Assert.assertEquals(SCRIPT_1_CONTENT, firstScript.getValue());
    }

    @Test
    public void testAddScriptSuccessful() {
        Assert.assertTrue(persistenceService.addScript(SCRIPT_1_NAME, SCRIPT_1_CONTENT));
        Assert.assertTrue(persistenceService.getScripts().containsKey(SCRIPT_1_NAME));
    }

    @Test
    public void testDeleteScriptSuccessful() {
        persistenceService.addScript(SCRIPT_1_NAME, SCRIPT_1_CONTENT);
        Assert.assertEquals(1, persistenceService.getScripts().size());
        Assert.assertTrue(persistenceService.deleteScript(SCRIPT_1_NAME));
        Assert.assertFalse(persistenceService.getScripts().containsKey(SCRIPT_1_NAME));

    }

}
