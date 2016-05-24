package nl.han.asd.project.client.commonclient.graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 20-4-2016
 */
public class Node {
    List<Edge> edges;
    private String id;
    private String ipAddress;
    private int port;
    private byte[] publicKey;
    private double distanceToSource; // g
    private double f;  // f = g + h

    public Node(String id, String ipAddress, int port, byte[] publicKey) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.port = port;
        this.publicKey = publicKey;
        this.distanceToSource = Double.MAX_VALUE;
        this.edges = new LinkedList<>();
    }

    @Override public boolean equals(Object anotherObj) {
        return anotherObj instanceof Node;
    }

    @Override public int hashCode() {
        return (id + "@" + ipAddress + ":" + port).hashCode();
    }

    public void addEdge(Node destination, double distance)
    {
        edges.add(new Edge(destination, distance));
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


    public void calcF(Node destination) {
        double h = findDistanceToDestination(destination);
        this.f = distanceToSource + h; // f = g + h
    }

    public double getF() {
        return f;
    }

    public double getDistanceToSource() {
        return this.distanceToSource;
    }

    public void setDistanceToSource(double value) {
        this.distanceToSource = value;
    }

    private double findDistanceToDestination(Node destination) {
        for (Edge edge : edges) {
            if (edge.getDestinationId() == destination.getId()) {
                return edge.getDistance();
            }
        }

        return Double.MAX_VALUE;
    }

    public List<Edge> getEdges() {
        return this.edges;
    }

}
