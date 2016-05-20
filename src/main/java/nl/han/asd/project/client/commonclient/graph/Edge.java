package nl.han.asd.project.client.commonclient.graph;

/**
 * Created by Julius on 26/04/16.
 */
public class Edge {
    private Node destination;

    private double distance;  // h is the heuristic of destination.

    public Edge(Node destination, double distance) {
        this.destination = destination;
        this.distance = distance;
    }
    // getH
    public double getDistance() {
        return this.distance;
    }

    public String getDestinationId() {
        return destination.getId();
    }

}
