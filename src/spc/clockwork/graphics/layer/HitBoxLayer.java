package spc.clockwork.graphics.layer;

import spc.clockwork.collections.GameWorld;
import spc.clockwork.graphics.RenderSystem;
import spc.clockwork.graphics.shader.BasicPerspectiveShader;
import spc.clockwork.window.Window;

/**
 * A Layer for HitBoxes display
 *
 * @author wize
 * @version 0 (22 July 2018)
 */
public final class HitBoxLayer extends GameLayer {

    /* ATTRIBUTES
    /*--------------------*/

    private float aspectRatio;
    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/

    /**
     * Aspect ratio setter
     * @param aspectRatio the new layer's aspect ratio
     */
    private void setAspectRatio(float aspectRatio) {
        try {
            this.setRenderSystem(new RenderSystem(new BasicPerspectiveShader(aspectRatio)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.aspectRatio = aspectRatio;
    }

    /**
     * Default render system constructor
     * @return a new instance of default render system for the current class of Layer
     */
    @Override
    protected RenderSystem constructDefaultRenderSystem() {
        try {
            return new RenderSystem(new BasicPerspectiveShader(1f));
        } catch (Exception e) {
            return null;
        }
    }
    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * Aspect ratio setter
     * @param window the window, whose aspect ratio will become the aspect ratio of this
     */
    public void setAspectRatio(Window window) {
        this.setAspectRatio(window.getAspectRatio());
    }

    /**
     * Gets the aspect ratio of this
     * @return the aspect ratio of this
     */
    public float getAspectRatio() {
        return this.aspectRatio;
    }
    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * The layer constructor, which fits into the window
     * @param gameWorld the game world of this
     * @param window the window to fit this in
     */
    public HitBoxLayer(GameWorld gameWorld, Window window) {
        this(gameWorld);

        try {
            this.setRenderSystem(new RenderSystem(new BasicPerspectiveShader(window.getAspectRatio())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Base constructor
     * @param gameWorld the game world of this
     */
    private HitBoxLayer(GameWorld gameWorld) {
        super(
                gameWorld,
                GameLayer.DimensionParameter.NORMAL,
                false,
                GameLayer.FaceCullingParameter.NONE
        );
    }
    /*--------------------*/
}
