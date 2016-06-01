package nl.han.asd.project.client.commonclient.path.algorithm;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import nl.han.asd.project.client.commonclient.graph.Edge;
import nl.han.asd.project.client.commonclient.graph.Node;
import nl.han.asd.project.commonservices.internal.utility.Check;

/**
 * Provides basic utilities for Dijkstra's algorithm.
 */
public class GraphMatrixPath implements IPathFind {
    private Map<String, Node> vertices;
    private GraphMatrix graphMatrix;

    public GraphMatrixPath(Map<String, Node> vertices, GraphMatrix graphMatrix) {
        this.vertices = Check.notNull(vertices, "vertices");
        this.graphMatrix = graphMatrix;
    }

    @Override
    public List<Node> findPath(Node startNode, Node endNode) {
        Check.notNull(startNode, "startNode");
        Check.notNull(endNode, "endNode");

        if (!vertices.containsKey(startNode.getId())) {
            throw new IllegalArgumentException("startNode not in graph.");
        }
        if (!vertices.containsKey(endNode.getId())) {
            throw new IllegalArgumentException("endNode not in graph.");
        }

        if (startNode == endNode) {
            return Arrays.asList(new Node[] { startNode, endNode });
        }

        int cost = graphMatrix.get(graphMatrix.findIndexOfKey(startNode.getId()),
                graphMatrix.findIndexOfKey(endNode.getId()));

        if (cost == 0) {
            return Collections.emptyList();
        }

        return buildPath(cost, startNode, endNode);
    }

    private List<Node> buildPath(int cost, Node currentNode, Node endNode) {
        if (cost < 0) {
            return null;
        }

        List<Node> listOfNodes;
        if (currentNode == endNode) {
            listOfNodes = new LinkedList<>();
            listOfNodes.add(currentNode);
            return listOfNodes;
        }

        for (Edge edge : currentNode.getEdges()) {
            Node linkedNode = vertices.get(edge.getDestinationId());

            if ((listOfNodes = buildPath((int) (cost - edge.getDistance()), linkedNode, endNode)) != null) {
                listOfNodes.add(0, currentNode);
                return listOfNodes;
            }
        }

        return null;
    }
}
