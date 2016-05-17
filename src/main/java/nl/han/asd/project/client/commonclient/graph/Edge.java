package nl.han.asd.project.client.commonclient.graph;

/**
 * Created by Julius on 26/04/16.
 */
public class Edge {
    private String destinationNodeId;
    private float weight;

    public Edge(String destinationNodeId, float weight) {
        this.destinationNodeId = destinationNodeId;
        this.weight = weight;
    }

    public String getDestinationNodeId() {
        return destinationNodeId;
    }

    public float getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object anotherObj) {
        return anotherObj instanceof Edge;
    }

    @Override
    public int hashCode() {
        return (destinationNodeId + "@" + weight).hashCode();
    }


}
