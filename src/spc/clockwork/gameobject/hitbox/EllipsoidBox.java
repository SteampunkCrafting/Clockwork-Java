package spc.clockwork.gameobject.hitbox;

import com.sun.istack.internal.NotNull;
import spc.clockwork.collections.GameWorld;
import spc.clockwork.gameobject.GameObject;
import spc.clockwork.graphics.mesh.Meshes;
import spc.clockwork.util.ObjDecoder;
import spc.clockwork.util.TemporaryStorage;
import spc.clockwork.util.math.geometry_3d.Triangle3D;
import spc.clockwork.util.math.matrix.Matrix4f;
import spc.clockwork.util.math.vector.Vector3f;

import java.util.Iterator;
import java.util.LinkedList;

import static spc.clockwork.gameobject.hitbox.CollisionAlgorithms.getEllipsoidIntoTrianglePenetration;


/**
 * {@link EllipsoidBox} is a {@link HitBox} with the ellipsoid form.
 * The object has a radius and a bunch of matrices inside.
 * The ellipsoid box is capable of detecting collisions with the Meshes,
 *      and also predict the movement collisions, giving some extra data such as collision point, or
 *      the time, where the object must stop in order to avoid collision.
 * @author wize
 * @version 0 (18 July 2018)
 * TODO: IMPLEMENT CLASS
 */
public class EllipsoidBox extends HitBox {

    /* ATTRIBUTES
    /*--------------------*/

    /** The radius of the ellipsoid in the parent space */
    private final Vector3f radius;

    /**
     * The storage of the matrix, which stretches the parent space such that the ellipsoid becomes a unit
     *  sphere in the center of the space
     */
    private final TemporaryStorage<Matrix4f> ellipsoidSpaceMatrix = new TemporaryStorage<Matrix4f>() {
        @Override
        protected Matrix4f getUpdatedElement() {
            Vector3f radius = EllipsoidBox.this.getRadius();
            return Matrix4f.newScaleMatrix(1f/radius.x(), 1f/radius.y(), 1f/radius.z());
        }
    };


    /**
     * The storage of the matrix, which transforms vectors from the absolute space to the ellipsoid space
     * Must be updated each ellipsoid box movement
     */
    private final TemporaryStorage<Matrix4f> absoluteToEllipsoidSpaceMatrix = new TemporaryStorage<Matrix4f>() {
        @Override
        protected Matrix4f getUpdatedElement() {
            return EllipsoidBox.this.ellipsoidSpaceMatrix.getElement()
                    .mul(EllipsoidBox.this.getAbsoluteTransformation().getAntiModelMatrix());
        }
    };

    /** A matrix that translates from 'movement' to 'velocity' vector format */
    private static final Matrix4f movementToVelocityMatrix = Matrix4f.newScaleMatrix(-1f, -1f, 1f);
    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/
    /* ---- HELPING METHODS ---- */
    /**
     * Gets all triangles from all of the mesh boxes given by an iterator,
     *      transforms them into the ellipsoid space and returns them as an instance of a {@link LinkedList}
     * @param meshBoxIterator an iterator that provides the mesh boxes to collect triangles from
     * @return a list of all triangles in the ellipsoid space
     */
    private LinkedList<Triangle3D> getTransformedTriangles(@NotNull final Iterator<MeshBox> meshBoxIterator) {
        MeshBox box;
        LinkedList<Triangle3D> listOfTransformedTriangles = new LinkedList<>();
        while(meshBoxIterator.hasNext()) {
            box = meshBoxIterator.next();
            Iterator<Triangle3D> triangleIterator = box.triangleIterator();
            Matrix4f toEllipsoidSpace = absoluteToEllipsoidSpaceMatrix.getElement().mul(box.getAbsoluteModelMatrix());
            while(triangleIterator.hasNext())
                listOfTransformedTriangles.add(triangleIterator.next().transform(toEllipsoidSpace));
        }
        return listOfTransformedTriangles;
    }

