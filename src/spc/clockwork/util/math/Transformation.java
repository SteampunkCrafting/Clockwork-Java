package spc.clockwork.util.math;


import spc.clockwork.util.TemporaryStorage;
import spc.clockwork.util.math.matrix.Matrix4f;
import spc.clockwork.util.math.vector.Quaternion;
import spc.clockwork.util.math.vector.Vector3f;

/**
 * {@link Transformation} is a mutable structure that encapsulates position, rotation and scale of an object.
 * The class also provides methods to modify and getConst those fields in different forms.
 *
 * @author wize
 * @version 1 (4 July 2018)
 */
public final class Transformation {

    /* ATTRIBUTES
    /*--------------------*/
    /* ---- MAIN TRANSFORMATION FIELDS ---- */
    /** The position vector of this transformation */
    private Vector3f position;

    /** The rotation quaternion of this transformation */
    private Quaternion rotation;

    /** The scale of this transformation */
    private float scale;

    /* ---- LOCAL SPACE DIRECTIONS ---- */
    /** The direction vector of the object's 'forward' */
    private Vector3f localForwardDirection;
    /** The direction vector of the object's 'upward' */
    private Vector3f localUpwardDirection;
    /** The direction vector of the object's 'rightward' */
    private Vector3f localRightwardDirection;

    /* ---- SIMPLE MATRIX TEMPORARY STORAGE ---- */
    private final TemporaryStorage<Matrix4f> translationMatrixStorage = new TemporaryStorage<Matrix4f>() {
        @Override
        protected Matrix4f getUpdatedElement() {
            return Matrix4f.newTranslationMatrix(Transformation.this.getPosition());
        }
    };
    private final TemporaryStorage<Matrix4f> antiTranslationMatrixStorage = new TemporaryStorage<Matrix4f>() {
        @Override
        protected Matrix4f getUpdatedElement() {
            return Matrix4f.newTranslationMatrix(Transformation.this.getPosition().negate());
        }
    };
    private final TemporaryStorage<Matrix4f> rotationMatrixStorage = new TemporaryStorage<Matrix4f>() {
        @Override
        protected Matrix4f getUpdatedElement() {
            return Transformation.this.getRotation().toRotationMatrix();
        }
    };
    private final TemporaryStorage<Matrix4f> antiRotationMatrixStorage = new TemporaryStorage<Matrix4f>() {
        @Override
        protected Matrix4f getUpdatedElement() {
            return Transformation.this.getRotation().conjugate().toRotationMatrix();
        }
    };
    private final TemporaryStorage<Matrix4f> scaleMatrixStorage = new TemporaryStorage<Matrix4f>() {
        @Override
        protected Matrix4f getUpdatedElement() {
            return Matrix4f.newScaleMatrix(Transformation.this.getScale());
        }
    };

    /* ---- COMPLEX MATRIX TEMPORARY STORAGE ---- */
    private final TemporaryStorage<Matrix4f> modelMatrixStorage = new TemporaryStorage<Matrix4f>() {
        @Override
        protected Matrix4f getUpdatedElement() {
            return Transformation.this.getTranslationMatrix()
                    .mul(Transformation.this.getRotationMatrix())
                    .mul(Transformation.this.getScaleMatrix());
        }
    };
    private final TemporaryStorage<Matrix4f> antiModelMatrixStorage = new TemporaryStorage<Matrix4f>() {
        @Override
        protected Matrix4f getUpdatedElement() {
            return Transformation.this.getModelMatrix().invert();
        }
    };
    private final TemporaryStorage<Matrix4f> viewMatrixStorage = new TemporaryStorage<Matrix4f>() {
        @Override
        protected Matrix4f getUpdatedElement() {
            return Transformation.this.getAntiRotationMatrix()
                    .mul(Transformation.this.getAntiTranslationMatrix());
        }
    };
    private final TemporaryStorage<Matrix4f> antiViewMatrixStorage = new TemporaryStorage<Matrix4f>() {
        @Override
        protected Matrix4f getUpdatedElement() {
            return Transformation.this.getViewMatrix().invert();
        }
    };
    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/

    /**
     * Sets all the instances of complex matrix storage as outdated
     */
    private void setComplexMatrixStorageInstancesOutdated() {
        this.modelMatrixStorage.setOutdated();
        this.viewMatrixStorage.setOutdated();
        this.antiModelMatrixStorage.setOutdated();
        this.antiViewMatrixStorage.setOutdated();
    }

