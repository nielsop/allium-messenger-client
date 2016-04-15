package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.node.Node;

/**
 * Created by Julius on 15/04/16.
 */
public class Contact {
    private String username;
    private Node[] connectedNodes;

    public Contact(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public Node[] getConnectedNodes() {
        return connectedNodes;
    }
    public void setConnectedNodes(Node[] connectedNodes){
        this.connectedNodes = connectedNodes;
    }
}
