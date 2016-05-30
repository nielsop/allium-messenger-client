package nl.han.asd.project.client.commonclient.graph;

import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 20-4-2016
 */
public class Node {
    private Map<String, Edge> adjacent;
    private String id;
    private String ipAddress;
    private int port;
    private byte[] publicKey;

    public Node(String id, String ipAddress, int port, byte[] publicKey) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.port = port;
        this.publicKey = publicKey;
        this.adjacent = new HashMap<>();
    }

    /**
     * add an edge
     * @param edge
     */
    public void addEdge(HanRoutingProtocol.Edge edge) {
        adjacent.put(edge.getTargetNodeId(), new Edge(edge.getTargetNodeId(), edge.getWeight()));
    }

    /**
     *
     * @param destinationNodeId
     *      Contains the Id from the edge's destination node.
     * @return
     *      The edge that has been found with the destination node id.
     */
    public Edge getEdge(String destinationNodeId) {
        Edge edge = adjacent.get(destinationNodeId);
        if (edge == null)
            throw new NoSuchElementException();
        return edge;
    }

    public Map<String, Edge> getAdjacent() {
        return adjacent;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object anotherObject) {
        return !(anotherObject == null || !(anotherObject instanceof Node));
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }
}
