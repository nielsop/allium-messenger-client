package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.persistence.IPersistence;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

/**
 * @author DDulos
 * @version 1.0
 * @since 27-May-16
 */
public class CurrentUserTest {

    private static final String TEST_CURRENTUSER_USERNAME = "testuser";
    private static final byte[] TEST_CURRENTUSER_PUBLICKEY = "thisIsTestPublicKey".getBytes();
    private static final String TEST_CURRENTUSER_SECRETHASH = "thisIsATestSecretHash";

    private ContactStore contactStore;

    @Before
    public void initialize() throws Exception {
        contactStore = new ContactStore(Mockito.mock(IPersistence.class));
    }

    @Test
    public void testCheckCurrentUserData() throws Exception {
        // Test
        contactStore.setCurrentUser(new CurrentUser(TEST_CURRENTUSER_USERNAME, TEST_CURRENTUSER_PUBLICKEY, TEST_CURRENTUSER_SECRETHASH));
        CurrentUser currentUser = contactStore.getCurrentUser();

        // Assert
        assertEquals(TEST_CURRENTUSER_USERNAME, contactStore.getCurrentUserAsContact().getUsername());
        assertEquals(TEST_CURRENTUSER_PUBLICKEY, contactStore.getCurrentUserAsContact().getPublicKey());
        assertEquals(TEST_CURRENTUSER_SECRETHASH, currentUser.getSecretHash());
    }
}