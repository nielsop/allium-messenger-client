package nl.han.asd.project.client.commonclient.path.matrix;

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
    protected void calculate(int steps) {
        // check for steps > max_steps
        tempMatrix = new short[size][size][MAX_STEPS];

        for (short row = 0; row < size; row++) {
            for (short column = 0; column < size; column++) {
                if (matrixEverHadAnyValue(row, column)) {

                    int currentValue = Integer.MAX_VALUE;
                    int[] currentPath = null;

                    short[][] rowOfColumn = matrix[column]; // target
                    short[] historyValues = matrix[row][column]; // current

                    for(int stepCounter = steps - 1; stepCounter >= 0; stepCounter--) { // continue from the latest found path
                        short historyValue = historyValues[stepCounter];
                        if (historyValue <= 0)
                           continue;

                        for (int columnRowOfColumn = 0; columnRowOfColumn < rowOfColumn.length; columnRowOfColumn++) { // get the target row

                            if (tempMatrixNotSetBefore(row, columnRowOfColumn) && columnRowOfColumn != row) { // check if we haven't set it already

                                for (int indexColumnRowOfColumn = 0; indexColumnRowOfColumn
                                        < rowOfColumn[columnRowOfColumn].length; indexColumnRowOfColumn++) { // loop through all (previous) values of the target row

                                    short selectedRowValue = rowOfColumn[columnRowOfColumn][indexColumnRowOfColumn];
                                    if (selectedRowValue != 0 && (historyValue + selectedRowValue)
                                            < currentValue &&
                                            (stepCounter + indexColumnRowOfColumn + 1)== steps) {

                                        currentValue = historyValue + selectedRowValue;
                                        currentPath = new int[] { columnRowOfColumn, row };
                                    }
                                }
                            }
                        }
                    }

                    // set new paths
                    if (currentPath != null) {
                        tempMatrix[currentPath[0]][currentPath[1]][0] = (short) currentValue;
                        tempMatrix[currentPath[1]][currentPath[0]][0] = (short) currentValue;
                    }

                    // save old values for traceability
                    for(int i = 1; i < MAX_STEPS; i++)
                        tempMatrix[row][column][i] = matrix[row][column][i - 1]; // do not set 0 value;
                }
            }
        }

        matrix = tempMatrix;
        this.steps = steps;
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
