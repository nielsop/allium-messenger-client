package nl.han.asd.project.client.commonclient.graph;

import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 20-4-2016
 */
public class Node {
    private Map<String,Edge> adjacent;
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

    public void addEdge(HanRoutingProtocol.Edge edge){
        adjacent.putIfAbsent(edge.getTargetNodeId(),new Edge(edge.getTargetNodeId(),edge.getWeight()));
    }
    public Edge getEdge(String destinationNodeId){
        return adjacent.get(destinationNodeId);
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
}
