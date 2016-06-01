package nl.han.asd.project.client.commonclient.path.algorithm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Matrix {
    private final int size;
    private short[] data;

    public Matrix(int size) {
        this.size = size;

        data = new short[(size * size - size) / 2];
    }

    public void set(int row, int col, short value) {
        if (row == col || value == 0) {
            return;
        }

        data[index(row, col)] = value;
    }

    private int index(int row, int col) {
        if (row < col) {
            return row * (size - 1) - row * (row - 1) / 2 + col - row - 1;
        }

        return col * (size - 1) - col * (col - 1) / 2 + row - col - 1;
    }

    public short get(int row, int col) {
        return data[index(row, col)];
    }

    public List<Integer> getPossibleStartingPoints(int endIndex) {
        List<Integer> possibleStartingPoints = new ArrayList<>();

        for (int currentIndex = 0; currentIndex < size; currentIndex++) {
            if (data[index(endIndex, currentIndex)] != 0)
                possibleStartingPoints.add(currentIndex);
        }

        return possibleStartingPoints;
    }

    public void calculate(int steps) {
        short[] alternate = new short[data.length];

        for (int step = 0; step < steps; step++) {
            if (step % 2 == 0) {
                internalCalculate(data, alternate);
            } else {
                internalCalculate(alternate, data);
            }
        }

        if (steps % 2 != 0) {
            data = alternate;
        }
    }

    private void internalCalculate(short[] previous, short[] current) {
        for (int rowNr = 0; rowNr < size; rowNr++) {
            for (int colNr = rowNr + 1; colNr < size; colNr++) {
                int actualIndex = index(rowNr, colNr);
                current[actualIndex] = previous[actualIndex];

                for (int rowColNr = 0; rowColNr < size; rowColNr++) {
                    if (rowColNr == rowNr || rowColNr == colNr) {
                        continue;
                    }

                    if (previous[index(rowNr, rowColNr)] == 0) {
                        continue;
                    }

                    if (previous[index(colNr, rowColNr)] == 0) {
                        continue;
                    }

                    short cost = (short) (previous[index(rowNr, rowColNr)] + previous[index(colNr, rowColNr)]);
                    if (cost < previous[actualIndex] || previous[actualIndex] == 0) {
                        current[actualIndex] = cost;
                    }
                }
            }
        }
    }

    @Override public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int colNr = 0; colNr < size; colNr++) {
            stringBuilder.append("\t" + colNr);
        }

        stringBuilder.append("\n");

        for (int rowNr = 0; rowNr < size; rowNr++) {
            stringBuilder.append(rowNr + "\t");

            for (int colNr = 0; colNr < size; colNr++) {
                if (colNr <= rowNr) {
                    stringBuilder.append("-\t");
                } else {
                    if (get(rowNr, colNr) == Short.MAX_VALUE) {
                        stringBuilder.append("X\t");
                    } else {
                        stringBuilder.append(get(rowNr, colNr) + "\t");
                    }
                }
            }

            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }
}
