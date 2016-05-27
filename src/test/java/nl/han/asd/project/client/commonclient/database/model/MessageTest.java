package nl.han.asd.project.client.commonclient.database.model;

import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.store.Contact;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 *
 * @author Niels Bokmans
 * @version 1.0
 * @since 23-5-2016
 */
public class MessageTest {

    private static Message message;
    private static Message sameMessage;
    private static Message differentMessage;

    private static final int PRIMARY_ID = 1;
    private static final int SECONDARY_ID = 2;
    private static final String PRIMARY_USERNAME = "TestUsername";
    private static final String SECONDARY_USERNAME = "TestUsername2";
    private static final Date DATE = new Date();
    private static final Timestamp TIMESTAMP = new Timestamp(DATE.getTime());
    private static final String PRIMARY_MESSAGE = "Testmessage";
    private static final String SECONDARY_MESSAGE = "Testmessage2";
    private static final String PRIMARY_MESSAGE_TOSTRING =
            "Message[sender=" + PRIMARY_USERNAME + ", timestamp=" + DATE + ", text=" + PRIMARY_MESSAGE + "]";

    @BeforeClass
    public static void beforeClass() {
        message = new Message(PRIMARY_ID, new Contact(PRIMARY_USERNAME), DATE, PRIMARY_MESSAGE);
        sameMessage = new Message(PRIMARY_ID, new Contact(PRIMARY_USERNAME), DATE, PRIMARY_MESSAGE);
        differentMessage = new Message(SECONDARY_ID, new Contact(SECONDARY_USERNAME), new Date(), SECONDARY_MESSAGE);
    }

    @Test
    public void testGetId() {
        Assert.assertEquals(PRIMARY_ID, message.getDatabaseId());
    }

    @Test
    public void testGetText() {
        Assert.assertEquals(PRIMARY_MESSAGE, message.getText());
    }

    @Test
    public void testGetTimestamp() {
        Assert.assertEquals(DATE, message.getMessageTimestamp());
    }

    @Test
    public void testGetSender() {
        final Contact newContact = new Contact(PRIMARY_USERNAME);
        Assert.assertEquals(newContact, message.getSender());
    }

    @Test
    public void testFromDatabaseSuccess() throws SQLException {
        final ResultSet set = Mockito.mock(ResultSet.class);
        Mockito.when(set.getObject(1)).thenReturn(1);
        Mockito.when(set.getObject(2)).thenReturn(PRIMARY_USERNAME);
        Mockito.when(set.getTimestamp(3)).thenReturn(TIMESTAMP);
        Mockito.when(set.getObject(4)).thenReturn(PRIMARY_MESSAGE);
        final Message messageFromDatabase = Message.fromDatabase(set);
        Assert.assertEquals(message, messageFromDatabase);
    }

    @Test
    public void testEqualsIsEqualWithSameObjectTypeAndSameVariables() {
        Assert.assertEquals(sameMessage, message);
    }

    @Test
    public void testEqualsIsNotEqualWithSameObjectTypeAndDifferentVariables() {
        Assert.assertNotEquals(differentMessage, message);
    }

    @Test
    public void testEqualsIsNotEqualWithDifferentObjectType() {
        System.out.println(message);
        Assert.assertNotEquals("Test", message);
    }

    @Test
    public void testToStringIsEqualToExpectedToString() {
        Assert.assertEquals(PRIMARY_MESSAGE_TOSTRING, message.toString());
    }

    @Test
    public void testToStringIsEqualToSameObjectInDifferentVariable() {
        Assert.assertEquals(sameMessage.toString(), message.toString());
    }

    @Test
    public void testToStringIsNotEqualToDifferentObject() {
        Assert.assertNotEquals(differentMessage.toString(), message.toString());
    }

    @Test
    public void testHashCodeIsEqualWithSameObject() {
        Assert.assertEquals(sameMessage.hashCode(), message.hashCode());
    }

    @Test
    public void testHashCodeIsNotEqualWithDifferentObject() {
        Assert.assertNotEquals(differentMessage.hashCode(), message.hashCode());
    }
}