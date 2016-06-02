package nl.han.asd.project.client.commonclient.path;

import java.util.List;

import nl.han.asd.project.client.commonclient.graph.Node;
import nl.han.asd.project.client.commonclient.store.Contact;

public interface IGetMessagePath {
    /**
     * Tries to find a path from the most updated graph.
     *
     * @param minHops Minimal amount of nodes that should be visited before sending it to the real receiver.
     * @param contactReciever The receiver of the message.
     * @return Returns a path, if found, otherwise it will return null.
     */
    List<Node> getPath(int minHops, Contact contactReciever);
}
