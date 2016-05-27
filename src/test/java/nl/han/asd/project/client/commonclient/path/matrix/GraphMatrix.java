package nl.han.asd.project.client.commonclient.path.matrix;

import nl.han.asd.project.client.commonclient.graph.Edge;
import nl.han.asd.project.client.commonclient.graph.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Jevgeni on 25-5-2016.
 */
public class GraphMatrix extends Matrix {

    private Map<String, Integer> internalMap;
    private Map<String, Node> graphMap;
    private int steps = 1;

    public GraphMatrix(Map<String, Node> graphMap) {
        super(graphMap.size());

        this.internalMap = new HashMap<>();
        this.graphMap = graphMap;

        this.prepareMatrixAndBuildInternalMap();
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

                int weight = (int) edge.getDistance();
                super.set(currentNodeIndex, destinationNodeIndex, weight);
                super.set(destinationNodeIndex, currentNodeIndex, weight);
            }
        }
    }

    /**
     * Also saves the current matrix. A list of these can be obtained through ..
     */
    public void calculate()
    {
        super.calculate(steps++);
    }


    public String toPrintableString() {
        StringBuilder builder = new StringBuilder();
        short[][][] matrix = super.getCurrentMatrix();

        String keys = internalMap.keySet().stream().map(k -> k).collect(Collectors.joining("\t"));
        builder.append(String.format("\t%s\n", keys));

        for (int i = 0; i < matrix.length; i++) {
            builder.append(internalMap.keySet().toArray()[i] + "\t");
            for (int j = 0; j < matrix.length; j++) {
                builder.append(matrix[i][j][0] + "\t");
            }

            builder.append("\n");
        }

        return builder.toString();
    }

}
