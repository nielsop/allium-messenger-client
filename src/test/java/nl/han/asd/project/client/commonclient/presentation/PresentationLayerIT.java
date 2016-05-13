package nl.han.asd.project.client.commonclient.presentation;

import com.google.inject.Guice;
import com.google.inject.Injector;
import nl.han.asd.project.client.commonclient.CommonclientModule;
import nl.han.asd.project.client.commonclient.utility.TestHelper;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Spy;
import org.mockito.internal.exceptions.ExceptionIncludingMockitoWarnings;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 *
 * @author Kenny
 * @version 1.0
 * @since 13-5-2016
 */
public class PresentationLayerIT {
    PresentationLayer pLayer;
    private String validUsername;
    private String validPassword = "TestPassword";
    private String invalidUsername = "AWayTooLongUsernameIsInvalid";
    private String invalidPassword = "AWayTooLongPasswordIsInvalid";

    @Before
    public void setup(){
        validUsername = TestHelper.createRandomValidUsername();
        Injector injector = Guice.createInjector(new CommonclientModule());
        pLayer = injector.getInstance(PresentationLayer.class);
    }

    @Test
    public void testRegisterRequestSucces() throws Exception{
        assertEquals(HanRoutingProtocol.ClientRegisterResponse.Status.SUCCES, pLayer.registerRequest(validUsername, validPassword));
    }

    @Test
    public void testRegisterRequestUsernameTaken() throws Exception {
        assertEquals(HanRoutingProtocol.ClientRegisterResponse.Status.SUCCES, pLayer.registerRequest(validUsername, validPassword));
        assertEquals(HanRoutingProtocol.ClientRegisterResponse.Status.TAKEN_USERNAME, pLayer.registerRequest(validUsername, validPassword));
    }

    @Ignore("Invalid username is not yet covered at master.")
    @Test
    public void testRegisterRequestInvalidUsername() throws Exception {
        assertEquals(HanRoutingProtocol.ClientRegisterResponse.Status.FAILED, pLayer.registerRequest(invalidUsername, validPassword));
    }

    @Ignore("Invalid password is not yet covered at master.")
    @Test
    public void testRegisterRequestInvalidPassword() throws Exception {
        assertEquals(HanRoutingProtocol.ClientRegisterResponse.Status.FAILED, pLayer.registerRequest(validUsername, invalidPassword));
    }

    @Ignore("Login check if user exists and if correct password has been set, is not yet covered at master.")
    @Test
    public void testLoginRequestSucces() throws Exception {
        pLayer.registerRequest(validUsername, validPassword);
        assertEquals(HanRoutingProtocol.ClientLoginResponse.Status.SUCCES, pLayer.loginRequest(validUsername, validPassword));
    }

    @Ignore("Checking for wrong password is not yet covered at master.")
    @Test
    public void testLoginRequestWrongPassword() throws  Exception {
        pLayer.registerRequest(validUsername, validPassword);
        assertEquals(HanRoutingProtocol.ClientLoginResponse.Status.INVALID_COMBINATION, pLayer.loginRequest(validUsername, "other pass"));
    }

    @Ignore("Checking if username exists is not yet covered at master.")
    @Test
    public void testLoginRequestUsernameNotRecognized() throws  Exception {
        pLayer.registerRequest(validUsername, validPassword);
        assertEquals(HanRoutingProtocol.ClientLoginResponse.Status.FAILED, pLayer.loginRequest(validUsername, "other pass"));
    }
}
