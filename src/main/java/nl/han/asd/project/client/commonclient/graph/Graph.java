package nl.han.asd.project.client.commonclient.graph;

import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Julius on 25/04/16.
 */
public class Graph {

    private Map<String, Node> graph = new HashMap<>();

    public Node getNodeVertex(String nodeID) {
        Node vertex = graph.get(nodeID);
        if (vertex == null) {
            throw new IllegalArgumentException("Node vertex with ID " + nodeID + " does not exist in the Graph");
        }
        return vertex;
    }

    public int getVertexMapSize() {
        return graph.size();
    }

    public void resetGraph() {
        graph = new HashMap<>();
    }

    public void addNodeVertex(HanRoutingProtocol.Node vertex) {
        Node node = new Node(vertex.getId(), vertex.getIPaddress(), vertex.getPort(),
                vertex.getPublicKey().toByteArray());
        graph.putIfAbsent(node.getId(), node);
    }

    public void addNodeVertex(Node vertex) {
        graph.putIfAbsent(vertex.getId(), vertex);
    }

    public void removeNodeVertex(HanRoutingProtocol.Node vertex) {
        graph.remove(vertex.getId());
    }

    public Map<String, Node> getGraphMap() {
        return this.graph;
    }


}
