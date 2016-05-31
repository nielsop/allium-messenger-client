package nl.han.asd.project.client.commonclient.path;

import nl.han.asd.project.client.commonclient.graph.Node;
import nl.han.asd.project.client.commonclient.master.IGetClientGroup;
import nl.han.asd.project.client.commonclient.master.IGetUpdatedGraph;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.commonservices.internal.utility.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PathDeterminationService implements IGetMessagePath {
    private static final Logger LOGGER = LoggerFactory.getLogger(PathDeterminationService.class);
    private IGetUpdatedGraph graphUpdates;
    private IGetClientGroup clientGroup;

    @Inject
    public PathDeterminationService(IGetUpdatedGraph graphUpdates, IGetClientGroup clientGroup) {
        this.graphUpdates = Check.notNull(graphUpdates, "graphUpdates");
        this.clientGroup = Check.notNull(clientGroup, "clientGroup");
    }

    @Override
    public List<Node> getPath(int minHops, Contact contactOntvanger) {

        if (minHops < 1) {
            throw new IllegalArgumentException("The minimum amount of Hops should be more than 0");
        }

        return buildPath(calculateUsableConnectedNode(contactOntvanger), minHops);
    }

    private Node calculateUsableConnectedNode(Contact contact) {
        Node[] connectedNodes = new Node[0];
        try {
            connectedNodes = contact.getConnectedNodes();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        Random ran = new Random();
        int indexConnectedNode = ran.nextInt(connectedNodes.length);

        return connectedNodes[indexConnectedNode];
    }

    private ArrayList<Node> buildPath(Node hostConnectedNode, int minHops) {
        ArrayList<Node> path = new ArrayList<Node>();
        path.add(hostConnectedNode);

        for (int i = 1; i < minHops; i++) {
            path.add(i, new Node("Node_ID1", "192.168.2.empty", 1234, "123456789".getBytes()));
        }

        return path;
    }
}
