package spc.clockwork.util.math.geometry_3d;

import com.sun.istack.internal.NotNull;
import spc.clockwork.util.math.MathUtils;
import spc.clockwork.util.math.vector.Vector2f;
import spc.clockwork.util.math.vector.Vector3f;

/**
 * A mathematical representation of a plane in a 3d space
 * consists of an equation (x + y + z = w), origin and normal
 *
 * @author wize
 * @version 0 (18 July 2018)
 */
public final class Plane3D {

    /* ATTRIBUTES
    /*--------------------*/

    /** Normal of the plane */
    private final Vector3f normal;

    /** Origin of the plane */
    private final Vector3f origin;

    /** X of the plane */
    private final float x;

    /** Y of the plane */
    private final float y;

    /** Z of the plane */
    private final float z;

    /** W of the plane */
    private final float w;

    /** The first base coordinate vector of the plane */
    private final Vector3f i;

    /** The second base coordinate vector of the plane */
    private final Vector3f j;
    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/
    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /* ---- GETTER METHODS ---- */
    /**
     * Gets the normal of the plane
     * @return the normal of the plane
     */
    public final Vector3f normal() {
        return this.normal;
    }

    /**
     * Gets the origin of the plane
     * @return the origin of the plane
     */
    public final Vector3f origin() {
        return this.origin;
    }

    /**
     * The first local space coordinate
     * @return the first local orthonormal basis vector
     */
    public final Vector3f i() {
        return this.i;
    }

    /**
     * The second local space coordinate
     * @return the second local orthonormal basis vector
     */
    public final Vector3f j() {
        return this.j;
    }

    /**
     * Gets the x component of the plane equation
     * @return the x component of the plane equation
     */
    public final float x() {
        return this.x;
    }

    /**
     * Gets the y component of the plane equation
     * @return the y component of the plane equation
     */
    public final float y() {
        return this.y;
    }

    /**
     * Gets the z component of the plane equation
     * @return the z component of the plane equation
     */
    public final float z() {
        return this.z;
    }

    /**
     * Gets the w component of the plane equation
     * @return the w component of the plane equation
     */
    public final float w() {
        return this.w;
    }

    /**
     * Computes the orthogonal projection of a 3d point onto the 2d plane,
     *  then returns the 3d space coordinates of a projected point
     */
    public final Vector3f project(@NotNull final Vector3f point) {
        return point.sub(this.normal().scale(this.getSignedDistanceTo(point)));
    }

    /**
     * Computes the coordinates of the orthogonal projection of a 3d point onto the 2d plane,
     *  then returns its 'local plane' coordinates as the 2d vector
     * @param point the 3d point to be projected onto a plane and then returned as a point in terms of the plane basis
     * @return the 2d vector of a projected on a plane point in the plain's 'local space'
     */
    public final Vector2f project2d(@NotNull Vector3f point) {
        if(!this.contains(point)) point = this.project(point);
        return new Vector2f(
                this.i().dot(point.sub(this.origin())),
                this.j().dot(point.sub(this.origin()))
        );
    }

    /**
     * Checks whether the plain contains point.
     * Warning: the method can give unreliable results because of the float math used.
     * @param point the point to be checked.
     * @return true, if the point lies on the plane, else return false
     */
    public final boolean contains(Vector3f point) {
        return MathUtils.equal(this.x() * point.x() + this.y() * point.y() + this.z() * point.z(), this.w());
    }


    /* ---- ANALYSIS METHODS ---- */
    /**
     * Checks whether normal of the plane faces the same direction as the direction given
     * @param direction the direction to test the plane
     * @return true, if the direction dot normal is less or equal to zero, else return false
     */
    public final boolean isFrontFacingTo(@NotNull final Vector3f direction) {
        return this.normal().dot(direction.normalize()) >= 0f;
    }

    /**
     * Checks the signed distance between the point and the plane
     * Returns the negative value, if the point sees the back face of the plane
     * @param point the point to check the signed distance to
     * @return the signed distance between the point and the plane
     */
    public final float getSignedDistanceTo(@NotNull final Vector3f point) {
        return point.dot(this.normal()) + this.w();
    }

    /* ---- OTHER METHODS ---- */

    @Override
    public String toString() {
        return String.format("plane{origin: %s; normal: %s}", this.origin(), this.normal());
    }
    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * A plane from origin and normal constructor
     * @param origin the origin point of the plane
     * @param normal the normal point of the plane
     */
    public Plane3D(@NotNull final Vector3f origin, @NotNull final Vector3f normal) {

        /* ---- Filling the data ---- */
        this.origin = origin;
        this.normal = normal.normalize();
        this.x = this.normal().x();
        this.y = this.normal().y();
        this.z = this.normal().z();
        this.w = -(this.normal().x() * origin.x() + this.normal().y() * origin.y() + this.normal().z() * origin.z());


        /* ---- Making the orthonormal basis ---- */
        if(this.normal().x() != 0 || this.normal().y() != 0) {
            this.i = this.normal().cross(this.normal().add(Vector3f.UP_VECTOR));
        } else if (this.normal().z() != 0 || this.normal().y() != 0) {
            this.i = this.normal().cross(this.normal().add(Vector3f.RIGHT_VECTOR));
        } else {
            this.i = this.normal().cross(this.normal().add(Vector3f.FORWARD_VECTOR)).normalize();
        }
        this.j = this.i().cross(this.normal());

    }

    /**
     * A plane from 3 points constructor
     *   with the front face as the counterclockwise order of the points
     *   and the origin at the point0
     * @param point0 the first point on the plane
     * @param point1 the second point on the plane
     * @param point2 the third point on the plane
     */
    public Plane3D(@NotNull final Vector3f point0, @NotNull final Vector3f point1, @NotNull final Vector3f point2) {
        this(point0, point1.add(point0.negate()).cross(point2.add(point0.negate())));
        // this.origin = point0;
        // this.normal = point1.add(point0.negate()).cross(point2.add(point0.negate())).normalize();
    }

    /**
     * A plane from triangle constructor
     * @param triangle the triangle, which lies on the plane, and whose point a is the origin of such a plane
     */
    public Plane3D(@NotNull final Triangle3D triangle) {
        this(triangle.a(), triangle.b(), triangle.c());
    }
    /*--------------------*/
}
