package nl.han.asd.project.client.commonclient.presentation;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import nl.han.asd.project.client.commonclient.CommonclientModule;
import nl.han.asd.project.client.commonclient.master.IRegistration;
import nl.han.asd.project.client.commonclient.master.wrapper.RegisterResponseWrapper;
import nl.han.asd.project.client.commonclient.presentation.gui.GUI;
import nl.han.asd.project.client.commonclient.utility.TestHelper;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import static nl.han.asd.project.protocol.HanRoutingProtocol.ClientRegisterResponse;
import static org.junit.Assert.assertEquals;

/**
 * Created by Kenny on 18-4-2016.
 */
public class PresentationLayerTest {

    PresentationLayer pLayer;

    private String validUsername;
    private String validPassword = "TestPassword";
    private String invalidUsername = "AWayTooLongUsernameIsInvalid";
    private String invalidPassword = "AWayTooLongPasswordIsInvalid";

    @Before
    public void initialize() {
        validUsername = TestHelper.createRandomValidUsername();
        Injector injector = Guice.createInjector(new CommonclientModule());
        pLayer = injector.getInstance(PresentationLayer.class);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testRegisterRequestInvalidUsername() throws Exception {
        pLayer.registerRequest(invalidUsername, validPassword);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testRegisterRequestInvalidPassword() throws Exception {
        pLayer.registerRequest(validUsername, invalidPassword);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testGetLoginRequestInvalidUsername() throws Exception {
        pLayer.loginRequest(invalidUsername, validPassword);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testGetLoginRequestInvalidPassword() throws Exception {
        pLayer.loginRequest(validUsername, invalidPassword);
    }
}
