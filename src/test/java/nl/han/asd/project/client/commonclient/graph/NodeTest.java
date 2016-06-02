package nl.han.asd.project.client.commonclient.graph;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.mockito.Mock;

/**
 * @author Julius
 * @version 1.0
 * @since 17/05/16
 */
public class NodeTest {

    private final byte[] EMPTY = new byte[0];

    private Node node1, node2;

    @Before
    public void setUp() {
        node1 = new Node("1", "localhost", 1024, EMPTY);
        node2 = new Node("2", "localhost", 1025, EMPTY);
    }

    @Test
    public void testInvalidEdge() throws Exception {
        Edge edge = node1.getEdge("3");
        Assert.assertEquals(null, edge);
    }

    @Test
    public void testValidEdge() throws Exception {
        node1.addEdge(node2, 10);
        Edge edge = node1.getEdge("2");
        Assert.assertEquals(node2.getId(), edge.getDestinationNodeId());
    }

    @Test
    public void testBidirectionalEdgeCreation() throws Exception {
        node1.addEdge(node2, 10);
        Edge edge = node2.getEdge("1");
        Assert.assertEquals(node1.getId(), edge.getDestinationNodeId());
    }

    @Test
    public void testEqualsImplementation() {
        Assert.assertEquals(false, node1.equals(node2));
        Assert.assertEquals(false, node1.equals(null));
        Assert.assertEquals(false, node1.equals(new Edge("X", 10)));
    }

    @Test
    public void testHashcodeImplemenation() {
        Assert.assertEquals(false, node1.hashCode() == node2.hashCode());
    }
}
