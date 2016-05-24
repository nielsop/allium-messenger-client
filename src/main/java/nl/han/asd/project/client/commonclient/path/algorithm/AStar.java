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

    private final Graph graph;
    private final Map<String, Node> vertices;

    public AStar(Map<String, Node> vertices) {
        //this.graph = graph;
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

        sourceNode.setDistanceToSource(0);
        sourceNode.calcF(destination);
        openQueue.add(sourceNode);

        final Map<String, Node> path = new HashMap<>();
        final Set<Node> closedList = new HashSet<Node>();

        while (!openQueue.isEmpty()) {
            final Node node = openQueue.poll();

            if (node.getId().equals(destination)) {
                return path(path, destination);
            }

            closedList.add(node);

            for (Edge neighborEntry : node.getEdges()) {
                Node neighbor = graph.getNodeVertex(neighborEntry.getDestinationId());

                if (closedList.contains(neighbor))
                    continue;

                double tentativeG = node.getDistanceToSource() + neighborEntry.getDistance();

                if (tentativeG < neighbor.getDistanceToSource()) {
                    neighbor.setDistanceToSource(tentativeG);
                    neighbor.calcF(destination);

                    path.put(neighbor.getId(), node);
                    if (!openQueue.contains(neighbor)) {
                        openQueue.add(neighbor);
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

