package nl.han.asd.project.client.commonclient.path.matrix;

import nl.han.asd.project.client.commonclient.graph.Node;
import nl.han.asd.project.client.commonclient.path.algorithm.IPathFind;

import java.util.List;
import java.util.Map;

/**
 * Created by Jevgeni on 25-5-2016.
 */
public class MatrixPathFind implements IPathFind {
    private final GraphMatrix graphMatrix;

    public MatrixPathFind(Map<String, Node> graphMap) {
        graphMatrix = new GraphMatrix(graphMap);
        graphMatrix.fillMatrix();
    }

    public short[][][] get() {
        return graphMatrix.getCurrentMatrix();
    }

    @Override public List<Node> findPath(Node startNode, Node endNode) {
        return null;
    }
}
