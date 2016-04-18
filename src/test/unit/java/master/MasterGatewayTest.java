package master;

import nl.han.asd.project.client.commonclient.master.MasterGateway;
import nl.han.asd.project.client.commonclient.utility.ResponseWrapper;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MasterGateway.class)
public class MasterGatewayTest {

    public static final String correctAdress = "10.182.5.162";
    public static final byte[] correctData = new byte[]{1, 2, 3, 4};
    public static final int port = 80;
    public static final String username = "username";
    public static final String password = "password";

    public MasterGateway gateway;

   @Test(expected = NullPointerException.class)
    public void testRegisterUsernameNull() throws Exception {
        gateway = new MasterGateway(null, port);
        gateway.register(null, password);
    }

    @Test(expected = NullPointerException.class)
    public void testRegisterPasswordNull() throws Exception {
        gateway = new MasterGateway(null, port);
        gateway.register(username, null);
    }

    @Test
    public void testRegisterGetsEnum() throws Exception {
        gateway = new MasterGateway(correctAdress, port);
        assertEquals(HanRoutingProtocol.ClientLoginResponse.Status.FAILED, gateway.register(username, password));
    }

    @Test
    public void testDependenciesNotNull() throws Exception {
        gateway = new MasterGateway(correctAdress, port);
        assertNotNull(gateway);
    }

    /*@Test
    public void testRequestBuild() throws Exception {
        HanRoutingProtocol.ClientRegisterRequest.Builder request = HanRoutingProtocol.ClientRegisterRequest.newBuilder();
        request.setUsername(username).setPassword(password);
        System.out.println(request.build().toString());
    }*/


/*    @Test(expected = NullPointerException.class)
    public void testSendMessageNullAdress() throws Exception {
        new MasterGateway(null, port).sendMessage(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSendMessageNullData() throws Exception {
        new MasterGateway(correctAdress, port).sendMessage(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSendMessageInvalidAdressValueTooHigh() throws Exception {
        new MasterGateway("101.202.303.404", port).sendMessage(correctData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSendMessageInvalidAdressTooManyGroups() throws Exception {
        new MasterGateway("192.168.121.222.121", port).sendMessage(correctData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSendMessageInvalidAdressAllZero() throws Exception {
        new MasterGateway("0.0.0.0", port).sendMessage(correctData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSendMessageEmptyData() throws Exception {
        new MasterGateway(correctAdress, port).sendMessage(new byte[0]);
    }
*/
    @Test
    public void testSendMessageValidInput() throws Exception {
        Socket s = Mockito.mock(Socket.class);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        InputStream in = Mockito.mock(InputStream.class);

        PowerMockito.whenNew(Socket.class).withArguments(correctAdress, port).thenReturn(s);
        Mockito.when(s.getOutputStream()).thenReturn(bos);
        Mockito.when(s.getInputStream()).thenReturn(in);

        ResponseWrapper wrapper = Mockito.mock(ResponseWrapper.class);
        PowerMockito.whenNew(ResponseWrapper.class).withAnyArguments().thenReturn(wrapper);

        Mockito.when(wrapper.read()).thenReturn(HanRoutingProtocol.ClientRegisterResponse.newBuilder().setStatus(HanRoutingProtocol.ClientRegisterResponse.Status.FAILED).build());

        MasterGateway mg = new MasterGateway(correctAdress, port);
        HanRoutingProtocol.ClientRegisterResponse resp = mg.register("username", "password");

        Assert.assertEquals(HanRoutingProtocol.ClientRegisterResponse.Status.FAILED, resp.getStatus());
    }


}
