package nl.han.asd.project.client.commonclient.node;

/**
 * Created by Marius on 25-04-16.
 */
public class Node {
    private String ip;
    private int port;
    private byte[] publicKey;

    @Override
    public boolean equals(Object anotherObj) {
        return anotherObj instanceof Node;
    }

    @Override
    public int hashCode() {
        return (ip + ":" + port).hashCode();
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }
}
