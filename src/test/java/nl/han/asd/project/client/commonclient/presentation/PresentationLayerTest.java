package nl.han.asd.project.client.commonclient.presentation;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import nl.han.asd.project.client.commonclient.CommonclientModule;
import nl.han.asd.project.client.commonclient.login.ILogin;
import nl.han.asd.project.client.commonclient.master.IRegistration;
import nl.han.asd.project.client.commonclient.master.wrapper.RegisterResponseWrapper;
import nl.han.asd.project.client.commonclient.message.IMessageBuilder;
import nl.han.asd.project.client.commonclient.presentation.gui.GUI;
import nl.han.asd.project.client.commonclient.store.IContactStore;
import nl.han.asd.project.client.commonclient.store.IMessageObserver;
import nl.han.asd.project.client.commonclient.store.IMessageStore;
import nl.han.asd.project.client.commonclient.utility.TestHelper;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static nl.han.asd.project.protocol.HanRoutingProtocol.ClientRegisterResponse;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Created by Kenny on 18-4-2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class PresentationLayerTest {

    @Mock
    private IContactStore contacts;
    @Mock
    private IMessageBuilder messageBuilder;
    @Mock
    private IMessageObserver messageObserver;
    @Mock
    private IRegistration registration;
    @Mock
    private ILogin login;
    @InjectMocks
    PresentationLayer pLayer;

    private String validUsername;
    private String validPassword = "TestPassword";
    private String invalidUsername = "AWayTooLongUsernameIsInvalid";
    private String invalidPassword = "AWayTooLongPasswordIsInvalid";

    @Before
    public void initialize() {
        validUsername = TestHelper.createRandomValidUsername();
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

    /*@Test //TODO: How to mock method inside method?
    public void testRegisterRequestGetWrongAnswer() throws Exception {
        PresentationLayer pLayerSpy = spy(pLayer);
        when(pLayerSpy.)
    }*/
}
