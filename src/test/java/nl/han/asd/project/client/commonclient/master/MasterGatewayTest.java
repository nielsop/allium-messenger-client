package nl.han.asd.project.client.commonclient.master;

import com.xebialabs.overcast.host.CloudHost;
import nl.han.asd.project.client.commonclient.utility.ResponseWrapper;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import static nl.han.asd.project.protocol.HanRoutingProtocol.*;
import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
//@RunWith(MockitoJUnitRunner.class)
@PrepareForTest(MasterGateway.class)

public class MasterGatewayTest {

    private CloudHost itestHost;
    private MasterGateway gateway;

    public static final String correctAddress = "10.182.5.162";
    public static final byte[] correctData = new byte[]{1, 2, 3, 4};
    public static final int port = 1024;
    public static final String username = "username";
    public static final String password = "password";

    @Before
    public void setup() throws Exception {
//        itestHost = CloudHostFactory.getCloudHost("server");
//        itestHost.setup();
//
//        String host = itestHost.getHostName();
//        int port = itestHost.getPort(31337);
//
//        System.out.println(host + ", " + port);
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        //Mock Socket with its outputStream and inputStream
        Socket s = Mockito.mock(Socket.class);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        InputStream in = Mockito.mock(InputStream.class);

        PowerMockito.whenNew(Socket.class).withArguments(correctAddress, port).thenReturn(s);
        Mockito.when(s.getOutputStream()).thenReturn(bos);
        Mockito.when(s.getInputStream()).thenReturn(in);

        gateway = new MasterGateway(correctAddress, 1234);
    }

