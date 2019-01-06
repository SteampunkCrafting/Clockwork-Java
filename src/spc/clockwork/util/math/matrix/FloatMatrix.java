package spc.clockwork.util.math.matrix;

import spc.clockwork.util.math.vector.FloatVector;

/**
 * Matrix of a floating point number in N*M dimensions
 *
 * @author wize
 * @version 0 (2018.03.22)
 */
public class FloatMatrix {

    /* ATTRIBUTES
    /*--------------------*/

    /**
     * A messages of an Exception that occurs each time there is an invalid operation between matrices
     */
    private final static String ERROR_INVALID_MATRIX_ADDITION =
            "Error: There was a matrix addition of incompatible matrices";
    private final static String ERROR_INVALID_MATRIX_MULTIPLICATION =
            "Error: There was a matrix multiplication of incompatible matrices";
    private final static String ERROR_INVALID_MATRIX_VECTOR_MULTIPLICATION =
            "Error: There was a matrix by vector multiplication, which cannot be multiplied";
    private final static String ERROR_IMPROPER_MATRIX =
            "Error: Improper matrix (does not form a numeric rectangle, missing some elements)";
    private static final String ERROR_NON_SQUARE_MATRIX =
            "Error: The matrix given is not a square matrix and thus the operation cannot be applied";
    private static final String ERROR_DET_IS_ZERO_NO_INVERSE =
            "Error: The matrix given has a determinant equal to zero and thus has no inverse";


    /**
     * A 2d array of the Matrix elements of size N*M
     */
    protected final float[][] elements;

    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/

    /**
     * Throws an exception, if the matrix is improper (i.e. does not form a numeric rectangle)
     */
    private void checkIfProperMatrix() {
        if (isImproperMatrix())
            throw new IndexOutOfBoundsException("\n" + ERROR_IMPROPER_MATRIX);
    }


    /**
     * Throws an exception, if we cannot add matrices
     * @param matrix another matrix
     */
    private void checkIfAdditionPossible(FloatMatrix matrix) {
        if (isAdditionImpossible(matrix))
            throw new IndexOutOfBoundsException("\n" + ERROR_INVALID_MATRIX_ADDITION);
    }


    /**
     * Throws an exception, if we cannot multiply matrices
     * @param matrix another matrix
     */
    private void checkIfMultiplicationPossible(FloatMatrix matrix) {
        if (isMultiplicationImpossible(matrix))
            throw new IndexOutOfBoundsException("\n" + ERROR_INVALID_MATRIX_MULTIPLICATION);
    }


    /**
     * Throws an exception, if we cannot multiply matrices
     * @param vector another vector
     */
    private void checkIfMultiplicationPossible(FloatVector vector) {
        if (isMultiplicationImpossible(vector))
            throw new IndexOutOfBoundsException("\n" + ERROR_INVALID_MATRIX_VECTOR_MULTIPLICATION);
    }


    /**
     * Checks whether this is a proper matrix (i.e. it has width*depth elements)
     * @return Return true, if this is proper matrix and false otherwise
     */
    private boolean isImproperMatrix() {
        for (int i = 0; i < this.depth(); i++) {
            if (this.elements[i].length != this.width())
                return true;
        }
        return false;
    }


    /**
     * Checks whether two matrices are compatible for addition (i.e. matrices have the same sizes)
     * @param matrix another matrix
     * @return true, if the addition is possible; else return false
     */
    private boolean isAdditionImpossible(FloatMatrix matrix) {
        return this.width() != matrix.width() || this.depth() != matrix.depth();
    }

    /**
     * Checks whether this matrix is compatible for multiplication on the argument matrix
     * @param matrix another matrix
     * @return true, if the multiplication by argument is possible; else return false
     */
    private boolean isMultiplicationImpossible(FloatMatrix matrix) {
        return matrix.depth() != this.width();
    }

    /**
     * Checks whether this matrix is compatible for multiplication on the argument matrix
     * @param vector another vector
     * @return true, if the multiplication by argument is possible; else return false
     */
    private boolean isMultiplicationImpossible(FloatVector vector) {
        return this.width() != vector.size();
    }

    /**
     * Turns row of a matrix into a vector
     * @param row the row number from 0 (top) to this.depth()-1 (bottom)
     * @return returns new vector that that has an array, which is row of a matrix
     */
    private FloatVector turnRowIntoVector(int row) {
        return new FloatVector(this.elements[row]);
    }

