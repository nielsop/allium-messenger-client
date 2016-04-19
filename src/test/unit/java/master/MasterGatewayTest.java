package master;

import nl.han.asd.project.client.commonclient.master.MasterGateway;
import nl.han.asd.project.client.commonclient.utility.RequestWrapper;
import nl.han.asd.project.client.commonclient.utility.ResponseWrapper;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.exceptions.ExceptionIncludingMockitoWarnings;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

import static nl.han.asd.project.protocol.HanRoutingProtocol.*;
import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MasterGateway.class)
public class MasterGatewayTest {

    public static final String correctAddress = "10.182.5.162";
    public static final byte[] correctData = new byte[]{1, 2, 3, 4};
    public static final int port = 1024;
    public static final String username = "username";
    public static final String password = "password";

    public MasterGateway gateway;
    @Before
    public void setup() throws Exception{
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

    @Test (expected = IllegalArgumentException.class)
    public void testRegisterPortTooLow() throws Exception {
        gateway = new MasterGateway(correctAddress, -1);
    }

    @Test (expected = IllegalArgumentException.class)
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

    //No idea if this is a good test, probably not.
    @Test
    public void testRegisterFailed() throws Exception {

        //Mock printWriter
        PrintWriter pw = Mockito.mock(PrintWriter.class);
        PowerMockito.whenNew(PrintWriter.class).withAnyArguments().thenReturn(pw);

        //Mock ResponseWrapper that is created with gateway.register
        gateway = new MasterGateway(correctAddress, port);
        ResponseWrapper responseWrapper = Mockito.mock(ResponseWrapper.class);
        PowerMockito.whenNew(ResponseWrapper.class).withAnyArguments().thenReturn(responseWrapper);
        PowerMockito.when(responseWrapper.read()).thenReturn(ClientRegisterResponse.newBuilder().setStatus(ClientRegisterResponse.Status.FAILED).build());

        assertEquals( ClientRegisterResponse.newBuilder().setStatus(ClientRegisterResponse.Status.FAILED).build() ,gateway.register(username, password));
    }

    @Test
    public void testSendMessageValidInput() throws Exception {
        Socket s = Mockito.mock(Socket.class);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        InputStream in = Mockito.mock(InputStream.class);

        PowerMockito.whenNew(Socket.class).withArguments(correctAddress, port).thenReturn(s);
        Mockito.when(s.getOutputStream()).thenReturn(bos);
        Mockito.when(s.getInputStream()).thenReturn(in);

        ResponseWrapper wrapper = Mockito.mock(ResponseWrapper.class);
        PowerMockito.whenNew(ResponseWrapper.class).withAnyArguments().thenReturn(wrapper);

        Mockito.when(wrapper.read()).thenReturn(ClientRegisterResponse.newBuilder().setStatus(ClientRegisterResponse.Status.FAILED).build());

        MasterGateway mg = new MasterGateway(correctAddress, port);
        ClientRegisterResponse resp = mg.register("username", "password");

        Assert.assertEquals(ClientRegisterResponse.Status.FAILED, resp.getStatus());

    /*@Test
    public void testDependenciesNotNull() throws Exception {
        gateway = new MasterGateway(correctAdress, port);
        assertNotNull(gateway);
    }*/

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
}
