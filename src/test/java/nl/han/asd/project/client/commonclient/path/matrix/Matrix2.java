package nl.han.asd.project.client.commonclient.path.matrix;

import java.util.LinkedList;
import java.util.List;

public class Matrix2 {
    private final int size;
    private short[] data;
    private int[] path;

    public Matrix2(int size) {
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

    public void calculate(int steps) {
        short[] alternate = new short[data.length];

        path = new int[data.length];

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
                        path[actualIndex] = rowColNr;
                    }
                }
            }
        }
    }

    //    private void internalCalculate(char b) {
    //        for (int rowNr = 0; rowNr < size; rowNr++) {
    //            for (int colNr = rowNr + 1; colNr < size; colNr++) {
    //                if (data[index(rowNr, colNr)][b] == 0) {
    //                    continue;
    //                }
    //
    //                data[index(rowNr, colNr)][b ^ 1] = data[index(rowNr, colNr)][b];
    //
    //                for (int rowColNr = 1; rowColNr < size; rowColNr++) {
    //                    if (data[index(rowNr, rowColNr)][b] == Short.MAX_VALUE || rowColNr == colNr) {
    //                        continue;
    //                    }
    //
    //                    if (data[index(colNr, rowColNr)][b] == Short.MAX_VALUE) {
    //                        continue;
    //                    }
    //
    //                    short cost = (short) (data[index(rowNr, rowColNr)][b] + data[index(colNr, rowColNr)][b]);
    //                    if (cost < data[index(rowNr, colNr)][b]) {
    //                        data[index(rowNr, colNr)][b ^ 1] = cost;
    //                    }
    //                }
    //            }
    //        }
    //    }

    @Override
    public String toString() {
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

    public String pathToString() {
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
                        stringBuilder.append(path[index(rowNr, colNr)] + "\t");
                    }
                }
            }

            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    public List<Integer> getPath(int source, int destination) {
        System.out.println(pathToString());

        List<Integer> pathNodes = new LinkedList<>();

        while (source != destination && path[index(source, destination)] != 0) {
            pathNodes.add(source = path[index(source, destination)]);
            System.out.println(source);
        }

        for (int i : pathNodes) {
            System.out.println(i + " <- ");
        }

        return pathNodes;
    }

}
