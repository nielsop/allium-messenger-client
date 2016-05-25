package nl.han.asd.project.client.commonclient.login;

import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import nl.han.asd.project.client.commonclient.master.IAuthentication;

public class LoginServiceTest {

    /* Valid credentials */
    private static final String VALID_USERNAME = "TestUsername";
    private static final String VALID_PASSWORD = "TestPassword";
    /* Invalid credentials */
    private static final String INVALID_USERNAME_EMPTY = "";
    private static final String INVALID_PASSWORD_EMPTY = "";
    private static final String INVALID_USERNAME_FORBIDDEN_CHARACTERS = "Test^Username";
    private static final String INVALID_USERNAME_TOO_LONG = ""; // Aanname dat de maximum lengte van een username 12 tekens is.
    private static final String INVALID_PASSWORD_TOO_LONG = ""; // Aanname dat de maximum lengte van een password 16 tekens is.
    private static final String INVALID_USERNAME_TOO_SHORT = ""; // Aanname dat de minimum lengte van een username 3 tekens is.
    private static final String INVALID_PASSWORD_TOO_SHORT = ""; // Aanname dat de minimum lengte van een password 8 tekens is.
    private static final String INVALID_USERNAME_NULL = null;
    private static final String INVALID_PASSWORD_NULL = null;

    private IAuthentication authenticationMock;

    private ILogin login;

    @Before
    public void setUp() {
        authenticationMock = mock(IAuthentication.class);

        login = new LoginService(authenticationMock);
    }

    @Test(expected = IllegalUsernameException.class)
    public void testIsUsernameEmpty() throws Exception {
        login.login(INVALID_USERNAME_EMPTY, VALID_PASSWORD);
    }

    @Test(expected = IllegalPasswordException.class)
    public void testIsPasswordEmpty() throws Exception {
        login.login(VALID_USERNAME, INVALID_PASSWORD_EMPTY);
    }

    @Test(expected = IllegalUsernameException.class)
    public void testIsUsernameTooLong() throws Exception {
        login.login(INVALID_USERNAME_TOO_LONG, VALID_PASSWORD);
    }

    @Test(expected = IllegalPasswordException.class)
    public void testIsPasswordTooLong() throws Exception {
        login.login(VALID_USERNAME, INVALID_PASSWORD_TOO_LONG);
    }

    @Test(expected = IllegalUsernameException.class)
    public void testIsUsernameTooShort() throws Exception {
        login.login(INVALID_USERNAME_TOO_SHORT, VALID_PASSWORD);
    }

    @Test(expected = IllegalPasswordException.class)
    public void testIsPasswordTooShort() throws Exception {
        login.login(VALID_USERNAME, INVALID_PASSWORD_TOO_SHORT);
    }

    @Test(expected = IllegalUsernameException.class)
    public void testUsernameContainsForbiddenCharacters() throws Exception {
        login.login(INVALID_USERNAME_FORBIDDEN_CHARACTERS, VALID_PASSWORD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsUsernameNull() throws Exception {
        login.login(INVALID_USERNAME_NULL, VALID_PASSWORD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsPasswordNull() throws Exception {
        login.login(VALID_USERNAME, INVALID_PASSWORD_NULL);
    }

}
