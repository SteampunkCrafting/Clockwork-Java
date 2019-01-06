package spc.clockwork.gameobject.light;


import spc.clockwork.collections.GameWorld;

/**
 * {@link DirectionalLight} is a special kind of {@link Light}, which has no position and has direction inside it.
 *
 * @author wize
 * @version 0 (28 June 2018)
 */
public final class DirectionalLight extends Light {

    /* CLASS CONSTRUCTORS
    /*--------------------*/
    /**
     * Default constructor with zero relative direction, unit intensity and white color
     */
    public DirectionalLight(GameWorld gameWorld) {
        super(gameWorld);
    }
    /*--------------------*/
}
