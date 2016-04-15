package unit.commonclient.master;

import nl.han.asd.project.client.commonclient.master.MasterGateway;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayOutputStream;
import java.net.Socket;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MasterGateway.class)
public class MasterGatewayTest {

    public static final String correctAdress = "10.182.5.162";
    public static final byte[] correctData = new byte[]{1,2,3,4};
    public static final int port = 80;

    @Test(expected = NullPointerException.class)
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

    @Test
    public void testSendMessageValidInput() throws Exception {
        Socket s = Mockito.mock(Socket.class);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        PowerMockito.whenNew(Socket.class).withArguments(correctAdress, port).thenReturn(s);
        Mockito.when(s.getOutputStream()).thenReturn(bos);

        new MasterGateway(correctAdress, port).sendMessage(correctData);
        assertArrayEquals(correctData, bos.toByteArray());
    }
}