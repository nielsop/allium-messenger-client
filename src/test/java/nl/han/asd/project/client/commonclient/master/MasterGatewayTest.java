package nl.han.asd.project.client.commonclient.master;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.google.protobuf.ByteString;

import nl.han.asd.project.client.commonclient.connection.IConnectionService;
import nl.han.asd.project.client.commonclient.connection.IConnectionServiceFactory;
import nl.han.asd.project.protocol.HanRoutingProtocol.Client;
import nl.han.asd.project.protocol.HanRoutingProtocol.ClientHeartbeat;
import nl.han.asd.project.protocol.HanRoutingProtocol.ClientLoginRequest;
import nl.han.asd.project.protocol.HanRoutingProtocol.ClientLoginResponse;
import nl.han.asd.project.protocol.HanRoutingProtocol.ClientRegisterRequest;
import nl.han.asd.project.protocol.HanRoutingProtocol.ClientRegisterResponse;
import nl.han.asd.project.protocol.HanRoutingProtocol.ClientRequest;
import nl.han.asd.project.protocol.HanRoutingProtocol.GraphUpdateRequest;
import nl.han.asd.project.protocol.HanRoutingProtocol.GraphUpdateResponse;
import nl.han.asd.project.protocol.HanRoutingProtocol.Wrapper;

public class MasterGatewayTest {

    private String host = "localhost";
    private int port = 1024;

    private Properties properties = new Properties();

    private IConnectionService connectionServiceMock;
    private IConnectionServiceFactory connectionServiceFactoryMock;

    private MasterGateway masterGateway;

