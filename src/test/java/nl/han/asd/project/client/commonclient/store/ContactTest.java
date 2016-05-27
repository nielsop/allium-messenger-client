package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.graph.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author DDulos
 * @version 1.0
 * @since 27-May-16
 */
public class ContactTest {

    private static final String TEST_CONTACT_USERNAME = "testUsername";
    private static final String TEST_CONTACT_PUBLICKEY = "test1234567890";
    private static final String TEST_CONTACT_NEW_PUBLICKEY = "test1234567NEW";
    private Contact testContact;
    private Node[] testConnectedNodesArray;

    @Before
    public void initialize() throws Exception {
        testContact = new Contact(TEST_CONTACT_USERNAME, TEST_CONTACT_PUBLICKEY);
        testContact.setOnline(true);

        testConnectedNodesArray = new Node[3];
        for (int i = 0; i < testConnectedNodesArray.length; i++) {
            testConnectedNodesArray[i] = new Node("Node_ID" + (i + 1), "127.0.0.1", 1234, ("12345678" + i).getBytes());
        }
    }

    @Test
    public void testCreatedContactHasCorrectUsername() throws Exception {
        assertEquals(TEST_CONTACT_USERNAME, testContact.getUsername());
    }

    @Test
    public void testCreatedContactHasCorrectPublicKey() throws Exception {
        assertEquals(TEST_CONTACT_PUBLICKEY, testContact.getPublicKey());
    }

    @Test
    public void testCreatedContactHasCorrectOnlineStatus() throws Exception {
        assertEquals(true, testContact.isOnline());
    }

    @Test
    public void testSetConnectedNodes() throws Exception {
        testContact.setConnectedNodes(testConnectedNodesArray);

        assertEquals(testConnectedNodesArray[0].getId(), testContact.getConnectedNodes()[0].getId());
    }

    @Test
    public void testSetOnlineStatus() throws Exception {
        testContact.setOnline(false);

        assertEquals(false, testContact.isOnline());
    }

    @Test
    public void testSetPublicKey() throws Exception {
        testContact.setPublicKey(TEST_CONTACT_NEW_PUBLICKEY);

        assertEquals(TEST_CONTACT_NEW_PUBLICKEY, testContact.getPublicKey());
    }

    @Test(expected = NoConnectedNodesException.class)
    public void testThrowNoConnectedNodesException() throws NoConnectedNodesException {
        Node[] connectedNodes = testContact.getConnectedNodes();
    }
}