package nl.han.asd.project.client.commonclient.connection;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import nl.han.asd.project.commonservices.encryption.EncryptionModule;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.*;

import java.io.IOException;
import java.net.SocketException;

import static nl.han.asd.project.protocol.HanRoutingProtocol.ClientLoginRequest;
import static nl.han.asd.project.protocol.HanRoutingProtocol.ClientLoginResponse;
import static org.junit.Assert.assertEquals;

/**
 * Created by Jevgeni on 15-4-2016.
 */
public class ConnectionServiceTestIT {

    private static Server server = new Server();
    private final byte[] EMPTY_PUBLICKEY_BYTES = new byte[] { 0x00 };
    private ConnectionService connectionService = null;


    @BeforeClass
    public static void InitServer() throws IOException {
        // setup the local server for testing purposes only, executing should happen before the class is initialized.
        server.Start(10002);
    }

    @AfterClass
    public static void StopServer() {
        // stop the local server after we ran all tests.
        server.Stop();
    }

    @Before
    public void InitConnectionService() throws IOException {
        final Injector injector = Guice.createInjector(new EncryptionModule());
        connectionService = new ConnectionService(injector.getInstance(IEncryptionService.class),
                server.getMyPublicKey());

        // set the public key of the connection server to the server
        server.setReceiverPublicKey(connectionService.getMyPublicKey());
        connectionService.open("127.0.0.1", 10002);
    }

    @After
    public void CloseConnectionService() throws IOException {
        connectionService.close();
    }

    @Test
    public void TestProtocol() throws InvalidProtocolBufferException, SocketException {
        ClientLoginRequest.Builder requestBuilder = ClientLoginRequest.newBuilder();
        requestBuilder.setUsername("test");
        requestBuilder.setPassword("test");
        requestBuilder.setPublicKey(ByteString.copyFrom(EMPTY_PUBLICKEY_BYTES));

        connectionService.write(requestBuilder);

        ClientLoginResponse response = connectionService.readGeneric(HanRoutingProtocol.ClientLoginResponse.class);
        assertEquals(response.getSecretHash(),
                String.format("%s:%s", requestBuilder.getUsername(), requestBuilder.getPassword()));
        assertEquals(response.getStatus(), ClientLoginResponse.Status.SUCCES);

    }
}