    /**
     * Updates the Local Space Direction vectors
     */
    private void updateLocalSpaceDirections() {
        this.localForwardDirection = Vector3f.FORWARD_VECTOR.rotate(this.getRotation());
        this.localRightwardDirection = Vector3f.RIGHT_VECTOR.rotate(this.getRotation());
        this.localUpwardDirection = Vector3f.UP_VECTOR.rotate(this.getRotation());
    }
    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /* ---- SIMPLE ACCESSOR METHODS ---- */

    /**
     * Gets the position vector of this transformation
     * @return the position vector of this transformation
     */
    public final Vector3f getPosition() {
        return this.position;
    }

    /**
     * Gets the rotation quaternion of this transformation
     * @return the rotation quaternion of this transformation
     */
    public final Quaternion getRotation() {
        return this.rotation;
    }

    /**
     * Gets the scale vector of this transformation
     * @return the scale vector of this transformation
     */
    public final float getScale() {
        return this.scale;
    }


    /* ---- MATRIX ACCESSOR METHODS ---- */

    /**
     * Gets the translation matrix of this transformation
     * @return the translation matrix of this transformation
     */
    public final Matrix4f getTranslationMatrix() {
        return this.translationMatrixStorage.getElement();
    }

    /**
     * Gets the anti translation matrix of this transformation
     * @return the anti translation matrix of this transformation
     */
    public final Matrix4f getAntiTranslationMatrix() {
        return this.antiTranslationMatrixStorage.getElement();
    }

    /**
     * Gets the rotation matrix of this transformation
     * @return the rotation matrix of this transformation
     */
    public final Matrix4f getRotationMatrix() {
        return this.rotationMatrixStorage.getElement();
    }

    /**
     * Gets the anti rotation matrix of this transformation
     * @return the anti rotation matrix of this transformation
     */
    public final Matrix4f getAntiRotationMatrix() {
        return this.antiRotationMatrixStorage.getElement();
    }

    /**
     * Gets the scale matrix of this transformation
     * @return the scale matrix of this transformation
     */
    public final Matrix4f getScaleMatrix() {
        return this.scaleMatrixStorage.getElement();
    }

    /**
     * Gets the model matrix of this transformation
     * @return the model matrix of this transformation
     */
    public final Matrix4f getModelMatrix() {
        return this.modelMatrixStorage.getElement();
    }

    /**
     * Gets the inverse of a model matrix for this transformation
     * @return the inverse of a model matrix of this transformation
     */
    public final Matrix4f getAntiModelMatrix() {
        return this.antiModelMatrixStorage.getElement();
    }

    /**
     * Gets the view matrix of this transformation
     * @return the view matrix of this transformation
     */
    public final Matrix4f getViewMatrix() {
        return this.viewMatrixStorage.getElement();
    }

    /**
     * Gets the inverse of a view matrix of this transformation
     * @return the inverse of a view matrix of this transformation
     */
    public final Matrix4f getAntiViewMatrix() {
        return this.antiViewMatrixStorage.getElement();
    }


    /* ---- DIRECTION ACCESSOR METHODS ---- */

    /**
     * Gets the local forward direction
     * @return the local forward direction vector
     */
    public final Vector3f getLocalForwardDirection() {
        return this.localForwardDirection;
    }

    /**
     * Gets the local upward direction
     * @return the local forward direction vector
     */
    public final Vector3f getLocalUpwardDirection() {
        return this.localUpwardDirection;
    }

    /**
     * Gets the local rightward direction
     * @return the local forward direction vector
     */
    public final Vector3f getLocalRightwardDirection() {
        return this.localRightwardDirection;
    }



    /* ---- SIMPLE MUTATOR METHODS ---- */

    /**
     * Sets the position of the Transformation
     * @param position new transformation's position
     */
    public final void setPosition(final Vector3f position) {
        this.position = position;
        this.translationMatrixStorage.setOutdated();
        this.antiTranslationMatrixStorage.setOutdated();
        this.setComplexMatrixStorageInstancesOutdated();
    }

    /**
     * Sets the rotation of the Transformation
     * @param rotation new transformation's rotation
     */
    public final void setRotation(final Quaternion rotation) {
        this.rotation = rotation.normalize();
        this.rotationMatrixStorage.setOutdated();
        this.antiRotationMatrixStorage.setOutdated();
        this.setComplexMatrixStorageInstancesOutdated();
        this.updateLocalSpaceDirections();
    }

