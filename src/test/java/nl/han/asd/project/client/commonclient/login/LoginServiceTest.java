package nl.han.asd.project.client.commonclient.login;

import nl.han.asd.project.client.commonclient.utility.Validation;
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

    @Before
    public void setUp() {
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsUsernameEmpty() {
        Validation.validateUserAndPass(INVALID_USERNAME_EMPTY, VALID_PASSWORD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsPasswordEmpty() {
        Validation.validateUserAndPass(VALID_USERNAME, INVALID_PASSWORD_EMPTY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsUsernameTooLong() {
        Validation.validateUserAndPass(INVALID_USERNAME_TOO_LONG, VALID_PASSWORD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsPasswordTooLong() {
        Validation.validateUserAndPass(VALID_USERNAME, INVALID_PASSWORD_TOO_LONG);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsUsernameTooShort() {
        Validation.validateUserAndPass(INVALID_USERNAME_TOO_SHORT, VALID_PASSWORD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsPasswordTooShort() {
        Validation.validateUserAndPass(VALID_USERNAME, INVALID_PASSWORD_TOO_SHORT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUsernameContainsForbiddenCharacters() {
        Validation.validateUserAndPass(INVALID_USERNAME_FORBIDDEN_CHARACTERS, VALID_PASSWORD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPasswordContainsForbiddenCharacters() {
        Validation.validateUserAndPass(VALID_USERNAME, INVALID_PASSWORD_FORBIDDEN_CHARACTERS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsUsernameNull() {
        Validation.validateUserAndPass(INVALID_USERNAME_NULL, VALID_PASSWORD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsPasswordNull() {
        Validation.validateUserAndPass(VALID_USERNAME, INVALID_PASSWORD_NULL);
    }

}
