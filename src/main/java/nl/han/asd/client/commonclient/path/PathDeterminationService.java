package nl.han.asd.client.commonclient.path;

import nl.han.asd.client.commonclient.master.IGetClientGroup;
import nl.han.asd.client.commonclient.master.IGetUpdatedGraph;
import nl.han.asd.client.commonclient.node.Node;
import nl.han.asd.client.commonclient.store.Contact;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Random;

public class PathDeterminationService implements IGetPath {
    public IGetUpdatedGraph updatedGraph;
    public IGetClientGroup clientGroup;

    @Inject
    public PathDeterminationService(IGetUpdatedGraph updatedGraph, IGetClientGroup clientGroup) {
        this.updatedGraph = updatedGraph;
        this.clientGroup = clientGroup;
    }

    @Override
    public ArrayList<Node> getPath(int minHops, Contact contactOntvanger) {
        if(minHops < 1){
            throw new IllegalArgumentException("The minimum amount of Hops should be more than 0");
        }

        return buildPath(calculateUsableConnectedNode(contactOntvanger),minHops);
    }

    private Node calculateUsableConnectedNode(Contact contact){
        Node[] connectedNodes = contact.getConnectedNodes();
        Random ran = new Random();
        int indexConnectedNode = ran.nextInt(connectedNodes.length);

        return connectedNodes[indexConnectedNode];
    }

    private ArrayList<Node> buildPath(Node hostConnectedNode, int minHops){
        ArrayList<Node> path = new ArrayList<Node>();
        path.add(hostConnectedNode);

        for(int i = 1; i < minHops; i++){
            path.add(i,new Node());
        }

        return path;
    }
}
