package spc.clockwork.util.math.vector;


/**
 * A mathematical representation of a 2d vector
 *
 * @author wize
 * @version 0 (18 July 2018)
 */
public class Vector2f extends FloatVector {

    /* ATTRIBUTES
    /*--------------------*/

    private static final String ERROR_INVALID_ELEMENTS_ARRAY =
            "Error: Invalid elements array in Vector3f construction (float[3] is required)";

    public static final Vector2f ZERO_VECTOR = new Vector2f();
    public static final Vector2f RIGHT_VECTOR = new Vector2f(1f, 0f);
    public static final Vector2f UP_VECTOR = new Vector2f(0f, 1f);
    public static final Vector2f VECTOR_11 = new Vector2f(1f, 1f);
    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/
    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * Gets the x element of vector
     * @return the x element of vector
     */
    public float x() {
        return this.getElement(0);
    }

    /**
     * Gets the y element of vector
     * @return the y element of vector
     */
    public float y() {
        return this.getElement(1);
    }



    /* ---- OVERRIDDEN METHODS ---- */

    @Override
    public Vector2f negate() {
        return new Vector2f(super.negate());
    }

    @Override
    public Vector2f scale(float scalar) {
        return new Vector2f(super.scale(scalar));
    }

    @Override
    public Vector2f add(FloatVector vector) {
        return new Vector2f(super.add(vector));
    }

    @Override
    public Vector2f sub(FloatVector vector) {
        return new Vector2f(super.sub(vector));
    }

    @Override
    public Vector2f normalize() {
        return this.length() == 1f ? this : new Vector2f(super.normalize());
    }

    @Override
    public Vector2f project(FloatVector vector) {
        return new Vector2f(super.project(vector));
    }

    @Override
    public Vector2f projectOnto(FloatVector vector) {
        return new Vector2f(super.projectOnto(vector));
    }

    @Override
    public String toString() {
        return "vec2[" +
                "x: " + this.x()
                + " |" +
                "y: " + this.y()
                + "]";
    }
    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * A zero-vector constructor
     */
    private Vector2f() {
        super(new float[] {0f, 0f});
    }

    /**
     * A classic constructor that creates a new 2d vector with defined coordinates
     * @param x the first coordinate
     * @param y the second coordinate
     */
    public Vector2f(float x, float y) {
        super(new float[]{x, y});
    }

    /**
     * Constructs the vector with its x, y, and z equal to the xy given
     * @param xy the value of x and y
     */
    public Vector2f(float xy) {
        this(xy, xy);
    }

    /**
     * An "auto-casting" constructor
     * @param vector a FloatVector to recreate
     */
    public Vector2f(FloatVector vector) {
        super(vector.elements);
        if (vector.elements.length != 2) throw new IndexOutOfBoundsException(ERROR_INVALID_ELEMENTS_ARRAY);
    }
    /*--------------------*/
}