    /**
     * Builds a matrix that translates the vectors from the point of view to the ellipsoid space
     * @param pointOvView point of view
     * @return the matrix that translates the vectors from the point of view to the ellipsoid space
     */
    private Matrix4f pointOfViewToEllipsoidTranslation(@NotNull final GameObject pointOvView) {
        //todo: fix the method: the absolute model matrix is used instead of the absolute view one
        return this.absoluteToEllipsoidSpaceMatrix.getElement()
                .mul(pointOvView.getAbsoluteModelMatrix())
                .mul(EllipsoidBox.movementToVelocityMatrix); // crutch
    }


    /* ---- COLLISION CHECKERS ---- */
    /**
     * Checks the fact of the collision with the {@link MeshBox}, assuming that there is no movement
     * @param hitBox the {@link MeshBox} to check collision with
     * @return true, if the collision between the {@link MeshBox} and this {@link EllipsoidBox} exists, else return false
     */
    private boolean collides(@NotNull final MeshBox hitBox) {
        final Iterator<Triangle3D> iterator =
                hitBox.triangleIterator();
        final Matrix4f meshBoxToEllipsoidSpaceMatrix =
                this.ellipsoidSpaceMatrix.getElement() // FROM ELLIPSOID BOX TO ELLIPSOID SPACE
                        .mul(this.getAbsoluteTransformation().getAntiModelMatrix() // FROM ABSOLUTE SPACE TO ELLIPSOID BOX
                        .mul(hitBox.getAbsoluteModelMatrix())); // FROM MESH BOX TO ABSOLUTE SPACE
        while(iterator.hasNext())
            if (getEllipsoidIntoTrianglePenetration(
                    iterator.next().transform(meshBoxToEllipsoidSpaceMatrix),
                    true) != null) return true;
        return false;
    }


    /* ---- OVERRIDDEN METHODS ---- */
    /**
     * Prescribes what happens with the {@link GameObject} on its movement
     * This is an overridable method
     */
    @Override
    protected void onMovement() {
        super.onMovement();
        if (this.ellipsoidSpaceMatrix != null) this.ellipsoidSpaceMatrix.setOutdated();
        if (this.absoluteToEllipsoidSpaceMatrix != null) this.absoluteToEllipsoidSpaceMatrix.setOutdated();
    }
    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/
    /**
     * Gets the radius of the ellipsoid in parent space
     * @return the radius of the ellipsoid in parent space
     */
    public Vector3f getRadius() {
        return this.radius;
    }


    /**
     * Given an {@link Iterator} over the {@link MeshBox} instances,
     *      checks collision with those boxes and estimates the minimal translation vector
     *      that is required for this to avoid the collision.
     * The minimal translation is given in format vec3[right, up, forward] in the space of pointOfView
     * @param boxesToCollide an iterator over the {@link MeshBox}es to collide
     * @param pointOfView a point of view of the minimal translation vector
     * @return the minimal translation vector of the collision. If there is no collision, the zero vector is returned.
     */
    public Vector3f collideAndSlide(@NotNull final Iterator<MeshBox> boxesToCollide,
                                    @NotNull final GameObject pointOfView,
                                    final boolean cullBackFaces) {
        final Iterator<Triangle3D> iterator = this.getTransformedTriangles(boxesToCollide).iterator();
        Vector3f penetration = Vector3f.ZERO_VECTOR;
        Vector3f temp;
        while(iterator.hasNext()) {
            temp = CollisionAlgorithms.getEllipsoidIntoTrianglePenetration(iterator.next(), cullBackFaces);
            if(temp != null) penetration = penetration.add(temp);
        }
        return pointOfViewToEllipsoidTranslation(pointOfView).invert().mul(penetration.negate(), 1f).negate();
    }


    @Override
    public boolean collides(HitBox hitBox) {
        if (hitBox instanceof MeshBox) return this.collides((MeshBox) hitBox);
        return super.collides(hitBox);
    }
    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * Default constructor
     * @param gameWorld the game world of this
     * @param radius the radius of this
     */
    public EllipsoidBox(@NotNull final GameWorld gameWorld, @NotNull final Vector3f radius) {
        super(gameWorld);
        this.radius = radius;
        try {
            this.setMesh(
                    ObjDecoder.loadMesh(
                            GameWorld.getTemp(),
                            Meshes.class,
                            "default_meshes/sphere.obj",
                            null,
                            radius)
            );
        } catch (Exception e) {
            this.setMesh(null);
        }
    }
    /*--------------------*/
}