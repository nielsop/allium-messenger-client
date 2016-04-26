package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.node.Node;

/**
 * Created by Marius on 25-04-16.
 */
public class Contact {
    private String username;
    private Node[] connectedNodes;
    private String publicKey;

    public Contact(String username, String publicKey) {
        this.username = username;
        this.publicKey = publicKey;
    }

    public String getUsername() {
        return username;
    }

    public Node[] getConnectedNodes() throws Exception {
        if (connectedNodes == null || connectedNodes.length <= 0) {
            throw new Exception("The conneted Nodes from the contact are not set");
        }
        return connectedNodes;

    }

    public void setConnectedNodes(Node[] connectedNodes) {
        this.connectedNodes = connectedNodes;
    }

    public String getPublicKey() {
        return publicKey;
    }
}
