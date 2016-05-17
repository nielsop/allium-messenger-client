package nl.han.asd.project.client.commonclient.graph;

import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created by Julius on 25/04/16.
 */
public class Graph {

    private Map<String,Node> vertexMap = new HashMap<>();

    public Node getNodeVertex(String nodeID) {
        Node vertex = vertexMap.get(nodeID);
        if (vertex == null) {
            throw new NoSuchElementException();
        }
        return vertex;
    }

    public int getVertexMapSize() {
        return vertexMap.size();
    }

    public void resetGraph(){
        vertexMap = new HashMap<>();
    }

    public void addNodeVertex(HanRoutingProtocol.Node vertex){
        Node node = new Node(vertex.getId(),vertex.getIPaddress(),vertex.getPort(),vertex.getPublicKeyBytes().toByteArray());
        vertexMap.put(node.getId(),node);
    }

    public void addEdgesToVertex(HanRoutingProtocol.Node vertex){
        Node node = getNodeVertex(vertex.getId());
        vertex.getEdgeList().forEach(node::addEdge);
    }

    public void removeNodeVertex(HanRoutingProtocol.Node vertex) {
        vertexMap.remove(vertex.getId());
    }

}
