package nl.han.asd.project.client.commonclient.path.matrix;

import javafx.util.Pair;

/**
 * Created by Jevgeni on 25-5-2016.
 */
class Matrix {
    private final static int MAX_STEPS = 10;

    private final short size;
    private int steps;

    /**
     * 1st [] = Pointer to rows
     * 2nd [] = Pointer to columns
     * 3rd [] =
     *          0 = current value
     *          1..n = old values ordered by steps
     */
    private short[][][] matrix;
    private short[][][] tempMatrix;

    /**
     * Initializes the matrix.
     *
     * @param size Size of the matrix, both rows and columns will get this size.
     */
    public Matrix(int size) {
        this.size = (short) size;

        steps = 1;
        matrix = new short[this.size][this.size][MAX_STEPS];
        prepareMatrix();
    }

    private void prepareMatrix() {
        for(int i = 0; i < size; i++)
            for(int j = 0; j < size; j++)
               if (i != j) matrix[i][j][0] = Short.MAX_VALUE;
    }

    /**
     * Add a value to the matrix.
     *
     * @param row   Row number to insert <code>value</code> on.
     * @param col   Column number to insert <code>value</code> on.
     * @param value Value to insert.
     */
    public final void set(int row, int col, int value) {
        matrix[row][col][0] = (short) value;
    }

    /**
     * Performs an calculation on the current matrix.
     *
     * @param steps Amount of steps to take.
     */
    protected void calculate(int steps, int indexOfEndPoint) {
        short[][] previous = new short[size][size];

        for(int i = 0; i < size; i++)
            for(int j = 0; j < size; j++) {
                previous[i][j] = matrix[i][j][0];
            }

        short[][] ret = internalCalculate(previous);

        for(int i = 0; i < size; i++)
            for(int j = 0; j < size; j++) {
                matrix[i][j][1] = ret[i][j];
            }

        ret = internalCalculate(ret);

        for(int i = 0; i < size; i++)
            for(int j = 0; j < size; j++) {
                matrix[i][j][2] = ret[i][j];
            }

        ret = internalCalculate(ret);

        for(int i = 0; i < size; i++)
            for(int j = 0; j < size; j++) {
                matrix[i][j][3] = ret[i][j];
            }


    }

    private short[][] internalCalculate(short[][] previous)
    {
        short[][] current = new short[size][size];

        for(int i = 0; i < size; i++)
            for(int j = 0; j < size; j++)
                if (i != j) current[i][j] = previous[i][j];

        for (short rowNumber = 0; rowNumber < size; rowNumber++) {
            for (short columnNumber = 0; columnNumber < size; columnNumber++) {
                if (previous[rowNumber][columnNumber] > 0 && rowNumber != columnNumber)
                {
                    short[] row = previous[rowNumber];

                    for(short rowColumnNumber = 0; rowColumnNumber < size; rowColumnNumber++)
                    {
                        if (rowColumnNumber == columnNumber || row[rowColumnNumber] == Short.MAX_VALUE)
                            continue;

                        for(short a = 0; a < size; a++) {
                            short valueOfA = previous[rowColumnNumber][a];
                            if (a == rowColumnNumber || a == rowNumber
                                    || valueOfA == Short.MAX_VALUE)
                                continue;

                            if (a != columnNumber)
                                continue;

                            short outcome = (short) (valueOfA + previous[rowNumber][rowColumnNumber]);
                            if (outcome < previous[rowNumber][columnNumber])
                                current[rowNumber][columnNumber] = outcome;
                        }
                    }

                }
            }
        }

        return current;
    }

    /**
     * Checks if we have already set this row:column combination.
     *
     * @param row Row to search for
     * @param column Column to search for
     * @return True if it isn't set (yet); false if it was.
     */
    private boolean tempMatrixNotSetBefore(int row, int column) {
        return tempMatrix[row][column][0] /* order doesn't matter */ == 0;
    }

    /**
     * Checks the third dimension for any value.
     *
     * @param row Row to search in
     * @param column Column to search in
     * @return True if row:column combination ever had a value.
     */
    private boolean matrixEverHadAnyValue(int row, int column) {
        for (short value : matrix[row][column]) {
            if (value > 0)
                return true;
        }

        return false;
    }

    /**
     * Gets the current matrix.
     *
     * @return The current matrix.
     */
    public final short[][][] getCurrentMatrix() {
        return matrix;
    }
}