    /**
     * Turns a column of a matrix into a vector
     * @param column the column number from 0 (left) to this.width()-1 (right)
     * @return returns new vector that has an array, which was constructed from matrix column
     */
    private FloatVector turnColumnIntoVector(int column) {
        float[] temporaryArray = new float[this.depth()];
        for (int i = 0; i < this.depth(); i++) {
            temporaryArray[i] = this.getElement(i, column);
        }
        return new FloatVector(temporaryArray);
    }

    /**
     * Gets the 'lower' matrix -- a matrix with a particular combination of row and column deleted from it
     * @param rowToRemove the row to be deleted
     * @param columnToRemove the column to be deleted
     * @return the new matrix, which is a lower matrix of this
     */
    private FloatMatrix getLowerMatrix(final int rowToRemove, final int columnToRemove) {
        float[][] temporaryArray = new float[this.depth()-1][this.width()-1];

        int lowerMatrixRow = 0;
        for(int row = 0; row < this.depth(); row++) {
            if(row == rowToRemove) continue;
            int lowerMatrixColumn = 0;
            for(int column = 0; column < this.width(); column++) {
                if(column == columnToRemove) continue;
                temporaryArray[lowerMatrixRow][lowerMatrixColumn] = this.getElement(row, column);
                lowerMatrixColumn++;
            }
            lowerMatrixRow++;
        }
        return new FloatMatrix(temporaryArray);
    }

    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * Element accessor
     * @param row    the row number from 0 (top) to this.depth()-1 (bottom)
     * @param column the column number from 0 (left) to this.width()-1 (right)
     * @return the value of the element of the matrix
     */
    public float getElement(int row, int column) {
        return this.elements[row][column];
    }

    /**
     * Gets width of a matrix
     * @return width of a matrix
     */
    public int width() {
        return this.elements[0].length;
    }

    /**
     * Gets depth (or height) of a matrix
     * @return depth of a matrix
     */
    public int depth() {
        return this.elements.length;
    }

    /**
     * Returns an addition of matrices
     * @param matrix another matrix
     * @return new matrix, which is an addition of this and argument
     */
    public FloatMatrix add(FloatMatrix matrix) {
        checkIfAdditionPossible(matrix);
        float[][] array = new float[this.depth()][this.width()];
        for (int i = 0; i < this.depth(); i++) {
            for (int j = 0; j < this.width(); j++) {
                array[i][j] = this.getElement(i, j) + matrix.getElement(i, j);
            }
        }
        return new FloatMatrix(array);
    }

    /**
     * General Matrix multiplication
     * @param matrix another matrix
     * @return new {@link FloatMatrix}, which is a product of this by the argument
     */
    public FloatMatrix mul(FloatMatrix matrix) {
        checkIfMultiplicationPossible(matrix);
        float[][] array = new float[this.depth()][matrix.width()];
        for (int i = 0; i < this.depth(); i++) {
            for (int j = 0; j < matrix.width(); j++) {
                array[i][j] = this.turnRowIntoVector(i).dot(matrix.turnColumnIntoVector(j));
            }
        }
        return new FloatMatrix(array);
    }

    /**
     * General matrix-vector multiplication
     * @param vector another vector
     * @return new {@link FloatVector}, which is a product of this by the argument
     */
    public FloatVector mul(FloatVector vector) {
        checkIfMultiplicationPossible(vector);
        float[] array = new float[vector.size()];
        for (int i = 0; i < vector.size(); i++) {
            array[i] = this.turnRowIntoVector(i).dot(vector);
        }
        return new FloatVector(array);
    }

    /**
     * General matrix by scalar multiplication
     * @param scalar float number
     * @return new {@link FloatMatrix}, which is a scaled by argument this
     */
    public FloatMatrix scale(float scalar) {
        float[][] array = new float[this.depth()][this.width()];
        for (int i = 0; i < this.depth(); i++) {
            for (int j = 0; j < this.width(); j++) {
                array[i][j] = scalar * this.getElement(i, j);
            }
        }
        return new FloatMatrix(array);
    }

    /**
     * General matrix negation
     * @return new {@link FloatMatrix}, which is a negation of this
     */
    public FloatMatrix negate() {
        return this.scale(-1);
    }


