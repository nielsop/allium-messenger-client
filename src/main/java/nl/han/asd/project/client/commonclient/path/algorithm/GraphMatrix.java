package nl.han.asd.project.client.commonclient.path.algorithm;

import java.util.HashMap;
import java.util.Map;

import nl.han.asd.project.client.commonclient.graph.Edge;
import nl.han.asd.project.client.commonclient.graph.Node;

public class GraphMatrix extends Matrix {

    private static final int ITERATIONS = 10;

    private Map<String, Integer> internalMap;
    private Map<String, Node> graphMap;

    public GraphMatrix(Map<String, Node> graphMap) {
        super(graphMap.size());

        internalMap = new HashMap<>();
        this.graphMap = graphMap;

        prepareMatrixAndBuildInternalMap();
        fillMatrix();
        calculate(ITERATIONS);
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

    public void fillMatrix() {
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
    }

    public int findIndexOfKey(String id) {
        return internalMap.get(id);
    }
}
