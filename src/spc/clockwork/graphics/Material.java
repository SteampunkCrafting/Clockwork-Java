package spc.clockwork.graphics;

import spc.clockwork.collections.GameWorld;
import spc.clockwork.core.GameAsset;
import spc.clockwork.graphics.mesh.Mesh;
import spc.clockwork.util.math.vector.Vector3f;

import static org.lwjgl.opengl.GL11.*;

/**
 * A class that describes how the vertices of the Entity Mesh will be colored. Takes light influence
 * into consideration. Used by ShaderPrograms to create fragment colors.
 * @author wize
 * @version 1 (4 June 2018)
 */
public final class Material extends GameAsset {

    /* ATTRIBUTES
    /*--------------------*/

    /** Determines the default material color */
    private static final Vector3f DEFAULT_MATERIAL_COLOR = new Vector3f(1f, 1f, 1f);


    /**
     * An enumeration of polygon modes. This will tell the {@link RenderSystem} how to draw the {@link Mesh}'s polygons
     */
    public static enum PolygonMode {
        POINT,
        LINE,
        FILL
    }

    /* ---- MATERIAL PARAMETERS ---- */

    /** The polygon draw mode of this material */
    private final PolygonMode polygonMode;
    /** The main material color (if not textured) */
    private final Vector3f ambientColor;
    /** The diffuse color of this material (determines, how material reacts on becoming brighter) */
    private final Vector3f diffuseColor;
    /** The specular reflectance color of this material (determines, how this material flares) */
    private final Vector3f specularColor;
    /** The reflectance of the material (materials with high reflectance look like polished or metallic) */
    private final float reflectance;
    /** The specular power of the Material (will be used for specular component computing) */
    private final float specularPower;
    /** The texture of the material. If is set, the parameters above (except reflectance) are not used */
    private final Texture texture;

    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/

    /**
     * An abstract method that is called upon the termination stage of the resource.
     * Is be overridden by the subclass, if the termination requires to do something
     * (e.g. making an off-heap memory cleanup)
     */
    @Override
    protected void onTerminate() {
    }

    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * Sets the polygon mode in the openGL for the vertices to draw according to the parameters of this object.
     */
    public void setupPolygonMode() {
        int mode = 0;
        switch(this.polygonMode) {
            case LINE:
                mode = GL_LINE;
                break;
            case FILL:
                mode = GL_FILL;
                break;
            case POINT:
                mode = GL_POINT;
                break;
        }
        glPolygonMode(GL_FRONT_AND_BACK, mode);
    }
    public Vector3f getAmbientColor() {
        return ambientColor;
    }
    public PolygonMode getPolygonMode() {
        return this.polygonMode;
    }
    public boolean hasTexture() {
        return this.texture != null;
    }
    public Texture getTexture() {
        return texture;
    }

    public Vector3f getDiffuseColor() {
        return diffuseColor;
    }

    public Vector3f getSpecularColor() {
        return specularColor;
    }

    public float getReflectance() {
        return reflectance;
    }

    public float getSpecularPower() {
        return specularPower;
    }

    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * Default constructor of a colored material
     * @param gameWorld the game world of this
     * @param color color of the material
     * @param polygonMode polygon mode of the material
     * @param description description of the material
     */
    public Material(GameWorld gameWorld, Vector3f color, PolygonMode polygonMode, String description) {
        super(gameWorld, null, null, description);
        this.ambientColor = color;
        this.polygonMode = polygonMode;
        this.texture = null;
        this.diffuseColor = color;
        this.specularColor = color;
        this.specularPower = 1f;
        this.reflectance = 1f;
    }


    /**
     * Default constructor of a textured material
     * @param gameWorld the game world of this
     * @param texture the texture of this material
     */
    public Material(GameWorld gameWorld, Texture texture) {
        super(gameWorld, texture.getClassPath(), texture.getFilePath(), texture.getDescription());
        this.ambientColor = DEFAULT_MATERIAL_COLOR;
        this.polygonMode = PolygonMode.FILL;
        this.texture = texture;
        this.diffuseColor = DEFAULT_MATERIAL_COLOR;
        this.specularColor = DEFAULT_MATERIAL_COLOR;
        this.specularPower = 1f;
        this.reflectance = 1f;
    }
    /*--------------------*/
}
