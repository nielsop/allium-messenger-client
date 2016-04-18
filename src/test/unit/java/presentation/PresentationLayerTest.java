package presentation;

import nl.han.asd.project.client.commonclient.master.MasterGateway;
import nl.han.asd.project.client.commonclient.presentation.PresentationLayer;
import nl.han.asd.project.client.commonclient.registration.IRegistration;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import nl.han.asd.project.protocol.HanRoutingProtocol.ClientRegisterResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import static nl.han.asd.project.protocol.HanRoutingProtocol.ClientLoginResponse.Status.SUCCES;
import static org.junit.Assert.assertEquals;

/**
 * Created by Kenny on 18-4-2016.
 */
public class PresentationLayerTest {

    private String address;
    private int port;
    PresentationLayer pLayer;


    @Before public void initialize(){
        address = "192.168.131.1";
        port = 6543;
    }

    /*@Test
    public void testRegisterStatus4(){
        assertEquals(4, pLayer.register("username", "password"));
    }*/

    @Test
    public void testRegisterStatus1() throws Exception {
        IRegistration registration = Mockito.mock(IRegistration.class);
        ClientRegisterResponse registerStatus = ClientRegisterResponse.newBuilder().setStatus(ClientRegisterResponse.Status.SUCCES).build();
        Mockito.when(registration.register("username", "password")).thenReturn(registerStatus);

        pLayer = new PresentationLayer(registration);
        assertEquals(registerStatus.getStatus(), pLayer.register("username", "password"));
    }
}