    /**
     * General matrix transposition
     * @return new {@link FloatMatrix}, which is a transpose of this
     */
    public FloatMatrix transpose() {
        float[][] newMatrixArray = new float[this.width()][this.depth()];
        for(int i = 0; i < this.width(); i++)
            for (int j = 0; j < this.depth(); j++)
                newMatrixArray[i][j] = this.getElement(j, i);
        return new FloatMatrix(newMatrixArray);
    }


    /**
     * General Matrix determinant finding
     * @return the determinant of this
     */
    public float det() {
        /* IF A MATRIX IS NOT A SQUARE ONE */
        if(this.width() != this.depth()) throw new RuntimeException(ERROR_NON_SQUARE_MATRIX);
        /* BASE CASES: 2X2 MATRIX AND 1X1 MATRIX */
        if(this.width() < 3)
            return this.width() == 1 ? this.getElement(0,0) :
                    this.getElement(0,0) * this.getElement(1,1)
                    - this.getElement(0,1) * this.getElement(1,0);
        /* OTHER CASES: GOING THROUGH THE TOP ROW, COMPUTING AND ADDING THE CO-FACTORS */
        boolean addStage = true;
        float determinant = 0f;
        for(int i = 0; i < this.width(); i++) {
            determinant += (addStage ? /* new FloatMatrix(lowerMatrixValues) */this.getLowerMatrix(0, i).det()
                    : - /* new FloatMatrix(lowerMatrixValues) */this.getLowerMatrix(0, i).det()) * this.getElement(0, i);
            addStage = !addStage;
        }
        return determinant;
    }


    /**
     * General Matrix inversion
     * @return new matrix, which is an inverse matrix of this
     */
    public FloatMatrix invert() {
        /* STAGE 1: DETERMINANT */
        float determinant;
        if((determinant = this.det()) == 0) throw new RuntimeException(ERROR_DET_IS_ZERO_NO_INVERSE);
        /* STAGE 2: CO-FACTORS, TRANSPOSITION AND SCALING */
        float[][] inverseMatrixValues = new float[this.depth()][this.width()];
        for(int currentRow = 0; currentRow < this.depth(); currentRow++) {
            for(int currentColumn = 0; currentColumn < this.width(); currentColumn++) {
                /* THE TRANSPOSITION AND SCALING IS ALSO PERFORMED HERE TO AVOID THE USELESS MATRIX CONSTRUCTION*/
                inverseMatrixValues[currentColumn][currentRow] =
                        this.getLowerMatrix(currentRow, currentColumn).det()
                                * (((currentRow + currentColumn) % 2) == 0 ? 1 : -1)
                                / determinant;
            }
        }
        return new FloatMatrix(inverseMatrixValues);
    }


    /**
     * Dumps matrix into an row-major array
     * @return a matrix dump as an array of floats in row-major format
     */
    public float[] dumpRowMajor() {
        float[] array = new float[this.depth() * this.width()];
        int i = 0;
        for (int j = 0; j < this.depth(); j++) {
            for (int k = 0; k < this.width(); k++) {
                array[i++] = this.getElement(j, k);
            }
        }
        return array;
    }


    /**
     * Dumps matrix into an column-major array
     * @return a matrix dump as an array of floats in column-major format
     */
    public float[] dumpColumnMajor() {
        float[] array = new float[this.depth() * this.width()];
        int i = 0;
        for (int j = 0; j < this.width(); j++) {
            for (int k = 0; k < this.depth(); k++) {
                array[i++] = this.getElement(k, j);
            }
        }
        return array;
    }

    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * A constructor that creates a 0*0 matrix
     * @deprecated
     */
    public FloatMatrix() {
        elements = new float[0][0];
    }

    /**
     * A constructor that creates a width*depth zero-matrix
     * @param width a width of a matrix
     * @param depth a depth of a matrix
     */
    public FloatMatrix(int depth, int width) {
        elements = new float[width][depth];
    }


    /**
     * A constructor that creates a Matrix out of the 2d array
     * @param elements an array of future matrix elements
     */
    public FloatMatrix(float[][] elements) {
        this.elements = elements;
        checkIfProperMatrix();
    }

    /**
     * A constructor that creates a Matrix out of the {@link FloatVector}
     * @param vector the vector, which is turned into a matrix
     */
    public FloatMatrix(FloatVector vector) {
        this.elements = new float[vector.size()][1];
        for (int i = 0; i < vector.size(); i++) {
            this.elements[i][0] = vector.getElement(i);
        }
    }
    /*--------------------*/
}
