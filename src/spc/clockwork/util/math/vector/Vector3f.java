package spc.clockwork.util.math.vector;

import java.math.MathContext;

/**
 * Vector3f is a developer-friendly class, which is used to manipulate with 3d space objects
 * Technically, it is a 4d vector, because in order to work with 4*4 matrices,
 * vectors must be 'extended' to 4 dimensions anyway
 *
 * @author wize
 * @version 0 (2018.03.22)
 */
public final class Vector3f extends FloatVector {

    /* ATTRIBUTES
    /*--------------------*/
    private static final String ERROR_INVALID_ELEMENTS_ARRAY =
            "Error: Invalid elements array in Vector3f construction (float[3] is required)";

    public static final Vector3f ZERO_VECTOR = new Vector3f();
    public static final Vector3f FORWARD_VECTOR = new Vector3f(0f,0f,-1f);
    public static final Vector3f RIGHT_VECTOR = new Vector3f(1f, 0f, 0f);
    public static final Vector3f UP_VECTOR = new Vector3f(0f, 1f, 0f);
    public static final Vector3f VECTOR_111 = new Vector3f(1f, 1f, 1f);
    public static final MathContext DEFAULT_PRECISION_AT_PRINT = new MathContext(3);
    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/
    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    @Override
    public Vector3f cross(FloatVector vector) {
        //return new Vector3f(super.cross(vector)); //TODO: IMPLEMENT AND USE THE GENERAL VECTOR CROSS PRODUCT
        Vector3f v = new Vector3f(vector);
        return new Vector3f(
                this.y() * v.z() - this.z() * v.y(),
                this.z() * v.x() - this.x() * v.z(),
                this.x() * v.y() - this.y() * v.x()
        );
    }

    @Override
    public Vector3f negate() {
        return new Vector3f(super.negate());
    }

    @Override
    public Vector3f scale(float scalar) {
        return new Vector3f(super.scale(scalar));
    }

    @Override
    public Vector3f add(FloatVector vector) {
        return new Vector3f(super.add(vector));
    }

    @Override
    public Vector3f sub(FloatVector vector) {
        return new Vector3f(super.sub(vector));
    }

    @Override
    public Vector3f project(FloatVector vector) {
        return new Vector3f(super.project(vector));
    }

    @Override
    public Vector3f projectOnto(FloatVector vector) {
        return new Vector3f(super.projectOnto(vector));
    }

    @Override
    public Vector3f normalize() {
        return this.length() == 1f ? this : new Vector3f(super.normalize());
    }

    @Override
    public String toString() {
        //BigDecimal x = new BigDecimal(this.x()).;

        return "vec3[" +
                "x: " + this.x() //new BigDecimal(this.x()).round(DEFAULT_PRECISION_AT_PRINT).floatValue()
                + " |" +
                "y: " + this.y() //new BigDecimal(this.y()).round(DEFAULT_PRECISION_AT_PRINT).floatValue()
                + " |" +
                "z: " + this.z() //new BigDecimal(this.z()).round(DEFAULT_PRECISION_AT_PRINT).floatValue()
                + "]";
    }

    /*--------------------*/

    /**
     * The x-coordinate getter
     * @return the first coordinate
     */
    public float x() {
        return this.getElement(0);
    }

    /**
     * The y-coordinate getter
     * @return the second coordinate
     */
    public float y() {
        return this.getElement(1);
    }

    /**
     * The z-coordinate getter
     * @return the third coordinate
     */
    public float z() {
        return this.getElement(2);
    }

    /**
     * Rotates the vector with the quaternion
     * @param quaternion a {@link Quaternion} that symbolizes the rotation
     * @return a new vector, which is a rotated by a quaternion this
     */
    public Vector3f rotate(Quaternion quaternion) {
        return new Vector3f(quaternion.mul(this).mul(quaternion.conjugate()).xyz());
    }

    /*--------------------*/




    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * A classic constructor that creates a new 3d zero-vector
     */
    private Vector3f() {
        super(new float[]{0f, 0f, 0f});
    }

    /**
     * A classic constructor that creates a new 3d vector with defined coordinates
     * @param x the first coordinate
     * @param y the second coordinate
     * @param z the third coordinate
     */
    public Vector3f(float x, float y, float z) {
        super(new float[]{x, y, z});
    }

    /**
     * Constructs the vector with its x, y, and z equal to the xyz given
     * @param xyz the value of x, y, and z
     */
    public Vector3f(float xyz) {
        this(xyz, xyz, xyz);
    }

    /**
     * An "auto-casting" constructor
     * @param vector a FloatVector to recreate
     */
    public Vector3f(FloatVector vector) {
        super(vector.elements);
        if (vector.elements.length != 3) throw new IndexOutOfBoundsException(ERROR_INVALID_ELEMENTS_ARRAY);
    }

    /*--------------------*/

}
