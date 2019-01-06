package spc.clockwork.graphics.layer;


import spc.clockwork.collections.GameObjectCollection;
import spc.clockwork.collections.GameWorld;
import spc.clockwork.core.Identifiable;
import spc.clockwork.gameobject.Camera;
import spc.clockwork.gameobject.GameObject;
import spc.clockwork.gameobject.entity.Entity;
import spc.clockwork.gameobject.light.Light;
import spc.clockwork.graphics.RenderSystem;
import spc.clockwork.util.math.vector.Vector3f;

import java.util.Iterator;

import static org.lwjgl.opengl.GL11.*;


/**
 * {@link GameLayer} is a helping class that renders a single layer of a frame.
 *
 * Each {@link GameLayer} holds a {@link GameObjectCollection}, which stores all the
 * {@link spc.clockwork.gameobject.GameObject}s to render, and {@link RenderSystem},
 * which describes how the GameObjectCollectionOld should be rendered.
 *
 * The {@link GameLayer} is abstract.
 *
 * @author wize
 * @version 0 (16 May 2018)
 */
public abstract class GameLayer extends Identifiable {

    /* ATTRIBUTES
    /*--------------------*/

    /**
     * Describes the type of a dimension parameter
     * Layers are rendered dimension by dimension:
     *
     * NORMAL does not clear depth buffer
     * FIRST_IN_DIMENSION clears depth buffer before being rendered
     * LAST_IN_DIMENSION clears depth buffer after being rendered
     * UNIQUE_IN_DIMENSION clears depth buffer before and after being rendered
     */
    protected enum DimensionParameter {
        NORMAL,
        FIRST_IN_DIMENSION,
        LAST_IN_DIMENSION,
        UNIQUE_IN_DIMENSION
    }


    /**
     * Describes which kind of face culling this layer provides
     * If culling is enabled, some faces on this layer are not drawn
     * This is used in order to optimize rendering or for creating visual effects
     *
     * NONE does not make any face culling
     * FRONT culls only front faces
     * BACK culls back faces
     * FRONT_AND_BACK culls all faces
     */
    protected enum FaceCullingParameter {
        NONE,
        FRONT,
        BACK,
        FRONT_AND_BACK
    }




    /** An error message, if renderSystem is null */
    private static final String ERROR_RENDER_SYSTEM_IS_NULL =
            "Error: no renderLayer system specified, use \"setRenderSystem\" method to fix.";
    /** An error message, if we add the GameObject, which is not supported */
    private static final String ERROR_UNKNOWN_GAME_OBJECT_TYPE =
            "Error: the GameObject provided for this Layer cannot be used";


    /** The GameObjectCollectionOld instance that is rendered in this layer */
    private final GameObjectCollection gameObjectCollection;
    /** The RenderSystem, which renders the GameObjectCollection */
    private RenderSystem renderSystem;

    /** Switcher of the shown/hidden state of the layer */
    private boolean visible;

    /** The {@link DimensionParameter} of this layer */
    private final DimensionParameter dimensionParameter;

    /** If set to true, clears the frame before rendering*/
    private final boolean clearsFrameBeforeRender;

    /** The {@link FaceCullingParameter} of this layer */
    private final FaceCullingParameter faceCullingParameter;

    /** The main {@link Camera} of this layer. From this object the {@link GameLayer} is seen */
    private Camera mainCamera;
    
    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/

    /**
     * Default render system constructor
     * @return a new instance of default render system for the current class of Layer
     */
    protected abstract RenderSystem constructDefaultRenderSystem();


    /**
     * Getter of the render system for the subclasses
     * @return current render system
     */
    protected final RenderSystem getRenderSystem() {
        return this.renderSystem;
    }


    /**
     * Sets the face culling for this layer up
     */
    private void setupCulling() {
        if (this.faceCullingParameter == FaceCullingParameter.NONE) {
            glDisable(GL_CULL_FACE);
            return;
        }

        glEnable(GL_CULL_FACE);
        switch(this.faceCullingParameter) {
            case FRONT:
                glCullFace(GL_FRONT);
                break;
            case BACK:
                glCullFace(GL_BACK);
                break;
            case FRONT_AND_BACK:
                glCullFace(GL_FRONT_AND_BACK);
                break;
            case NONE:
                throw new IllegalStateException();
        }
    }


    /**
     * Prescribes what happens to this object on the termination
     */
    @Override
    protected void onTerminate() {

    }


    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * Checks, if this layer clears frame before rendering
     * @return true, if this layer clears frame before rendering, else false
     */
    public final boolean clearsFrameBeforeRender() {
        return this.clearsFrameBeforeRender;
    }


    /**
     * Checks if this has a rendering system
     * @return true, if it has a rendering system, else returns false
     */
    public final boolean hasRenderSystem() {
        return this.renderSystem != null;
    }


