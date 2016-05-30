package nl.han.asd.project.client.commonclient.graph;

import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Julius
 * @version 1.0
 * @since 17/05/16
 */
public class NodeTest {

    private Node node;
    private HanRoutingProtocol.Edge edge;
    private final String EDGE_DESTINATIONNODE_ID = "NODE_2";
    private final float EDGE_WEIGHT = 12.0f;
    private final String NODE_ID = "NODE_1";
    private final String NODE_IP = "192.168.2.16";
    private final int NODE_PORT = 1337;
    private final byte[] NODE_PUBLICKEY = new byte[] {0x00};

    @Before
    public void setup(){
        node = new Node(NODE_ID,NODE_IP,NODE_PORT,NODE_PUBLICKEY);
        edge = HanRoutingProtocol.Edge.newBuilder().setTargetNodeId(EDGE_DESTINATIONNODE_ID).setWeight(EDGE_WEIGHT).build();
    }

    @Test
    public void testAddEdge() throws Exception {
        node.addEdge(edge);
        assertEquals(node.getAdjacent().size(),1);
    }

    @Test
    public void testGetEdge() throws Exception {
        node.addEdge(edge);
        Edge getEdge = node.getEdge(EDGE_DESTINATIONNODE_ID);
        assertEquals(getEdge.getWeight(),EDGE_WEIGHT);
        assertEquals(getEdge.getDestinationNodeId(),EDGE_DESTINATIONNODE_ID);
    }
}