    /**
     * Sets the scale of the Transformation
     * @param scale new transformation's scale
     */
    public final void setScale(float scale) {
        this.scale = scale;
        this.scaleMatrixStorage.setOutdated();
        this.setComplexMatrixStorageInstancesOutdated();
    }

    /**
     * Sets the Transformation's values to default
     */
    public void reset() {
        this.setPosition(Vector3f.ZERO_VECTOR);
        this.setRotation(Quaternion.IDENTITY_QUATERNION);
        this.setScale(1f);
    }


    /* ---- COMPLEX MUTATOR METHODS ---- */

    /**
     * Moves the transformation along the (parent's) axis
     * @param offset the movement vector of this
     */
    public final void moveAxis(final Vector3f offset) {
        this.setPosition(this.getPosition().add(offset));
    }

    /**
     * Moves the object along the (parent's) axis
     * @param offsetX the x-offset of movement
     * @param offsetY the y-offset of movement
     * @param offsetZ the z-offset of movement
     */
    public final void moveAxis(final float offsetX, final float offsetY, final float offsetZ) {
        this.moveAxis(new Vector3f(offsetX, offsetY, offsetZ));
    }

    /**
     * Moves the transformation along the local directions
     * @param right movement right magnitude
     * @param up movement up magnitude
     * @param forward movement forward magnitude
     */
    public final void move(final float right, final float up, final float forward) {
        this.moveAxis(
                this.getLocalForwardDirection().scale(forward)
                        .add(this.getLocalRightwardDirection().scale(right))
                        .add(this.getLocalUpwardDirection().scale(up)));
    }

    /**
     * Moves the transformation along the local directions
     * @param offset the vector offset of the movement
     */
    public final void move(final Vector3f offset) {
        this.move(offset.x(), offset.y(), offset.z());
    }

    /**
     * Rotates the object on some quaternion
     * @param rotation the rotation expressed as quaternion
     */
    public final void rotate(final Quaternion rotation) {
        this.setRotation(this.getRotation().mul(rotation));
    }

    /**
     * Rotates the transformation on a given angle in degrees along a given axis
     * @param angle the angle of rotation
     * @param axis the axis of rotation
     */
    public final void rotate(final float angle, final Vector3f axis) {
        this.setRotation(this.getRotation().mul(Quaternion.rotation(angle, axis)));
    }

    /**
     * Sets the direction of the transformation, changing its rotation accordingly to it
     * @param direction the new direction of the transformation
     */
    public final void setDirection(Vector3f direction) {
        this.setRotation(Quaternion.findRotation(Vector3f.FORWARD_VECTOR, direction));
    }

    /**
     * Scales the transformation
     * @param scalar the float scale of this transformation
     */
    public final void scale(final float scalar) {
        this.setScale(this.getScale() * scalar);
    }


    /* ---- TRANSFORMATION COMBINING ---- */

    /**
     * Combines this transformation with another one.
     * The result will be the same as we have applied this transformation and then applied another one.
     * @param transformation another transformation that is added to this
     * @return returns a combination of this and argument transformation, assuming that this comes first
     */
    public final Transformation combine(final Transformation transformation) {
        Transformation result = new Transformation();

        result.setPosition(this.getModelMatrix().mul(transformation.getPosition(), 1f));
        result.setRotation(this.getRotation());
        result.rotate(transformation.getRotation());

        result.setScale(this.getScale() * transformation.getScale());

        return result;
    }
    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * Default constructor that creates a default transformation
     */
    public Transformation() {
        // this.translationMatrixStorage = new MatrixTemporaryStorage();
        // this.antiTranslationMatrixStorage = new MatrixTemporaryStorage();
        // this.rotationMatrixStorage = new MatrixTemporaryStorage();
        // this.antiRotationMatrixStorage = new MatrixTemporaryStorage();
        // this.scaleMatrixStorage = new MatrixTemporaryStorage();
        // this.modelMatrixStorage = new MatrixTemporaryStorage();
        // this.antiModelMatrixStorage = new MatrixTemporaryStorage();
        // this.viewMatrixStorage = new MatrixTemporaryStorage();
        // this.antiViewMatrixStorage = new MatrixTemporaryStorage();

        this.reset();
    }
    /*--------------------*/
}
