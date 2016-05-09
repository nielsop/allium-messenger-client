package nl.han.asd.project.client.commonclient.graph;


import java.util.LinkedList;
import java.util.List;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 20-4-2016
 */
public class Node {
    private String ID;
    private String IP;
    private int port;
    private byte[] publicKey;
    List<Edge> adj;


    public Node(String ID ,String IP, int port, byte[] publicKey){
        this.ID = ID;
        this.IP = IP;
        this.port = port;
        this.publicKey = publicKey;
        adj = new LinkedList<Edge>();
    }

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

    public byte[] getPublicKey() {
        return publicKey;
    }

    public String getID() {
        return ID;
    }
}
