package nl.han.asd.project.client.commonclient.presentation;

import nl.han.asd.project.client.commonclient.master.IRegistration;
import nl.han.asd.project.client.commonclient.master.wrapper.RegisterResponseWrapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static nl.han.asd.project.protocol.HanRoutingProtocol.ClientRegisterResponse;
import static org.junit.Assert.assertEquals;

/**
 * Created by Kenny on 18-4-2016.
 */
public class PresentationLayerTest {

    PresentationLayer pLayer;
    IRegistration registration;

    @Before
    public void initialize() {
        registration = Mockito.mock(IRegistration.class);
    }

    @Test
    public void testRegisterStatusSuccess() throws Exception {
        RegisterResponseWrapper registerResponse = new RegisterResponseWrapper(
                ClientRegisterResponse.newBuilder().setStatus(ClientRegisterResponse.Status.SUCCES).getStatus());
        Mockito.when(registration.register("username", "password")).thenReturn(registerResponse);
        pLayer = new PresentationLayer(registration);
        assertEquals(registerResponse.getStatus(), pLayer.registerRequest("username", "password"));
    }

    @Test
    public void testRegisterStatusFailed() throws Exception {
        RegisterResponseWrapper registerResponse = new RegisterResponseWrapper(
                ClientRegisterResponse.newBuilder().setStatus(ClientRegisterResponse.Status.FAILED).getStatus());
        Mockito.when(registration.register("username", "password")).thenReturn(registerResponse);
        pLayer = new PresentationLayer(registration);
        assertEquals(registerResponse.getStatus(), pLayer.registerRequest("username", "password"));
    }

    @Test
    public void testRegisterStatusUsernameTaken() throws Exception {
        RegisterResponseWrapper registerResponse = new RegisterResponseWrapper(
                ClientRegisterResponse.newBuilder().setStatus(ClientRegisterResponse.Status.TAKEN_USERNAME)
                        .getStatus());
        Mockito.when(registration.register("username", "password")).thenReturn(registerResponse);
        pLayer = new PresentationLayer(registration);
        assertEquals(registerResponse.getStatus(), pLayer.registerRequest("username", "password"));
    }
}
