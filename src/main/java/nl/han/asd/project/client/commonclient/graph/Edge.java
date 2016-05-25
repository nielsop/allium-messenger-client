package nl.han.asd.project.client.commonclient.graph;

/**
 * Created by Julius on 26/04/16.
 */
public class Edge {
    private Node destination;
    private float weight;

    public Edge(Node destination, float weight) {
        this.destination = destination;
        this.weight = weight;
    }
}
