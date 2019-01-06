package spc.clockwork.util.math.vector;

import spc.clockwork.util.math.MathUtils;

/**
 * Vector of a floating point number in N dimensions
 *
 * @author wize
 * @version 0 (2018.03.21)
 */
public class FloatVector {

    /* ATTRIBUTES
    /*--------------------*/

    /**
     * A message of an Exception that occurs each time there is an operation between incompatible vectors
     */
    private final static String ERROR_INCOMPATIBLE_VECTOR_SPACES =
            "Error: There was a vector operation with vectors of different spaces";
    /**
     * A message of an Exception that occurs each time there is an attempt of normalizing zero-length vector
     */
    private static final String ERROR_VECTOR_LENGTH_ZERO_NORMALIZATION =
            "Error: There was an attempt of normalizing vector of the length zero";

    private static final String ERROR_OPERATION_NOT_IMPLEMENTED =
            "Error: The vector operation was not implemented";

    /**
     * An array of elements of the vector
     */
    protected final float[] elements;

    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/

    /**
     * Checks, whether two vectors have the same sizes and are allowed to be operands in the same operation
     * @param vector the second vector
     * @return true, if this has the same size as vector
     */
    private boolean isNotInTheSameSpace(FloatVector vector) {
            return (this.size() != vector.size());
    }

    /**
     * Checks, whether two vectors have the same sizes and are allowed to be operands in the same operation
     * If it is not true, that the exception is raised.
     * @param vector the second vector
     */
    private void checkIfIsInTheSameSpace(FloatVector vector) {
        if (this.isNotInTheSameSpace(vector))
            throw new ArrayIndexOutOfBoundsException("\n" + ERROR_INCOMPATIBLE_VECTOR_SPACES);
    }


    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * Element accessor
     * @param position the position of the element in the vector
     * @return element of the vector
     */
    public float getElement(int position) {
        return this.elements[position];
    }

    /**
     * Vector size
     * @return returns the total number of values (dimensions) the vector has
     */
    public int size() {
        return this.elements.length;
    }

    /**
     * Vector addition
     * @param vector the second operand of addition
     * @return new {@link FloatVector}, which is the sum of this and the argument
     */
    public FloatVector add(FloatVector vector) {
        this.checkIfIsInTheSameSpace(vector);
        float[] temporaryArray = new float[this.size()];
        for (int i = 0; i < this.size(); i++) {
            temporaryArray[i] = this.elements[i] + vector.elements[i];
        }
        return new FloatVector(temporaryArray);
    }


    /**
     * Vector subtraction
     * @param vector the second operand of subtraction
     * @return new {@link FloatVector}, which is the difference of this and the argument
     */
    public FloatVector sub(FloatVector vector) {
        this.checkIfIsInTheSameSpace(vector);
        float[] temporaryArray = new float[this.size()];
        for (int i = 0; i < this.size(); i++) {
            temporaryArray[i] = this.elements[i] - vector.elements[i];
        }
        return new FloatVector(temporaryArray);
    }

    /**
     * The cross product of this and argument vectors
     * @param vector the second vector
     * @return a new vector, which is a cross product of this and the argument vector
     */
    public FloatVector cross(FloatVector vector) {
        //TODO: IMPLEMENT THE GENERAL CROSS PRODUCT
        throw new RuntimeException(ERROR_OPERATION_NOT_IMPLEMENTED);
    }

    /**
     * Vector scalar multiplication (or the dot product)
     * @param vector the second operand of dot product
     * @return a a float number, which is the dot product of this by the argument
     */
    public float dot(FloatVector vector) {
        checkIfIsInTheSameSpace(vector);

        float temporaryNumber = 0f;
        for (int i = 0; i < this.size(); i++) {
            temporaryNumber += this.elements[i] * vector.elements[i];
        }
        return temporaryNumber;
    }

    /**
     * Vector multiplication by scalar
     * @param scalar the scalar, which will be multiplied by the vector
     * @return new {@link FloatVector}, which is on the same line as this, but has different scale
     */
    public FloatVector scale(float scalar) {
        float[] temporaryArray = new float[this.size()];
        for (int i = 0; i < this.size(); i++) {
            temporaryArray[i] = this.elements[i] * scalar;
        }
        return new FloatVector(temporaryArray);
    }

    /**
     * Vector negation
     * @return new {@link FloatVector}, which is the negated this
     */
    public FloatVector negate() {
        return this.scale(-1f);
    }


    /**
     * Vector normalization (getting a unit-vector of this)
     * @return new {@link FloatVector}, which is a unit-vector of this (or return this if this length is 1)
     */
    public FloatVector normalize() {
        if(this.length() == 1f) return this;
        //if(this.length() == 0f) throw new RuntimeException(ERROR_VECTOR_LENGTH_ZERO_NORMALIZATION);
        float length = this.length();
        float[] temporaryArray = new float[this.size()];
        for (int i = 0; i < this.size(); i++) {
            temporaryArray[i] = this.elements[i] / length;
        }
        return new FloatVector(temporaryArray);
    }

    /**
     * Vector length
     * @return returns the length of a vector in a space (its absolute scalar value)
     */
    public float length() {
        float temporaryValue = 0f;
        for (int i = 0; i < this.size(); i++) {
            temporaryValue += this.elements[i] * this.elements[i];
        }
        return (float) Math.sqrt(temporaryValue);
    }

    /**
     * Angle in degrees between this and argument vectors
     * @param vector the second vector
     * @return an angle between vectors in degrees as float
     */
    public float angleBetween(FloatVector vector) {
        return (float) Math.toDegrees(Math.acos(this.normalize().dot(vector.normalize())));
    }


    /**
     * Projects a vector given onto this vector
     * @param vector a vector to be projected onto this
     * @return a new vector, which is a projected argument onto this
     */
    public FloatVector project(FloatVector vector) {
        this.checkIfIsInTheSameSpace(vector);
        return this.scale(vector.dot(this) / (float)Math.pow(this.length(), 2d));
    }


    /**
     * Projects this onto a given vector
     * @param vector a vector to project this on
     * @return a new vector, which is a projection of this onto an argument
     */
    public FloatVector projectOnto(FloatVector vector) {
        return vector.project(this);
    }


    @Override
    public String toString() {
        if(this.size() == 0) return "vec0[]";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("vec" + this.size() + "[");
        stringBuilder.append(this.getElement(0));
        for(int i = 1; i < this.size(); i++) {
            stringBuilder.append(" | ");
            stringBuilder.append(this.getElement(i));
        }
        stringBuilder.append("]");

        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof FloatVector)) return false;
        FloatVector vector = (FloatVector) obj;
        if(this.size() != vector.size()) return false;
        for(int i = 0; i < this.size(); i++) {
            if(!MathUtils.equal(this.getElement(i), vector.getElement(i))) return false;
        }
        return true;
    }

    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * A constructor that creates a vector out of the array elements
     * @param elements the floating point number array of elements
     */
    public FloatVector(float[] elements) {
        this.elements = elements;
    }

    /**
     * A constructor that creates an N-dimensional zero vector
     * @param noElements the number of dimensions
     */
    public FloatVector(int noElements) {
        this.elements = new float[noElements];
    }

    /**
     * A constructor that creates a 0-dimensional vector
     * @deprecated
     */
    public FloatVector() {
        this.elements = new float[0];
    }

    /**
     * A constructor that creates a copy of the {@link FloatVector} given
     * @param floatVector a float vector instance
     */
    public FloatVector(FloatVector floatVector) {
        this(floatVector.elements);
    }
    /*--------------------*/
}