    /**
     * A method that inits the rendering of the layer
     */
    public final void render() {
        /* ---- CHECKING, IF THE LAYER IS NOT HIDDEN ---- */
        if (!this.getVisible()) return;

        /* ---- SETTING FACE CULLING UP ---- */
        this.setupCulling();

        /* ---- CHECKING IF THE RENDERING SYSTEM EXISTS ---- */
        if (renderSystem == null) {
            System.err.println(ERROR_RENDER_SYSTEM_IS_NULL + "\nLayer: " + this.toString());
            return;
        }

        /* ---- CHECKING IF THE LAYER MUST CLEAR COLOR BEFORE RENDER ---- */
        if (this.clearsFrameBeforeRender()) this.getRenderSystem().clearFrame();

        /* ---- CHECKING, IF THE LAYER MUST CLEAR DEPTH BEFORE RENDER ---- */
        if (this.dimensionParameter == DimensionParameter.FIRST_IN_DIMENSION
                || this.dimensionParameter == DimensionParameter.UNIQUE_IN_DIMENSION) glClear(GL_DEPTH_BUFFER_BIT);


        /* ---- RENDERING LAYER ---- */
        this.renderSystem.renderLayer(this);

        /* ---- CHECKING, IF THE LAYER MUST CLEAR DEPTH AFTER RENDER ---- */
        if (this.dimensionParameter == DimensionParameter.LAST_IN_DIMENSION
                || this.dimensionParameter == DimensionParameter.UNIQUE_IN_DIMENSION) glClear(GL_DEPTH_BUFFER_BIT);
    }


    /**
     * Render system setter
     * @param renderSystem new render system of this layer
     */
    public final void setRenderSystem(RenderSystem renderSystem) {
        if (this.hasRenderSystem()) this.renderSystem.terminate();
        this.renderSystem = renderSystem;
    }


    /**
     * Clear color setter
     * @param clearColorRGB RGB channel
     * @param alpha alpha channel
     */
    public final void setClearColor(Vector3f clearColorRGB, float alpha) {
        this.setClearColor(clearColorRGB.x(), clearColorRGB.y(), clearColorRGB.z(), alpha);
    }


    /**
     * Clear color setter
     * @param red red channel
     * @param green green channel
     * @param blue blue channel
     * @param alpha alpha channel
     */
    public final void setClearColor(float red, float green, float blue, float alpha) {
        this.renderSystem.setClearColor(red, green, blue, alpha);
    }


    /**
     * The shown/hidden state getter
     * @return true, if the layer is shown, false, if the layer is hidden
     */
    public final boolean getVisible() {
        return this.visible;
    }


    /**
     * visible state of the layer setter
     * @param visible new value of the field
     */
    public final void setVisible(boolean visible) {
        this.visible = visible;
    }


    /**
     * Adds a GameObject to this Layer
     * @param name a name of the {@link GameObject} as {@link String}
     * @param gameObject an instance of a {@link GameObject}
     */
    public final void add(String name, GameObject gameObject) {
        this.gameObjectCollection.add(name, gameObject);
    }


    /**
     * Removes a {@link GameObject} from the {@link GameLayer} by its name
     * @param name a name of the game object
     */
    public final void remove(String name) {
        this.gameObjectCollection.remove(name);
    }


    /**
     * Removes a {@link GameObject} from the {@link GameLayer} by its value
     * @param gameObject an instance of a game object
     */
    public final void remove(GameObject gameObject) {
        this.gameObjectCollection.remove(gameObject);
    }


    /**
     * Sets the camera to the {@link GameLayer} by its name
     * The set of cameras is stored within the local Layer's {@link GameObjectCollection}
     * @param name a name of the {@link Camera} in the context of local {@link GameObjectCollection}
     * @throws ClassCastException if the name does not belong to the {@link Camera} instance
     */
    public final void setMainCamera(String name) {
        this.mainCamera = (Camera) this.gameObjectCollection.getElement(name);
    }

    /**
     * Sets the camera
     * @param camera new camera of the layer
     */
    public final void setMainCamera(Camera camera) {
        if(this.getMainCamera() != null)
            this.remove(this.getMainCamera());
        camera.addToLayer(this.getName());
        this.mainCamera = camera;
    }


    /**
     * Creates and returns a new instance of {@link Iterator} of all instances of {@link Entity}
     * that belong to this layer
     * @return a new iterator of the layer's entities
     */
    public final Iterator<Entity> entityIterator() {
        return this.gameObjectCollection.entityIterator();
    }


    /**
     * Creates and returns a new instance of {@link Iterator} of all instances of
     * {@link spc.clockwork.gameobject.light.Light} that belong to this layer
     * @return a new iterator of the layer's lights
     */
    public final Iterator<Light> lightIterator() {
        return this.gameObjectCollection.lightIterator();
    }


    /**
     * Main {@link Camera} getter
     * @return the main camera of this layer
     */
    public final Camera getMainCamera() {
        return this.mainCamera;
    }

    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * Default class constructor
     * Creates a normal background layer
     */
    public GameLayer(GameWorld gameWorld) {
        this(gameWorld, DimensionParameter.NORMAL, true, FaceCullingParameter.BACK);
    }

    /**
     * Default constructor that specifies the layer type
     * @param dimensionParameter the dimension parameter of this layer
     * @param clearsFrameBeforeRender specifies, if this layer is a 'background' layer
     */
    public GameLayer(GameWorld gameWorld,
                     DimensionParameter dimensionParameter,
                     boolean clearsFrameBeforeRender,
                     FaceCullingParameter faceCullingParameter) {
        super(null, gameWorld);
        this.dimensionParameter = dimensionParameter;
        this.faceCullingParameter = faceCullingParameter;
        this.clearsFrameBeforeRender = clearsFrameBeforeRender;
        this.gameObjectCollection = new GameObjectCollection();
        this.renderSystem = constructDefaultRenderSystem();
        this.visible = true;
    }
    /*--------------------*/


}
