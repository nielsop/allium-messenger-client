package nl.han.asd.project.client.commonclient.path.matrix;

import java.util.HashMap;
import java.util.Map;

import nl.han.asd.project.client.commonclient.graph.Edge;
import nl.han.asd.project.client.commonclient.graph.Node;

/**
 * Created by Jevgeni on 25-5-2016.
 */
public class GraphMatrix2 extends Matrix2 {

    private Map<String, Integer> internalMap;
    private Map<Integer, String> internalMap2;
    private Map<String, Node> graphMap;
    private int steps = 1;

    public GraphMatrix2(Map<String, Node> graphMap) {
        super(graphMap.size());

        internalMap = new HashMap<>();
        internalMap2 = new HashMap<>();
        this.graphMap = graphMap;

        prepareMatrixAndBuildInternalMap();
    }

    private void prepareMatrixAndBuildInternalMap() {
        int index = 0;
        for (Map.Entry<String, Node> nodeEntry : graphMap.entrySet()) {
            if (!internalMap.containsKey(nodeEntry.getKey())) {
                internalMap.put(nodeEntry.getKey(), index);
                internalMap2.put(index, nodeEntry.getKey());
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

    /**
     * Also saves the current matrix. A list of these can be obtained through ..
     */
    public void calculate() {
        super.calculate(10);
    }

    public String getFormattedPath(String source, String destination) {
        StringBuilder sb = new StringBuilder();

        sb.append(source);

        for (Integer index : super.getPath(internalMap.get(source).intValue(),
                internalMap.get(destination).intValue())) {
            sb.append(" -> ").append(internalMap2.get(index));
        }

        sb.append(" -> ").append(destination);

        return sb.toString();
    }

}
