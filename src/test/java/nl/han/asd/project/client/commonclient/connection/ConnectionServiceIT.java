package nl.han.asd.project.client.commonclient.connection;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.protobuf.ByteString;

import nl.han.asd.project.commonservices.encryption.EncryptionModule;
import nl.han.asd.project.protocol.HanRoutingProtocol.ClientLoginRequest;
import nl.han.asd.project.protocol.HanRoutingProtocol.ClientLoginResponse;
import nl.han.asd.project.protocol.HanRoutingProtocol.Wrapper;

public class ConnectionServiceIT {

    private IConnectionService connectionService = null;

    @Before
    public void setup() throws IOException {
        final Injector injector = Guice.createInjector(new EncryptionModule(), new ConnectionModule());

        IConnectionServiceFactory factory = injector.getInstance(IConnectionServiceFactory.class);
        connectionService = factory.create("127.0.0.1", 10002);
    }

    @Test
    public void testProtocol() throws Exception {

        Thread t = new Thread(() -> {
            ServerSocket serverSocket = null;
            Socket socket = null;
            try {
                serverSocket = new ServerSocket(10002);
                socket = serverSocket.accept();

                ClientLoginResponse.Builder responseBuilder = ClientLoginResponse.newBuilder();
                responseBuilder.setStatus(ClientLoginResponse.Status.SUCCES);

                Wrapper wrapper = connectionService.wrap(responseBuilder.build(), Wrapper.Type.CLIENTLOGINRESPONSE);

                wrapper.writeDelimitedTo(socket.getOutputStream());

            } catch (Exception e) {
            } finally {
                try {
                    serverSocket.close();
                } catch (Exception e) {
                }

                try {
                    socket.close();
                } catch (Exception e) {
                }
            }
        });
        t.start();

        ClientLoginRequest.Builder requestBuilder = ClientLoginRequest.newBuilder();
        requestBuilder.setUsername("username");
        requestBuilder.setPassword("password");
        requestBuilder.setPublicKey(ByteString.EMPTY);

        Wrapper wrapper = connectionService.wrap(requestBuilder.build(), Wrapper.Type.CLIENTLOGINREQUEST);

        ClientLoginResponse response = (ClientLoginResponse) connectionService.writeAndRead(wrapper);
        assertEquals(ClientLoginResponse.Status.SUCCES, response.getStatus());

        t.join();
    }

    @Test
    public void testMultipleWriteReads() throws Exception {
        Thread t = new Thread(() -> {
            ServerSocket serverSocket = null;
            Socket socket = null;
            try {
                serverSocket = new ServerSocket(10002);
                socket = serverSocket.accept();

                ClientLoginResponse.Builder responseBuilder = ClientLoginResponse.newBuilder();
                responseBuilder.setStatus(ClientLoginResponse.Status.SUCCES);

                Wrapper wrapper = connectionService.wrap(responseBuilder.build(), Wrapper.Type.CLIENTLOGINRESPONSE);
                wrapper.writeDelimitedTo(socket.getOutputStream());

                socket = serverSocket.accept();

                responseBuilder = ClientLoginResponse.newBuilder();
                responseBuilder.setStatus(ClientLoginResponse.Status.FAILED);

                wrapper = connectionService.wrap(responseBuilder.build(), Wrapper.Type.CLIENTLOGINRESPONSE);
                wrapper.writeDelimitedTo(socket.getOutputStream());
            } catch (Exception e) {
            } finally {
                try {
                    serverSocket.close();
                } catch (Exception e) {
                }

                try {
                    socket.close();
                } catch (Exception e) {
                }
            }
        });
        t.start();

        ClientLoginRequest.Builder requestBuilder = ClientLoginRequest.newBuilder();
        requestBuilder.setUsername("username");
        requestBuilder.setPassword("password");
        requestBuilder.setPublicKey(ByteString.EMPTY);

        Wrapper wrapper = connectionService.wrap(requestBuilder.build(), Wrapper.Type.CLIENTLOGINREQUEST);

        ClientLoginResponse response = (ClientLoginResponse) connectionService.writeAndRead(wrapper);
        assertEquals(ClientLoginResponse.Status.SUCCES, response.getStatus());

        response = (ClientLoginResponse) connectionService.writeAndRead(wrapper);
        assertEquals(ClientLoginResponse.Status.FAILED, response.getStatus());

        t.join();
    }

    @Test
    public void connectionModuleTest() throws Exception {
        Injector injector = Guice.createInjector(new ConnectionModule(), new EncryptionModule());
        injector.getInstance(IConnectionServiceFactory.class);
    }
}
