package nl.han.asd.project.client.commonclient.path;

import java.util.*;

import javax.inject.Inject;

import nl.han.asd.project.client.commonclient.graph.IGetVertices;
import nl.han.asd.project.client.commonclient.graph.Node;
import nl.han.asd.project.client.commonclient.path.algorithm.GraphMatrix;
import nl.han.asd.project.client.commonclient.path.algorithm.GraphMatrixPath;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.client.commonclient.store.NoConnectedNodesException;
import nl.han.asd.project.commonservices.internal.utility.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to determinate paths.
 */
public class PathDeterminationService implements IGetMessagePath {
    private static final Logger LOGGER = LoggerFactory.getLogger(PathDeterminationService.class);
    public static final int ITERATIONS = 10;

    private Map<Contact, Set<Node>> startingPoints = new HashMap<>();
    private Random random = new Random();
    private IGetVertices getVertices;
    private GraphMatrix graphMatrix;

    /**
     * Creates an instance of PathDeterminationService.
     *
     * @param getVertices Instance of IGetVertices; used to get the graph vertices.
     */
    @Inject public PathDeterminationService(IGetVertices getVertices) {
        this.getVertices = Check.notNull(getVertices, "getVertices");
    }

    /**
     * {@inheritDoc}
     */
    @Override public List<Node> getPath(int minHops, Contact contactReceiver) {
        Check.notNull(contactReceiver, "contactReceiver");

        Node[] receiverConnectedNodes;
        try {
            receiverConnectedNodes = contactReceiver.getConnectedNodes();
        } catch (NoConnectedNodesException e) {
            LOGGER.error("No connected nodes.", e);
            return null;
        }
        if (receiverConnectedNodes == null || receiverConnectedNodes.length == 0) {
            throw new IllegalArgumentException("No connected nodes for: " + contactReceiver);
        }

        Map<String, Node> vertices = getVertices.getVertices();
        graphMatrix = new GraphMatrix(vertices, ITERATIONS);
        graphMatrix.fillAndCalculateMatrix();

        List<Node> path;
        do {
            path = findPath(contactReceiver, receiverConnectedNodes[random
                    .nextInt(receiverConnectedNodes.length)]);

        } while (path.size() < (minHops + 2));

        return path;
    }



    private List<Node> findPath(Contact contactReceiver,
            Node receiverConnectedNode) {
        List<Node> path = null;
        Set<Node> usedStartingPointsForContact = startingPoints.get(contactReceiver);
        if (usedStartingPointsForContact == null) {
            usedStartingPointsForContact = new HashSet<>();
        }

        for (int i = 0; path == null && i < ITERATIONS; i++) {
            Node connectedEndpoint = receiverConnectedNode;
            path = getPathTo(connectedEndpoint, usedStartingPointsForContact);
        }

        if (path == null)
            path = Collections.emptyList();

        return path;
    }

    private List<Node> getPathTo(Node endpoint, Set<Node> usedStartingPoints) {
        Map<String, Node> vertices = getVertices.getVertices();

        Node startingPoint = getRandomNodeFromMap(vertices);
        for (int i = 0; usedStartingPoints.contains(startingPoint) && i < ITERATIONS; i++) {
            startingPoint = getRandomNodeFromMap(vertices);
        }
        usedStartingPoints.add(startingPoint);

        return new GraphMatrixPath(vertices, graphMatrix)
                .findPath(startingPoint, endpoint);
    }

    private Node getRandomNodeFromMap(Map<String, Node> map) {
        Iterator<Map.Entry<String, Node>> iterator = map.entrySet().iterator();

        int randomIndex = random.nextInt(map.size());
        while (randomIndex > 0) {
            iterator.next();
            randomIndex--;
        }

        return iterator.next().getValue();
    }

}
