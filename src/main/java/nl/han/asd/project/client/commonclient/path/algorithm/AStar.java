package nl.han.asd.project.client.commonclient.path.algorithm;

import nl.han.asd.project.client.commonclient.graph.Edge;
import nl.han.asd.project.client.commonclient.graph.Graph;
import nl.han.asd.project.client.commonclient.graph.Node;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by Jevgeni on 19-5-2016.
 */
public class AStar {

    private final Graph graph;

    public AStar(Graph graph) {
        this.graph = graph;
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

    /**
     * Implements the A-star algorithm and returns the path from source to destination
     *
     * @param source        the source nodeid
     * @param destination   the destination nodeid
     * @return the path from source to destination
     */
    public List<String> aStar(String source, String destination) {
        /**
         * http://stackoverflow.com/questions/20344041/why-does-priority-queue-has-default-initial-capacity-of-11
         */
        final Queue<Node> openQueue = new PriorityQueue<Node>(11, new NodeComparator());

        Node sourceNode = graph.getNodeVertex(source);
        sourceNode.setDistanceToSource(0);
        sourceNode.calcF(destination);
        openQueue.add(sourceNode);

        final Map<String, String> path = new HashMap<String, String>();
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

                    path.put(neighbor.getId(), node.getId());
                    if (!openQueue.contains(neighbor)) {
                        openQueue.add(neighbor);
                    }
                }
            }
        }

        return null;
    }

    private List<String> path(Map<String, String> path, String destination) {
        assert path != null;
        assert destination != null;

        final List<String> pathList = new ArrayList<String>();
        pathList.add(destination);
        while (path.containsKey(destination)) {
            destination = path.get(destination);
            pathList.add(destination);
        }
        Collections.reverse(pathList);
        return pathList;
    }
}

