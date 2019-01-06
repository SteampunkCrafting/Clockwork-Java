package spc.clockwork.graphics.shader;


import spc.clockwork.gameobject.entity.Entity;
import spc.clockwork.util.math.matrix.Matrix4f;
import spc.clockwork.window.Window;

/**
 * The shader that renders HUD elements
 * @author wize
 * @version 0 (24 May 2018)
 */
public final class HUDShader extends ShaderProgram {

    /* ATTRIBUTES
    /*--------------------*/

    /** Path to vertex shader */
    private static final String VERTEX_PATH = "hud_shader/HUDVertex.glsl";

    /** Path to fragment shader */
    private static final String FRAGMENT_PATH = "hud_shader/HUDFragment.glsl";

    /** Default z position of a far plane */
    private static final float Z_FAR = -1000f;

    /** Default z position of a near plane */
    private static final float Z_NEAR = 1000f;

    /** This left */
    private final float left;

    /** This right */
    private final float right;

    /** This bottom */
    private final float bottom;

    /** This top */
    private final float top;
    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/

    /**
     * Creates uniforms
     * You have to use the static methods glUniform..() from org.lwjgl.opengl.GL20
     * in order to set the uniforms
     */
    @Override
    protected void createUniforms() {
        try {
            this.createUniform("projectionModelMatrix");
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            this.createMaterialUniform("entityMaterial");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * Gives a value to "global" uniforms, which will be applied to whole collection of game objects
     */
    @Override
    public void setGlobalUniforms() {
    }


    /**
     * Local uniforms setter
     */
    public void setLocalUniforms(Entity entity) {
        this.setUniform(
                "projectionModelMatrix",
                Matrix4f.newOrthographicProjectionMatrix(
                        this.left, this.right, this.bottom, this.top, HUDShader.Z_FAR,  HUDShader.Z_NEAR)
                        //.mul(Matrix4f.newModelMatrix(entity))
                        .mul(entity.getAbsoluteModelMatrix())
        );

        // this.setUniform(
        //         "projectionModelMatrix",
        //         Matrix4f.newModelMatrix(entity)
        //                 .mul(Matrix4f.newOrthographicProjectionMatrix(
        //                         this.left, this.right, this.bottom, this.top, HUDShader.Z_FAR,  HUDShader.Z_NEAR))
        // );

        this.setUniform("entityMaterial",
                entity.getMaterial());
    }
    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * A constructor that creates a shader, which produces an output that fits into a given window
     * @param window the window to getConst the view parameters from
     * @throws Exception throws exception, if is unable to getConst the shader sources
     */
    public HUDShader(Window window) throws Exception {
        this(
                0f,
                window.getWidth(),
                window.getHeight(),
                0f
        );
    }


    /**
     * A default constructor
     * @param left leftmost point of projection
     * @param right rightmost point of projection
     * @param top highest point of projection
     * @param bottom lowest point of projection
     * @throws Exception throws exception, if is unable to getConst the shader sources
     */
    public HUDShader(float left, float right, float top, float bottom) throws Exception {
        super(HUDShader.VERTEX_PATH, HUDShader.FRAGMENT_PATH);
        this.left = left;
        this.top = top;
        this.bottom = bottom;
        this.right = right;
    }
    /*--------------------*/
}
