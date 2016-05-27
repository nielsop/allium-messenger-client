package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.graph.Node;

/**
 * The contact which the current user can communicate to.
 *
 * @version 1.0
 */
public class Contact {
    private String username;
    private Node[] connectedNodes;
    private String publicKey;
    private boolean online;

    /**
     * Constructor of Contact.
     *
     * @param username username of contact
     */
    public Contact(String username) {
        this.username = username;
    }

    /**
     * Constructor of Contact.
     *
     * @param username username of contact
     * @param publicKey publicKey of contact
     */
    public Contact(String username, String publicKey) {
        this.username = username;
        this.publicKey = publicKey;
    }

    /**
     * Getter for username.
     *
     * @return username of contact
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter for connectedNodes.
     *
     * @return an array of nodes where the contact is connected to
     * @throws NoConnectedNodesException when no nodes aren't initialized
     */
    public Node[] getConnectedNodes() throws NoConnectedNodesException {
        if (connectedNodes == null || connectedNodes.length <= 0) {
            throw new NoConnectedNodesException("The connected Nodes from the contactStore are not set");
        }
        return connectedNodes;
    }

    /**
     * Setter for connectedNodes.
     *
     * @param connectedNodes an array of nodes where the contact is connected to
     */
    public void setConnectedNodes(Node[] connectedNodes) {
        this.connectedNodes = connectedNodes;
    }

    /**
     * Setter for publicKey.
     *
     * @param publicKey the publicKey of the contact for encrypting messages.
     */
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * Getter for publicKey.
     *
     * @return the publicKey of the contact for encrypting messages.
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * Getter for online status.
     *
     * @return if the contact is online or not
     */
    public boolean isOnline() {
        return online;
    }

    /**
     * Setter for online status.
     *
     * @param online if the contact is online or not
     */
    public void setOnline(boolean online) {
        this.online = online;
    }
}
