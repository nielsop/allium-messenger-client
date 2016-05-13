package nl.han.asd.project.client.commonclient.graph;

import com.google.protobuf.ByteString;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Julius
 * @version 1.0
 * @since 29/04/16
 */
public class GraphTest {
    private static final byte[] EMPTY_PUBLICKEY_BYTES = new byte[] { 0x00 };
    private Graph graph;

    @Before
    public void setUp() throws Exception {
        graph = new Graph();
    }

    @Test
    public void testResetGraph() throws Exception {
        HanRoutingProtocol.Node node = HanRoutingProtocol.Node.newBuilder().setPort(1337).setIPaddress("192.168.2.1")
                .setId("NODE_1").setPublicKey(
                        ByteString.copyFrom(EMPTY_PUBLICKEY_BYTES)).build();
        HanRoutingProtocol.Node node2 = HanRoutingProtocol.Node.newBuilder().setPort(1337).setIPaddress("192.168.2.1")
                .setId("NODE_2").setPublicKey(
                        ByteString.copyFrom(EMPTY_PUBLICKEY_BYTES)).build();
        graph.addNodeVertex(node);
        graph.addNodeVertex(node2);
        Assert.assertEquals(2, graph.getVertexMapSize());
        graph.resetGraph();
        Assert.assertEquals(0, graph.getVertexMapSize());
    }

    @Test
    public void testAddNodeVertex() throws Exception {
        HanRoutingProtocol.Node node = HanRoutingProtocol.Node.newBuilder().setPort(1337).setIPaddress("192.168.2.1")
                .setId("NODE_1").setPublicKey(
                        ByteString.copyFrom(EMPTY_PUBLICKEY_BYTES)).build();
        graph.addNodeVertex(node);
        Assert.assertEquals(1, graph.getVertexMapSize());
    }

    @Test
    public void testRemoveNodeVertex() throws Exception {
        HanRoutingProtocol.Node node = HanRoutingProtocol.Node.newBuilder().setPort(1337).setIPaddress("192.168.2.1")
                .setId("NODE_1").setPublicKey(
                        ByteString.copyFrom(EMPTY_PUBLICKEY_BYTES)).build();
        graph.addNodeVertex(node);
        Assert.assertEquals(1, graph.getVertexMapSize());

        graph.removeNodeVertex(node);
        Assert.assertEquals(0, graph.getVertexMapSize());

    }

    @Test
    public void testGetVertexMapSize() {
        HanRoutingProtocol.Node node = HanRoutingProtocol.Node.newBuilder().setPort(1337).setIPaddress("192.168.2.1")
                .setId("NODE_1").setPublicKey(
                        ByteString.copyFrom(EMPTY_PUBLICKEY_BYTES)).build();
        graph.addNodeVertex(node);

        Node nodeVertex = graph.getNodeVertex("NODE_1");

        Assert.assertTrue(nodeVertex != null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExeptionWhenGetVertexMapSizeCantFindVertex() {
        HanRoutingProtocol.Node node = HanRoutingProtocol.Node.newBuilder().setPort(1337).setIPaddress("192.168.2.1")
                .setId("NODE_1").setPublicKey(
                        ByteString.copyFrom(EMPTY_PUBLICKEY_BYTES)).build();
        graph.addNodeVertex(node);

        Node nodeVertex = graph.getNodeVertex("NODE_2");

    }
}
