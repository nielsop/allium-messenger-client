package nl.han.asd.project.client.commonclient.path;

import nl.han.asd.project.client.commonclient.node.Node;
import nl.han.asd.project.client.commonclient.store.Contact;

import java.util.ArrayList;

public interface IGetPath {
    ArrayList<Node> getPath(int minHops, Contact contactOntvanger);
}