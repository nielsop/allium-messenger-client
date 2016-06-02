package nl.han.asd.project.client.commonclient.graph;

import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.util.*;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 20-4-2016
 */
public class Node {
    List<Edge> edges;
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
        edges = new LinkedList<>();
    }

    @Override
    public boolean equals(Object anotherObj) {
        if (anotherObj == null) {
            return false;
        }
        if (!(anotherObj instanceof Node)) {
            return false;
        }
        return ((Node) anotherObj).getId().equals(getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public void addEdge(Node destination, float distance) {
        edges.add(new Edge(destination.getId(), distance));
        destination.edges.add(new Edge(this.getId(), distance));
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

    public List<Edge> getEdges() {
        return edges;
    }
}
