package nl.han.asd.project.client.commonclient.graph;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 20-4-2016
 */
public class Node {
    private List<Edge> adjacent;
    private String id;
    private String ipAddress;
    private int port;
    private byte[] publicKey;

    public Node(String id, String ipAddress, int port, byte[] publicKey) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.port = port;
        this.publicKey = publicKey;
        this.adjacent = new LinkedList<>();
    }

    @Override
    public boolean equals(Object anotherObj) {
        return anotherObj instanceof Node;
    }

    @Override
    public int hashCode() {
        return (id + "@" + ipAddress + ":" + port).hashCode();
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
