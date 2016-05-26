package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.graph.Node;

public class Contact {
    private String username;
    private Node[] connectedNodes;
    private String publicKey;
    private boolean online;

    /**
     * Creates a new contact with a username and a publicKey
     * @param username username
     * @param publicKey publicKey
     */
    public Contact(String username, String publicKey) {
        this.username = username;
        this.publicKey = publicKey;
    }

    // TODO test, remove
    public Contact(String username, String publicKey, boolean online) {
        this.username = username;
        this.publicKey = publicKey;
        this.online = online;
    }

    public String getUsername() {
        return username;
    }

    public Node[] getConnectedNodes() throws NoConnectedNodesException {
        if (connectedNodes == null || connectedNodes.length <= 0) {
            throw new NoConnectedNodesException("The connected Nodes from the contact are not set");
        }
        return connectedNodes;

    }

    public void setConnectedNodes(Node[] connectedNodes) {
        this.connectedNodes = connectedNodes;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}
