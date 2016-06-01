package nl.han.asd.project.client.commonclient.path;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.inject.Inject;

import nl.han.asd.project.client.commonclient.graph.IGetVertices;
import nl.han.asd.project.client.commonclient.graph.Node;
import nl.han.asd.project.client.commonclient.master.IGetClientGroup;
import nl.han.asd.project.client.commonclient.path.algorithm.GraphMatrix;
import nl.han.asd.project.client.commonclient.path.algorithm.GraphMatrixPath;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.commonservices.internal.utility.Check;

public class PathDeterminationService implements IGetMessagePath {
    private Map<Contact, Set<Node>> startingPoints = new HashMap<>();

    private Random random = new Random();
    private GraphMatrix graphMatrix;

    private IGetVertices getVertices;
    private IGetClientGroup getClientGroup;

    @Inject
    public PathDeterminationService(IGetVertices getVertices, IGetClientGroup getClientGroup) {
        this.getVertices = Check.notNull(getVertices, "getVertices");
        this.getClientGroup = Check.notNull(getClientGroup, "getClientGroup");
    }

    @Override
    public List<Node> getPath(int minHops, Contact contactReciever) {
        Check.notNull(contactReciever, "contactReciever");

        Node[] receiverConnectedNodes = contactReciever.getConnectedNodes();
        if (receiverConnectedNodes == null || receiverConnectedNodes.length == 0) {
            throw new IllegalArgumentException("No connected nodes for: " + contactReciever);
        }

        graphMatrix = new GraphMatrix(getVertices.getVertices());

        Set<Node> usedStartingPointsForContact = startingPoints.get(contactReciever);
        if (usedStartingPointsForContact == null) {
            usedStartingPointsForContact = new HashSet<>();
        }

        List<Node> path = null;
        for (int i = 0; path == null && i < 10; i++) {
            Node connectedEndpoint = receiverConnectedNodes[random.nextInt(receiverConnectedNodes.length)];
            path = getPathTo(connectedEndpoint, usedStartingPointsForContact);
        }

        return path;
    }

    private List<Node> getPathTo(Node endpoint, Set<Node> usedStartingPoints) {
        Map<String, Node> vertices = getVertices.getVertices();

        Node startingPoint = null;
        for (int i = 0; usedStartingPoints.contains(startingPoint) && i < 10; i++) {
            startingPoint = vertices.get(random.nextInt(vertices.size()));
        }

        usedStartingPoints.add(startingPoint);
        return new GraphMatrixPath(vertices, graphMatrix).findPath(startingPoint, endpoint);
    }

}