    @Before
    public void setup() throws Exception {
        properties.setProperty("master-server-host", host);
        properties.setProperty("master-server-port", Integer.toString(port));

        connectionServiceMock = mock(IConnectionService.class);
        connectionServiceFactoryMock = mock(IConnectionServiceFactory.class);

        when(connectionServiceFactoryMock.create(eq(host), eq(port))).thenReturn(connectionServiceMock);
        masterGateway = new MasterGateway(properties, connectionServiceFactoryMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullProperties() throws Exception {
        new MasterGateway(null, connectionServiceFactoryMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullFactory() throws Exception {
        new MasterGateway(properties, null);
    }

    @Test
    public void constructorValid() throws Exception {
        when(connectionServiceFactoryMock.create(eq(host), eq(port))).thenReturn(connectionServiceMock);
        new MasterGateway(properties, connectionServiceFactoryMock);

        // ignore the extra create from the junit setup
        verify(connectionServiceFactoryMock, times(2)).create(eq(host), eq(port));
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerNullRequest() throws Exception {
        masterGateway.register(null);
    }

    @Test
    public void registerValid() throws Exception {
        ClientRegisterRequest.Builder requestBuilder = ClientRegisterRequest.newBuilder();
        requestBuilder.setUsername("username");
        requestBuilder.setPassword("password");

        ClientRegisterRequest request = requestBuilder.build();

        Wrapper.Builder wrapperBuilder = Wrapper.newBuilder();
        wrapperBuilder.setData(request.toByteString());
        wrapperBuilder.setType(Wrapper.Type.CLIENTREGISTERREQUEST);

        Wrapper wrapper = wrapperBuilder.build();

        when(connectionServiceMock.wrap(eq(request), eq(Wrapper.Type.CLIENTREGISTERREQUEST))).thenReturn(wrapper);

        ClientRegisterResponse.Builder responseBuilder = ClientRegisterResponse.newBuilder();
        responseBuilder.setStatus(ClientRegisterResponse.Status.SUCCES);

        ClientRegisterResponse response = responseBuilder.build();

        when(connectionServiceMock.writeAndRead(eq(wrapper))).thenReturn(response);

        assertEquals(response, masterGateway.register(request));
    }

    @Test(expected = IllegalArgumentException.class)
    public void sendHeartbeatNullHeartbeat() throws Exception {
        masterGateway.sendHeartbeat(null);
    }

    @Test
    public void sendHeartbeatValid() throws Exception {
        ClientHeartbeat.Builder heartbeatBuilder = ClientHeartbeat.newBuilder();
        heartbeatBuilder.setUsername("username");
        heartbeatBuilder.setSecretHash("hash");

        ClientHeartbeat heartbeat = heartbeatBuilder.build();

        Wrapper.Builder wrapperBuilder = Wrapper.newBuilder();
        wrapperBuilder.setData(heartbeat.toByteString());
        wrapperBuilder.setType(Wrapper.Type.CLIENTHEARTBEAT);

        Wrapper wrapper = wrapperBuilder.build();

        when(connectionServiceMock.wrap(eq(heartbeat), eq(Wrapper.Type.CLIENTHEARTBEAT))).thenReturn(wrapper);

        masterGateway.sendHeartbeat(heartbeat);

        verify(connectionServiceMock).write(eq(wrapper));
    }

    @Test(expected = IllegalArgumentException.class)
    public void loginNullRequest() throws Exception {
        masterGateway.login(null);
    }

    @Test
    public void loginValid() throws Exception {
        ClientLoginRequest.Builder requestBuilder = ClientLoginRequest.newBuilder();
        requestBuilder.setUsername("username");
        requestBuilder.setPassword("password");
        requestBuilder.setPublicKey(ByteString.copyFrom("public key".getBytes()));

        ClientLoginRequest request = requestBuilder.build();

        Wrapper.Builder wrapperBuilder = Wrapper.newBuilder();
        wrapperBuilder.setData(request.toByteString());
        wrapperBuilder.setType(Wrapper.Type.CLIENTLOGINREQUEST);

        Wrapper wrapper = wrapperBuilder.build();

        when(connectionServiceMock.wrap(eq(request), eq(Wrapper.Type.CLIENTLOGINREQUEST))).thenReturn(wrapper);

        ClientLoginResponse.Builder responseBuilder = ClientLoginResponse.newBuilder();
        responseBuilder.setStatus(ClientLoginResponse.Status.SUCCES);

        ClientLoginResponse response = responseBuilder.build();

        when(connectionServiceMock.writeAndRead(eq(wrapper))).thenReturn(response);

        assertEquals(response, masterGateway.login(request));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getUpdatedGraphNullRequest() throws Exception {
        masterGateway.getUpdatedGraph(null);
    }

    @Test
    public void getUpdatedGraphValid() throws Exception {
        GraphUpdateRequest.Builder requestBuilder = GraphUpdateRequest.newBuilder();
        requestBuilder.setCurrentVersion(0);

        GraphUpdateRequest request = requestBuilder.build();

        Wrapper.Builder wrapperBuilder = Wrapper.newBuilder();
        wrapperBuilder.setData(request.toByteString());
        wrapperBuilder.setType(Wrapper.Type.GRAPHUPDATEREQUEST);

        Wrapper wrapper = wrapperBuilder.build();

        when(connectionServiceMock.wrap(eq(request), eq(Wrapper.Type.GRAPHUPDATEREQUEST))).thenReturn(wrapper);

        GraphUpdateResponse.Builder responseBuilder = GraphUpdateResponse.newBuilder();
        GraphUpdateResponse response = responseBuilder.build();

        when(connectionServiceMock.writeAndRead(eq(wrapper))).thenReturn(response);

        assertEquals(response, masterGateway.getUpdatedGraph(request));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getClientGroupNullRequest() throws Exception {
        masterGateway.getClientGroup(null);
    }

    @Test
    public void getClientGroupValid() throws Exception {
        ClientRequest.Builder requestBuilder = ClientRequest.newBuilder();
        requestBuilder.setClientGroup(0);

        ClientRequest request = requestBuilder.build();

        Wrapper.Builder wrapperBuilder = Wrapper.newBuilder();
        wrapperBuilder.setData(request.toByteString());
        wrapperBuilder.setType(Wrapper.Type.CLIENTREQUEST);

        Wrapper wrapper = wrapperBuilder.build();

        when(connectionServiceMock.wrap(eq(request), eq(Wrapper.Type.CLIENTREQUEST))).thenReturn(wrapper);

        Client.Builder responseBuilder = Client.newBuilder();
        responseBuilder.setUsername("username");
        responseBuilder.setPublicKey(ByteString.copyFrom("public key".getBytes()));

        Client response = responseBuilder.build();

        when(connectionServiceMock.writeAndRead(eq(wrapper))).thenReturn(response);

        assertEquals(response, masterGateway.getClientGroup(request));
    }
}
