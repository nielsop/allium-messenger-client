package nl.han.asd.project.client.commonclient.store;

/**
 *
 *
 * @author Niels Bokmans
 * @version 1.0
 * @since 10-5-2016
 */
public class NoConnectedNodesException extends Exception {
    public NoConnectedNodesException() {

    }

    public NoConnectedNodesException(String message) {
        super(message);
    }
}
