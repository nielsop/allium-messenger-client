package nl.han.asd.project.client.commonclient.path.algorithm;

import java.util.HashMap;
import java.util.Map;

import nl.han.asd.project.client.commonclient.graph.Edge;
import nl.han.asd.project.client.commonclient.graph.Node;
import nl.han.asd.project.commonservices.internal.utility.Check;

/**
 * Class that provides GraphMatrix functions on top of the default {@link Matrix} class.
 */
public class GraphMatrix extends Matrix {

    private static final int ITERATIONS = 10;

    private Map<String, Integer> internalMap;
    private Map<String, Node> graphMap;

    /**
     * Initializes the graph.
     *
     * @param graphMap Map that represents the graph.
     */
    public GraphMatrix(Map<String, Node> graphMap) {
        super(Check.notNull(graphMap, "Graphmap").size());

        internalMap = new HashMap<>();
        this.graphMap = graphMap;

        prepareMatrixAndBuildInternalMap();
    }

    /**
     * Fills the matrix using the map that was passed as parameter in the constructor.
     * This method will add found edges bidirectional to the matrix.
     */
    public void fillAndCalculateMatrix() {
        for (Map.Entry<String, Node> nodeEntry : graphMap.entrySet()) {
            Node currentNode = nodeEntry.getValue();
            int currentNodeIndex = internalMap.get(currentNode.getId());

            for (Edge edge : currentNode.getEdges()) {
                int destinationNodeIndex = internalMap.get(edge.getDestinationId());

                short weight = (short) edge.getDistance();
                super.set(currentNodeIndex, destinationNodeIndex, weight);
                super.set(destinationNodeIndex, currentNodeIndex, weight);
            }
        }

        calculate(ITERATIONS);
    }

    /**
     * Finds the index of a given key.
     *
     * @param id Identifier of the node to find.
     * @return The found key.
     */
    public int findIndexOfKey(String id) {
        if (!internalMap.containsKey(id))
            throw new IllegalArgumentException("Graph doesn't contain key.");

        return internalMap.get(id);
    }

    private void prepareMatrixAndBuildInternalMap() {
        int index = 0;
        for (Map.Entry<String, Node> nodeEntry : graphMap.entrySet()) {
            if (!internalMap.containsKey(nodeEntry.getKey())) {
                internalMap.put(nodeEntry.getKey(), index);
                index++;
            }
        }
    }
}
