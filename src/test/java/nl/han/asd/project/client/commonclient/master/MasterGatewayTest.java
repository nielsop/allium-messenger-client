package nl.han.asd.project.client.commonclient.master;

import com.xebialabs.overcast.host.CloudHost;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 13-4-2016
 */
public class MasterGatewayTest {

    private CloudHost itestHost;
    private MasterGateway masterGateway;

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

    @Test
    public void registerClientTest() {
        HanRoutingProtocol.ClientRegisterResponse response = masterGateway.testRegisterClient("banaan", "Koen", "abcde12345");

        HanRoutingProtocol.ClientRegisterResponse.Builder expectedResponse = HanRoutingProtocol.ClientRegisterResponse.newBuilder();
        expectedResponse.setStatus(HanRoutingProtocol.ClientRegisterResponse.Status.SUCCES);

        Assert.assertEquals(response, expectedResponse.build());
    }

    @Test
    public void updateGraphTest() {
        HanRoutingProtocol.GraphUpdateResponse response = masterGateway.testGraphUpdate(10, "abcde12345");

        ArrayList<HanRoutingProtocol.Edge> edges = new ArrayList<>();
        edges.add(createEdge("1", 5.0f));
        HanRoutingProtocol.Node node1 = createNode("2", "192.168.0.1", 80, "abcdef123456", edges);

        edges.clear();
        edges.add(createEdge("2", 2.0f));
        HanRoutingProtocol.Node node2 = createNode("1", "192.168.0.2", 80, "zyx123", edges);

        edges.clear();
        HanRoutingProtocol.Node node3 = createNode("3", "192.168.0.3", 80, "abc123", edges);

        HanRoutingProtocol.GraphUpdateResponse.Builder expectedResponse = HanRoutingProtocol.GraphUpdateResponse.newBuilder();
        expectedResponse.setNewVersion(12345).setIsFullGraph(false).addAddedNodes(node1).addUpdatedNodes(node2).addDeletedNodes(node3);

        Assert.assertEquals(response, expectedResponse.build());
    }

    @Test
    public void registerNodeTest() {
        HanRoutingProtocol.NodeRegisterResponse response = masterGateway.testRegisterNode("192.168.0.0", 80, "12345abcde");

        HanRoutingProtocol.NodeRegisterResponse.Builder expectedResponse = HanRoutingProtocol.NodeRegisterResponse.newBuilder();
        expectedResponse.setStatus(HanRoutingProtocol.NodeRegisterResponse.Status.SUCCES).setId("1337").setSecretHash("HASH123");

        Assert.assertEquals(response, expectedResponse.build());
    }

    @Test
    public void updateNodeTest() {
        HanRoutingProtocol.NodeUpdateResponse response = masterGateway.testUpdateNode("2", "56857cfc709d3996f057252c16ec4656f5292802", "192.170.0.1", 90, "abcde12345");

        HanRoutingProtocol.NodeUpdateResponse.Builder expectedResponse = HanRoutingProtocol.NodeUpdateResponse.newBuilder();
        expectedResponse.setStatus(HanRoutingProtocol.NodeUpdateResponse.Status.SUCCES);

        Assert.assertEquals(response, expectedResponse.build());
    }

    @Test
    public void deleteNodeTest() {
        HanRoutingProtocol.NodeDeleteResponse response = masterGateway.testDeleteNode("2", "56857cfc709d3996f057252c16ec4656f5292802", "abcde12345");

        HanRoutingProtocol.NodeDeleteResponse.Builder expectedResponse = HanRoutingProtocol.NodeDeleteResponse.newBuilder();
        expectedResponse.setStatus(HanRoutingProtocol.NodeDeleteResponse.Status.SUCCES);

        Assert.assertEquals(response, expectedResponse.build());
    }

    @Test
    public void getClients() {
        HanRoutingProtocol.ClientResponse response = masterGateway.testGetClients(10, "abcde12345");

        ArrayList<HanRoutingProtocol.Edge> edges = new ArrayList<>();
        edges.add(createEdge("1", 5.0f));

        ArrayList<HanRoutingProtocol.Node> connectedNodes = new ArrayList<>();
        connectedNodes.add(createNode("2", "192.168.0.1", 80, "abcdef123456", edges));

        ArrayList<HanRoutingProtocol.Client> clients = new ArrayList<>();
        clients.add(createClient("123abc", "123456abcde", connectedNodes));
        clients.add(createClient("456def", "654321fedcba", connectedNodes));

        HanRoutingProtocol.ClientResponse.Builder expectedResponse = HanRoutingProtocol.ClientResponse.newBuilder();

        for(HanRoutingProtocol.Client client: clients)
            expectedResponse.addClients(client);

        Assert.assertEquals(response, expectedResponse.build());
    }

    @After
    public void tearDown() throws Exception {
//        itestHost.teardown();
    }

    private HanRoutingProtocol.Node createNode(String id, String ip, int port, String key, ArrayList<HanRoutingProtocol.Edge> edges) {
        HanRoutingProtocol.Node.Builder builder = HanRoutingProtocol.Node.newBuilder();

        builder.setId(id);
        builder.setIPaddress(ip);
        builder.setPort(port);
        builder.setPublicKey(key);

        for(HanRoutingProtocol.Edge edge: edges) {
            builder.addEdge(edge);
        }

        return builder.build();
    }

    private HanRoutingProtocol.Client createClient(String username, String key, ArrayList<HanRoutingProtocol.Node> connectedNodes) {
        HanRoutingProtocol.Client.Builder builder = HanRoutingProtocol.Client.newBuilder();

        builder.setUsername(username);
        builder.setPublicKey(key);

        for(HanRoutingProtocol.Node connectedNode: connectedNodes) {
            builder.addConnectedNodes(connectedNode);
        }

        return builder.build();
    }

    private HanRoutingProtocol.Edge createEdge(String toNode, float weight) {
        HanRoutingProtocol.Edge.Builder builder = HanRoutingProtocol.Edge.newBuilder();

        builder.setTargetNodeId(toNode);
        builder.setWeight(weight);

        return builder.build();
    }
}