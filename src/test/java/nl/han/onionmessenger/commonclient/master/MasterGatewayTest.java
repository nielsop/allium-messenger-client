package nl.han.onionmessenger.commonclient.master;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayOutputStream;
import java.net.Socket;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MasterGateway.class)
public class MasterGatewayTest {

    public static final String correctAdress = "192.168.1.1";
    public static final byte[] correctData = new byte[]{1,2,3,4};
    public static final int port = 80;

   /* @Test(expected = NullPointerException.class)
    public void testSendMessageNullAdress() throws Exception {
        new MasterGateway().sendMessage(null, correctData);
    }*/

    @Test(expected = NullPointerException.class)
    public void testSendMessageNullData() throws Exception {
        new MasterGateway(correctAdress, port).sendMessage(null);
    }

    /*@Test(expected = IllegalArgumentException.class)
    public void testSendMessageInvalidAdress() throws Exception {
        new MasterGateway().sendMessage("102.928", correctData);
    }*/

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