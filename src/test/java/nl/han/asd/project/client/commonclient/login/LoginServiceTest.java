package nl.han.asd.project.client.commonclient.login;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 18-4-2016
 */
public class LoginServiceTest {

    private LoginService loginService;

    /* Valid credentials */
    private static final String VALID_USERNAME = "TestUsername";
    private static final String VALID_PASSWORD = "TestPassword";
    private static final String VALID_PUBLIC_KEY = "0e1b8b3ef01dad60a89c3b16b6eeff54";

    /* Invalid credentials */
    private static final String INVALID_USERNAME_EMPTY = "";
    private static final String INVALID_PASSWORD_EMPTY = "";
    private static final String INVALID_PUBLIC_KEY_EMPTY = "";

    private static final String INVALID_USERNAME_FORBIDDEN_CHARACTERS = "Test^Username";
    private static final String INVALID_PASSWORD_FORBIDDEN_CHARACTERS = "Test^Password";
    private static final String INVALID_PUBLIC_KEY_FORBIDDEN_CHARACTERS = "0e1b8b3ef01dad6^a89c3b16b6eeff54";

    private static final String INVALID_USERNAME_TOO_LONG = ""; // Aanname dat de maximum lengte van een username 12 tekens is.
    private static final String INVALID_PASSWORD_TOO_LONG = ""; // Aanname dat de maximum lengte van een password 16 tekens is.
    private static final String INVALID_PUBLIC_KEY_TOO_LONG = "0e1b8b3ef01dad60a89c3b16b6eeff54e"; // Aanname dat de maximum lengte van een public key 32 tekens is.

    private static final String INVALID_USERNAME_TOO_SHORT = ""; // Aanname dat de minimum lengte van een username 3 tekens is.
    private static final String INVALID_PASSWORD_TOO_SHORT = ""; // Aanname dat de minimum lengte van een password 8 tekens is.
    private static final String INVALID_PUBLIC_KEY_TOO_SHORT = "1234abcd5678"; // Aanname dat de minimum lengte van een public key 32 tekens is.

    private static final String INVALID_USERNAME_NULL = null;
    private static final String INVALID_PASSWORD_NULL = null;
    private static final String INVALID_PUBLIC_KEY_NULL = null;

    @Before
    public void setUp() {
        loginService = new LoginService();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsUsernameEmpty() {
        loginService.validateLoginData(INVALID_USERNAME_EMPTY, VALID_PASSWORD, VALID_PUBLIC_KEY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsPasswordEmpty() {
        loginService.validateLoginData(VALID_USERNAME, INVALID_PASSWORD_EMPTY, VALID_PUBLIC_KEY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsPublicKeyEmpty() {
        loginService.validateLoginData(VALID_USERNAME, VALID_PASSWORD, INVALID_PUBLIC_KEY_EMPTY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsUsernameTooLong() {
        loginService.validateLoginData(INVALID_USERNAME_TOO_LONG, VALID_PASSWORD, VALID_PUBLIC_KEY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsPasswordTooLong() {
        loginService.validateLoginData(VALID_USERNAME, INVALID_PASSWORD_TOO_LONG, VALID_PUBLIC_KEY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsPublicKeyTooLong() {
        loginService.validateLoginData(VALID_USERNAME, VALID_PASSWORD, INVALID_PUBLIC_KEY_TOO_LONG);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsUsernameTooShort() {
        loginService.validateLoginData(INVALID_USERNAME_TOO_SHORT, VALID_PASSWORD, VALID_PUBLIC_KEY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsPasswordTooShort() {
        loginService.validateLoginData(VALID_USERNAME, INVALID_PASSWORD_TOO_SHORT, VALID_PUBLIC_KEY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsPublicKeyTooShort() {
        loginService.validateLoginData(VALID_USERNAME, VALID_PASSWORD, INVALID_PUBLIC_KEY_TOO_SHORT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUsernameContainsForbiddenCharacters() {
        loginService.validateLoginData(INVALID_USERNAME_FORBIDDEN_CHARACTERS, VALID_PASSWORD, VALID_PUBLIC_KEY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPasswordContainsForbiddenCharacters() {
        loginService.validateLoginData(VALID_USERNAME, INVALID_PASSWORD_FORBIDDEN_CHARACTERS, VALID_PUBLIC_KEY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPublicKeyContainsForbiddenCharacters() {
        loginService.validateLoginData(VALID_USERNAME, VALID_PASSWORD, INVALID_PUBLIC_KEY_FORBIDDEN_CHARACTERS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsUsernameNull() {
        loginService.validateLoginData(INVALID_USERNAME_NULL, VALID_PASSWORD, VALID_PUBLIC_KEY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsPasswordNull() {
        loginService.validateLoginData(VALID_USERNAME, INVALID_PASSWORD_NULL, VALID_PUBLIC_KEY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsPublicKeyNull() {
        loginService.validateLoginData(VALID_USERNAME, VALID_PASSWORD, INVALID_PUBLIC_KEY_NULL);
    }

    @Test
    public void testIsValidLogin() {
        LoginService mockLoginService = Mockito.mock(LoginService.class);
        Mockito.when(mockLoginService.validateLoginData(VALID_USERNAME, VALID_PASSWORD, VALID_PUBLIC_KEY)).thenReturn(true);
        Mockito.when(mockLoginService.login(VALID_USERNAME, VALID_PASSWORD, VALID_PUBLIC_KEY)).thenReturn(true);
        Assert.assertTrue(mockLoginService.login(VALID_USERNAME, VALID_PASSWORD, VALID_PUBLIC_KEY));
    }
}