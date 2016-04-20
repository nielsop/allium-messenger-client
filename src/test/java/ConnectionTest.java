import LocalServer.Server;
import com.google.protobuf.InvalidProtocolBufferException;
import nl.han.asd.client.commonclient.connection.ConnectionService;
import org.junit.*;

import java.io.IOException;

import static nl.han.asd.project.protocol.HanRoutingProtocol.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by Jevgeni on 15-4-2016.
 */
public class ConnectionTest {

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
        connectionService = new ConnectionService();
        connectionService.Start("127.0.0.1", 10002);
    }

    @After
    public void CloseConnectionService() throws IOException {
        connectionService.Close();
    }

    @Test
    public void TestConnection() throws IOException {
        String testString = "HelloDarknessMyOldFriend";
        byte[] data = (testString).getBytes();
        connectionService.Write(data);

        byte[] buffer = connectionService.Read();

        String receivedData = new String(buffer, "UTF-8");
        assertEquals(receivedData, testString);
    }

    @Test
    public void TestProtocol() throws InvalidProtocolBufferException {
        ClientLoginRequest.Builder requestBuilder = ClientLoginRequest.newBuilder();
        requestBuilder.setUsername("test");
        requestBuilder.setPassword("test");
        requestBuilder.setPublicKey("test");

        byte[] data = requestBuilder.build().toByteArray();
        connectionService.Write(data);

        byte[] buffer = connectionService.Read();
        ClientLoginResponse response = ClientLoginResponse.parseFrom(buffer);
        assertEquals(response.getSecretHash(), String.format("%s:%s", requestBuilder.getUsername(), requestBuilder.getPassword()));
        assertEquals(response.getStatus(), ClientLoginResponse.Status.SUCCES);

    }
}
