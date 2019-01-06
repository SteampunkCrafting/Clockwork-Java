package spc.clockwork.util.math.vector;


import spc.clockwork.util.math.matrix.Matrix4f;

/**
 * A Quaternion implementation with the most important operations on it
 *
 * @author wize
 * @version 0 (28 June 2018)
 */
public final class Quaternion extends FloatVector {

    /* ATTRIBUTES
    /*--------------------*/

    /** An error message, when the vector cannot be converted into a quaternion */
    private static final String ERROR_CANNOT_CONVERT_TO_QUATERNION =
            "Error, cannot convert FloatVector to Quaternion";

    /** A Quaternion with no rotation */
    public static final Quaternion IDENTITY_QUATERNION = new Quaternion();
    /*--------------------*/


    /* PUBLIC METHODS
    /*--------------------*/

    public float x() {
        return this.getElement(0);
    }

    public float y() {
        return this.getElement(1);
    }

    public float z() {
        return this.getElement(2);
    }

    public float w() {
        return this.getElement(3);
    }

    public Quaternion conjugate() {
        return new Quaternion(-this.x(), -this.y(), -this.z(), this.w());
    }

    public Quaternion mul(Quaternion quaternion) {
        //return new Quaternion(this.toRotationMatrix().mul(quaternion));

        // float w_ = this.w() * quaternion.w() - this.x() * quaternion.x() - this.y() * quaternion.y() - this.z() * quaternion.z();
        // float x_ = this.x() * quaternion.w() + this.w() * quaternion.x() + this.y() * quaternion.z() - this.z() * quaternion.y();
        // float y_ = this.y() * quaternion.w() + this.w() * quaternion.y() + this.z() * quaternion.x() - this.x() * quaternion.z();
        // float z_ = this.z() * quaternion.w() + this.w() * quaternion.z() + this.x() * quaternion.y() - this.y() * quaternion.x();
        return new Quaternion(
                this.x() * quaternion.w()
                        + this.w() * quaternion.x()
                        + this.y() * quaternion.z()
                        - this.z() * quaternion.y(),
                this.y() * quaternion.w()
                        + this.w() * quaternion.y()
                        + this.z() * quaternion.x()
                        - this.x() * quaternion.z(),
                this.z() * quaternion.w()
                        + this.w() * quaternion.z()
                        + this.x() * quaternion.y()
                        - this.y() * quaternion.x(),
                this.w() * quaternion.w()
                        - this.x() * quaternion.x()
                        - this.y() * quaternion.y()
                        - this.z() * quaternion.z()
        );
    }

    public Quaternion mul(Vector3f vector3f) {
        //return this.mul(new Quaternion(vector3f));

        // float w_ = -this.x() * vector3f.x() - this.y() * vector3f.y() - this.z() * vector3f.z();
        // float x_ =  this.w() * vector3f.x() + this.y() * vector3f.z() - this.z() * vector3f.y();
        // float y_ =  this.w() * vector3f.y() + this.z() * vector3f.x() - this.x() * vector3f.z();
        // float z_ =  this.w() * vector3f.z() + this.x() * vector3f.y() - this.y() * vector3f.x();

        return new Quaternion(
                this.w() * vector3f.x() + this.y() * vector3f.z() - this.z() * vector3f.y(),
                this.w() * vector3f.y() + this.z() * vector3f.x() - this.x() * vector3f.z(),
                this.w() * vector3f.z() + this.x() * vector3f.y() - this.y() * vector3f.x(),
                -this.x() * vector3f.x() - this.y() * vector3f.y() - this.z() * vector3f.z()
        );
    }

