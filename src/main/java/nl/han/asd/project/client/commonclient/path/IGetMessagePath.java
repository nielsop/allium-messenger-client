package nl.han.asd.project.client.commonclient.path;

import java.util.List;

import nl.han.asd.project.client.commonclient.graph.Node;
import nl.han.asd.project.client.commonclient.store.Contact;

public interface IGetMessagePath {
    List<Node> getPath(int minHops, Contact contactReciever);
}
