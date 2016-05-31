package nl.han.asd.project.client.commonclient.store;

public class NoConnectedNodesException extends Exception {
    public NoConnectedNodesException() {
        super("No connected nodes found!");
    }

    public NoConnectedNodesException(String message) {
        super(message);
    }
}
