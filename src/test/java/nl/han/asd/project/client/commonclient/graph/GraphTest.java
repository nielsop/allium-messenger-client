package nl.han.asd.project.client.commonclient.graph;

import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Base64;

/**
 * @author Julius
 * @version 1.0
 * @since 29/04/16
 */
public class GraphTest {
    private Graph graph;
    private HanRoutingProtocol.Node node;
    private HanRoutingProtocol.Edge edge;
    private Edge edgeNode1;
    private final String EDGEDESTINATIONNODE = "NODE_2";
    private final int EDGEDESTINATIONNODEWEIGHT = 12;
    private final int NODE1_PORT = 1337;
    private final String NODE1_IP = "192.168.2.1";
    private final String NODE1_ID = "NODE_1";
    private final byte[] NODE1_PUBLICKEY = Base64.getEncoder().encode(("12345".getBytes()));

    @Before
    public void setUp() throws Exception {
        graph = new Graph();
        edge = HanRoutingProtocol.Edge.newBuilder().setTargetNodeId(EDGEDESTINATIONNODE).setWeight(EDGEDESTINATIONNODEWEIGHT).build();
        edgeNode1 = new Edge(EDGEDESTINATIONNODE,EDGEDESTINATIONNODEWEIGHT);
        node = HanRoutingProtocol.Node.newBuilder().setPort(NODE1_PORT).setIPaddress(NODE1_IP).setId(NODE1_ID).setPublicKey( new String(NODE1_PUBLICKEY)).addEdge(edge).build();
    }

    @Test
    public void testResetGraph() throws Exception {
        HanRoutingProtocol.Node node2 = HanRoutingProtocol.Node.newBuilder().setPort(1337).setIPaddress("192.168.2.1")
                .setId("NODE_2").setPublicKey("12345").build();
        graph.addNodeVertex(node);
        graph.addNodeVertex(node2);
        Assert.assertEquals(2, graph.getVertexMapSize());
        graph.resetGraph();
        Assert.assertEquals(0, graph.getVertexMapSize());
    }

    @Test
    public void testAddNodeVertex() throws Exception {
        graph.addNodeVertex(node);
        Assert.assertEquals(1, graph.getVertexMapSize());

    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEdgesToVertexWhenNodeDoesNotExistInGraph() throws Exception {
        graph.resetGraph();
        graph.addEdgesToVertex(node);
    }

    @Test
    public void testAddEdgesToVertex() throws Exception{
        graph.addNodeVertex(node);
        graph.addEdgesToVertex(node);

        Node nodeWithEdges = graph.getNodeVertex(node.getId());
        Assert.assertEquals(nodeWithEdges.getAdjacent().size(), 1);
        Assert.assertEquals(nodeWithEdges.getAdjacent().get(EDGEDESTINATIONNODE).getDestinationNodeId(),EDGEDESTINATIONNODE);
    }

    @Test
    public void testRemoveNodeVertex() throws Exception {

        graph.addNodeVertex(node);
        Assert.assertEquals(1, graph.getVertexMapSize());

        graph.removeNodeVertex(node);
        Assert.assertEquals(0, graph.getVertexMapSize());

    }

    @Test
    public void testGetVertexFromGraph() {
        graph.addNodeVertex(node);
        graph.addEdgesToVertex(node);

        Node nodeVertex = graph.getNodeVertex("NODE_1");
        Assert.assertEquals(nodeVertex.getId(),NODE1_ID);
        Assert.assertEquals(nodeVertex.getIpAddress(),NODE1_IP);
        Assert.assertEquals(nodeVertex.getPort(),NODE1_PORT);
        Assert.assertArrayEquals(nodeVertex.getPublicKey(),NODE1_PUBLICKEY);
        Assert.assertEquals(nodeVertex.getEdge(EDGEDESTINATIONNODE),edgeNode1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExeptionWhenGetVertexMapSizeCantFindVertex() {

        graph.addNodeVertex(node);

        Node nodeVertex = graph.getNodeVertex("NODE_2");

    }
}