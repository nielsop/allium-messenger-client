package nl.han.asd.project.client.commonclient.path;

import nl.han.asd.project.client.commonclient.graph.IGetVertices;
import nl.han.asd.project.client.commonclient.graph.Node;
import nl.han.asd.project.client.commonclient.path.matrix.PathMatrix;
import nl.han.asd.project.client.commonclient.path.matrix.PathMatrix.PathOption;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.client.commonclient.store.NoConnectedNodesException;
import nl.han.asd.project.commonservices.internal.utility.Check;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * IGetMessagePath implementation using the PathMatrix class
 * to generate and cache paths;
 *
 * @version 1.0
 */
public class PathDeterminationService implements IGetMessagePath {

    private static final int MAX_HOPS = 10;

    private IGetVertices getVertices;

    private Map<String, Node> vertices;
    private PathMatrix pathMatrix;

    @Inject
    public PathDeterminationService(IGetVertices getVertices) {
        this.getVertices = Check.notNull(getVertices, "getVertices");

        vertices = getVertices.getVertices();
        pathMatrix = new PathMatrix(vertices, MAX_HOPS);
    }

    @Override
    public List<Node> getPath(int minHops, Contact contactReceiver) {
        if (minHops > MAX_HOPS) {
            throw new IllegalArgumentException(minHops + " > " + MAX_HOPS);
        }

        if (minHops < 0) {
            return Collections.emptyList();
        }
        Check.notNull(contactReceiver, "contactReceiver");

        Node[] contactReceiverNodes = null;
        try {
            contactReceiverNodes = contactReceiver.getConnectedNodes();
        } catch (NoConnectedNodesException e) {
            return Collections.emptyList();
        }

        Map<String, Node> newVertices = getVertices.getVertices();
        if (!newVertices.equals(vertices)) {
            vertices = newVertices;
            pathMatrix = new PathMatrix(vertices, MAX_HOPS);
        }

        Random random = new Random();

        Node endNode = contactReceiverNodes[random
                .nextInt(contactReceiverNodes.length)];
        List<PathOption> pathOptions = pathMatrix.getOptions(endNode.getId(),
                minHops);

        if (pathOptions.isEmpty()) {
            return Collections.emptyList();
        }

        return pathOptions.get(random.nextInt(pathOptions.size()))
                .getPath(vertices);
    }

}
