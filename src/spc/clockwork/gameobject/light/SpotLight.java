package spc.clockwork.gameobject.light;

import spc.clockwork.collections.GameWorld;


/**
 * {@link SpotLight} is a {@link Light}, which lights towards the direction of itself.
 * The light is affected by attenuation.
 *
 * @author wize
 * @version 0 (28 June 2018)
 */
public final class SpotLight extends Light {

    /* ATTRIBUTES
    /*--------------------*/

    /** The default cone angle in degrees */
    private static final float DEFAULT_CONE_ANGLE = 10f;

    /** The angle of the object's cone */
    private float coneAngle;
    /** The light's attenuation */
    private Attenuation attenuation;
    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /* ---- CONE ANGLE ---- */
    /**
     * Gets the cone angle of the object
     * @return the cone angle of the object in degrees
     */
    public float getConeAngle() {
        return this.coneAngle;
    }

    /**
     * Sets the cone angle of the object
     * @param coneAngle the new cone angle in degrees
     */
    public void setConeAngle(float coneAngle) {
        this.coneAngle = coneAngle;
    }

    /* ---- ATTENUATION ---- */

    /**
     * Gets the constant attenuation of the object
     * @return the object'c constant attenuation
     */
    public float getConstAttenuation() {
        return attenuation.constant;
    }

    /**
     * Gets the linear attenuation of the object
     * @return the object'c linear attenuation
     */
    public float getLinAttenuation() {
        return attenuation.linear;
    }

    /**
     * Gets the exponent attenuation of the object
     * @return the object'c exponent attenuation
     */
    public float getExptAttenuation() {
        return attenuation.exponent;
    }

    /**
     * Sets the attenuation of this
     * @param constant the attenuation constant
     * @param linear the attenuation linear
     * @param exponent the attenuation exponent
     */
    public void setAttenuation(float constant, float linear, float exponent) {
        this.attenuation = new Attenuation(constant, linear, exponent);
    }

    /**
     * Sets the default attenuation for this
     */
    public void setDefaultAttenuation() {
        this.attenuation = DEFAULT_ATTENUATION;
    }
    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * Constructs the light with white color, unit intensity, [0,0,-1] direction, default cone angle, default attenuation
     */
    public SpotLight(GameWorld gameWorld) {
        super(gameWorld);
        this.setDefaultAttenuation();
        this.setConeAngle(DEFAULT_CONE_ANGLE);
    }
    /*--------------------*/
}
