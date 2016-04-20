package nl.han.asd.client.commonclient.path;

import nl.han.asd.client.commonclient.node.Node;
import nl.han.asd.client.commonclient.store.Contact;

import java.util.ArrayList;

public interface IGetPath {
    ArrayList<Node> getPath(int minHops, Contact contactOntvanger);
}
