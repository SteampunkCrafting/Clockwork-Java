package spc.clockwork.graphics.shader;

import org.lwjgl.opengl.GL20;
import spc.clockwork.gameobject.Camera;
import spc.clockwork.gameobject.light.DirectionalLight;
import spc.clockwork.gameobject.light.PointLight;
import spc.clockwork.gameobject.light.SpotLight;
import spc.clockwork.graphics.Material;
import spc.clockwork.util.math.matrix.Matrix4f;
import spc.clockwork.util.math.vector.Vector3f;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;


/**
 * An abstract class that works with shader programs
 * It is responsible to create memory cells in the GPU and then
 * compile and initiate the on-GPU execution of the shader, written in GLSL.
 *
 * For any new shader we create a new subclass.
 * The {@link ShaderProgram} must be created and within one line;
 * @author wize
 * @version 1 (4 June 2018)
 */
public abstract class ShaderProgram {

    /* ATTRIBUTES
    /*--------------------*/

    /** Default path to shader sources */
    private static final String DEFAULT_PATH_TO_SHADER_SOURCES =
            "/spc/clockwork/graphics/shader_sources/";

    /* -- MESSAGES -- */
    /** An error message text, if we could not create ShaderProgram in GL */
    private static final String ERROR_COULD_NOT_CREATE_PROGRAM =
            "Error: could not create shader program. ";
    /** An error message text, if we could not create Shader in GL */
    private static final String ERROR_COULD_NOT_CREATE_SHADER =
            "Error: could not create shader of type ";
    /** An error message text, if we could not create Shader in GL */
    private static final String ERROR_COULD_NOT_COMPILE_SHADER =
            "Error: could not compile shader of type ";
    /** An error message text, if we could not create Shader in GL */
    private static final String ERROR_COULD_NOT_CREATE_UNIFORM =
            "Error: could not create uniform. Most likely, it is not used in shader. Uniform type: ";
    /** An error message text, if we could not create Shader in GL */
    private static final String ERROR_COULD_NOT_LINK_SHADER_PROGRAM =
            "Error: could not link shader program. \n";
    /** An error message text, if we could not validate Shader in GL */
    private static final String WARNING_COULD_NOT_VALIDATE_SHADER =
            "Warning: could not validate shader. ";
    // /** An error message text, if we failed to create a uniform for Shader */
    // protected final String ERROR_FAILED_TO_CREATE_UNIFORM_TYPE =
    //         "Error: Failed to create uniform of type ";


    /* -- FIELDS -- */
    /** A pointer to instance of this ShaderProgram in the GL system */
    protected final int id;
    /** A pointer to the vertex shader of this ShaderProgram in the GL system */
    protected final int vertexShaderID;
    /** A pointer to the fragment shader of this ShaderProgram in the GL system */
    protected final int fragmentShaderID;
    /** A {@link java.util.Map} of uniforms -- public variables that we use to insert data into GPU */
    protected Map<String, Integer> uniformMap;

    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/

    /**
     * Creates uniforms
     * You have to use the static methods glUniform..() from org.lwjgl.opengl.GL20
     * in order to set the uniforms
     */
    protected abstract void createUniforms();


