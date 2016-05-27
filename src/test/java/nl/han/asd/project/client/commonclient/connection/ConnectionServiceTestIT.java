package nl.han.asd.project.client.commonclient.connection;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import nl.han.asd.project.client.commonclient.message.EncryptedMessage;
import nl.han.asd.project.commonservices.encryption.EncryptionModule;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.net.SocketException;

import static nl.han.asd.project.protocol.HanRoutingProtocol.ClientLoginRequest;
import static nl.han.asd.project.protocol.HanRoutingProtocol.ClientLoginResponse;
import static org.junit.Assert.assertEquals;

/**
 * Created by Jevgeni on 15-4-2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class ConnectionServiceTestIT {

    private static Server server = new Server();
    private final byte[] EMPTY_PUBLICKEY_BYTES = new byte[] { 0x00 };
    private ConnectionService connectionService = null;

    private IEncryptionService cryptographyService;

    public ConnectionServiceTestIT() {
    }

    @BeforeClass
    public static void initServer() throws IOException {
        // setup the local server for testing purposes only, executing should happen before the class is initialized.
        server.start(10002);
    }

    @AfterClass
    public static void stopServer() {
        // stop the local server after we ran all tests.
        server.stop();
    }

    @Before
    public void initConnectionService() throws IOException {
        final Injector injector = Guice.createInjector(new ConnectionModule(), new EncryptionModule());
        connectionService = injector.getInstance(IConnectionServiceFactory.class).create(server.getMyPublicKey());


        // set the public key of the connection server to the server
        server.setReceiverPublicKey(connectionService.getMyPublicKey());
        connectionService.open("127.0.0.1", 10002);
    }

    @After
    public void closeConnectionService() throws IOException {
        connectionService.close();
    }

    @Test
    public void testProtocol() throws InvalidProtocolBufferException, SocketException {
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

    @Test(expected = SocketException.class)
    public void testHost() throws IOException {
        ConnectionService connection2 = new ConnectionService(cryptographyService,
                EMPTY_PUBLICKEY_BYTES);
        connection2.open("127.0.", 10);
    }

    @Test(expected = SocketException.class)
    public void testPort() throws IOException {
        ConnectionService connection2 = new ConnectionService(cryptographyService,
                EMPTY_PUBLICKEY_BYTES);
        connection2.open("127.0.0.1", -20);
    }

    @Test
    public void testGenerics() throws SocketException, InvalidProtocolBufferException {
        ClientLoginRequest.Builder builder = ClientLoginRequest.newBuilder();
        builder.setUsername("test");
        builder.setPassword("test");
        builder.setPublicKey(ByteString.copyFrom(EMPTY_PUBLICKEY_BYTES));
        connectionService.write(builder);

        ClientLoginResponse response = connectionService.readGeneric(ClientLoginResponse.class);
        assertEquals(ClientLoginResponse.Status.SUCCES, response.getStatus());
    }

    @Test(expected = SocketException.class)
    public void testInvalidGeneric() throws IOException {
        connectionService.close();
        ClientLoginResponse response = connectionService.readGeneric(ClientLoginResponse.class);
    }

    @Test(expected = SocketException.class)
    public void testWriteInvalid() throws IOException {
        connectionService.close();
        ClientLoginRequest.Builder builder = ClientLoginRequest.newBuilder();
        connectionService.write(builder);
    }

    @Test(expected = SocketException.class)
    public void testInvalidIP() throws IOException {
        connectionService.open("127.1.1.", 1345);
    }

    @Test(expected = SocketException.class)
    public void testReadInvalid() throws IOException {
        connectionService.close();
        connectionService.readGeneric(HanRoutingProtocol.ClientLoginRequest.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructor() throws IOException {
        ConnectionService service = new ConnectionService(cryptographyService, null);
    }

    @Test
    public void testConnectedState() throws IOException {
        connectionService.close();
        assertEquals(false, connectionService.isConnected());
    }

    @Test(expected = SocketException.class)
    public void testInvalidEndpoint() throws IOException
    {
        connectionService.open("192.1.1.1", 1001);
    }

    @Test(expected =  InvalidProtocolBufferException.class)
    public void testExpectDifferentType()
            throws SocketException, InvalidProtocolBufferException {
        ClientLoginRequest.Builder requestBuilder = ClientLoginRequest.newBuilder();
        requestBuilder.setUsername("test");
        requestBuilder.setPassword("test");
        requestBuilder.setPublicKey(ByteString.copyFrom(EMPTY_PUBLICKEY_BYTES));

        connectionService.write(requestBuilder);

        connectionService.readGeneric(HanRoutingProtocol.Wrapper.class);
    }
}
