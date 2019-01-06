package spc.clockwork.gameobject;


import com.sun.istack.internal.NotNull;
import spc.clockwork.collections.GameObjectCollection;
import spc.clockwork.collections.GameWorld;
import spc.clockwork.core.Identifiable;
import spc.clockwork.util.TemporaryStorage;
import spc.clockwork.util.math.Transformation;
import spc.clockwork.util.math.matrix.Matrix4f;
import spc.clockwork.util.math.vector.Quaternion;
import spc.clockwork.util.math.vector.Vector3f;


/**
 * A {@link GameObject} class is a class of {@link Identifiable} objects that represent distinguishable
 * unit objects inside the {@link spc.clockwork.collections.GameWorld}.
 * Every {@link GameObject} has its own local and absolute {@link Transformation}, parent and children set-like-collection.
 * The objects can be visible or invisible.
 * The parent-child relation makes the child to inherit the position and visibility of its parent.
 *
 * @author wize
 * @version 1 (28 June 2018)
 */
public abstract class GameObject extends Identifiable {

    /* ATTRIBUTES
    /*--------------------*/

    /** The object's parent */
    private GameObject parent;

    /* The object's children set */
    private final GameObjectCollection children;



    /** The transformation of this GameObject */
    private final Transformation transformation;

    /** The absolute transformation storage of this GameObject */
    private final TemporaryStorage<Transformation> absoluteTransformationStorage = new TemporaryStorage<Transformation>() {
        @Override
        protected Transformation getUpdatedElement() {
            if(GameObject.this instanceof KernelObject) return new Transformation();
            return GameObject.this.getParentAbsoluteTransformation().combine(GameObject.this.getTransformation());
        }
    };



    /** The 'visible' parameter of this object */
    private boolean visible;
    /** The AND of 'visible' parameters of all the parents of this object */
    private boolean inheritedVisible;

    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/
    /* ---- OVERRIDABLE PROTECTED METHODS ---- */
    /**
     * Computes the inherited visible of this
     * @return the inherited visible of this
     */
    protected boolean getInheritedVisible() {
        return this.getParent().getVisible();
    }

    /**
     * Adds the new child to the set of children
     * @param child the new child
     */
    protected void addChild(GameObject child) {
        this.children.add(child);
    }

    /**
     * Removes the child from the set of children
     * @param child a child to remove
     */
    protected void removeChild(GameObject child) {
        this.children.remove(child);
    }

    /**
     * Prescribes what happens with the {@link GameObject} on its movement
     * This is an overridable method
     */
    protected void onMovement() {
    }

    /**
     * Prescribes what happens to this object on the termination
     */
    @Override
    protected void onTerminate() {
        this.terminateChildren();
        this.detachChildren();
    }

    /* ---- PROTECTED METHODS ---- */
    /**
     * Gets the transformation of this object.
     * This protected method allows subclasses to access and mutate the data of the {@link Transformation} directly
     * in order to construct more complex ways of transforming the game object, than the default {@link GameObject}
     * methods allow
     * @return the transformation of this
     */
    protected final Transformation getTransformation() {
        return this.transformation;
    }

    /**
     * Gets the absolute transformation of this object.
     * This protected method allows subclasses to access and mutate the data of the {@link Transformation} directly
     * in order to construct more complex ways of transforming the game object, than the default {@link GameObject}
     * methods allow
     * @return the transformation of this
     */
    protected final Transformation getAbsoluteTransformation() {
        return this.absoluteTransformationStorage.getElement();
    }

    /**
     * Gets the absolute transformation of the parent
     * @return the absolute transformation of the parent of this
     */
    protected final Transformation getParentAbsoluteTransformation() {
        return this.getParent().getAbsoluteTransformation();
    }

    /**
     * Invokes the terminate() method on all direct children of this (not invoking children of children etc.)
     */
    protected final void terminateChildren() {
        this.children.forEach(GameObject::terminate);
    }

    /**
     * Sets the KernelObject as a parent of all direct children of this (not the children of children)
     */
    protected final void detachChildren() {
        this.children.forEach(child -> child.setParent(null));
    }


    /* ---- OTHER METHODS ---- */

