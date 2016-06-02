package nl.han.asd.project.client.commonclient.path.algorithm;

/**
 * This exception is throwed by GraphMatrixPath whenever the passed node is not in the graph.
 */
public class NodeNotInGraphException extends RuntimeException {
    public NodeNotInGraphException(String s) {
        super(s);
    }
}
