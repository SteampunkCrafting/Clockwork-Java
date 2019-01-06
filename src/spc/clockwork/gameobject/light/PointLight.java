package spc.clockwork.gameobject.light;

import spc.clockwork.collections.GameWorld;


/**
 * A {@link Light} subclass, which is affected by attenuation
 *
 * @author wize
 * @version 0 (28 June 2018)
 */
public final class PointLight extends Light {

    /* ATTRIBUTES
    /*--------------------*/
    /* The light's attenuation */
    private Attenuation attenuation;
    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * Gets the constant attenuation of this
     * @return the constant attenuation coefficient
     */
    public float getConstAttenuation() {
        return attenuation.constant;
    }

    /**
     * Gets the linear attenuation of this
     * @return the constant attenuation coefficient
     */
    public float getLinAttenuation() {
        return attenuation.linear;
    }

    /**
     * Gets the exponent attenuation of this
     * @return the exponent attenuation coefficient
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
     * Constructs a {@link PointLight} with white color, default attenuation and unit intensity
     */
    public PointLight(GameWorld gameWorld) {
        super(gameWorld);
        this.setDefaultAttenuation();
    }
    /*--------------------*/
}
