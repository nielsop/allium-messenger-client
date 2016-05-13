package nl.han.asd.project.client.commonclient.graph;

import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.util.HashMap;

/**
 * Created by Julius on 25/04/16.
 */
public class Graph {

    private HashMap<String, Node> vertexMap = new HashMap<String, Node>();

    public Node getNodeVertex(String nodeID) {
        Node vertex = vertexMap.get(nodeID);
        if (vertex == null) {
            throw new IllegalArgumentException("Node vertex with ID " + nodeID + " does not exist in the Graph");
        }
        return vertex;
    }

    public int getVertexMapSize() {
        return vertexMap.size();
    }

    public void resetGraph() {
        vertexMap = new HashMap<String, Node>();
    }

    public void addNodeVertex(HanRoutingProtocol.Node vertex) {
        Node node = new Node(vertex.getId(), vertex.getIPaddress(), vertex.getPort(),
                vertex.getPublicKey().toByteArray());
        vertexMap.putIfAbsent(node.getId(), node);
    }

    public void removeNodeVertex(HanRoutingProtocol.Node vertex) {
        vertexMap.remove(vertex.getId());
    }

}