    /**
     * Fetches the inherited visible from parent by means of calling this.getParent().getVisible()
     */
    private void setInheritedVisible() {
        this.inheritedVisible = this.getInheritedVisible();
    }

    /**
     * Recursively traverses this and the children of this, invoking the setAbsoluteTransformation() of this
     * The method updates the absolute transformation for every child of this
     */
    private void onTransformationChange() {
        this.onMovement();
        this.absoluteTransformationStorage.setOutdated();
        this.children.forEach(GameObject::onTransformationChange);
    }

    /**
     * Recursively traverses this and the children of this, invoking the setInheritedVisible() of this
     * The method updates the visibility for every child of this
     */
    private void onVisibleChange() {
        this.setInheritedVisible();
        this.children.forEach(GameObject::onVisibleChange);
    }

    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /* ---- ACCESSOR METHODS ---- */
    /* -- PARENT -- */
    /**
     * Gets parent of this
     * @return a parent of this
     */
    public final GameObject getParent() {
        return this.parent;
    }



    /* -- LOCAL TRANSFORMATION -- */
    /**
     * Gets the local position of this
     * @return the vector position in a parent's space
     */
    public final Vector3f getPosition() {
        return this.getTransformation().getPosition();
    }

    /**
     * Gets the local rotation of this
     * @return the quaternion rotation in a parent's space
     */
    public final Quaternion getRotation() {
        return this.getTransformation().getRotation();
    }

    /**
     * Gets the local scale of this
     * @return the vector scale in a parent's space
     */
    public final float getScale() {
        return this.getTransformation().getScale();
    }

    /**
     * Gets the local forward direction of this
     * @return the vector forward in a parent's space
     */
    public final Vector3f getDirection() {
        return this.getTransformation().getLocalForwardDirection();
    }



    /* -- GLOBAL TRANSFORMATION -- */

    /**
     * Gets the absolute model matrix of this
     * @return the absolute model matrix of this
     */
    public final Matrix4f getAbsoluteModelMatrix() {
        return this.getAbsoluteTransformation().getModelMatrix();
    }

    /**
     * Gets the absolute position of this
     * @return an absolute position of this
     */
    public final Vector3f getAbsolutePosition() {
        return this.getAbsoluteTransformation().getPosition();
    }

    /**
     * Gets the absolute rotation of this
     * @return an absolute rotation of this
     */
    public final Quaternion getAbsoluteRotation() {
        return this.getAbsoluteTransformation().getRotation();
    }

    /**
     * Gets the absolute scale of this
     * @return an absolute scale of this
     */
    public final float getAbsoluteScale() {
        return this.getAbsoluteTransformation().getScale();
    }

    /**
     * Gets the absolute forward direction of this
     * @return an absolute forward direction of this
     */
    public final Vector3f getAbsoluteDirection() {
        return this.getAbsoluteTransformation().getLocalForwardDirection();
    }



    /* -- VISIBILITY -- */
    /**
     * Gets the visibility of this object
     * @return true, if the object (and also all its parents) is visible, else false
     */
    public final boolean getVisible() {
        return this.visible && this.inheritedVisible;
    }



    /* ---- MUTATOR METHODS ---- */
    /* -- PARENT -- */
    /**
     * Sets the parent for this
     * @param parent the new parent of this
     */
    public final void setParent(GameObject parent) {
        if(this instanceof KernelObject) return;
        if(parent == null) { this.setParent(KernelObject.get()); return; }
        if(parent.equals(this.getParent())) return;

        if(this.getParent() != null) this.getParent().removeChild(this);
        this.parent = parent;
        this.parent.addChild(this);
        this.onTransformationChange();
        this.onVisibleChange();
    }



    /* -- LOCAL TRANSFORMATION -- */
    /**
     * Sets the position of the GameObject
     * @param position new object's position
     */
    public final void setPosition(Vector3f position) {
        this.transformation.setPosition(position);
        this.onTransformationChange();
    }

    /**
     * Moves an object in the local space in the local directions
     * @param right right magnitude of movement
     * @param up up magnitude of movement
     * @param forward forward magnitude of movements
     */
    public final void move(float right, float up, float forward) {
        this.transformation.move(right, up, forward);
        this.onTransformationChange();
    }

