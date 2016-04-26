package nl.han.asd.project.client.commonclient.login;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 18-4-2016
 */
public class LoginServiceTest {

    /* Valid credentials */
    private static final String VALID_USERNAME = "TestUsername";
    private static final String VALID_PASSWORD = "TestPassword";
    /* Invalid credentials */
    private static final String INVALID_USERNAME_EMPTY = "";
    private static final String INVALID_PASSWORD_EMPTY = "";
    private static final String INVALID_USERNAME_FORBIDDEN_CHARACTERS = "Test^Username";
    private static final String INVALID_PASSWORD_FORBIDDEN_CHARACTERS = "Test^Password";
    private static final String INVALID_USERNAME_TOO_LONG = ""; // Aanname dat de maximum lengte van een username 12 tekens is.
    private static final String INVALID_PASSWORD_TOO_LONG = ""; // Aanname dat de maximum lengte van een password 16 tekens is.
    private static final String INVALID_USERNAME_TOO_SHORT = ""; // Aanname dat de minimum lengte van een username 3 tekens is.
    private static final String INVALID_PASSWORD_TOO_SHORT = ""; // Aanname dat de minimum lengte van een password 8 tekens is.
    private static final String INVALID_USERNAME_NULL = null;
    private static final String INVALID_PASSWORD_NULL = null;
    private LoginService loginService;

    @Before
    public void setUp() {
        loginService = new LoginService();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsUsernameEmpty() {
        loginService.validateLoginData(INVALID_USERNAME_EMPTY, VALID_PASSWORD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsPasswordEmpty() {
        loginService.validateLoginData(VALID_USERNAME, INVALID_PASSWORD_EMPTY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsUsernameTooLong() {
        loginService.validateLoginData(INVALID_USERNAME_TOO_LONG, VALID_PASSWORD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsPasswordTooLong() {
        loginService.validateLoginData(VALID_USERNAME, INVALID_PASSWORD_TOO_LONG);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsUsernameTooShort() {
        loginService.validateLoginData(INVALID_USERNAME_TOO_SHORT, VALID_PASSWORD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsPasswordTooShort() {
        loginService.validateLoginData(VALID_USERNAME, INVALID_PASSWORD_TOO_SHORT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUsernameContainsForbiddenCharacters() {
        loginService.validateLoginData(INVALID_USERNAME_FORBIDDEN_CHARACTERS, VALID_PASSWORD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPasswordContainsForbiddenCharacters() {
        loginService.validateLoginData(VALID_USERNAME, INVALID_PASSWORD_FORBIDDEN_CHARACTERS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsUsernameNull() {
        loginService.validateLoginData(INVALID_USERNAME_NULL, VALID_PASSWORD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsPasswordNull() {
        loginService.validateLoginData(VALID_USERNAME, INVALID_PASSWORD_NULL);
    }

}