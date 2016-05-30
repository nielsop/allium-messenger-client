package nl.han.asd.project.client.commonclient.login;

import nl.han.asd.project.client.commonclient.utility.Validation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
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
        Validation.validateCredentials(INVALID_USERNAME_EMPTY, VALID_PASSWORD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsPasswordEmpty() {
        Validation.validateCredentials(VALID_USERNAME, INVALID_PASSWORD_EMPTY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsUsernameTooLong() {
        Validation.validateCredentials(INVALID_USERNAME_TOO_LONG, VALID_PASSWORD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsPasswordTooLong() {
        Validation.validateCredentials(VALID_USERNAME, INVALID_PASSWORD_TOO_LONG);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsUsernameTooShort() {
        Validation.validateCredentials(INVALID_USERNAME_TOO_SHORT, VALID_PASSWORD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsPasswordTooShort() {
        Validation.validateCredentials(VALID_USERNAME, INVALID_PASSWORD_TOO_SHORT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUsernameContainsForbiddenCharacters() {
        Validation.validateCredentials(INVALID_USERNAME_FORBIDDEN_CHARACTERS, VALID_PASSWORD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPasswordContainsForbiddenCharacters() {
        Validation.validateCredentials(VALID_USERNAME, INVALID_PASSWORD_FORBIDDEN_CHARACTERS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsUsernameNull() {
        Validation.validateCredentials(INVALID_USERNAME_NULL, VALID_PASSWORD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsPasswordNull() {
        Validation.validateCredentials(VALID_USERNAME, INVALID_PASSWORD_NULL);
    }
}
