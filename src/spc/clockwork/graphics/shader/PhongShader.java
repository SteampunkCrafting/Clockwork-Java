package spc.clockwork.graphics.shader;

import spc.clockwork.gameobject.Camera;
import spc.clockwork.gameobject.entity.Entity;
import spc.clockwork.gameobject.light.DirectionalLight;
import spc.clockwork.gameobject.light.Light;
import spc.clockwork.gameobject.light.PointLight;
import spc.clockwork.gameobject.light.SpotLight;
import spc.clockwork.util.math.matrix.Matrix4f;

import java.util.Iterator;

/**
 * A ShaderProgram that simulates light reflectance from the objects.
 * Currently supports a single {@link PointLight}
 * @author wize
 * @version 0 (2018.04.18)
 */
public final class PhongShader extends ShaderProgram {

    /* ATTRIBUTES
    /*--------------------*/
    /* -- SHADER SOURCES -- */
    private static final String VERTEX_SHADER_PATH = "phong_shader/PhongVertex.glsl";
    private static final String FRAGMENT_SHADER_PATH = "phong_shader/PhongFragment.glsl";

    /* -- SHADER PARAMETERS -- */
    public static final int MAX_AMOUNT_OF_POINT_LIGHTS = 64;
    public static final int MAX_AMOUNT_OF_SPOT_LIGHTS = 64;
    public static final int MAX_AMOUNT_OF_DIR_LIGHTS = 4;

    /* -- PROJECTION PARAMETERS -- */
    private final float aspectRatio;
    private static final float FIELD_OF_VIEW = (float)Math.toRadians(60);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000f;
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
        /* ---- Creating matrices ---- */
        try {
            this.createUniform("projectionMatrix");
        } catch (Exception e) {
            //System.err.println(ERROR_FAILED_TO_CREATE_UNIFORM_TYPE + "projectionMatrix");
            e.printStackTrace();
        }

        try {
            this.createUniform("modelViewMatrix");
        } catch (Exception e) {
            e.printStackTrace();
        }


        /* ---- Creating material ---- */
        try {
            this.createMaterialUniform("entityMaterial");
        } catch (Exception e) {
            e.printStackTrace();
        }


        /* ---- Creating light(s) ---- */
        try {
            this.createUniform("ambientLightColor");
            this.createPointLightUniformsArray("pointLights", MAX_AMOUNT_OF_POINT_LIGHTS);
            this.createDirLightUniformsArray("directionalLights", MAX_AMOUNT_OF_DIR_LIGHTS);
            this.createSpotLightUniformArray("spotLights", MAX_AMOUNT_OF_SPOT_LIGHTS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Gives a value to "global" uniforms, which will be applied to whole collection of game objects
     */
    @Override
    public void setGlobalUniforms() {
        setUniform("projectionMatrix",
                Matrix4f.newPerspectiveProjectionMatrix(aspectRatio, FIELD_OF_VIEW, Z_NEAR, Z_FAR));
    }
    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * Sets local uniforms of this shader for this entity and point lights
     * @param camera {@link Camera} instance
     * @param entity {@link Entity} instance
     * @param lights {@link Light} instance iterator
     */
    public void setLocalUniforms(Camera camera, Entity entity, Iterator<Light> lights) {

        /* -- SETTING LIGHT UNIFORMS -- */
        PointLight[] pointLights = new PointLight[MAX_AMOUNT_OF_POINT_LIGHTS];
        DirectionalLight[] directionalLights = new DirectionalLight[MAX_AMOUNT_OF_DIR_LIGHTS];
        SpotLight[] spotLights = new SpotLight[MAX_AMOUNT_OF_SPOT_LIGHTS];

        int pointLightIterator = 0;
        int dirLightIterator = 0;
        int spotLightIterator = 0;

        while (lights.hasNext()) {
            Light light = lights.next();
            if(light.getClass().getCanonicalName().equals(PointLight.class.getCanonicalName())) {
                if (pointLightIterator < MAX_AMOUNT_OF_POINT_LIGHTS)
                    pointLights[pointLightIterator++] = (PointLight) light;
            } else if(light.getClass().getCanonicalName().equals(DirectionalLight.class.getCanonicalName())) {
                if (dirLightIterator < MAX_AMOUNT_OF_DIR_LIGHTS)
                    directionalLights[dirLightIterator++] = (DirectionalLight) light;
            } else if(light.getClass().getCanonicalName().equals(SpotLight.class.getCanonicalName())) {
                if (spotLightIterator < MAX_AMOUNT_OF_SPOT_LIGHTS)
                    spotLights[spotLightIterator++] = (SpotLight) light;
            }
        }

        setUniform("directionalLights", camera, directionalLights);
        setUniform("pointLights", camera, pointLights);
        setUniform("spotLights", camera, spotLights);
        setUniform("ambientLightColor", Light.getGlobalAmbientColor());

        /* -- SETTING MATERIAL UNIFORMS -- */
        setUniform("entityMaterial", entity.getMaterial());

        /* -- SETTING MATRIX UNIFORMS -- */
        setUniform("modelViewMatrix",
                Matrix4f.newModelViewMatrix(camera, entity));

    }
    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/
    public PhongShader(final float aspectRatio) throws Exception {
        super(VERTEX_SHADER_PATH, FRAGMENT_SHADER_PATH);
        this.aspectRatio = aspectRatio;
    }
    /*--------------------*/
}
