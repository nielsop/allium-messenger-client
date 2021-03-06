package nl.han.asd.project.client.commonclient.message;

import nl.han.asd.project.client.commonclient.store.Contact;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MessageTest {

    private Date date = new Date();
    private String testData = "testData";
    private Message message;
    private Message message2;
    private String messageId = "messageId12345";

    private String username;
    private byte[] array;
    private Boolean online;
    private Contact sender;
    private Contact contactReceiver;

    @Before
    public void setUp() {
        username = "Username";
        array = new byte[]{123, 123};
        online = true;
        sender = new Contact(username, array, online);
        contactReceiver = new Contact("username2", array, online);
        message = new Message(sender, contactReceiver, date, testData, messageId);
        message2 = new Message(sender, contactReceiver, date, testData, messageId);
    }

    @Test
    public void testGetSender() throws Exception {
        assertEquals(sender, message.getSender());
    }

    @Test
    public void testGetText() throws Exception {
        assertEquals(testData, message.getText());
    }

    @Test
    public void toStringCreatesRightString() throws Exception {
        final StringBuilder sb = new StringBuilder();
        sb.append("Message[messageId = " + messageId + ", sender=").append(message.getSender().getUsername()).append(", receiver=").append(message.getReceiver().getUsername()).append(", timestamp=").append(message.getMessageTimestamp()).append(", text=").append(message.getText()).append("]");
        assertEquals(sb.toString(), message.toString());
    }

    @Test
    public void equalsThrowsFalseWhenComparedWithNull() throws Exception {
        assertFalse(message.equals(null));
    }

    @Test
    public void equalsWithDifferentObjectButSameMessageContentReturnsTrue(){
        assertTrue(message.equals(message2));
    }

    @Test
    public void getMessageTimeStampReturnsRightTimeStamp() throws Exception {
        assertEquals(message.getMessageTimestamp(), date);
    }
}
