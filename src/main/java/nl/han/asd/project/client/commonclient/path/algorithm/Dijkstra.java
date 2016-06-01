package nl.han.asd.project.client.commonclient.path.algorithm;

import nl.han.asd.project.client.commonclient.graph.Edge;
import nl.han.asd.project.client.commonclient.graph.Graph;
import nl.han.asd.project.client.commonclient.graph.Node;
import nl.han.asd.project.commonservices.internal.utility.Check;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Provides basic utilities for Dijkstra's algorithm.
 */
public class Dijkstra implements IPathFind {
    private Map<String, Node> vertices;
    private GraphMatrix graphMatrix;

    public Dijkstra(Map<String, Node> vertices, GraphMatrix graphMatrix) {
        this.vertices = Check.notNull(vertices, "vertices");
        this.graphMatrix = graphMatrix;
    }


    @Override public List<Node> findPath(Node startNode, Node endNode) {
        Check.notNull(startNode, "startNode");
        Check.notNull(endNode, "endNode");

        if (!vertices.containsKey(startNode.getId()))
            throw new IllegalArgumentException("startNode not in graph.");
        if (!vertices.containsKey(endNode.getId()))
            throw new IllegalArgumentException("endNode not in graph.");

        if (startNode == endNode)
            return Arrays.asList(new Node[] { startNode, endNode });

        int cost = graphMatrix
                .get(graphMatrix.findIndexOfKey(startNode.getId()), graphMatrix.findIndexOfKey(endNode.getId()));
        return func(cost, startNode, endNode);
    }

    private List<Node> func(int cost, Node currentNode, Node endNode) {
        System.out.println(String.format("Current: %s, Cost: %d, End: %s", currentNode.getId(), cost, endNode.getId()));

        if (cost < 0)
            return null;

        if (currentNode == endNode) {
            List<Node> listOfNodes = new LinkedList<>();
            listOfNodes.add(currentNode);
            return listOfNodes;
        }

        for (Edge edge : currentNode.getEdges()) {
            Node linkedNode = vertices.get(edge.getDestinationId());
            List<Node> l;

            if ((l = func((int) (cost - edge.getDistance()), linkedNode, endNode)) != null) {
                l.add(0, currentNode);
                return l;
            }
        }

        return null;
    }
}