    @Test(expected = NullPointerException.class)
    public void testRegisterAddressNull() throws Exception {
        gateway = new MasterGateway(null, port);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterPortTooLow() throws Exception {
        gateway = new MasterGateway(correctAddress, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterPortTooHigh() throws Exception {
        gateway = new MasterGateway(correctAddress, 65536);
    }

    @Test(expected = NullPointerException.class)
    public void testRegisterUsernameNull() throws Exception {
        gateway = new MasterGateway(correctAddress, port);
        gateway.register(null, password);
    }

    @Test(expected = NullPointerException.class)
    public void testRegisterPasswordNull() throws Exception {
        gateway = new MasterGateway(correctAddress, port);
        gateway.register(username, null);
    }


    //TODO: Fix, worked before...
    /*@Test
    public void testRegisterFailed() throws Exception {
        ResponseWrapper wrapper = getMockedResponseWrapper();
        gateway = new MasterGateway(correctAddress, port);
        PowerMockito.when(wrapper.read()).thenReturn(ClientRegisterResponse.newBuilder().setStatus(ClientRegisterResponse.Status.FAILED).build());
        assertEquals(ClientRegisterResponse.newBuilder().setStatus(ClientRegisterResponse.Status.FAILED).build(), gateway.register(username, password));
    }

    @Test
    public void testRegisterSuccess() throws Exception {
        ResponseWrapper wrapper = getMockedResponseWrapper();
        gateway = new MasterGateway(correctAddress, port);
        PowerMockito.when(wrapper.read()).thenReturn(ClientRegisterResponse.newBuilder().setStatus(ClientRegisterResponse.Status.SUCCES).build());
        assertEquals(ClientRegisterResponse.newBuilder().setStatus(ClientRegisterResponse.Status.SUCCES).build(), gateway.register(username, password));
    }*/

    @Test(expected = IllegalArgumentException.class)
    public void testGatewayInvalidAddressValueTooHigh() throws Exception {
        new MasterGateway("101.202.303.404", port);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGatewayInvalidAddressTooManyGroups() throws Exception {
        new MasterGateway("192.168.121.222.121", port);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGatewayInvalidAddressAllZero() throws Exception {
        new MasterGateway("0.0.0.0", port);
    }

    @Test
    public void testGatewayValidAddress() {
        Exception ex = null;
        try {
            new MasterGateway(correctAddress, port);
        } catch (Exception e) {
            ex = e;
        }
        assertEquals(null, ex);
    }

    /*@Test
    public void testDependenciesNotNull() throws Exception {
        gateway = new MasterGateway(correctAdress, port);
        assertNotNull(gateway);
    }*/



    @Test
    public void testAuthUserCorrectCredentials() {
        MasterGateway mockGateway = Mockito.mock(MasterGateway.class);
        ClientLoginResponse successResponse = ClientLoginResponse.newBuilder().setSecretHash("secretHash").setStatus(ClientLoginResponse.Status.SUCCES).build();
       //TODO: deze test verbeteren
        //Mockito.when(mockGateway.authenticateUser("correctUser", "correctPassword", "correctPublicKey")).thenReturn(successResponse);
        //Assert.assertEquals(mockGateway.authenticateUser("correctUser", "correctPassword", "correctPublicKey"), successResponse);
    }
//
//    @Test
//    @Category(IntegrationTest.class)
//    public void registerClientTest() {
//        HanRoutingProtocol.ClientRegisterResponse response = masterGateway.testRegisterClient("banaan", "Koen", "abcde12345");
//
//        HanRoutingProtocol.ClientRegisterResponse.Builder expectedResponse = HanRoutingProtocol.ClientRegisterResponse.newBuilder();
//        expectedResponse.setStatus(HanRoutingProtocol.ClientRegisterResponse.Status.SUCCES);
//
//        Assert.assertEquals(response, expectedResponse.build());
//    }
//
//    @Test
//    @Category(IntegrationTest.class)
//    public void updateGraphTest() {
//        HanRoutingProtocol.GraphUpdateResponse response = masterGateway.testGraphUpdate(10, "abcde12345");
//
//        ArrayList<HanRoutingProtocol.Edge> edges = new ArrayList<>();
//        edges.add(createEdge("1", 5.0f));
//        HanRoutingProtocol.Node node1 = createNode("2", "192.168.0.1", 80, "abcdef123456", edges);
//
//        edges.clear();
//        edges.add(createEdge("2", 2.0f));
//        HanRoutingProtocol.Node node2 = createNode("1", "192.168.0.2", 80, "zyx123", edges);
//
//        edges.clear();
//        HanRoutingProtocol.Node node3 = createNode("3", "192.168.0.3", 80, "abc123", edges);
//
//        HanRoutingProtocol.GraphUpdateResponse.Builder expectedResponse = HanRoutingProtocol.GraphUpdateResponse.newBuilder();
//        expectedResponse.setNewVersion(12345).setIsFullGraph(false).addAddedNodes(node1).addUpdatedNodes(node2).addDeletedNodes(node3);
//
//        Assert.assertEquals(response, expectedResponse.build());
//    }
//
//    @Test
//    @Category(IntegrationTest.class)
//    public void registerNodeTest() {
//        HanRoutingProtocol.NodeRegisterResponse response = masterGateway.testRegisterNode("192.168.0.0", 80, "12345abcde");
//
//        HanRoutingProtocol.NodeRegisterResponse.Builder expectedResponse = HanRoutingProtocol.NodeRegisterResponse.newBuilder();
//        expectedResponse.setStatus(HanRoutingProtocol.NodeRegisterResponse.Status.SUCCES).setId("1337").setSecretHash("HASH123");
//
//        Assert.assertEquals(response, expectedResponse.build());
//    }
//
//    @Test
//    @Category(IntegrationTest.class)
//    public void updateNodeTest() {
//        HanRoutingProtocol.NodeUpdateResponse response = masterGateway.testUpdateNode("2", "56857cfc709d3996f057252c16ec4656f5292802", "192.170.0.1", 90, "abcde12345");
//
//        HanRoutingProtocol.NodeUpdateResponse.Builder expectedResponse = HanRoutingProtocol.NodeUpdateResponse.newBuilder();
//        expectedResponse.setStatus(HanRoutingProtocol.NodeUpdateResponse.Status.SUCCES);
//
//        Assert.assertEquals(response, expectedResponse.build());
//    }
//
//    @Test
//    @Category(IntegrationTest.class)
//    public void deleteNodeTest() {
//        HanRoutingProtocol.NodeDeleteResponse response = masterGateway.testDeleteNode("2", "56857cfc709d3996f057252c16ec4656f5292802", "abcde12345");
//
//        HanRoutingProtocol.NodeDeleteResponse.Builder expectedResponse = HanRoutingProtocol.NodeDeleteResponse.newBuilder();
//        expectedResponse.setStatus(HanRoutingProtocol.NodeDeleteResponse.Status.SUCCES);
//
//        Assert.assertEquals(response, expectedResponse.build());
//    }
//
//    @Test
//    @Category(IntegrationTest.class)
//    public void getClients() {
//        HanRoutingProtocol.ClientResponse response = masterGateway.testGetClients(10, "abcde12345");
//
//        ArrayList<HanRoutingProtocol.Edge> edges = new ArrayList<>();
//        edges.add(createEdge("1", 5.0f));
//
//        ArrayList<HanRoutingProtocol.Node> connectedNodes = new ArrayList<>();
//        connectedNodes.add(createNode("2", "192.168.0.1", 80, "abcdef123456", edges));
//
//        ArrayList<HanRoutingProtocol.Client> clients = new ArrayList<>();
//        clients.add(createClient("123abc", "123456abcde", connectedNodes));
//        clients.add(createClient("456def", "654321fedcba", connectedNodes));
//
//        HanRoutingProtocol.ClientResponse.Builder expectedResponse = HanRoutingProtocol.ClientResponse.newBuilder();
//
//        for (HanRoutingProtocol.Client client : clients)
//            expectedResponse.addClients(client);
//
//        Assert.assertEquals(response, expectedResponse.build());
//    }
//
//    @After
//    public void tearDown() throws Exception {
////        itestHost.teardown();
//    }
//
//    private HanRoutingProtocol.Node createNode(String id, String ip, int port, String key, ArrayList<HanRoutingProtocol.Edge> edges) {
//        HanRoutingProtocol.Node.Builder builder = HanRoutingProtocol.Node.newBuilder();
//
//        builder.setId(id);
//        builder.setIPaddress(ip);
//        builder.setPort(port);
//        builder.setPublicKey(key);
//
//        for (HanRoutingProtocol.Edge edge : edges) {
//            builder.addEdge(edge);
//        }
//
//        return builder.build();
//    }
//
//    private HanRoutingProtocol.Client createClient(String username, String key, ArrayList<HanRoutingProtocol.Node> connectedNodes) {
//        HanRoutingProtocol.Client.Builder builder = HanRoutingProtocol.Client.newBuilder();
//
//        builder.setUsername(username);
//        builder.setPublicKey(key);
//
//        for (HanRoutingProtocol.Node connectedNode : connectedNodes) {
//            builder.addConnectedNodes(connectedNode);
//        }
//
//        return builder.build();
//    }
//
//    private HanRoutingProtocol.Edge createEdge(String toNode, float weight) {
//        HanRoutingProtocol.Edge.Builder builder = HanRoutingProtocol.Edge.newBuilder();
//
//        builder.setTargetNodeId(toNode);
//        builder.setWeight(weight);
//
//        return builder.build();
//    }
        private ResponseWrapper getMockedResponseWrapper() throws Exception {
            ResponseWrapper wrapper = Mockito.mock(ResponseWrapper.class);
            PowerMockito.whenNew(ResponseWrapper.class).withAnyArguments().thenReturn(wrapper);
            return wrapper;
}
}