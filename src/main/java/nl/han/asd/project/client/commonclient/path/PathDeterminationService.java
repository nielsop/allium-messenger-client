package nl.han.asd.project.client.commonclient.path;

import nl.han.asd.project.client.commonclient.master.IGetUpdatedGraph;
import nl.han.asd.project.client.commonclient.node.Node;
import nl.han.asd.project.client.commonclient.store.Contact;

import java.util.Random;

public class PathDeterminationService implements IGetPath {
    public IGetUpdatedGraph updatedGraph;

    @Override
    public Node[] getPath(int minHops, Contact contactOntvanger) {
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

    private Node[] buildPath(Node hostConnectedNode, int minHops){
        Node[] path = new Node[minHops];
        path[0] = hostConnectedNode;
        for(int i = 1; i < minHops; i++){
            path[i] = new Node();
        }

        return path;
    }


}
