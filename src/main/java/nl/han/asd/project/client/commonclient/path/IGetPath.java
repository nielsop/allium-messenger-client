package nl.han.asd.project.client.commonclient.path;

import nl.han.asd.project.client.commonclient.graph.Node;
import nl.han.asd.project.client.commonclient.store.Contact;

import java.util.ArrayList;
import java.util.List;

@FunctionalInterface
public interface IGetPath {
    List<Node> getPath(int minHops, Contact contactReciever);
}