    public Matrix4f toRotationMatrix() {
        /*
        1 - 2*qy2 - 2*qz2 	2*qx*qy - 2*qz*qw 	2*qx*qz + 2*qy*qw
        2*qx*qy + 2*qz*qw 	1 - 2*qx2 - 2*qz2 	2*qy*qz - 2*qx*qw
        2*qx*qz - 2*qy*qw 	2*qy*qz + 2*qx*qw 	1 - 2*qx2 - 2*qy2
         */
        float m00 = 1 - 2*this.y()*this.y() - 2*this.z()*this.z();
        float m01 = 2*this.x()*this.y() - 2*this.z()*this.w();
        float m02 = 2*this.x()*this.z() + 2*this.y()*this.w();
        float m10 = 2*this.x()*this.y() + 2*this.z()*this.w();
        float m11 = 1 - 2*this.x()*this.x() - 2*this.z()*this.z();
        float m12 = 2*this.y()*this.z() - 2*this.x()*this.w();
        float m20 = 2*this.x()*this.z() - 2*this.y()*this.w();
        float m21 = 2*this.y()*this.z() + 2*this.x()*this.w();
        float m22 = 1 - 2*this.x()*this.x() - 2*this.y()*this.y();

        return new Matrix4f(new float[][] {
                {m00, m01, m02, 0f},
                {m10, m11, m12, 0f},
                {m20, m21, m22, 0f},
                { 0f,  0f,  0f, 1f}
        });
    }

    public FloatVector xyz() {
        return new FloatVector(new float[] { this.x(), this.y(), this.z() });
    }

    @Override
    public String toString() {
        return "quat[" +
                "x: " + this.x()
                + "|" +
                "y: " + this.y()
                + "|" +
                "z: " + this.z()
                + "|" +
                "w: " + this.w()
                + "]";
    }

    /**
     * Vector normalization (getting a unit-vector of this)
     *
     * @return new {@link FloatVector}, which is a unit-vector of this
     */
    @Override
    public Quaternion normalize() {
        return this.length() == 1f ? this : new Quaternion(super.normalize());
    }

    /**
     * Creates a Quaternion that represents the counterclockwise rotation on a DEGREE angle around the vector axis
     * @param angle an angle of rotation in degrees counterclockwise
     * @param axis an axis of rotation
     * @return a new {@link Quaternion} that describes the angle-axis rotation given
     */
    public static Quaternion rotation(float angle, Vector3f axis) {
        angle = (float)Math.toRadians(angle) / 2f; // WE DIVIDE ANGLE BY 2 HERE, AND TRANSLATE IT TO RADIANS
        return new Quaternion(
                axis.x() * (float) Math.sin(angle),
                axis.y() * (float) Math.sin(angle),
                axis.z() * (float) Math.sin(angle),
                (float) Math.cos(angle)
        );
    }

    /**
     * Computes the quaternion rotation that rotates the vector from one position to another
     * @param from the vector 'before'
     * @param to the vector 'after'
     * @return the Quaternion instance that performs the rotation from one to another vectors
     */
    public static Quaternion findRotation(Vector3f from, Vector3f to) {
        if(from.equals(to)) return Quaternion.IDENTITY_QUATERNION;
        Vector3f crossProduct = from.normalize().cross(to.normalize());
        float fromLen = from.length();
        float toLen = to.length();
        return new Quaternion(
                crossProduct.x(),
                crossProduct.y(),
                crossProduct.z(),
                (float) Math.sqrt(fromLen * fromLen * toLen * toLen) + from.dot(to)
        );
    }
    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * Default constructor
     * @param x the x (i) imaginary component
     * @param y the y (j) imaginary component
     * @param z the z (k) imaginary component
     * @param w the w (scalar) real component
     */
    public Quaternion(final float x, final float y, final float z, final float w) {
        super(new float[] {x, y, z, w});
    }

    /**
     * A constructor of the zero-rotation quaternion with w component equal to 1
     */
    public Quaternion() {
        this(0f, 0f, 0f, 1f);
    }

    /**
     * A constructor that converts a 4d vector into a quaternion
     * @param vector a vector that should be converted into quaternion
     */
    public Quaternion(FloatVector vector) {
        super(vector);
        if(vector.elements.length != 4)
            throw new RuntimeException(ERROR_CANNOT_CONVERT_TO_QUATERNION + " " + this.toString());
    }

    /**
     * A constructor that converts a 3d vector into a quaternion with zero real component
     * @param vector3f a vector that should be converted into quaternion
     */
    public Quaternion(Vector3f vector3f) {
        this(vector3f.x(), vector3f.y(), vector3f.z(), 0f);
    }
    /*--------------------*/
}

