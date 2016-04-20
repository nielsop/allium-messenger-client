package nl.han.asd.project.client.commonclient.node;


/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 20-4-2016
 */
public class Node {
    private String IP;
    private int port;

    @Override
    public boolean equals(Object anotherObj) {
        if (!(anotherObj instanceof NodeGateway)) return false;
        return true;
    }

    public String getIP() {
        return IP;
    }

    public int getPort() {
        return port;
    }

}
