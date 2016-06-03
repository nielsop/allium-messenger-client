package nl.han.asd.project.client.commonclient.graph;

import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class Graph {
    private Map<String, Node> graph = new HashMap<>();

    public Node getNodeVertex(String nodeID) {
        Node vertex = graph.get(nodeID);
        if (vertex == null) {
            throw new NoSuchElementException();
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
        addNodeVertex(node);
    }

    public void addNodeVertex(Node vertex) {
        if (!graph.containsKey(vertex.getId())) {
            graph.put(vertex.getId(), vertex);
        }
    }

    public void addEdgesToVertex(HanRoutingProtocol.Node vertex) {
        Node node = getNodeVertex(vertex.getId());
        for (final HanRoutingProtocol.Edge edge : vertex.getEdgeList()) {
            node.addEdge(edge);
        }
    }

    public void removeNodeVertex(HanRoutingProtocol.Node vertex) {
        graph.remove(vertex.getId());
    }

    public Map<String, Node> getGraphMap() {
        return this.graph;
    }

}
