package nl.han.asd.project.client.commonclient.path;

import java.util.*;

import javax.inject.Inject;

import nl.han.asd.project.client.commonclient.graph.IGetVertices;
import nl.han.asd.project.client.commonclient.graph.Node;
import nl.han.asd.project.client.commonclient.master.IGetClientGroup;
import nl.han.asd.project.client.commonclient.path.algorithm.GraphMatrix;
import nl.han.asd.project.client.commonclient.path.algorithm.GraphMatrixPath;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.commonservices.internal.utility.Check;
import nl.han.asd.project.protocol.HanRoutingProtocol;

public class PathDeterminationService implements IGetMessagePath {
    private Map<Contact, Set<Node>> startingPoints = new HashMap<>();

    private Random random = new Random();
    private GraphMatrix graphMatrix;

    private IGetVertices getVertices;

    @Inject
    public PathDeterminationService(IGetVertices getVertices) {
        this.getVertices = Check.notNull(getVertices, "getVertices");
    }

    @Override
    public List<Node> getPath(int minHops, Contact contactReceiver) {
        Check.notNull(contactReceiver, "contactReceiver");

        Node[] receiverConnectedNodes = contactReceiver.getConnectedNodes();
        if (receiverConnectedNodes == null || receiverConnectedNodes.length == 0) {
            throw new IllegalArgumentException("No connected nodes for: " + contactReceiver);
        }

        Map<String, Node> vertices = getVertices.getVertices();
        graphMatrix = new GraphMatrix(vertices);

        List<Node> path = null;
        do {
            Set<Node> usedStartingPointsForContact = startingPoints.get(contactReceiver);
            if (usedStartingPointsForContact == null) {
                usedStartingPointsForContact = new HashSet<>();
            }

            for (int i = 0; path == null && i < 10; i++) {
                Node connectedEndpoint = receiverConnectedNodes[random.nextInt(receiverConnectedNodes.length)];
                path = getPathTo(connectedEndpoint, usedStartingPointsForContact);
            }

            if (path == null) continue;

        } while(path.size() < (minHops + 2));

        return path;
    }

    private List<Node> getPathTo(Node endpoint, Set<Node> usedStartingPoints) {
        Map<String, Node> vertices = getVertices.getVertices();

        Node startingPoint = vertices.get(random.nextInt(vertices.size()));
        for (int i = 0; usedStartingPoints.contains(startingPoint) && i < 10; i++) {
            startingPoint = vertices.get(random.nextInt(vertices.size()));
        }

        usedStartingPoints.add(startingPoint);
        return new GraphMatrixPath(vertices, graphMatrix).findPath(startingPoint, endpoint);
    }

}
