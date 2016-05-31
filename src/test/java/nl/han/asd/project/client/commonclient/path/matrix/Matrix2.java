package nl.han.asd.project.client.commonclient.path.matrix;

import java.util.LinkedList;
import java.util.List;

public class Matrix2 {
    public class Element {
        Element previous;
        short weight;

        int id;

        Element(Element previous, int id, short weight) {
            this.previous = previous;
            this.id = id;
            this.weight = weight;
        }
    }

    public final int size;

    /*
     * history
     * current
     */
    public Element[][] matrix;

    public Matrix2(int size) {
        this.size = size;

        matrix = new Element[20][(size * size - size) / 2];
    }

    public void set(int row, int col, short value) {
        if (row == col || value == 0) {
            return;
        }

        matrix[0][index(row, col)] = new Element(null, row, value);
    }

    public int index(int row, int col) {
        if (row < col) {
            return row * (size - 1) - row * (row - 1) / 2 + col - row - 1;
        }

        return col * (size - 1) - col * (col - 1) / 2 + row - col - 1;
    }

    public short get(int row, int col) {
        if (matrix[matrix.length - 1][index(row, col)] == null) {
            return Short.MAX_VALUE;
        }

        return matrix[matrix.length - 1][index(row, col)].weight;
    }

    public void calculate(int steps) {
        for (int step = 0; step < matrix.length - 1; step++) {
            internalCalculate(step);
        }
    }

    private void internalCalculate(int previous) {
        for (int rowNr = 0; rowNr < size; rowNr++) {
            for (int colNr = rowNr + 1; colNr < size; colNr++) {
                int actualIndex = index(rowNr, colNr);

                for (int rowColNr = 0; rowColNr < size; rowColNr++) {
                    if (rowNr == rowColNr || colNr == rowColNr) {
                        continue;
                    }

                    if (matrix[previous][index(rowNr, rowColNr)] == null) {
                        continue;
                    }

                    if (rowNr == 3) {
                        System.out.println(rowNr);
                    }

                    if (matrix[0][index(colNr, rowColNr)] == null) {
                        continue;
                    }

                    short cost = (short) (matrix[previous][index(rowNr, rowColNr)].weight
                            + matrix[0][index(colNr, rowColNr)].weight);

                    if (matrix[previous + 1][actualIndex] == null || cost < matrix[previous + 1][actualIndex].weight) {
                        Element previousElement = matrix[previous][index(rowNr, rowColNr)];
                        matrix[previous + 1][actualIndex] = new Element(previousElement, rowColNr, cost);
                    }
                }
            }
        }
    }

    public List<Integer> getPath(int source, int destination, int pathLength) {
        List<Integer> pathNodes = new LinkedList<>();

        if (matrix[pathLength][index(source, destination)] == null) {
            System.err.println("is null");

            return null;
        }

        Element current = matrix[pathLength][index(source, destination)];

        System.err.println(current);

        while (current != null && current.previous != null) {
            pathNodes.add(current.id);
            current = current.previous;
        }

        return pathNodes;
    }

}
