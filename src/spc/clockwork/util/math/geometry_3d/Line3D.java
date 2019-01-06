package spc.clockwork.util.math.geometry_3d;


import com.sun.istack.internal.NotNull;
import spc.clockwork.util.math.MathUtils;
import spc.clockwork.util.math.vector.Vector3f;

/**
 * A mathematical representation of a line in a 3d space
 * Consists of the origin and the direction of this line in 3d space
 *
 * @author wize
 * @version 0 (26 July 2018)
 */
public final class Line3D {

    /* ATTRIBUTES
    /*--------------------*/

    /** The first point that define a line */
    private final Vector3f origin;

    /** The direction of the line */
    private final Vector3f direction;
    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/
    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * Gets the origin of this line
     * @return the origin of this line
     */
    public Vector3f origin() {
        return this.origin;
    }

    /**
     * Gets the direction of this line
     * @return the direction of this line
     */
    public Vector3f direction() {
        return this.direction;
    }

    /**
     * Gets the distance between the point and the line
     * @param point the point whose distance is needed to check
     * @return the nearest distance between the point and this line
     */
    public float getDistanceTo(@NotNull final Vector3f point) {
        return this.project(point).sub(point).length();
    }

    /**
     * Gets the 3d coordinates of the point on the line
     * @param point the coordinate of the point on the line in terms of this line
     * @return the 3d point that has a projection, given by the coordinate
     */
    public Vector3f get3dCoordinatesOf(final float point) {
        return this.origin().add(this.direction().scale(point));
    }

    /**
     * Projects the point onto the line (if the point is not on the line)
     *      and returns its float coordinate in terms of this line (with the origin = 0f and direction = 1f)
     * @param point the point to project onto the line
     * @return the coordinate of the projected point in terms of this line
     */
    public float project1d(@NotNull Vector3f point) {
        point = this.project(point.sub(origin()));
        final float pointLength = point.length();
        final float dirLength = this.direction().length();
        return point.dot(this.direction()) > 0f ?
                pointLength / dirLength :
                - pointLength / dirLength;
    }

    /**
     * Projects the point onto the line (if the point is not on the line)
     *      and returns the point, which is an orthogonal projection onto the line of the point given
     * @param point the point to be projected on this
     * @return the point, which is a projection on the line of this
     */
    public Vector3f project(@NotNull Vector3f point) {
        if(this.contains(point)) return point;
        final Vector3f normalizedDirection = this.direction().normalize();
        return this.project(
                this.origin().add(normalizedDirection.scale(point.sub(this.origin()).dot(normalizedDirection)))
        );
    }

    /**
     * Checks, whether this line contains that point inside
     * @param point the point to check
     * @return true, if this point is contained by this line, else return false
     */
    public boolean contains(@NotNull Vector3f point) {
        return (point = point.sub(this.origin())).equals(Vector3f.ZERO_VECTOR)
                 || MathUtils.equal(Math.abs(point.normalize().dot(this.direction().normalize())), 1f);

    }

    /**
     * Checks, whether this line contains the segment given
     * @param segment the segment to check
     * @return true, if this segment is contained by this line, else return false
     */
    public boolean contains(@NotNull final LineSegment3D segment) {
        return this.contains(segment.a()) && this.contains(segment.b());
    }
    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * Default line out of the origin and the direction constructor
     * the unit of distance in the space of this line is equal to the length of the direction vector
     * @param origin the origin of a line
     * @param direction the direction of a line
     */
    public Line3D(Vector3f origin, Vector3f direction) {
        this.origin = origin;
        this.direction = direction;
    }


    /**
     * Constructs the line out of the line segment, where the origin of the line is the point a of the segment
     *      and the direction of the line is the difference between point b and point a of the segment
     * @param segment the line segment to create the line from
     */
    public Line3D(LineSegment3D segment) {
        this(segment.a(), segment.b().sub(segment.a()));
    }
    /*--------------------*/
}
