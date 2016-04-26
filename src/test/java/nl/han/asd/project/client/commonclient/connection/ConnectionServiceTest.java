package nl.han.asd.project.client.commonclient.connection;

import LocalServer.Server;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.sun.deploy.util.SessionState;
import nl.han.asd.project.client.commonclient.connection.ConnectionService;
import nl.han.asd.project.client.commonclient.connection.IConnectionService;
import org.junit.*;

import java.io.IOException;
import java.net.SocketException;

import static nl.han.asd.project.protocol.HanRoutingProtocol.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by Jevgeni on 15-4-2016.
 */
public class ConnectionServiceTest implements IConnectionService {

    private static final Server server = new Server();
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
        connectionService = new ConnectionService("publickey", this);
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

        ParsedMessage message = connectionService.read();
        ClientLoginResponse response = ClientLoginResponse.parseFrom(message.getData());
        assertEquals(response.getSecretHash(), String.format("%s:%s", requestBuilder.getUsername(), requestBuilder.getPassword()));
        assertEquals(response.getStatus(), ClientLoginResponse.Status.SUCCES);

    }

    @Test(expected = IllegalArgumentException.class)
    public void TestSleepTime()
    {
        ConnectionService connection2 = new ConnectionService(-20, "publicKey");
    }

    @Test(expected =  IllegalArgumentException.class)
    public void TestHost() throws IOException {
        ConnectionService connection2 = new ConnectionService(20, "publicKey", this);
        connection2.open("127.0.", 10);
    }

    @Test(expected = IllegalArgumentException.class)
     public void TestPort() throws IOException {
        ConnectionService connection2 = new ConnectionService("publicKey");
        connection2.open("127.0.0.1", -20);
    }

    @Test(expected = SocketException.class)
    public void TestReadAsync() throws IOException {
        ConnectionService connection2 = new ConnectionService("publicKey");
        connection2.readAsync();
    }

    @Test(expected = SocketException.class)
    public void TestStopReadyAsync() throws IOException {
        ConnectionService connection2 = new ConnectionService("publicKey");
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
    public void TestReadInvalid() throws IOException {
        connectionService.close();
        ParsedMessage message = connectionService.read();
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

    @Override
    public void onReceiveRead(ParsedMessage message) {
        try {
            connectionService.stopReadAsync();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
