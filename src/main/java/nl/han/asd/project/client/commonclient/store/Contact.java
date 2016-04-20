package nl.han.asd.project.client.commonclient.store;


import nl.han.asd.project.client.commonclient.node.Node;

/**
 * Created by Julius on 15/04/16.
 */
public class Contact {
    private String username;
    private Node[] connectedNodes;
    private String publicKey;

    public Contact(String username,String publicKey) {
        this.username = username;
        this.publicKey = publicKey;
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

    public String getPublicKey() {
        return publicKey;
    }
}
