package nl.han.asd.project.client.commonclient.message;

import nl.han.asd.project.client.commonclient.store.Contact;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;

/**
 * @author Julius
 * @version 1.0
 * @since 09/05/16
 */
public class MessageTest {

    private Message message;
    private String testdata;
    @Mock
    private Contact contactSender;
    @Mock
    private Contact contactReceiver;

    @Before
    public void setUp(){
        testdata = "testdata";
        message = new Message(testdata,contactSender,contactReceiver, System.currentTimeMillis());
    }

    @Test
    public void testGetReceiver() throws Exception {
        assertEquals(contactReceiver,message.getReceiver());
    }

    @Test
    public void testGetSender() throws Exception {
        assertEquals(contactSender,message.getSender());
    }

    @Test
    public void testGetText() throws Exception {
        assertEquals(testdata,message.getText());
    }
}
