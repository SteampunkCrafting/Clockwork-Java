package spc.clockwork.util.math;


/**
 * A class with some methods that help to work with math numbers
 *
 * @author wize
 * @version 0 (24 July 2018)
 */
public class MathUtils {

    /* ATTRIBUTES
    /*--------------------*/

    /** The default difference of the two float numbers, below which they are considered to be equal */
    public static final float DEFAULT_FLOAT_COMPARISON_EPSILON = .001f;
    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/
    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * Compares two float numbers, returning true, if they are equal within the default epsilon
     * @param n0 the first floating point number
     * @param n1 the second floating point number
     * @return true, if two numbers are equal within the default epsilon, else return false
     */
    public static boolean equal(final float n0, final float n1) {
        return MathUtils.equal(n0, n1, MathUtils.DEFAULT_FLOAT_COMPARISON_EPSILON);
    }

    /**
     * Compares two float numbers, returning true, if they are equal within the custom epsilon
     * @param n0 the first floating point number
     * @param n1 the second floating point number
     * @param comparisonEpsilon the custom epsilon  of this comparison
     * @return true, if two numbers are equal within the custom epsilon, else return false
     */
    public static boolean equal(final float n0, final float n1, final float comparisonEpsilon) {
        return Math.abs(n0 - n1) < comparisonEpsilon;
    }
    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/
    /*--------------------*/
}
