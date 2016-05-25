package nl.han.asd.project.client.commonclient.path;

import nl.han.asd.project.client.commonclient.graph.IGetVertices;
import nl.han.asd.project.client.commonclient.graph.Node;
import nl.han.asd.project.client.commonclient.master.IGetClientGroup;
import nl.han.asd.project.client.commonclient.path.algorithm.AStar;
import nl.han.asd.project.client.commonclient.path.algorithm.IPathFind;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PathDeterminationService implements IGetMessagePath {
    private final Random random = new Random();

    private static final Logger LOGGER = LoggerFactory.getLogger(PathDeterminationService.class);
    private IGetClientGroup clientGroup;
    private IGetVertices vertices;

    @Inject
    public PathDeterminationService(IGetVertices vertices, IGetClientGroup clientGroup) {
        this.vertices = vertices;
        this.clientGroup = clientGroup;
    }

    @Override public List<Node> getPath(int minHops, Contact contactReciever) {
        try {
            return internalGetPath(minHops, contactReciever);
        } catch (Exception e) {
            return null;
        }
    }

    private List<Node> internalGetPath(int minHops, Contact contactReceiver)
            throws Exception {
        if (minHops < 1) {
            throw new IllegalArgumentException(
                    "The minimum amount of Hops should be more than 0");
        }

        Map<String, Node> mapVertices = this.vertices.getVertices();

        List<Node> listOfNodes = new ArrayList<>(mapVertices.values());
        Node startNode = getRandomConnectedNode(listOfNodes);

        HanRoutingProtocol.Client receiver = getClientFromContact(
                contactReceiver);
        String nodeId = getRandomConnectedNode(receiver.getConnectedNodesList());
        Node endNode = nodeIdentifierToNodeFromGraph(mapVertices, nodeId);

        return calculatePath(mapVertices, startNode, endNode);
    }

    private List<Node> calculatePath(Map<String, Node> vertices, Node startNode,
            Node endNode) {
        IPathFind pathFinder = new AStar(vertices);
        return pathFinder.findPath(startNode, endNode);
    }

    private Node nodeIdentifierToNodeFromGraph(Map<String, Node> mapOfNodes,
            String identifier) {
        for (Map.Entry<String, Node> entry : mapOfNodes.entrySet()) {
            if (entry.getKey() == identifier)
                return entry.getValue();
        }

        LOGGER.error("Couldn't find matching identifier in the map.");
        throw new RuntimeException(
                "Couldn't find matching identifier in the map.");
    }

    private <T> T getRandomConnectedNode(List<T> nodeList) throws Exception {
        if (nodeList.isEmpty())
            throw new Exception("Empty list");

        T genericNode = nodeList.get(random.nextInt(nodeList.size()));
        return genericNode;
    }

    private HanRoutingProtocol.Client getClientFromContact(Contact contactReciever)
            throws Exception {
        List<HanRoutingProtocol.Client> clients = clientGroup.getClientGroup();
        for (HanRoutingProtocol.Client client : clients) {
            if (client.getUsername() == contactReciever.getUsername())
                return client;
        }

        LOGGER.error(String.format("Cannot find Client for contact %s.",
                contactReciever.getUsername()));
        throw new Exception("Cannot find Client from Contact.");
    }
}
