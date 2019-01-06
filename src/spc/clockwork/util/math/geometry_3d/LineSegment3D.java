package spc.clockwork.util.math.geometry_3d;


import com.sun.istack.internal.NotNull;
import spc.clockwork.util.TemporaryStorage;
import spc.clockwork.util.math.vector.Vector3f;

/**
 * A mathematical representation of a lineStorage segment in a 3d space
 * Consists of two points that define this segment
 *
 * @author wize
 * @version 0 (26 July 2018)
 */
public final class LineSegment3D {

    /* ATTRIBUTES
    /*--------------------*/

    /** The first point of the lineStorage segment */
    private final Vector3f a;

    /** The last point of the lineStorage segment */
    private final Vector3f b;

    /** The temporary storage of the length of the lineStorage segment; Is always actual after update */
    private final TemporaryStorage<Float> lengthStorage = new TemporaryStorage<Float>() {
        @Override
        protected Float getUpdatedElement() {
            return (LineSegment3D.this.b().sub(LineSegment3D.this.a())).length();
        }
    };

    /** The temporary storage of the direction of the lineStorage segment; Is always actual after update */
    private final TemporaryStorage<Vector3f> directionStorage = new TemporaryStorage<Vector3f>() {
        @Override
        protected Vector3f getUpdatedElement() {
            return LineSegment3D.this.b().sub(LineSegment3D.this.a()).normalize();
        }
    };

    /** The temporary storage of the lineStorage, defined by this lineStorage segment; Is always actual after update */
    private final TemporaryStorage<Line3D> lineStorage = new TemporaryStorage<Line3D>() {
        @Override
        protected Line3D getUpdatedElement() {
            return new Line3D(LineSegment3D.this);
        }
    };
    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/
    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * Gets the first point of a segment
     * @return the first point of a segment
     */
    public Vector3f a() {
        return this.a;
    }

    /**
     * Gets the last point of a segment
     * @return the last point of a segment
     */
    public Vector3f b() {
        return this.b;
    }

    /**
     * Gets the directionStorage of a lineStorage segment
     * @return the unit vector which points in the directionStorage from a to b
     */
    public Vector3f direction() {
        return this.directionStorage.getElement();
    }

    /**
     * Gets the lengthStorage of a segment
     * @return the lengthStorage of a segment
     */
    public float length() {
        return this.lengthStorage.getElement();
    }

    /**
     * Gets the line, which is defined by this segment
     * @return the defined by this segment line
     */
    public Line3D getLine() {
        return this.lineStorage.getElement();
    }

    /**
     * Checks whether the segment contains the point given (lies on the lineStorage within the range of this segment)
     * @param point the point given
     * @return true, if the point lies on the segment, else return false
     */
    public boolean contains(@NotNull Vector3f point) {
        final float coordinate;
        return this.getLine().contains(point)
                && ((coordinate = this.getLine().project1d(point)) <= 1f)
                && (coordinate >= 0f);
    }

    /**
     * Checks whether the projection of the given point onto the line of this segment does belong to this segment
     * @param point the point to be projected and checked
     * @return true, if the projection of this point belongs to this segment, else return false
     */
    public boolean containsProjectionOf(@NotNull final Vector3f point) {
        return this.contains(this.getLine().project(point));
    }

    /**
     * Turns the line segment definition upside down
     * @return the new line, which is a revert of this
     */
    public LineSegment3D revert() {
        return new LineSegment3D(this.b(), this.a());
    }

    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * Creates a lineStorage segment out of 2 3d points
     * @param a the first point of the segment
     * @param b the last point of the segment
     */
    public LineSegment3D(@NotNull final Vector3f a, @NotNull final Vector3f b) {
        this.a = a;
        this.b = b;
    }

    /**
     * Creates a lineStorage segment out of a lineStorage and 2 1d coordinates in terms of that lineStorage
     * @param line the lineStorage, which is a basis of the segment
     * @param a the coordinate of the first point in the lineStorage space
     * @param b the coordinate of the last point in the lineStorage space
     */
    public LineSegment3D(@NotNull final Line3D line, final float a, final float b) {
        this(line.get3dCoordinatesOf(a), line.get3dCoordinatesOf(b));
    }
    /*--------------------*/
}