    /**
     * Moves an object in the local space in the local directions
     * @param offset the movement offset
     */
    public final void move(@NotNull final Vector3f offset) {
        this.transformation.move(offset);
        this.onTransformationChange();
    }

    /**
     * Rotates the object on some angle in degrees over some vector axis
     * @param angle the angle of rotation
     * @param axis the axis of rotation
     */
    public final void rotate(float angle, Vector3f axis) {
        this.transformation.rotate(angle, axis);
        this.onTransformationChange();
    }

    /**
     * Sets the direction of the object, rotating it accordingly to it
     * @param direction new direction of the object
     */
    public final void setDirection(Vector3f direction) {
        this.getTransformation().setDirection(direction);
        this.onTransformationChange();
    }

    /**
     * Sets the scale of the object
     * @param scale the scalar float of this
     */
    public final void setScale(float scale) {
        this.getTransformation().setScale(scale);
    }


    /* -- GAME WORLD -- */
    /**
     * Adds this object to the layer, whose name is given
     * @param layerName the name of the layer
     */
    public final void addToLayer(String layerName) {
        this.getGameWorld().addToLayer(this, layerName);
    }

    /**
     * Adds this object and every object that is currently its child (or the child of a child, etc.) to
     * the layer given.
     * @param layerName the name of the layer
     */
    public final void addToLayerRecursively(String layerName) {
        this.addToLayer(layerName);
        this.children.forEach(child -> child.addToLayerRecursively(layerName));
    }

    /**
     * Adds this object to the layer, whose name is given
     * @param layerName the name of the layer
     */
    public final void removeFromLayer(String layerName) {
        this.getGameWorld().removeFromLayer(this, layerName);
    }

    /**
     * Adds this object and every object that is currently its child (or the child of a child, etc.) to
     * the layer given.
     * @param layerName the name of the layer
     */
    public final void removeFromLayerRecursively(String layerName) {
        this.removeFromLayer(layerName);
        this.children.forEach(child -> child.removeFromLayerRecursively(layerName));
    }


    /* -- GLOBAL TRANSFORMATION -- */

    /**
     * Sets the direction towards the {@link GameObject} given and rotating the object accordingly to it
     * @param gameObject the game object to 'stare at'
     */
    public final void setAbsoluteDirection(GameObject gameObject) {
        this.setAbsoluteDirection(this.getAbsolutePosition().negate().add(gameObject.getAbsolutePosition()).normalize());
    }

    /**
     * Sets the absolute direction for this object
     * @param absoluteDirection the absolute vector of the object's direction
     */
    public final void setAbsoluteDirection(Vector3f absoluteDirection) {
        this.setDirection(this.getParentAbsoluteTransformation().getAntiModelMatrix().mul(absoluteDirection, 0f));
    }

    /**
     * Sets the absolute position for this object
     * @param absolutePosition the object's absolute position
     */
    public final void setAbsolutePosition(Vector3f absolutePosition) {
        this.setPosition(this.getParentAbsoluteTransformation().getAntiModelMatrix().mul(absolutePosition, 1f));
    }

    /**
     * Sets the absolute scale for this object
     * @param absoluteScale the object's absolute scale
     */
    public final void setAbsoluteScale(float absoluteScale) {
        this.setScale(absoluteScale / this.getParentAbsoluteTransformation().getScale());
    }



    /* -- VISIBILITY -- */
    /**
     * Sets the visible parameter of the object and inherited visible parameter of all of its children
     * @param visible the new value of visible
     */
    public final void setVisible(boolean visible) {
        this.visible = visible;
        this.onVisibleChange();
    }

    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * A constructor that creates an empty object with the specified id and default parameters
     * @param id an object, which is a convertible to long number or a string representation of a hex long number
     * @param gameWorld the game world to attach this object to
     */
    public GameObject(final Object id, @NotNull final GameWorld gameWorld) {
        /* SETTING UP THE GAME OBJECT */
        super(id, gameWorld);
        this.children = new GameObjectCollection();
        this.transformation = new Transformation();
        this.setParent(KernelObject.get());
        this.setVisible(true);
    }
    /*--------------------*/
}