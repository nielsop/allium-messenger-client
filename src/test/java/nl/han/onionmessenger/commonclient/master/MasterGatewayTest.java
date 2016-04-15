package nl.han.onionmessenger.commonclient.master;

import com.xebialabs.overcast.host.CloudHost;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.UnknownHostException;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 13-4-2016
 */
public class MasterGatewayTest {

    private CloudHost itestHost;
    private MasterGateway masterGateway;

    @Test
    public void registerClientTest() {

        HanRoutingProtocol.ClientRegisterResponse response = masterGateway.testRegisterClient("banaan", "Koen", "abcde12345");

        HanRoutingProtocol.ClientRegisterResponse.Builder builder = HanRoutingProtocol.ClientRegisterResponse.newBuilder();
        builder.setStatus(1);

        HanRoutingProtocol.ClientRegisterResponse expectedResponse = builder.build();

        Assert.assertEquals(response, expectedResponse);
    }

    @Test
    public void updateGraphTest() {
        HanRoutingProtocol.GraphUpdateResponse response = masterGateway.testGraphUpdate();

        HanRoutingProtocol.GraphUpdateResponse.Builder builder = HanRoutingProtocol.GraphUpdateResponse.newBuilder();

        HanRoutingProtocol.GraphUpdateResponse expectedResponse = builder.build();

        Assert.assertEquals(response, expectedResponse);
    }

    @Test
    public void registerNodeTest() {
        HanRoutingProtocol.NodeRegisterResponse response = masterGateway.testRegisterNode();

        HanRoutingProtocol.NodeRegisterResponse.Builder builder = HanRoutingProtocol.NodeRegisterResponse.newBuilder();
        builder.setStatus(HanRoutingProtocol.NodeRegisterResponse.Status.SUCCES);
        builder.setId("abc123");
        builder.setSecretHash("56857cfc709d3996f057252c16ec4656f5292802");

        HanRoutingProtocol.NodeRegisterResponse expectedResponse = builder.build();

        Assert.assertEquals(response, expectedResponse);
    }

    @Test
    public void updateNodeTest() {
        HanRoutingProtocol.NodeUpdateResponse response = masterGateway.testUpdateNode();

        HanRoutingProtocol.NodeUpdateResponse.Builder builder = HanRoutingProtocol.NodeUpdateResponse.newBuilder();
        builder.setStatus(HanRoutingProtocol.NodeUpdateResponse.Status.SUCCES);

        HanRoutingProtocol.NodeUpdateResponse expectedResponse = builder.build();

        Assert.assertEquals(response, expectedResponse);
    }

    @Test
    public void deleteNodeTest() {
        HanRoutingProtocol.NodeDeleteResponse response = masterGateway.testDeleteNode();

        HanRoutingProtocol.NodeDeleteResponse.Builder builder = HanRoutingProtocol.NodeDeleteResponse.newBuilder();
        builder.setStatus(HanRoutingProtocol.NodeDeleteResponse.Status.SUCCES);

        HanRoutingProtocol.NodeDeleteResponse expectedResponse = builder.build();

        Assert.assertEquals(response, expectedResponse);
    }

    @Test
    public void getClients() {
        HanRoutingProtocol.ClientResponse response = masterGateway.testGetClients();

        HanRoutingProtocol.Edge edge1 = createEdge("1", 5);
        HanRoutingProtocol.Node node1 = createNode("2", "192.168.0.1", 80, "abcdef123456", edge1);
        HanRoutingProtocol.Client client1 = createClient("123abc", "123456abcde", node1);

        HanRoutingProtocol.ClientResponse.Builder builder = HanRoutingProtocol.ClientResponse.newBuilder();
        builder.addClients(client1);

        HanRoutingProtocol.ClientResponse expectedResponse = builder.build();

        Assert.assertEquals(response, expectedResponse);
    }

    @Before
    public void before() throws UnknownHostException {
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

        masterGateway = new MasterGateway("10.182.5.216", 1337);
    }

    @After
    public void tearDown() throws Exception {
//        itestHost.teardown();
    }

    private HanRoutingProtocol.Node createNode(String id, String ip, int port, String key, HanRoutingProtocol.Edge edge) {
        HanRoutingProtocol.Node.Builder builder = HanRoutingProtocol.Node.newBuilder();

        builder.setId(id);
        builder.setIPaddress(ip);
        builder.setPort(port);
        builder.setPublicKey(key);
        builder.addEdge(edge);

        return builder.build();
    }

    private HanRoutingProtocol.Client createClient(String username, String key, HanRoutingProtocol.Node connectedNode) {
        HanRoutingProtocol.Client.Builder builder = HanRoutingProtocol.Client.newBuilder();

        builder.setUsername(username);
        builder.setPublicKey(key);
        builder.addConnectedNodes(connectedNode);

        return builder.build();
    }

    private HanRoutingProtocol.Edge createEdge(String toNode, float weight) {
        HanRoutingProtocol.Edge.Builder builder = HanRoutingProtocol.Edge.newBuilder();

        builder.setTargetNodeId(toNode);
        builder.setWeight(weight);

        return builder.build();
    }
}