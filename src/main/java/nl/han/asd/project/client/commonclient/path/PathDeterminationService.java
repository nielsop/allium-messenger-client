package nl.han.asd.project.client.commonclient.path;

import nl.han.asd.project.client.commonclient.graph.IGetVertices;
import nl.han.asd.project.client.commonclient.graph.Node;
import nl.han.asd.project.client.commonclient.master.IGetClientGroup;
import nl.han.asd.project.client.commonclient.master.IGetNodes;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PathDeterminationService implements IGetPath {
    private final Random random = new Random();

    private static final Logger LOGGER = LoggerFactory.getLogger(PathDeterminationService.class);
    private IGetClientGroup clientGroup;
    private IGetVertices vertices;
    private IGetNodes nodes;

    @Inject
    public PathDeterminationService(IGetVertices vertices, IGetClientGroup clientGroup, IGetNodes nodes) {
        this.vertices = vertices;
        this.clientGroup = clientGroup;
        this.nodes = nodes;
    }

    @Override
    public ArrayList<Node> getPath(int minHops, Contact contactReciever) {
        try {
            return internalGetPath(minHops, contactReciever);
        } catch (Exception e) {
            return null;
        }
    }

    private ArrayList<Node> internalGetPath(int minHops, Contact contactReceiver)
            throws Exception {
        if (minHops < 1) {
            throw new IllegalArgumentException(
                    "The minimum amount of Hops should be more than 0");
        }

        Map<String, Node> vertices = this.vertices.getVertices();

        List<HanRoutingProtocol.Node> myConnectedNodes = nodes.getAllNodes();
        HanRoutingProtocol.Node startNode = getConnectedNode(myConnectedNodes);

        HanRoutingProtocol.Client receiver = getClientFromContact(contactReceiver);
        //.getConnectedNodesList() rets -> array van node ids
        HanRoutingProtocol.Node endNode = HanRoutingProtocol.Node.parseFrom(
                getConnectedNode(receiver.getConnectedNodesList().asByteStringList()));

        // hanprotocol omzetten naar -> graph node
        return null;
        //return calculatePath(vertices, startNode, endNode);
    }

    private ArrayList<Node> calculatePath(Map<String, Node> vertices, Node startNode, Node endNode)
    {
        //IPathFind pathFinder = new AStar();
        //return pathFinder.findPath(startNode, endNode);
        //startNode.getEdgeList().get(0).getWeight();
        return null; //
    }

    private <T> T getConnectedNode(List<T> nodeList)
            throws Exception {
        if (nodeList.isEmpty())
            throw new Exception("Empty list");

        T genericNode = nodeList.get(random.nextInt(nodeList.size()));
        return genericNode;
    }

    private HanRoutingProtocol.Client getClientFromContact(Contact contactReciever)
            throws Exception {
        List<HanRoutingProtocol.Client> clients = clientGroup.getClientGroup();
        for(HanRoutingProtocol.Client client : clients)
        {
            if (client.getUsername() == contactReciever.getUsername())
            {
                 return client;
            }
        }

        throw new Exception("Cannot find Client from Contact.");
    }
}
