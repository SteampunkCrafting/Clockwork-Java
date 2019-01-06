package spc.clockwork.util.math.geometry_3d;


import com.sun.istack.internal.NotNull;
import spc.clockwork.util.TemporaryStorage;
import spc.clockwork.util.math.MathUtils;
import spc.clockwork.util.math.matrix.FloatMatrix;
import spc.clockwork.util.math.matrix.Matrix4f;
import spc.clockwork.util.math.vector.Vector3f;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


/**
 * A mathematical representation of a triangle in a 3d space
 * consists of 3 points: A, B, and C
 *
 * @author wize
 * @version 0 (21 July 2018)
 */
public final class Triangle3D {

    /* ATTRIBUTES
    /*--------------------*/

    /** An error message, when the triangle was constructed with 2 or more same points */
    private static final String ERROR_TRIANGLE_HAS_REPEATING_POINTS =
            "Error: the triangle constructed must have 3 different points as its vertices";

    /** The first triangle point */
    private final Vector3f a;

    /** The second triangle point */
    private final Vector3f b;

    /** The third triangle point */
    private final Vector3f c;

    /** The 'triangle space' matrix storage. The matrix is constructed only once and does not require any updates. */
    private final TemporaryStorage<FloatMatrix> triangleSpaceMatrixStorage = new TemporaryStorage<FloatMatrix>() {
        @Override
        protected FloatMatrix getUpdatedElement() {
            Vector3f ab = Triangle3D.this.b().sub(Triangle3D.this.a()); // Triangle3D.this.a().sub(Triangle3D.this.b());
            Vector3f ac = Triangle3D.this.c().sub(Triangle3D.this.a()); // Triangle3D.this.a().sub(Triangle3D.this.c());
            Vector3f cross = ab.cross(ac);
                return new FloatMatrix(new float[][] {
                        {ab.x(), ac.x(), cross.x()},
                        {ab.y(), ac.y(), cross.y()},
                        {ab.z(), ac.z(), cross.z()}
                }).invert();
            }
    };

    /** The temporary storage of triangle sides. Is actual since being updated for the first time */
    private final TemporaryStorage<List<LineSegment3D>> sides = new TemporaryStorage<List<LineSegment3D>>() {
        @Override
        protected List<LineSegment3D> getUpdatedElement() {
            return Arrays.asList(
                    new LineSegment3D(Triangle3D.this.a(), Triangle3D.this.b()),
                    new LineSegment3D(Triangle3D.this.b(), Triangle3D.this.c()),
                    new LineSegment3D(Triangle3D.this.c(), Triangle3D.this.a())
            );
        }
    };

    /** The temporary storage of triangle sides. Is actual since being updated for the first time */
    private final TemporaryStorage<List<Vector3f>> vertices = new TemporaryStorage<List<Vector3f>>() {
        @Override
        protected List<Vector3f> getUpdatedElement() {
            return Arrays.asList(
                    Triangle3D.this.a(),
                    Triangle3D.this.b(),
                    Triangle3D.this.c()
            );
        }
    };

    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/
    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * Gets the first point of the triangle
     * @return the first point of the triangle
     */
    public final Vector3f a() {
        return this.a;
    }

    /**
     * Gets the second point of the triangle
     * @return the second point of the triangle
     */
    public final Vector3f b() {
        return this.b;
    }

    /**
     * Gets the third point of the triangle
     * @return the third point of the triangle
     */
    public final Vector3f c() {
        return this.c;
    }

    /**
     * Transforms the triangle points by means of using the transformation, defined by {@link Matrix4f}
     * @param transformation the transformation to be applied to triangles
     * @return new triangle, which is the transformed version of this one
     */
    public final Triangle3D transform(Matrix4f transformation) {
        return new Triangle3D(
                transformation.mul(this.a(), 1f),
                transformation.mul(this.b(), 1f),
                transformation.mul(this.c(), 1f)
        );
    }

    /**
     * Creates and returns an iterator over the sides of this triangle
     * @return the triangle side iterator
     */
    public final Iterator<LineSegment3D> sideIterator() {
        return this.sides.getElement().iterator();
    }

    /**
     * Creates and return an iterator over the vertices of this triangle
     * @return the triangle vertex iterator
     */
    public final Iterator<Vector3f> vertexIterator() {
        return this.vertices.getElement().iterator();
    }

    /**
     * Checks whether this triangle contains the point inside its area.
     * It is assumed that both the triangle and the point belong to the same space.
     * @param point the point to be checked
     * @return true, if the 3d point lies on the triangle, else return false
     * TODO: CHECK IF THE METHOD WORKS CORRECTLY. IF SO, OPTIMIZE IT, SO IT CONSUMES LESS MEMORY SPACE
     */
    public final boolean contains(@NotNull Vector3f point) {
        /* ---- TRANSFORMING THE POINT INTO THE TRIANGLE SPACE ---- */
        /* A triangle space is a space, where the a is the origin point, ab is the unit of x axis,
        ac is the unit of y axis, and the z axis is the cross product of ab and ac
        The transformation is performed by finding the change of basis matrix */
        point = new Vector3f(this.triangleSpaceMatrixStorage.getElement().mul(point.sub(this.a())));
        /* ---- PERFORMING A CHECK ---- */
        return MathUtils.equal(0f, point.z())
                && point.x() >= 0f
                && point.y() >= 0f
                && point.x() + point.y() <= 1f;
    }

    /**
     * Turns the triangle definition upside down
     * @return the new triangle, which is the same triangle with the points defined in the opposite order
     */
    public final Triangle3D revert() {
        return new Triangle3D(this.c(), this.b(), this.a());
    }
    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * Default triangle constructor from 3 points
     * @param a the first point of the triangle
     * @param b the second point of the triangle
     * @param c the third point of the triangle
     */
    public Triangle3D(@NotNull final Vector3f a, @NotNull final Vector3f b, @NotNull final Vector3f c) {
        if(a.equals(b) || b.equals(c) || c.equals(a)) throw new RuntimeException(ERROR_TRIANGLE_HAS_REPEATING_POINTS);
        this.a = a;
        this.b = b;
        this.c = c;
    }
    /*--------------------*/
}
