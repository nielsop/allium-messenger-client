package nl.han.asd.project.client.commonclient.path.matrix;

/**
 * Exception thrown whenever the passed Node is not in the Graph.
 */
public class NodeNotInGraphException extends RuntimeException {
    public NodeNotInGraphException(String id) { super(id);
    }
}
