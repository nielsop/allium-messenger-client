package nl.han.asd.project.client.commonclient.connection;

import LocalServer.Server;
import com.google.protobuf.InvalidProtocolBufferException;
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
public class ConnectionTest implements IConnectionService {

    private static final Server server = new Server();
    private ConnectionService connectionService = null;

    public  ConnectionTest() {
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
        connectionService = new ConnectionService(this);
        connectionService.open("127.0.0.1", 10002);
    }

    @After
    public void CloseConnectionService() throws IOException {
        connectionService.close();
    }

    @Test
    public void TestConnection() throws IOException {
        String testString = "HelloDarknessMyOldFriend";
        byte[] data = (testString).getBytes();
        connectionService.write(data);

        byte[] buffer = connectionService.read();

        String receivedData = new String(buffer, "UTF-8");
        assertEquals(receivedData, testString);
    }

    @Test
    public void TestProtocol() throws InvalidProtocolBufferException, SocketException {
        ClientLoginRequest.Builder requestBuilder = ClientLoginRequest.newBuilder();
        requestBuilder.setUsername("test");
        requestBuilder.setPassword("test");
        requestBuilder.setPublicKey("test");

        byte[] data = requestBuilder.build().toByteArray();
        connectionService.write(data);

        byte[] buffer = connectionService.read();
        ClientLoginResponse response = ClientLoginResponse.parseFrom(buffer);
        assertEquals(response.getSecretHash(), String.format("%s:%s", requestBuilder.getUsername(), requestBuilder.getPassword()));
        assertEquals(response.getStatus(), ClientLoginResponse.Status.SUCCES);

    }

    @Test(expected = IllegalArgumentException.class)
    public void TestSleepTime()
    {
        ConnectionService connection2 = new ConnectionService(-20);
    }

    @Test(expected =  IllegalArgumentException.class)
    public void TestHost() throws IOException {
        ConnectionService connection2 = new ConnectionService(20, this);
        connection2.open("127.0.", 10);
    }

    @Test(expected = IllegalArgumentException.class)
     public void TestPort() throws IOException {
        ConnectionService connection2 = new ConnectionService();
        connection2.open("127.0.0.1", -20);
    }

    @Test(expected = SocketException.class)
    public void TestReadAsync() throws IOException {
        ConnectionService connection2 = new ConnectionService();
        connection2.readAsync();
    }

    @Test(expected = SocketException.class)
    public void TestStopReadyAsync() throws IOException {
        ConnectionService connection2 = new ConnectionService();
        connection2.stopReadAsync();
    }
    // ...

    @Test
    public void TestWriteAndRead() throws SocketException {
        String testString = "HelloDarknessMyOldFriend";
        byte[] data = (testString).getBytes();

        connectionService.write(data);
        connectionService.readAsync();
    }

    @Test
    public void TestConnectedState() throws IOException {
        connectionService.close();

        assertEquals(false, connectionService.isConnected());
    }

    @Override
    public void onReceiveRead(byte[] buffer) {
        try {
            connectionService.stopReadAsync();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
