package spc.clockwork.graphics.layer;


import spc.clockwork.collections.GameWorld;
import spc.clockwork.graphics.RenderSystem;
import spc.clockwork.graphics.shader.PhongShader;
import spc.clockwork.window.Window;

/**
 * 3D Rendering Layer
 * This layer uses 3D projection with Phong Shading to render its objects
 *
 * @author wize
 * @version 0 (25 March 2018)
 */
public final class Layer3D extends GameLayer {

    /* ATTRIBUTES
    /*--------------------*/

    /** The window aspect ratio which is used to properly project things */
    private float aspectRatio;
    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/

    @Override
    protected RenderSystem constructDefaultRenderSystem() {
        return null;
    }

    /**
     * Aspect ratio setter
     * @param aspectRatio the new layer's aspect ratio
     */
    private void setAspectRatio(float aspectRatio) {
        try {
            this.setRenderSystem(new RenderSystem(new PhongShader(aspectRatio)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.aspectRatio = aspectRatio;
    }
    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * Aspect ratio getter
     * @return the layer's aspect ratio
     */
    public float getAspectRatio() {
        return this.aspectRatio;
    }

    /**
     * Aspect ratio setter
     * @param window the window, whose aspect ratio will become the aspect ratio of this
     */
    public void setAspectRatio(Window window) {
        this.setAspectRatio(window.getAspectRatio());
    }
    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * The layer constructor, which fits into the window
     * @param window the window to fit the layer in
     */
    public Layer3D(GameWorld gameWorld, Window window) {
        this(gameWorld);
        this.setAspectRatio(window);
        try {
            this.setRenderSystem(new RenderSystem(new PhongShader(window.getAspectRatio())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Base constructor
     * @see Layer3D::Layer3D(GameWorld, Window)
     */
    private Layer3D(GameWorld gameWorld) {
        super(gameWorld, DimensionParameter.NORMAL, false, FaceCullingParameter.BACK);
    }
    /*--------------------*/
}
