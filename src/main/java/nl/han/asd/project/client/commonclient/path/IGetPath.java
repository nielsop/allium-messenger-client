package nl.han.asd.project.client.commonclient.path;

import nl.han.asd.project.client.commonclient.node.Node;
import nl.han.asd.project.client.commonclient.store.Contact;

public interface IGetPath {
    Node[] getPath(int minHops, Contact contactOntvanger);
}
