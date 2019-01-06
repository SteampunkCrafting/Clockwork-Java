package spc.clockwork.util;

import java.util.Iterator;
import java.util.List;

/**
 * A helping class that contains different useful methods
 *
 * @author wize
 * @version 0 (2 June 2018)
 */
public class Utils {

    /* ATTRIBUTES
    /*--------------------*/
    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/
    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * Given a {@link List} of type {@link Float}, convert it into an array of floats
     * @param floatList the list of Float objects
     * @return an array of type float[], which contains the same values
     */
    public static float[] floatListToArray(final List<Float> floatList) {
        Iterator<Float> iterator = floatList.iterator();
        float[] floatArray = new float[floatList.size()];
        for(int i = 0; i < floatArray.length; i++)
            floatArray[i] = iterator.next();
        return floatArray;
    }


    /**
     * Given a {@link List} of type {@link Integer}, convert it into an array of ints
     * @param intList the list of Integer objects
     * @return an array of type int[], which contains the same values
     */
    public static int[] intListToArray(final List<Integer> intList) {
        Iterator<Integer> iterator = intList.iterator();
        int[] intArray = new int[intList.size()];
        for(int i = 0; i < intArray.length; i++)
            intArray[i] = iterator.next();
        return intArray;
    }
    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/
    /*--------------------*/
}
