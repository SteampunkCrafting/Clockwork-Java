package spc.clockwork.graphics;

import com.sun.istack.internal.NotNull;
import spc.clockwork.gameobject.Camera;
import spc.clockwork.gameobject.entity.Entity;
import spc.clockwork.gameobject.light.Light;
import spc.clockwork.graphics.layer.GameLayer;
import spc.clockwork.graphics.mesh.Mesh;
import spc.clockwork.graphics.shader.*;
import spc.clockwork.util.math.vector.Vector3f;

import java.util.Iterator;

import static org.lwjgl.opengl.GL11.*;

/**
 * A class that is responsible to draw objects by means of using {@link spc.clockwork.graphics.shader.ShaderProgram}
 * {@link RenderSystem} gets a {@link spc.clockwork.gameobject.GameObject} and creates vectors and matrices out of them
 * {@link RenderSystem} is owned by each {@link spc.clockwork.window.Window}.
 *
 * This class is complete, however, if there are brand new kinds of GameObjects, inheritance is possible
 * //TODO: MAKE THE CLASS UNIVERSAL, IF IT IS NOT
 * @author wize
 * @version 0 (2018.03.29)
 */
public class RenderSystem {

    /* ATTRIBUTES
    /*--------------------*/

    /** An error message, if the shader is unknown */
    private final String ERROR_UNKNOWN_SHADER_TYPE =
            "Error: RenderSystem class does not know this shader. " +
                    "You have to modify the renderLayer methods in order to support this shader." +
                    "\nShader type: ";
    /** An error message, if the game world has no camera */
    private final String ERROR_GW_HAS_NO_CAMERA =
            "Error: The GameObjectCollectionOld provided has no Camera and thus cannot be rendered";
    /** An error message, if the Entity has no Material */
    private final String ERROR_ENTITY_HAS_NO_MATERIAL =
            "Error: Entity has no Material at all: ";
    /** An error message, if the Entity has no Mesh */
    private final String ERROR_ENTITY_HAS_NO_MESH =
            "Error: Entity has no Mesh at all: ";


    /** A personal {@link ShaderProgram} of this object */
    private final ShaderProgram shaderProgram;

    /** A clear color of this rendering */
    private Vector3f clearColor;
    /** An alpha chanel of clear color of this rendering */
    private float clearColorAlpha;


    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/

    /**
     * Renders the mesh of the entity after the shader uniforms were set up
     * @param mesh a mesh to render
     */
    private void renderMesh(Mesh mesh) {
        mesh.render();
    }

    /* ----- SHADER PROCESSING ----- */

    /**
     * Processes the entity using {@link BasicShader}.
     * @param entity An entity to getConst mesh
     * @throws ClassCastException the exception will be thrown, if the system's shader is not {@link BasicShader}
     */
    private void processWithBasicShader(Entity entity) throws ClassCastException {
        BasicShader program = (BasicShader) shaderProgram;
        this.renderMesh(entity.getMesh());
    }


    /**
     * Processes the entity using {@link BasicPerspectiveShader}.
     * @param camera A camera to set view matrix
     * @param entity An entity to set model matrix and to getConst mesh and material from
     * @throws ClassCastException will be thrown, if the system's shader is not {@link BasicPerspectiveShader}
     */
    private void processWithBasicPerspectiveShader(Camera camera, Entity entity) throws ClassCastException {
        BasicPerspectiveShader program = (BasicPerspectiveShader) shaderProgram;
        program.setModelViewMatrix(camera, entity);
        program.setMeshColor(entity);
        program.setHasTexture(entity);
        program.setTextureSampler(entity);
        if (entity.hasTexture()) entity.getMaterial().getTexture().bind();
        this.renderMesh(entity.getMesh());
        if (entity.hasTexture()) entity.getMaterial().getTexture().unbind();
    }


    /**
     * Processes the entity using {@link PhongShader}
     * @param camera A camera to set view matrix
     * @param entity An entity to set model matrix and to getConst mesh and material from
     * @param lights An iterator of lights. The lights provided will influence the object's image.
     * @throws ClassCastException will be thrown, if the system's shader is not {@link PhongShader}
     */
    private void processWithPhongShader(Camera camera, Entity entity, Iterator<Light> lights)
            throws ClassCastException {
        PhongShader program = (PhongShader) shaderProgram;
        program.setLocalUniforms(camera, entity, lights);
        if (entity.hasTexture()) entity.getMaterial().getTexture().bind();
        this.renderMesh(entity.getMesh());
        if (entity.hasTexture()) entity.getMaterial().getTexture().unbind();
    }


