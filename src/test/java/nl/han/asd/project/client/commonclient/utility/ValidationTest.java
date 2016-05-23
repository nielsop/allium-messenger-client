package nl.han.asd.project.client.commonclient.utility;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Bram Heijmink on 10-5-2016.
 */
public class ValidationTest {
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
        Validation.validateLoginData(INVALID_USERNAME_EMPTY, VALID_PASSWORD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsPasswordEmpty() {
        Validation.validateLoginData(VALID_USERNAME, INVALID_PASSWORD_EMPTY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsUsernameTooLong() {
        Validation.validateLoginData(INVALID_USERNAME_TOO_LONG, VALID_PASSWORD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsPasswordTooLong() {
        Validation.validateLoginData(VALID_USERNAME, INVALID_PASSWORD_TOO_LONG);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsUsernameTooShort() {
        Validation.validateLoginData(INVALID_USERNAME_TOO_SHORT, VALID_PASSWORD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsPasswordTooShort() {
        Validation.validateLoginData(VALID_USERNAME, INVALID_PASSWORD_TOO_SHORT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUsernameContainsForbiddenCharacters() {
        Validation.validateLoginData(INVALID_USERNAME_FORBIDDEN_CHARACTERS, VALID_PASSWORD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPasswordContainsForbiddenCharacters() {
        Validation.validateLoginData(VALID_USERNAME, INVALID_PASSWORD_FORBIDDEN_CHARACTERS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsUsernameNull() {
        Validation.validateLoginData(INVALID_USERNAME_NULL, VALID_PASSWORD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsPasswordNull() {
        Validation.validateLoginData(VALID_USERNAME, INVALID_PASSWORD_NULL);
    }

    @Test
    public void TestValidIPV4Address() {
        assertEquals(Validation.isValidAddress("64.233.161.147"), true);
    }

    @Test
    public void TestValidIPV6AddressLong() {
        assertEquals(Validation.isValidAddress("2001:cdba:0000:0000:0000:0000:3257:9652"), true);
    }

    @Test
    public void TestValidIPV6AddressSingleZeroGroup() {
        assertEquals(Validation.isValidAddress("2001:cdba:0:0:0:0:3257:9652"), true);
    }

    @Test
    public void TestValidIPV6AddressNoZeroGroup() {
        assertEquals(Validation.isValidAddress("2001:cdba::3257:9652"), true);
    }

    @Test
    public void TestInvalidIPAddress() {
        assertEquals(Validation.isValidAddress("df.34.23.23fsd"), false);
    }
}
