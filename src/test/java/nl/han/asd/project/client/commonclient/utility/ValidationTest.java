package nl.han.asd.project.client.commonclient.utility;

import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by Bram Heijmink on 10-5-2016.
 */
public class ValidationTest {

    private static final String VALID_USERNAME_3CHARS = "OKE";
    private static final String VALID_USERNAME_40CHARS = "ThisIsAUsernameOf50CharactersIIIIIIIIIII";
    private static final String INVALID_USERNAME_2CHARS = "NO";
    private static final String INVALID_USERNAME_41CHARS = "ThisIsAUsernameOf51CharactersIIIIIIIIIIII";
    private static final String VALID_USERNAME_RIGHT_CHARS = "This-Is_Valid-Username";
    private static final String INVALID_USERNAME_WRONG_CHARS = "WrongUsername*&^%$";

    private static final String INVALID_PASSWORD_7CHARS = "Invalid";
    private static final String VALID_PASSWORD_8CHARS = "ThisIsOk";
    private static final String VALID_PASSWORD_40CHARS = "ThisIsAPasswordOf40CharactersOOOOOOOOOOO";
    private static final String INVALID_PASSWORD_41CHARS = "ThisIsAPasswordOf41CharactersOOOOOOOOOOOO";
    private static final String VALID_PASSWORD_RIGHT_CHARS = "This-Is_Valid-Password";
    private static final String INVALID_PASSWORD_WRONG_CHARS = "WrongPassword*&@#$";

    /* Testing ipAddress */
    @Test
    public void testValidIPV4Address() {
        assertEquals(Validation.isValidAddress("64.233.161.147"), true);
    }

    @Test
    public void testValidIPV6AddressLong() {
        assertEquals(Validation.isValidAddress("2001:cdba:0000:0000:0000:0000:3257:9652"), true);
    }

    @Test
    public void testValidIPV6AddressSingleZeroGroup() {
        assertEquals(Validation.isValidAddress("2001:cdba:0:0:0:0:3257:9652"), true);
    }

    @Test
    public void testValidIPV6AddressNoZeroGroup() {
        assertEquals(Validation.isValidAddress("2001:cdba::3257:9652"), true);
    }

    @Test
    public void testInvalidIPAddress() {
        assertEquals(Validation.isValidAddress("df.34.23.23fsd"), false);
    }

    /* Testing ports */
    @Test
    public void testValidPort1023() { assertEquals(Validation.isValidPort(1023), false) ; }

    @Test
    public void testValidPort1024() { assertEquals(Validation.isValidPort(1024), true) ; }

    @Test
    public void testValidPort65535() { assertEquals(Validation.isValidPort(65535), true) ; }

    @Test
    public void testInvalidPort65536() { assertEquals(Validation.isValidPort(65536), false) ; }

    /* Testing validating username */
    @Test (expected =  IllegalArgumentException.class)
    public void testValidationRegister2CharsUsernameFailed() {
        Validation.validateCredentials(INVALID_USERNAME_2CHARS, VALID_PASSWORD_8CHARS);
    }

    @Test
    public void testValidationRegister3CharsUsernameSuccess() {
        assertTrue(Validation.validateCredentials(VALID_USERNAME_3CHARS, VALID_PASSWORD_8CHARS));
    }

    @Test
    public void testValidationRegister40CharsUsernameSuccess() {
        assertTrue(Validation.validateCredentials(VALID_USERNAME_40CHARS, VALID_PASSWORD_8CHARS));
    }

    @Test (expected =  IllegalArgumentException.class)
    public void testValidationRegister41CharsUsernameFailed() {
        Validation.validateCredentials(INVALID_USERNAME_41CHARS, VALID_PASSWORD_8CHARS);
    }

    @Test
    public void testValidationRegisterUsernameWithRightChars() {
        assertTrue(Validation.validateCredentials(VALID_USERNAME_RIGHT_CHARS, VALID_PASSWORD_8CHARS));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testValidationRegisterUsernameWithWrongChars() {
        Validation.validateCredentials(INVALID_USERNAME_WRONG_CHARS, VALID_PASSWORD_8CHARS);
    }

    /* Testing validating password */
    @Test (expected = IllegalArgumentException.class)
    public void testValidationRegister7CharsPasswordFailed() {
        Validation.validateCredentials(VALID_USERNAME_3CHARS, INVALID_PASSWORD_7CHARS);
    }

    @Test
    public void testValidationRegister8CharsPasswordSuccess() {
        assertTrue(Validation.validateCredentials(VALID_USERNAME_3CHARS, VALID_PASSWORD_8CHARS));
    }

    @Test
    public void testMasterGatewayRegister40CharsPasswordSuccess() {
        assertTrue(Validation.validateCredentials(VALID_USERNAME_3CHARS, VALID_PASSWORD_40CHARS));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testValidationRegister41CharsPasswordFailed() {
        Validation.validateCredentials(VALID_USERNAME_3CHARS, INVALID_PASSWORD_41CHARS);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testValidationRegisterWrongCharsFailed() {
        Validation.validateCredentials(VALID_USERNAME_3CHARS, INVALID_PASSWORD_WRONG_CHARS);
    }

    @Test
    public void testValidationRegisterRightCharsSucces() {
        assertTrue(Validation.validateCredentials(VALID_USERNAME_3CHARS, VALID_PASSWORD_RIGHT_CHARS));
    }
}

