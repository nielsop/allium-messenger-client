package nl.han.asd.project.client.commonclient.path.algorithm;

import nl.han.asd.project.client.commonclient.graph.Edge;
import nl.han.asd.project.client.commonclient.graph.Graph;
import nl.han.asd.project.client.commonclient.graph.Node;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by Jevgeni on 19-5-2016.
 */
public class AStar implements IPathFind {

    private final Map<String, Node> vertices;

    public AStar(Map<String, Node> vertices) {
        this.vertices = vertices;
    }

    // extend comparator.
    public class NodeComparator implements Comparator<Node> {
        public int compare(Node nodeFirst, Node nodeSecond) {
            if (nodeFirst.getF() > nodeSecond.getF())
                return 1;
            if (nodeSecond.getF() > nodeFirst.getF())
                return -1;
            return 0;
        }
    }

    @Override
    public List<Node> findPath(Node sourceNode, Node destination) {
        final Queue<Node> openQueue = new PriorityQueue<Node>(11, new NodeComparator());
        /*
         * h(n) = cost of the cheapest path from n to the goal
         * g(n) = is the cost of the path from the start node to n
         *
         *          if h(n) is 0 then only g(n) is used for pathfinding,
         *          which makes this algorithm function like Dijkstra.
         *
         *          if h(n) is greater than the cost of moving from n to goal,
         *          then A* is not guaranteed to find a shortest path.
         *
         *          Precomputed exact heuristic:
         *              - Pre-compute the length of the shortest path between every
         *                  pair of points.
         *
         *          Linear exact heuristic:
         *              - If there are no obstacles and no slow terrain, then the
         *                  shortest path from the starting point to the goal should be a straight line
         *
         *          Manhattan distance:
         *              - Uses the minimal costs and multiplies it by the amount of n it has to pass
         *                  in order to reach the end point.
         *              - The path with the smallest outcome will be the shortest path.
         *
         *          Diagonal distance:
         *              - If the map allows diagonal movement.
         *              - If the Manhattan function would require 8n (4 east, 4 north)
         *                  you could simply move 4n, going into and diagonal, in this case northeast,
         *                  direction.
         *
         *          Euclidean distance:
         *              - If our units can move at any angle (instead of grid directions),
         *                  use a straight line distance.
         *
         *              - Euclidean distance, squared:
         *
         *
         */

        sourceNode.setDistanceToSource(0);
        // h(n) cannot be 0, if at one point h(n) does return 0; A* will turn into Dijkstra's algorithm
        sourceNode.calcF(destination); // h(n), no calcF, will always return double.MaxValue
        openQueue.add(sourceNode);

        final Map<String, Node> path = new HashMap<>();
        final Set<Node> closedList = new HashSet<Node>();

        while (!openQueue.isEmpty()) {
            final Node currentNode = openQueue.poll();

            if (currentNode.getId().equals(destination.getId())) {
                return path(path, destination);
            }

            closedList.add(currentNode);

            for (Edge neighborEntry : currentNode.getEdges()) {
                Node neighborNode = vertices.values().stream()
                        .filter(node -> node.getId() == neighborEntry.getDestinationId()).findFirst().orElse(null);
                if (closedList.contains(neighborNode))
                    continue;

                double tentativeG =
                        currentNode.getDistanceToSource() + neighborEntry.getDistance();

                if (tentativeG < neighborNode.getDistanceToSource()) {
                    neighborNode.setDistanceToSource(tentativeG);
                    neighborNode.calcF(destination); // heuristic

                    path.put(neighborNode.getId(), currentNode);
                    if (!openQueue.contains(neighborNode)) {
                        openQueue.add(neighborNode);
                    }
                }
            }
        }

        return null;
    }

    private List<Node> path(Map<String, Node> path, Node destination) {
        assert path != null;
        assert destination != null;

        final List<Node> pathList = new ArrayList<>();
        pathList.add(destination);

        while (path.containsKey(destination)) {
            destination = path.get(destination);
            pathList.add(destination);
        }
        Collections.reverse(pathList);
        return pathList;
    }
}