    /**
     * Loads shader from file, compiles it into object (AS C/C++ DOES) and returns the id of the new shader
     * @param path a path of the shader source file (FROM THE POSITION OF {@link ShaderProgram} CLASS)
     * @param type a type of shader (GL ENUM)
     */
    private int buildShader(String path, int type) throws Exception {
        /* ---- GETTING SOURCE CODE FROM SHADER FILE ---- */
        BufferedReader sourceReader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream(DEFAULT_PATH_TO_SHADER_SOURCES + path)));

        /* ---- READING SOURCE FROM SHADER FILE ---- */
        StringBuilder sourceCode = new StringBuilder();
        String line;
        while (( line = sourceReader.readLine()) != null)
            sourceCode.append(line + "\n");

        /* ---- CREATING EMPTY SHADER ---- */
        int shaderID = glCreateShader(type);
        if (shaderID == GL_FALSE)
            throw new Exception(ERROR_COULD_NOT_CREATE_SHADER + type);

        /* ---- COMPILING SHADER FROM SOURCE AND ATTACHING IT TO PROGRAM ---- */
        glShaderSource(shaderID, sourceCode);
        glCompileShader(shaderID);
        if(glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new Exception(ERROR_COULD_NOT_COMPILE_SHADER + type + "\n" + glGetShaderInfoLog(shaderID, 1024));
        }
        glAttachShader(this.id, shaderID);
        return shaderID;
    }


    /**
     * Creates an empty uniform and attaches it to this shader program
     * @param name name of the uniform
     */
    protected final void createUniform(String name) throws Exception {
        int pointerToUniform = glGetUniformLocation(this.id, name);
        if (pointerToUniform < 0)
            throw new Exception(ERROR_COULD_NOT_CREATE_UNIFORM + name);
        this.uniformMap.put(name, pointerToUniform);
    }


    /**
     * Creates an empty complex uniform to store {@link spc.clockwork.graphics.Material}
     * @param name a uniform name
     * @throws Exception if a uniform creation was failed
     */
    protected final void createMaterialUniform(String name) throws Exception {
        createUniform(name + ".ambientColor");
        createUniform(name + ".diffuseColor");
        createUniform(name + ".specularColor");
        createUniform(name + ".specularPower");
        createUniform(name + ".reflectance");
        createUniform(name + ".textureSampler");
        createUniform(name + ".hasTexture");
    }


    /**
     * Creates an empty super complex uniform to store a {@link PointLight} array
     * @param name a uniform name
     * @param arraySize a maximum amount of lights that was declared in the shader
     * @throws Exception if a uniform creation was failed
     */
    protected final void createPointLightUniformsArray(String name, int arraySize) throws Exception {
        for (int i = 0; i < arraySize; i++) {
            createUniform(name + "[" + i + "].position");
            createUniform(name + "[" + i + "].color");
            createUniform(name + "[" + i + "].intensity");
            createUniform(name + "[" + i + "].attenuation.constant");
            createUniform(name + "[" + i + "].attenuation.linear");
            createUniform(name + "[" + i + "].attenuation.exponent");
        }
    }


    /**
     * Creates an empty super complex uniform to store a {@link DirectionalLight}
     * @param name a uniform name
     * @param arraySize a maximum amount of lights that was declared in the shader
     * @throws Exception if a uniform creation was failed
     */
    protected final void createDirLightUniformsArray(String name, int arraySize) throws Exception {
        for (int i = 0; i < arraySize; i++) {
            createUniform(name + "[" + i + "].color");
            createUniform(name + "[" + i + "].intensity");
            createUniform(name + "[" + i + "].direction");
        }
    }


    /**
     * Creates an empty super complex uniform to store a {@link SpotLight} array
     * @param name a uniform name
     * @param arraySize a maximum amount of lights that was declared in the shader
     * @throws Exception if a uniform creation was failed
     */
    protected final void createSpotLightUniformArray(String name, int arraySize) throws Exception {
        for (int i = 0; i < arraySize; i++) {
            createUniform(name + "[" + i + "].position");
            createUniform(name + "[" + i + "].coneDirection");
            createUniform(name + "[" + i + "].cosOfConeAngle");
            createUniform(name + "[" + i + "].color");
            createUniform(name + "[" + i + "].intensity");
            createUniform(name + "[" + i + "].attenuation.constant");
            createUniform(name + "[" + i + "].attenuation.linear");
            createUniform(name + "[" + i + "].attenuation.exponent");
        }
    }

    /**
     * Sets the 3d float vector uniform
     * @param name uniform name
     * @param value uniform value
     */
    protected final void setUniform(String name, Vector3f value) {
        glUniform3f(
                this.uniformMap.get(name),
                value.x(),
                value.y(),
                value.z()
        );
    }


    /**
     * Sets the 4*4 float matrix uniform
     * @param name uniform name
     * @param value uniform value
     */
    protected void setUniform(String name, Matrix4f value) {
        glUniformMatrix4fv(
                this.uniformMap.get(name),
                false,
                value.dumpColumnMajor()
        );
    }


    /**
     * Sets the boolean uniform (the function casts false into 0 and true into 1).
     * ATTENTION: In the shader part the zero and one are casted to false and true back.
     * @param name name of the uniform
     * @param value value of the uniform
     */
    protected void setUniform(String name, boolean value) {
        glUniform1i(this.uniformMap.get(name), value ? 1 : 0);
    }


    /**
     * Sets a float uniform
     * @param name name of the uniform
     * @param value value of the uniform
     */
    protected void setUniform(String name, float value) {
        glUniform1f(this.uniformMap.get(name), value);
    }


    /**
     * Sets a complex {@link Material} uniform
     * @param name uniform name
     * @param value value of the uniform
     */
    protected void setUniform(String name, Material value) {
       setUniform(name + ".ambientColor", value.getAmbientColor());
       setUniform(name + ".diffuseColor", value.getDiffuseColor());
       setUniform(name + ".specularColor", value.getSpecularColor());
       setUniform(name + ".specularPower", value.getSpecularPower());
       setUniform(name + ".reflectance", value.getReflectance());
       //todo: the system does not support several textures, fix this
       setUniform(name + ".textureSampler", false);
       setUniform(name + ".hasTexture", value.hasTexture());
    }


    /**
     * Sets a complex {@link DirectionalLight} uniform
     * WARNING: THE DIRECTION OF THE LIGHT IS PASSED IN VIEW ROTATION
     * @param name uniform name
     * @param camera camera
     * @param light value of the uniform
     */
    protected void setUniform(String name, Camera camera, DirectionalLight light) {
        setUniform(name + ".direction", camera.getAbsoluteViewMatrix().mul(light.getAbsoluteDirection(), 0f));
        setUniform(name + ".color", light.getColor());
        setUniform(name + ".intensity", light.getIntensity());
    }


    /**
     * Sets a complex {@link PointLight} uniform
     * WARNING: THE POSITION OF THE LIGHT IS PASSED IN VIEW COORDINATES
     * @param name name of the uniform
     * @param camera camera
     * @param light the light
     */
    protected void setUniform(String name, Camera camera, PointLight light) {
        setUniform(name + ".position", camera.getAbsoluteViewMatrix().mul(light.getAbsolutePosition(), 1f));
        setUniform(name + ".color", light.getColor());
        setUniform(name + ".intensity", light.getIntensity());
        setUniform(name + ".attenuation.constant", light.getConstAttenuation());
        setUniform(name + ".attenuation.linear", light.getLinAttenuation());
        setUniform(name + ".attenuation.exponent", light.getExptAttenuation());
    }


    /**
     * Sets a complex {@link SpotLight} uniform
     * WARNING: THE POSITION OF THE LIGHT IS PASSED IN VIEW COORDINATES
     * WARNING: THE DIRECTION OF THE LIGHT IS PASSED IN VIEW COORDINATES
     * @param name name of the uniform
     * @param camera camera
     * @param light the light
     */
    protected void setUniform(String name, Camera camera, SpotLight light) {
        setUniform(name + ".position", camera.getAbsoluteViewMatrix().mul(light.getAbsolutePosition(),1f));
        setUniform(name + ".coneDirection", camera.getAbsoluteViewMatrix().mul(light.getAbsoluteDirection(), 0f));
        setUniform(name + ".cosOfConeAngle", (float)Math.cos(Math.toRadians(light.getConeAngle())));
        setUniform(name + ".color", light.getColor());
        setUniform(name + ".intensity", light.getIntensity());
        setUniform(name + ".attenuation.constant", light.getConstAttenuation());
        setUniform(name + ".attenuation.linear", light.getLinAttenuation());
        setUniform(name + ".attenuation.exponent", light.getExptAttenuation());
    }


    /**
     * Sets a super complex {@link PointLight} array uniform
     * @param name uniform name
     * @param camera camera
     * @param lights values of the uniform
     */
    protected void setUniform(String name, Camera camera, PointLight[] lights) {
        for (int i = 0; i < lights.length && lights[i] != null; i++) {
            setUniform(name + "[" + i + "]", camera, lights[i]);
        }
    }


    /**
     * Sets a super complex {@link DirectionalLight} array uniform
     * @param name uniform name
     * @param camera camera
     * @param lights values of the uniform
     */
    protected void setUniform(String name, Camera camera, DirectionalLight[] lights) {
        for (int i = 0; i < lights.length && lights[i] != null; i++) {
            setUniform(name + "[" + i + "]", camera, lights[i]);
        }
    }


    /**
     * Sets a super complex {@link SpotLight} array uniform
     * @param name uniform name
     * @param camera camera
     * @param lights values of the uniform
     */
    protected void setUniform(String name, Camera camera, SpotLight[] lights) {
        for (int i = 0; i < lights.length && lights[i] != null; i++) {
            setUniform(name + "[" + i + "]", camera, lights[i]);
        }
    }

    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * Links the two object shaders of this program together, making them the GPU executable (AS IN C/C++)
     * This is done before the drawing the frame
     */
    public void link() throws Exception {
        /* ---- LINKING PROGRAM ---- */
        glLinkProgram(this.id);
        if (glGetProgrami(this.id, GL_LINK_STATUS) == GL_FALSE)
            throw new Exception(ERROR_COULD_NOT_LINK_SHADER_PROGRAM + glGetProgramInfoLog(this.id));
        glDetachShader(this.id, vertexShaderID);
        glDetachShader(this.id, fragmentShaderID);
    }

    /**
     * Binds program to GPU pipeline
     * This is done before the single renderLayer
     */
    public void bind() {
        glUseProgram(this.id);
    }


    /**
     * Unbinds program from GPU pipeline
     * This is done after the single renderLayer
     */
    public void unbind() {
        glUseProgram(GL_FALSE);
    }


    /**
     * Destroys the shader program
     * This is done after renderer is not needed anymore
     */
    public void terminate() {
        this.unbind();
        if (this.id != GL_FALSE)
            glDeleteProgram(this.id);

    }


    /**
     * Validates the ShaderProgram and prints the warning, if the validation was failed
     */
    public void validate() {
        glValidateProgram(this.id);
        if (glGetProgrami(this.id, GL_VALIDATE_STATUS) == GL_FALSE)
            System.err.println(WARNING_COULD_NOT_VALIDATE_SHADER+ "\n" + glGetProgramInfoLog(id, 1024));
    }


    /**
     * Gives a value to "global" uniforms, which will be applied to whole collection of game objects
     */
    public abstract void setGlobalUniforms();

    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * Creates a shader program that will be used to draw 3d objects
     * @param vertexPath a path to vertex shader source file
     * @param fragmentPath a path to fragment shader source file
     * @throws Exception if something is gone wrong with GL (most cases provide additional info)
     */
    public ShaderProgram(String vertexPath, String fragmentPath) throws Exception {
        if((this.id = GL20.glCreateProgram()) == GL_FALSE)
            throw new Exception(ERROR_COULD_NOT_CREATE_PROGRAM);
        this.uniformMap = new HashMap<>();
        this.vertexShaderID = this.buildShader(vertexPath, GL_VERTEX_SHADER);
        this.fragmentShaderID = this.buildShader(fragmentPath, GL_FRAGMENT_SHADER);
        this.link();
        this.createUniforms();
    }

    /*--------------------*/

}
