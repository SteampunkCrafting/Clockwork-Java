package spc.clockwork.gameobject.light;

import spc.clockwork.collections.GameWorld;
import spc.clockwork.gameobject.GameObject;
import spc.clockwork.graphics.mesh.Mesh;
import spc.clockwork.util.math.vector.Vector3f;


/**
 * {@link Light} is a special kind of {@link GameObject},
 * which has no {@link Mesh}es and {@link spc.clockwork.graphics.Material}s, as, for example,
 * the {@link spc.clockwork.gameobject.entity.Entity} class. However it has color and intensity.
 *
 * The light is an abstract class of 3 kinds of concrete classes. Each of the subclasses is treated differently by the
 * renderer system, though from the Java's point of view, they are almost same.
 *
 * The class has 1 static component -- the color of a GLOBAL AMBIENT LIGHT
 * Global ambient light -- is a light that goes from everywhere to everywhere -- the 'background light' of the world
 *
 * @author wize
 * @version 0 (28 June 2018)
 */
public abstract class Light extends GameObject {
    /* ATTRIBUTES
    /*--------------------*/
    /* ---- DEFAULT PARAMETERS ---- */
    /** Default light color */
    protected static final Vector3f DEFAULT_COLOR = Vector3f.VECTOR_111;
    /** Default light intensity */
    protected static final float DEFAULT_INTENSITY = 1f;
    /** Default light attenuation */
    protected static final Attenuation DEFAULT_ATTENUATION = new Attenuation();

    /* ---- LIGHT OBJECT PARAMETERS ---- */
    /** The light's color */
    private Vector3f color;
    /** The light's intensity */
    private float intensity;

    /* ---- AMBIENT LIGHT STATIC PARAMETER ---- */
    /** The color of a global ambient light */
    private static Vector3f globalAmbientColor = new Vector3f(1f,1f,1f);
    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/

    /**
     * Prescribes what happens to this object on the termination
     * IS EMPTY FOR LIGHTS
     */
    @Override
    protected void onTerminate() {

    }

    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /* ---- LIGHT OBJECT GETTERS ---- */
    /**
     * Gets the light color
     * @return the color of the light
     */
    public Vector3f getColor() {
        return color;
    }

    /**
     * Gets the intensity of the light
     * @return the light intensity
     */
    public float getIntensity() {
        return intensity;
    }


    /* ---- LIGHT OBJECT SETTERS ---- */
    /**
     * Sets the color of the light
     * @param color the light color
     */
    public void setColor(Vector3f color) {
        this.color = color;
    }

    /**
     * Sets the intensity of the Light object
     * @param intensity the intensity multiplier
     */
    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }


    /* ---- AMBIENT LIGHT STATIC METHODS ---- */
    /**
     * Gets the global ambient color
     * @return the global ambient color of the world
     */
    public static Vector3f getGlobalAmbientColor() {
        return Light.globalAmbientColor;
    }

    /**
     * Sets the global ambient color
     * @param color the global ambient color of the world
     */
    public static void setGlobalAmbientColor(Vector3f color) {
        globalAmbientColor = color;
    }
    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * A constructor that creates an empty object with random id and default parameters
     *
     * @param gameWorld the game world to attach this object to
     */
    public Light(GameWorld gameWorld) {
        super(null, gameWorld);
        this.setColor(DEFAULT_COLOR);
        this.setIntensity(DEFAULT_INTENSITY);
    }
    /*--------------------*/
}


/**
 * The {@link Attenuation} class encapsulates the ability of the {@link Light} objects to attenuate their influence
 * on other material objects' appearance.
 *
 * @author wize
 * @version 0 (28 June 2018)
 */
final class Attenuation {
    /** Default attenuation constant */
    public static final float DEFAULT_ATTENUATION_CONSTANT = .5f;
    /** Default attenuation linear */
    public static final float DEFAULT_ATTENUATION_LINEAR = .01f;
    /** Default attenuation exponent */
    public static final float DEFAULT_ATTENUATION_EXPONENT = .002f;

    /** Current attenuation constant */
    public final float constant;
    /** Current attenuation linear */
    public final float linear;
    /** Current attenuation exponent */
    public final float exponent;

    /**
     * A constructor with non-default light parameters
     * @param constant the constant attenuation of the light
     * @param linear the linear attenuation of the light
     * @param exponent the exponent attenuation of the light
     */
    Attenuation(float constant, float linear, float exponent) {
        this.constant = constant;
        this.linear = linear;
        this.exponent = exponent;
    }

    /**
     * Default constructor
     */
    Attenuation() {
        this.constant = DEFAULT_ATTENUATION_CONSTANT;
        this.linear = DEFAULT_ATTENUATION_LINEAR;
        this.exponent = DEFAULT_ATTENUATION_EXPONENT;
    }
}