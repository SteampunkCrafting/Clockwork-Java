package spc.clockwork.graphics.shader;

import spc.clockwork.gameobject.Camera;
import spc.clockwork.gameobject.entity.Entity;
import spc.clockwork.util.math.matrix.Matrix4f;

/**
 * A basic perspective shader that creates a single-colored objects in a 3d space from the camera perspective.
 * The camera perspective is created by multiplication of a model view matrix by a vector.
 *
 * The "3D space illusion", or perspective is created by multiplication of projection matrix by the
 * multiplication of a model view matrix by a vector.
 *
 * Two matrices are defined by the uniform (mat4).
 * The color of the object is specified by the uniform (vec3).
 *
 * @author wize
 * @version 0 (2018.04.03)
 */
public final class BasicPerspectiveShader extends ShaderProgram {
    /* ATTRIBUTES
    /*--------------------*/

    /* ---- PATHS TO SHADERS ---- */
    private static final String VERTEX_PATH = "basic_perspective_shader/BasicPerspectiveVertex.glsl";
    private static final String FRAGMENT_PATH = "basic_perspective_shader/BasicPerspectiveFragment.glsl";

    /* ---- PROJECTION MATRIX CONSTANTS ---- */
    private static final float FIELD_OF_VIEW = (float) Math.toRadians(60);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000f;
    private final float ASPECT_RATIO;

    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/
    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * Creates uniforms
     * You have to use the static methods glUniform..() from org.lwjgl.opengl.GL20
     * in order to set the uniforms
     */
    @Override
    protected void createUniforms() {
        try {
            this.createUniform("projectionMatrix");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            this.createUniform("modelViewMatrix");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            this.createUniform("meshColor");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            this.createUniform("hasTexture");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            this.createUniform("textureSampler");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Gives a value to "global" uniforms, which will be applied to whole collection of game objects
     */
    @Override
    public void setGlobalUniforms() {
        Matrix4f mat = Matrix4f.newPerspectiveProjectionMatrix(ASPECT_RATIO, FIELD_OF_VIEW, Z_NEAR, Z_FAR);

        this.setUniform(
                "projectionMatrix",
                mat
        );
    }


    /**
     * Sets the model view matrix
     * @param camera camera
     * @param entity entity
     */
    public void setModelViewMatrix(Camera camera, Entity entity) {
        Matrix4f mat = Matrix4f.newModelViewMatrix(camera, entity);
        this.setUniform(
                "modelViewMatrix",
                mat
        );
    }


    /**
     * Sets the meshColor for the entity
     * @param entity
     */
    public void setMeshColor(Entity entity) {
        this.setUniform("meshColor", entity.getMaterial().getAmbientColor());
    }


    /**
     * Sets the hasTexture for the entity
     * @param entity entity
     */
    public void setHasTexture(Entity entity) {
        this.setUniform("hasTexture", entity.hasTexture());
    }


    /**
     * Sets the textureSampler for the entity
     * @param entity entity
     */
    public void setTextureSampler(Entity entity) {
        this.setUniform("textureSampler", false); //todo: understand why the value is always zero
    }

    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    public BasicPerspectiveShader(float aspectRatio) throws Exception {
        super(BasicPerspectiveShader.VERTEX_PATH, BasicPerspectiveShader.FRAGMENT_PATH);
        this.ASPECT_RATIO = aspectRatio;
    }

    /*--------------------*/
}
