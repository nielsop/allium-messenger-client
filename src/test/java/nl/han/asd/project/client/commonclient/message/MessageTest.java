package nl.han.asd.project.client.commonclient.message;

import nl.han.asd.project.client.commonclient.store.Contact;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class MessageTest {

    private Date date = new Date();
    private String testData = "testData";
    private Message message;

    private String username;
    private byte[] array;
    private Boolean online;
    private Contact sender;

    @Mock
    private Contact contactReceiver;

    @Before
    public void setUp() {
        username = "Username";
        array = new byte[]{123, 123};
        online = true;
        sender = new Contact(username, array, online);
        message = new Message(sender, contactReceiver, date, testData);
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
        String stringToBeBuild = "Message[sender=Username, timestamp=" + date + ", text=testData]";
        final StringBuilder sb = new StringBuilder();
        sb.append("Message[sender=").append(sender.getUsername()).append(", timestamp=").append(message.getMessageTimestamp()).append(", text=").append(message.getText()).append("]");
        assertEquals(stringToBeBuild, message.toString());
    }

    @Test
    public void equalsThrowsFalseWhenComparedWithNull() throws Exception {
        assertFalse(message.equals(null));
    }

    @Test
    public void getMessageTimeStampReturnsRightTimeStamp() throws Exception {
        assertEquals(message.getMessageTimestamp(), date);
    }
}
