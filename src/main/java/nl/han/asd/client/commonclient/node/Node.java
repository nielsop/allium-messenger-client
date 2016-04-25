package nl.han.asd.client.commonclient.node;

/**
 * Created by Marius on 25-04-16.
 */
public class Node {
    private String IP;
    private int port;
    private String publicKey;

    @Override
    public boolean equals(Object anotherObj) {
        return anotherObj instanceof Node;
    }

    public String getIP() {
        return IP;
    }

    public int getPort() {
        return port;
    }

    public String getPublicKey() {
        return publicKey;
    }
}
