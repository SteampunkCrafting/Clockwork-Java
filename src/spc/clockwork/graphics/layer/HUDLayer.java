package spc.clockwork.graphics.layer;

import spc.clockwork.collections.GameWorld;
import spc.clockwork.graphics.RenderSystem;
import spc.clockwork.graphics.shader.HUDShader;
import spc.clockwork.window.Window;


/**
 * HUDLayer is a special kind of layer that is used to project 2d game objects on the screen plane
 *
 * @author wize
 * @version 0 (24 May 2018)
 */
public final class HUDLayer extends GameLayer {
    /* ATTRIBUTES
    /*--------------------*/

    private final float DEFAULT_LEFT = 0;
    private final float DEFAULT_RIGHT = 1024;
    private final float DEFAULT_BOTTOM = 0;
    private final float DEFAULT_TOP = 768;

    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/

    /**
     * Default render system constructor
     * @return a new instance of default render system for the current class of Layer
     */
    @Override
    protected RenderSystem constructDefaultRenderSystem() {
        try {
            return new RenderSystem(new HUDShader(DEFAULT_LEFT,DEFAULT_RIGHT,DEFAULT_TOP,DEFAULT_BOTTOM));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /*--------------------*/


    /* PUBLIC METHODS
    /*--------------------*/
    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * The layer constructor, which fits into the window
     * @param window the window to fit the layer in
     */
    public HUDLayer(GameWorld gameWorld, Window window) {
        this(gameWorld);
        try {
            this.setRenderSystem(new RenderSystem(new HUDShader(window)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The base layer
     * @param gameWorld the game world of this
     */
    private HUDLayer(GameWorld gameWorld) {
        super(gameWorld, DimensionParameter.UNIQUE_IN_DIMENSION, false, FaceCullingParameter.NONE);
    }
    /*--------------------*/
}