    /**
     * Processes the entity using {@link HUDShader}
     * @param entity An entity to render
     * @throws ClassCastException will be thrown, if the system's shader is not {@link HUDShader}
     */
    private void processWithHUDShader(Entity entity) throws ClassCastException{
        HUDShader program = (HUDShader) shaderProgram;
        program.setLocalUniforms(entity);
        if (entity.hasTexture()) entity.getMaterial().getTexture().bind();
        this.renderMesh(entity.getMesh());
        if (entity.hasTexture()) entity.getMaterial().getTexture().unbind();
    }

    /* ----- OTHER ----- */

    /**
     * Checks, what kind of shader program this object operates
     * @param c class of the ShaderProgram
     * @return true, if it belongs to the same class as argument, false otherwise
     */
    private boolean shaderProgramIs(Class c) {
        return (this.shaderProgram.getClass().getCanonicalName().equals(c.getCanonicalName()));
    }


    /**
     * Renders the single entity using the shaderProgram, which was given
     * @param entity a single entity to renderLayer
     */
    private void renderEntity(Camera camera, Entity entity, Iterator<Light> lights) {
        /* ---- Checking entity visibility ---- */
        if(entity.getVisible()) {
            /* ---- Checking entity correctness ---- */
            if(entity.hasMaterial() && entity.hasMesh()) {
                /* ---- Choosing render methods and rendering shader ---- */

                /* -- Methods -- */
                entity.getMaterial().setupPolygonMode();

                /* -- Shader -- */
                if (this.shaderProgramIs(BasicShader.class))
                    this.processWithBasicShader(entity);
                else if (this.shaderProgramIs(BasicPerspectiveShader.class))
                    this.processWithBasicPerspectiveShader(camera, entity);
                else if (this.shaderProgramIs(PhongShader.class))
                    this.processWithPhongShader(camera, entity, lights);
                else if (this.shaderProgramIs(HUDShader.class))
                    this.processWithHUDShader(entity);
                else System.err.println(ERROR_UNKNOWN_SHADER_TYPE + this.shaderProgram.getClass().getCanonicalName());
            } else {
                if(!entity.hasMaterial()) System.err.println(ERROR_ENTITY_HAS_NO_MATERIAL + "\n" + entity.toString());
                if(!entity.hasMesh()) System.err.println(ERROR_ENTITY_HAS_NO_MESH + "\n" + entity.toString());
            }
        }
    }

    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * Sets the clear color for the window frames
     * @param red red channel (value range from 0f to 1f)
     * @param green green channel (value range from 0f to 1f)
     * @param blue blue channel (value range from 0f to 1f)
     * @param alpha alpha channel (value range from 0f to 1f)
     */
    public void setClearColor(float red, float green, float blue, float alpha) {
        this.clearColor = new Vector3f(red, green, blue);
        this.clearColorAlpha = alpha;
    }


    /**
     * Sets the clear color for the window frames, making alpha channel 100%
     * @param red red channel (value range from 0f to 1f)
     * @param green green channel (value range from 0f to 1f)
     * @param blue blue channel (value range from 0f to 1f)
     */
    public void setClearColor(float red, float green, float blue) {
        this.setClearColor(red, green, blue, 1f);
    }


    /**
     * Clears the GPU buffers, making the "empty canvas" for the graphic system to draw on.
     * Is called before every frame renderLayer.
     */
    public void clearFrame() {
        glClearColor(this.clearColor.x(), this.clearColor.y(), this.clearColor.z(), this.clearColorAlpha);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }


    /**
     * The method, which is called by {@link spc.clockwork.graphics.layer.GameLayer},
     * gets the {@link GameLayer}, which contains {@link spc.clockwork.gameobject.GameObject}s,
     * and creates an image out of them. This method is called at each frame.
     * @param layer the layer to render
     */
    public void renderLayer(@NotNull final GameLayer layer) {

        /* ---- Setting things up ---- */
        this.shaderProgram.bind();
        this.shaderProgram.setGlobalUniforms();

        /* ---- Rendering Entities ---- */
        Iterator<Entity> entities = layer.entityIterator();
        while (entities.hasNext())
            this.renderEntity(layer.getMainCamera(), entities.next(), layer.lightIterator());

        /* ---- Cleaning up ---- */
        this.shaderProgram.unbind();
    }


    /**
     * Terminates the internal shader program, deleting it from the GPU memory
     */
    public void terminate() {
        this.shaderProgram.terminate();
    }

    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * Default constructor of the RenderSystem
     * @param shaderProgram a ShaderProgram, that is going to be used by this
     */
    public RenderSystem(ShaderProgram shaderProgram) {
        this.shaderProgram = shaderProgram;
        this.setClearColor(0f,0f,0f,1f);
    }

    /*--------------------*/
}
