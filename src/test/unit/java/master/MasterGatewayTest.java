package master;

import nl.han.asd.client.commonclient.master.MasterGateway;
import nl.han.asd.client.commonclient.utility.ResponseWrapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.Socket;

import static nl.han.asd.project.protocol.HanRoutingProtocol.ClientRegisterResponse;
import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MasterGateway.class)
public class MasterGatewayTest {

    public static final String correctAddress = "10.182.5.139";
    public static final byte[] correctData = new byte[]{1, 2, 3, 4};
    public static final int port = 1024;
    public static final String username = "username";
    public static final String password = "password";

    public MasterGateway gateway;


    @Before
    public void setup() throws Exception {
        //Mock Socket with its outputStream and inputStream
        Socket s = Mockito.mock(Socket.class);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        InputStream in = Mockito.mock(InputStream.class);

        PowerMockito.whenNew(Socket.class).withArguments(correctAddress, port).thenReturn(s);
        Mockito.when(s.getOutputStream()).thenReturn(bos);
        Mockito.when(s.getInputStream()).thenReturn(in);
    }

    @Test(expected = NullPointerException.class)
    public void testRegisterAddressNull() throws Exception {
        gateway = new MasterGateway(null, port);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterPortTooLow() throws Exception {
        gateway = new MasterGateway(correctAddress, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterPortTooHigh() throws Exception {
        gateway = new MasterGateway(correctAddress, 65536);
    }

    @Test(expected = NullPointerException.class)
    public void testRegisterUsernameNull() throws Exception {
        gateway = new MasterGateway(correctAddress, port);
        gateway.register(null, password);
    }

    @Test(expected = NullPointerException.class)
    public void testRegisterPasswordNull() throws Exception {
        gateway = new MasterGateway(correctAddress, port);
        gateway.register(username, null);
    }

    @Test
    public void testRegisterFailed() throws Exception {
        ResponseWrapper wrapper = getMockedResponseWrapper();

        gateway = new MasterGateway(correctAddress, port);
        PowerMockito.when(wrapper.read()).thenReturn(ClientRegisterResponse.newBuilder().setStatus(ClientRegisterResponse.Status.FAILED).build());
        assertEquals(ClientRegisterResponse.newBuilder().setStatus(ClientRegisterResponse.Status.FAILED).build(), gateway.register(username, password));
    }

    @Test
    public void testRegisterSuccess() throws Exception {
        ResponseWrapper wrapper = getMockedResponseWrapper();

        gateway = new MasterGateway(correctAddress, port);
        PowerMockito.when(wrapper.read()).thenReturn(ClientRegisterResponse.newBuilder().setStatus(ClientRegisterResponse.Status.SUCCES).build());
        assertEquals(ClientRegisterResponse.newBuilder().setStatus(ClientRegisterResponse.Status.SUCCES).build(), gateway.register(username, password));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGatewayInvalidAddressValueTooHigh() throws Exception {
        new MasterGateway("101.202.303.404", port);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGatewayInvalidAddressTooManyGroups() throws Exception {
        new MasterGateway("192.168.121.222.121", port);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGatewayInvalidAddressAllZero() throws Exception {
        new MasterGateway("0.0.0.0", port);
    }

    @Test
    public void testGatewayValidAddress() {
        Exception ex = null;
        try {
            new MasterGateway("10.182.5.139", port);
        } catch (Exception e) {
            ex = e;
        }
        assertEquals(null, ex);
    }

    /*@Test
    public void testDependenciesNotNull() throws Exception {
        gateway = new MasterGateway(correctAdress, port);
        assertNotNull(gateway);
    }*/

    private ResponseWrapper getMockedResponseWrapper() throws Exception {
        ResponseWrapper wrapper = Mockito.mock(ResponseWrapper.class);
        PowerMockito.whenNew(ResponseWrapper.class).withAnyArguments().thenReturn(wrapper);
        return wrapper;
    }
}
