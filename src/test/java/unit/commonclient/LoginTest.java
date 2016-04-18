package unit.commonclient;

import nl.han.asd.project.client.commonclient.login.LoginService;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by DDulos on 13-Apr-16.
 */
public class LoginTest {
    private static final String CORRECT_USERNAME = "admin";
    private static final String CORRECT_PASSWORD = "test1234";
    private static final String CORRECT_PUBLICKEY = "asdf";

    private static final String INCORRECT_USERNAME = "tester";
    private static final String INCORRECT_PASSWORD = "test12";
    private static final String INCORRECT_PUBLICKEY = "fdsa";

    LoginService loginService;

    @Before
    public void setUp() {
        loginService = new LoginService();
    }

    @Test
    public void testLoginWithCorrectUsernameAndPassword() {
    }

//    @Test
//    public void testLoginWithCorrectUsernameAndIncorrectPassword() {
//        HanRoutingProtocol.ClientLoginResponse clientLoginResponse = loginService.authUser(CORRECT_USERNAME, CORRECT_PASSWORD, CORRECT_PUBLICKEY);
//        assertNotNull(clientLoginResponse.getStatus());
//        assertEquals(3, clientLoginResponse.getStatus());
//    }
//
//    @Test
//    public void testLoginWithIncorrectUsernameAndCorrectPassword() {
//        HanRoutingProtocol.ClientLoginResponse clientLoginResponse = loginService.authUser(CORRECT_USERNAME, CORRECT_PASSWORD, CORRECT_PUBLICKEY);
//        assertNotNull(clientLoginResponse.getStatus());
//        assertEquals(3, clientLoginResponse.getStatus());
//    }
//
//    @Test
//    public void testLoginWithIncorrectUsernameAndPassword() {
//        HanRoutingProtocol.ClientLoginResponse clientLoginResponse = loginService.authUser(CORRECT_USERNAME, CORRECT_PASSWORD, CORRECT_PUBLICKEY);
//        assertNotNull(clientLoginResponse.getStatus());
//        assertEquals(3, clientLoginResponse.getStatus());
//    }
//
//    @Test (expected = IllegalArgumentException.class)
//    public void testLoginWithEmptyUsername() {
//    }
//
//    @Test (expected = IllegalArgumentException.class)
//    public void testLoginWithEmptyPassword() {
//    }
//
//    @Test (expected = IllegalArgumentException.class)
//    public void testLoginWithEmptyUsernamAndPassword() {
//    }

}
