package spc.clockwork.graphics.layer;

import spc.clockwork.collections.GameWorld;
import spc.clockwork.graphics.RenderSystem;
import spc.clockwork.graphics.shader.BasicPerspectiveShader;
import spc.clockwork.graphics.shader.BasicShader;
import spc.clockwork.window.Window;

/**
 * A special kind of {@link GameLayer}, which allows the game developer to place the backgrounds on it.
 * This layer is unique in its dimension (clears depth buffer before and after itself).
 * This layer is clearing the frame before being rendered
 * This layer culls the front faces of the object
 * This layer uses {@link BasicPerspectiveShader} by default
 * Perfect for setting sky boxes up
 *
 * @author wize
 * @version 0 (28 June 2018)
 */
public final class ClearLayer extends  GameLayer {

    /* PRIVATE METHODS
    /*--------------------*/

    /**
     * Default render system constructor
     * @return a new instance of default render system for the current class of Layer
     */
    @Override
    protected RenderSystem constructDefaultRenderSystem() {
        RenderSystem renderSystem = null;
        try {
            renderSystem = new RenderSystem(new BasicShader());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return renderSystem;
    }
    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * Default constructor with {@link spc.clockwork.graphics.shader.BasicPerspectiveShader}
     * @param window a window to fit the projection in
     */
    public ClearLayer(GameWorld gameWorld, Window window) {
        this(gameWorld);
        BasicPerspectiveShader shader = null;
        try {
            shader = new BasicPerspectiveShader(window.getAspectRatio());
            this.setRenderSystem(new RenderSystem(shader));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Default constructor with {@link BasicShader}
     */
    public ClearLayer(GameWorld gameWorld) {
        super(gameWorld, DimensionParameter.UNIQUE_IN_DIMENSION, true, FaceCullingParameter.FRONT);
    }
    /*--------------------*/
}
