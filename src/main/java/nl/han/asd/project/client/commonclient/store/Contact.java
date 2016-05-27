package nl.han.asd.project.client.commonclient.store;

import com.google.inject.Guice;
import com.google.inject.Injector;
import nl.han.asd.project.client.commonclient.CommonclientModule;
import nl.han.asd.project.client.commonclient.graph.Node;
import nl.han.asd.project.commonservices.encryption.EncryptionService;

public class Contact {
    private String username;
    private Node[] connectedNodes;
    private byte[] publicKey;
    private boolean online;

    public Contact(String username) {
        this(username, new byte[] {}, false);
    }

    public Contact(String username, byte[] publicKey) {
        this(username, publicKey, false);
    }

    public Contact(String username, byte[] publicKey, boolean online) {
        this.username = username;
        this.publicKey = publicKey;
        this.online = online;
    }

    public static Contact fromDatabase(String username) {
        Injector injector = Guice.createInjector(new CommonclientModule());
        EncryptionService service = injector.getInstance(EncryptionService.class);
        return new Contact(username, service.getPublicKey());

    }

    public String getUsername() {
        return username;
    }

    public Node[] getConnectedNodes() throws NoConnectedNodesException {
        if (connectedNodes == null || connectedNodes.length <= 0) {
            throw new NoConnectedNodesException("The connected Nodes from the contactStore are not set");
        }
        return connectedNodes;

    }

    public void setConnectedNodes(Node[] connectedNodes) {
        this.connectedNodes = connectedNodes;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    @Override
    public String toString() {
        return getUsername();
    }

    @Override
    public boolean equals(Object anotherObject) {
        if (anotherObject == null || !(anotherObject instanceof Contact)) {
            return false;
        }
        Contact contact = (Contact) anotherObject;
        return contact.getUsername().equals(getUsername());
    }

    @Override
    public int hashCode() {
        return getUsername().hashCode();
    }
}
