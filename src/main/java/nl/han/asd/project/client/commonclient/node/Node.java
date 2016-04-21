package nl.han.asd.project.client.commonclient.node;

/**
 * Created by Julius on 15/04/16.
 */
public class Node {

    private String IP;
    private int port;
    private String publicKey;

    @Override
    public boolean equals(Object anotherObj){
        if (!(anotherObj instanceof Node)) return false;
        return true;
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
