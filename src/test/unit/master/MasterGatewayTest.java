package unit.master;

import com.google.inject.Guice;
import com.google.inject.Injector;
import nl.han.asd.project.client.commonclient.master.MasterGateway;
import nl.han.asd.project.client.commonclient.utility.ResponseWrapper;
import nl.han.asd.project.commonservices.encryption.EncryptionModule;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.Socket;

import static junit.framework.TestCase.assertNotNull;
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
    private IEncryptionService encryptionService;

    @Before
    public void setup() throws Exception {
        //Mock Socket with its outputStream and inputStream
        Socket s = Mockito.mock(Socket.class);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        InputStream in = Mockito.mock(InputStream.class);

        PowerMockito.whenNew(Socket.class).withArguments(correctAddress, port).thenReturn(s);
        Mockito.when(s.getOutputStream()).thenReturn(bos);
        Mockito.when(s.getInputStream()).thenReturn(in);
        Injector injector = Guice.createInjector(new EncryptionModule());
        encryptionService = injector.getInstance(IEncryptionService.class);
    }

    @Test
    public void testGatewayCreatingGatewayValid() {
        Exception ex = null;
        try {
            new MasterGateway(encryptionService);
        } catch (Exception e) {
            ex = e;
        }
        assertEquals(null, ex);
    }

    @Test
    public void testDependenciesNotNull() throws Exception {
        gateway = new MasterGateway(encryptionService);
        assertNotNull(gateway);
    }

    private ResponseWrapper getMockedResponseWrapper() throws Exception {
        ResponseWrapper wrapper = Mockito.mock(ResponseWrapper.class);
        PowerMockito.whenNew(ResponseWrapper.class).withAnyArguments().thenReturn(wrapper);
        return wrapper;
    }
}
