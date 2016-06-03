package nl.han.asd.project.client.commonclient.path.matrix;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nl.han.asd.project.client.commonclient.graph.Edge;
import nl.han.asd.project.client.commonclient.graph.Node;
import nl.han.asd.project.commonservices.internal.utility.Check;

/**
 * Calculate all best paths within
 * a specified range of hops;
 *
 * @version 1.0
 */
public class PathMatrix {
    class Element {
        Element previous;

        String node;
        float cost;

        Element(Element previous, String node, float cost) {
            this.previous = previous;
            this.node = node;
            this.cost = cost;
        }
    }

    /**
     * Returned by the PathMatrix class
     * representing a possible path to take.
     *
     * @version 1.0
     */
    public class PathOption {
        String src;
        String dest;

        Element startingElement;

        PathOption(String src, String dest, Element startingElement) {
            this.src = src;
            this.dest = dest;

            this.startingElement = startingElement;
        }

        /**
         * Get the path represented
         * by this instance.
         *
         * @param vertices vertices contained in the
         *      graph used to construct the path-list
         *
         * @return the constructed path
         *
         * @throws IllegalArgumentException if
         *      vertices is null
         */
        public List<Node> getPath(Map<String, Node> vertices) {
            Check.notNull(vertices, "vertices");

            List<Node> hops = new LinkedList<>();
            hops.add(vertices.get(src));

            Element curr = startingElement;
            while (curr.previous != null) {
                hops.add(vertices.get(curr.node));
                curr = curr.previous;
            }

            hops.add(vertices.get(dest));
            return hops;
        }

        public String getSource() {
            return src;
        }

        public float getCost() {
            return startingElement.cost;
        }
    }

    private String[] nodes;
    private Element[][][] data;

    /**
     * Construct a new PathMatrix instance. Note that this
     * constructor also constructs the matrix.
     *
     * @param vertices vertices contained in the
     *          graph
     * @param depth the maximum depth (hops) to consider
     *          when constructing the matrix
     *
     *  @throws IllegalArgumentException if vertices is null
     *          and/or the value of depth is less than 0
     */
    public PathMatrix(Map<String, Node> vertices, int depth) {
        Check.notNull(vertices, "vertices");

        if (depth < 0) {
            throw new IllegalArgumentException(depth + " < 0");
        }

        if (vertices.isEmpty()) {
            return;
        }

        nodes = vertices.keySet().toArray(new String[0]);
        data = new Element[vertices.size()][vertices.size()][depth];

        for (Entry<String, Node> pair : vertices.entrySet()) {
            for (Edge edge : pair.getValue().getEdges()) {
                set(pair.getValue().getId(), edge.getDestinationNodeId(),
                        edge.getWeight());
            }
        }

        for (int currDepth = 1; currDepth < data[0][0].length; currDepth++) {
            internalCalculate(currDepth);
        }
    }

    private int getIndex(String id) {
        int index = 0;

        while (!nodes[index++].equals(id)) {
            ;
        }

        if (index > nodes.length) {
            throw new NodeNotInGraphException(id);
        }

        return index - 1;
    }

    private void set(String source, String destination, float cost) {
        set(getIndex(source), getIndex(destination), cost);
    }

    private void set(int row, int col, float cost) {
        if (row == col) {
            return;
        }

        data[row][col][0] = new Element(null, nodes[col], cost);
    }

    private void internalCalculate(int current) {
        for (int row = 0; row < data.length; row++) {
            for (int col = 0; col < data[row].length; col++) {
                if (row == col) {
                    continue;
                }

                for (int colRow = 0; colRow < data.length; colRow++) {
                    if (colRow == row || colRow == col) {
                        continue;
                    }

                    if (data[colRow][col][current - 1] == null) {
                        continue;
                    }

                    if (data[row][colRow][0] == null) {
                        continue;
                    }

                    float cost = data[row][colRow][0].cost
                            + data[colRow][col][current - 1].cost;

                    Element element = new Element(
                            data[colRow][col][current - 1], nodes[colRow],
                            cost);

                    if (element.previous.previous != null
                            && element.previous.previous.node
                                    .equals(element.node)) {
                        continue;
                    }

                    if (data[row][col][current] == null
                            || data[row][col][current].cost > cost) {
                        data[row][col][current] = element;
                    }
                }
            }
        }
    }

    /**
     * Get a list of all possible path with the
     * provided number of hops that lead to the
     * specified destination.
     *
     * @param dest the destination of the path
     * @param hops the number of hops
     *
     * @return all found paths
     *
     * @throws IllegalArgumentException if dest is null
     *          and/or the value of hops is less than 0
     */
    public List<PathOption> getOptions(String dest, int hops) {
        Check.notNull(dest, "dest");

        if (hops < 0) {
            throw new IllegalArgumentException(hops + " < 0");
        }

        List<PathOption> options = new ArrayList<>(data.length);

        int destIndex = getIndex(dest);
        for (int row = 0; row < data.length; row++) {
            Element element = data[row][destIndex][hops];
            if (element != null) {
                options.add(new PathOption(nodes[row], dest, element));
            }
        }

        return options;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int iteration = 0; iteration < data[0][0].length; iteration++) {
            sb.append("- iteration ").append(iteration).append("\n");

            for (int col = 0; col < data[0].length; col++) {
                sb.append("\t").append(nodes[col]);
            }
            sb.append("\n");

            for (int row = 0; row < data.length; row++) {
                sb.append(nodes[row]);

                for (int col = 0; col < data[row].length; col++) {
                    sb.append("\t");

                    if (col == row) {
                        sb.append("-");
                        continue;
                    }

                    Element currentElement = data[row][col][iteration];
                    if (currentElement == null) {
                        sb.append("X");
                    } else {
                        sb.append(currentElement.node);
                        sb.append(" (").append(currentElement.cost).append(")");
                    }
                }

                sb.append("\n");
            }

            sb.append("\n");
        }

        return sb.toString();
    }

}
