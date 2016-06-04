package nl.han.asd.project.client.commonclient.path;

import java.util.List;

import nl.han.asd.project.client.commonclient.graph.Node;

/**
 * Used for pathfinding classes.
 */
public interface IPathFind {
    /**
     * Finds a path between a start node and end node.
     *
     * @param startNode A node the path has to start from.
     * @param endNode A node the path has to end at.
     * @return A path if succeed, null if no path was found.
     */
    List<Node> findPath(Node startNode, Node endNode);
}
