package nl.han.asd.project.client.commonclient.master;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.protobuf.ByteString;
import com.xebialabs.overcast.host.CloudHost;
import com.xebialabs.overcast.host.CloudHostFactory;
import com.xebialabs.overcast.host.DockerHost;
import nl.han.asd.project.client.commonclient.connection.ConnectionModule;
import nl.han.asd.project.client.commonclient.connection.MessageNotSentException;
import nl.han.asd.project.commonservices.encryption.EncryptionModule;
import nl.han.asd.project.protocol.HanRoutingProtocol.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MasterGatewayIT {

    private static final String VALID_USERNAME = "valid_username";
    private static final String VALID_PASSWORD = "valid_password";

    private CloudHost master;
    private MasterGateway gateway;

    @Before
    public void setup() {
        master = CloudHostFactory.getCloudHost("master");
        master.setup();
        checkDockerContainer(master.getHostName(), master.getPort(1337), 2000);

        final Properties properties = new Properties();
        properties.setProperty("master-server-host", master.getHostName());
        properties.setProperty("master-server-port",
                Integer.toString(master.getPort(1337)));

        Injector injector = Guice
                .createInjector(new MasterModule(), new ConnectionModule(),
                        new EncryptionModule(), new AbstractModule() {
                            @Override protected void configure() {
                                bind(Properties.class).toInstance(properties);
                            }
                        });
        gateway = (MasterGateway) injector.getInstance(IAuthentication.class);
    }

    @After
    public void after() {
        master.teardown();
    }

    /* Registration of clients on master server */
    @Test
    public void testRegisterClientSuccessful() throws Exception {
        ClientRegisterRequest.Builder requestBuilder = ClientRegisterRequest
                .newBuilder();

        requestBuilder.setUsername(VALID_USERNAME);
        requestBuilder.setPassword(VALID_PASSWORD);

        ClientRegisterResponse response = gateway
                .register(requestBuilder.build());

        assertEquals(ClientRegisterResponse.Status.SUCCES,
                response.getStatus());
    }

    @Test
    public void testRegisterClientSameUsernameFails() throws Exception {
        ClientRegisterRequest.Builder requestBuilder = ClientRegisterRequest
                .newBuilder();
        requestBuilder.setUsername(VALID_USERNAME);
        requestBuilder.setPassword(VALID_PASSWORD);

        ClientRegisterResponse response = gateway
                .register(requestBuilder.build());
        assertEquals(ClientRegisterResponse.Status.SUCCES,
                response.getStatus());

        response = gateway.register(requestBuilder.build());
        assertEquals(ClientRegisterResponse.Status.TAKEN_USERNAME,
                response.getStatus());
    }

    /* Login of clients on master server */
    @Test
    public void testLoginSuccessful() throws Exception {
        ClientRegisterRequest.Builder reqisterRequestBuilder = ClientRegisterRequest
                .newBuilder();
        reqisterRequestBuilder.setUsername(VALID_USERNAME);
        reqisterRequestBuilder.setPassword(VALID_PASSWORD);

        ClientRegisterResponse registerResponse = gateway
                .register(reqisterRequestBuilder.build());
        assertEquals(ClientRegisterResponse.Status.SUCCES,
                registerResponse.getStatus());

        ClientLoginRequest.Builder loginRequestBuilder = ClientLoginRequest
                .newBuilder();
        loginRequestBuilder.setUsername(VALID_USERNAME);
        loginRequestBuilder.setPassword(VALID_PASSWORD);
        loginRequestBuilder.setPublicKey(ByteString.EMPTY);

        ClientLoginResponse loginResponse = gateway
                .login(loginRequestBuilder.build());
        assertEquals(ClientLoginResponse.Status.SUCCES,
                loginResponse.getStatus());
    }

    /* Get updated graph from master server */
    @Test
    public void testGetUpdatedGraphSuccessful()
            throws IOException, MessageNotSentException {
        DockerHost node = (DockerHost) CloudHostFactory.getCloudHost("node");
        List<String> nodeEnvironmentVariables = new ArrayList<>();
        nodeEnvironmentVariables.add("masterIP=" + master.getHostName());
        nodeEnvironmentVariables.add("masterPort=" + master.getPort(1337));
        nodeEnvironmentVariables
                .add("externalPort=" + (master.getPort(1337) + 1));
        node.setEnv(nodeEnvironmentVariables);
        node.setup();

        checkDockerContainer(node.getHostName(), node.getPort(1337), 5000);
        GraphUpdateResponse response = gateway.getUpdatedGraph(
                GraphUpdateRequest.newBuilder().setCurrentVersion(1).build());
        assertNotNull(response);
        assertEquals(1, response.getGraphUpdatesCount());
        node.teardown();
    }

    /* Get active client group from master server */

    @Test
    public void testGetClientGroupSuccessful()
            throws IOException, MessageNotSentException {
        gateway.register(ClientRegisterRequest.newBuilder().setUsername("test")
                .setPassword("123456789").build());
        gateway.login(ClientLoginRequest.newBuilder().setUsername("test")
                .setPassword("123456789").setPublicKey(ByteString.EMPTY)
                .build());
        ClientRequest request = ClientRequest.newBuilder().setClientGroup(1)
                .build();
        ClientResponse response = gateway.getClientGroup(request);
        assertNotNull(response);
        assertEquals(1, response.getClientsCount());
    }

    private void checkDockerContainer(String hostName, int port, long delay) {
        while (true) {
            try {
                new Socket(hostName, port).close();
                break;
            } catch (IOException e) {
                System.out.println(
                         "Trying connection to " + hostName + ":" + port + " again in " + delay/1000 + " seconds");
            }

            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
