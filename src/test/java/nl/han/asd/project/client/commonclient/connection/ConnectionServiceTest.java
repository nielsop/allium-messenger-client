package nl.han.asd.project.client.commonclient.connection;

import com.google.protobuf.InvalidProtocolBufferException;
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
public class ConnectionServiceTest implements IConnectionService {

    private static Server server = new Server();
    private final byte[] EMPTY_BYTE_ARRAY = new byte[] { 0x00 };
    private ConnectionService connectionService = null;

    public ConnectionServiceTest() {
    }

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
        connectionService = new ConnectionService(EMPTY_BYTE_ARRAY, this);

        // set the public key of the server to our connection service
        connectionService.setReceiverPublicKey(server.getMyPublicKey());
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
        requestBuilder.setPublicKey("test");

        connectionService.write(requestBuilder);

        ClientLoginResponse response = connectionService.readGeneric(HanRoutingProtocol.ClientLoginResponse.class);
        assertEquals(response.getSecretHash(),
                String.format("%s:%s", requestBuilder.getUsername(), requestBuilder.getPassword()));
        assertEquals(response.getStatus(), ClientLoginResponse.Status.SUCCES);

    }

    @Test(expected = IllegalArgumentException.class)
    public void TestSleepTime() {
        ConnectionService connection2 = new ConnectionService(-20, EMPTY_BYTE_ARRAY);
    }

    @Test(expected = SocketException.class)
    public void TestHost() throws IOException {
        ConnectionService connection2 = new ConnectionService(20, EMPTY_BYTE_ARRAY, this);
        connection2.open("127.0.", 10);
    }

    @Test(expected = SocketException.class)
    public void TestPort() throws IOException {
        ConnectionService connection2 = new ConnectionService(EMPTY_BYTE_ARRAY);
        connection2.open("127.0.0.1", -20);
    }

    @Test(expected = SocketException.class)
    public void TestReadAsync() throws IOException {
        ConnectionService connection2 = new ConnectionService(EMPTY_BYTE_ARRAY);
        connection2.readAsync();
    }

    @Test(expected = SocketException.class)
    public void TestStopReadyAsync() throws IOException {
        ConnectionService connection2 = new ConnectionService(EMPTY_BYTE_ARRAY);
        connection2.stopReadAsync();
    }

    @Test
    public void TestGenerics() throws SocketException, InvalidProtocolBufferException {
        ClientLoginRequest.Builder builder = ClientLoginRequest.newBuilder();
        builder.setUsername("test");
        builder.setPassword("test");
        builder.setPublicKey("xxxx");
        connectionService.write(builder);

        ClientLoginResponse response = connectionService.readGeneric(ClientLoginResponse.class);
        assertEquals(ClientLoginResponse.Status.SUCCES, response.getStatus());
    }

    @Test(expected = SocketException.class)
    public void TestInvalidGeneric() throws IOException {
        connectionService.close();
        ClientLoginResponse response = connectionService.readGeneric(ClientLoginResponse.class);
    }

    @Test(expected = SocketException.class)
    public void TestWriteInvalid() throws IOException {
        connectionService.close();
        ClientLoginRequest.Builder builder = ClientLoginRequest.newBuilder();
        connectionService.write(builder);
    }

    @Test(expected = SocketException.class)
    public void TestInvalidIP() throws IOException {
        connectionService.open("127.1.1.", 1345);
    }

    @Test(expected = SocketException.class)
    public void TestReadInvalid() throws IOException {
        connectionService.close();
        connectionService.readGeneric(HanRoutingProtocol.ClientLoginRequest.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestInvalidConstructor() throws IOException {
        ConnectionService service = new ConnectionService(25, null);
    }

    @Test
    public void TestConnectedState() throws IOException {
        connectionService.close();

        assertEquals(false, connectionService.isConnected());
    }

    @Test
    public void TestAsyncReadCall() throws SocketException {
        connectionService.readAsync();
        connectionService.stopReadAsync();
    }

    @Override
    public void onReceiveRead(UnpackedMessage message) {
        try {
            connectionService.stopReadAsync();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
