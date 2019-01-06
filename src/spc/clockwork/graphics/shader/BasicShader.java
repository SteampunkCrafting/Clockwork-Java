package spc.clockwork.graphics.shader;

/**
 * A very basic shader that creates white-colored triangles out of the obtained 3d positions array
 * This shader does not apply any projections to the vertices it gets.
 *
 * @author wize
 * @version 0 (2018.04.02)
 */
public final class BasicShader extends ShaderProgram {

    /* ATTRIBUTES
    /*--------------------*/

    private static final String vertexPath = "basic_shader/BasicVertex.glsl";
    private static final String fragmentPath = "basic_shader/BasicFragment.glsl";

    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/

    /**
     * Gives a value to "global" uniforms, which will be applied to whole collection of game objects
     */
    @Override
    public void setGlobalUniforms() {

    }


    /**
     * Creates uniforms
     */
    @Override
    protected void createUniforms() {
    }
    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/
    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * BasicShader constructor
     * @throws Exception case something is wrong with GL
     */
    public BasicShader() throws Exception {
        super(vertexPath, fragmentPath);
    }

    /*--------------------*/
}
