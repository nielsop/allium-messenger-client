package nl.han.asd.project.client.commonclient.path.algorithm;

import nl.han.asd.project.client.commonclient.graph.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Jevgeni on 19-5-2016.
 */
public interface IPathFind {
    List<Node> findPath(Node startNode, Node endNode);
}
