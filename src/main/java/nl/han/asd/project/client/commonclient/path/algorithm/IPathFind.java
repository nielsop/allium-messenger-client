package nl.han.asd.project.client.commonclient.path.algorithm;

import nl.han.asd.project.client.commonclient.graph.Node;

import java.util.ArrayList;

/**
 * Created by Jevgeni on 19-5-2016.
 */
interface IPathFind {
    ArrayList<Node> findPath(Node startNode, Node endNode);
}
