package nl.han.asd.project.client.commonclient.path.algorithm;

import java.util.List;

import nl.han.asd.project.client.commonclient.graph.Node;

/**
 * Created by Jevgeni on 19-5-2016.
 */
public interface IPathFind {
    List<Node> findPath(Node startNode, Node endNode);
}
