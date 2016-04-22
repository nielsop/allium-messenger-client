package nl.han.asd.project.client.commonclient.node;


/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 20-4-2016
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
