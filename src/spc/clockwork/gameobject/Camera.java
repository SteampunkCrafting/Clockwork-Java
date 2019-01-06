package spc.clockwork.gameobject;


import spc.clockwork.collections.GameWorld;
import spc.clockwork.util.math.matrix.Matrix4f;

/**
 * Camera is a special kind of {@link GameObject}, which can post the absolute model matrix of itself.
 * From the point of view of this object, the world can be seen.
 *
 * @author wize
 * @version 0 (28 June 2018)
 */
public class Camera extends GameObject {

    /* PRIVATE METHODS
    /*--------------------*/

    /**
     * Prescribes what happens to this object on the termination
     */
    @Override
    protected void onTerminate() {}

    /*--------------------*/


    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * Gets the absolute view matrix of this
     * @return the absolute view matrix of this
     */
    public final Matrix4f getAbsoluteViewMatrix() {
        return this.getAbsoluteTransformation().getViewMatrix();
    }

    /**
     * Sets this camera as the main camera for the layer with the name given
     * @param layerName the name of the layer
     * @throws NullPointerException if the layer with the name given does not exist
     */
    public final void setAsMainCameraForLayer(String layerName) {
        this.getGameWorld().getLayer(layerName).setMainCamera(this);
    }
    /*--------------------*/



    /* CONSTRUCTORS
    /*--------------------*/

    /**
     * A constructor that creates an empty object with random id and default parameters
     *
     * @param gameWorld the game world to attach this object to
     */
    public Camera(GameWorld gameWorld) {
        this(null, gameWorld);
    }

    /**
     * A constructor that creates an empty object with the specified id and default parameters
     *
     * @param id        an object, which is a convertible to long number or a string representation of a hex long number
     * @param gameWorld the game world to attach this object to
     */
    public Camera(Object id, GameWorld gameWorld) {
        super(id, gameWorld);
    }

    /*--------------------*/
}
