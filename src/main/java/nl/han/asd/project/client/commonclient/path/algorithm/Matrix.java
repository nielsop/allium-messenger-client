package nl.han.asd.project.client.commonclient.path.algorithm;

/**
 * Provides functionality for
 */
public class Matrix {
    private final int size;
    private short[] data;

    /**
     * Initializes the class.
     *
     * @param size THe size of the matrix, must be bigger then 2 (otherwise we can't find a path).
     */
    public Matrix(int size) {
        if (size < 2) {
            throw new IllegalArgumentException("Size cannot be lower than 2.");
        }
        this.size = size;

        data = new short[(size * size - size) / 2];
    }

    /**
     * Set a value inside the matrix.
     *
     * @param row Index of row to write to.
     * @param col Index of column to write to.
     * @param value Value to write to row + column index inside the matrix.
     */
    void set(int row, int col, short value) {
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

    /**
     * Gets a value from the matrix.
     *
     * @param row Index of the row to read from.
     * @param col Index of the column to read from.
     * @return The value at the row + column index inside the matrix.
     */
    short get(int row, int col) {
        return data[index(row, col)];
    }

    /**
     * Calculates the new matrix.
     *
     * @param steps Amount of times the matrix should calculate.
     */
    void calculate(int steps) {
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
                    calculateNewStep(previous, current, rowNr, colNr,
                            actualIndex, rowColNr);
                }
            }
        }
    }

    private void calculateNewStep(short[] previous, short[] current, int rowNr,
            int colNr, int actualIndex, int rowColNr) {
        if (checkCollisionsAndPreviousValues(previous, rowNr, colNr,
                rowColNr))
            return;

        short cost = (short) (previous[index(rowNr, rowColNr)] + previous[index(colNr, rowColNr)]);
        if (cost < previous[actualIndex] || previous[actualIndex] == 0) {
            current[actualIndex] = cost;
        }
    }

    private boolean checkCollisionsAndPreviousValues(short[] previous,
            int rowNr, int colNr, int rowColNr) {
        if (rowColNr == rowNr || rowColNr == colNr) {
            return true;
        }

        if (previous[index(rowNr, rowColNr)] == 0) {
            return true;
        }

        if (previous[index(colNr, rowColNr)] == 0) {
            return true;
        }
        return false;
    }
}
