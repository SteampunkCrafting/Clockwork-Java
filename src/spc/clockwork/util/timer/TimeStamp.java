package spc.clockwork.util.timer;


/**
 * Immutable class that stores a timestamp can be used to count time intervals
 * By default, the time in {@link TimeStamp} is measured in milliseconds
 * @author wize
 * @version 0 (2018.03.25)
 */
final class TimeStamp {
    /* ATTRIBUTES
    /*--------------------*/

    /** Value of the {@link TimeStamp} */
    private final long value;

    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/
    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * Checks, whether this {@link TimeStamp} is equal to the argument {@link TimeStamp}
     * 
     * @param timeStamp another {@link TimeStamp}
     * @return true, if this is equal to the argument and false otherwise
     */
    public boolean isEqualTo(TimeStamp timeStamp) {
        return this.value == timeStamp.value;
    }


    /**
     * Checks, whether this {@link TimeStamp} is earlier than the argument {@link TimeStamp}
     * 
     * @param timeStamp another {@link TimeStamp}
     * @return true, if this is earlier than the argument and false, if not
     */
    public boolean isEarlierThan(TimeStamp timeStamp) {
        return this.value < timeStamp.value;
    }


    /**
     * Checks, whether this {@link TimeStamp} is later than the argument {@link TimeStamp}
     * 
     * @param timeStamp another {@link TimeStamp}
     * @return true, if this is later than the argument and false, if not
     */
    public boolean isLaterThan(TimeStamp timeStamp) {
        return this.value > timeStamp.value;
    }


    /**
     * Value accessor
     * @return value of the {@link TimeStamp}
     */
    public long getValue() {
        return this.value;
    }


    /**
     * Gets the interval length (non-absolute difference between values of this and argument {@link TimeStamp})
     * @param timeStamp another {@link TimeStamp}
     * @return the difference between values of {@link TimeStamp}s as a long number
     */
    public long difference(TimeStamp timeStamp) {
        return this.getValue() - timeStamp.getValue();
    }


    /**
     * Gets the absolute value of difference between values of {@link TimeStamp}s
     * @param timeStamp another {@link TimeStamp}
     * @return the absolute difference between values of {@link TimeStamp}s as a long number
     */
    public long distance(TimeStamp timeStamp) {
        return Math.abs(this.getValue() - timeStamp.getValue());
    }


    /**
     * Adds two timestamps
     * @param timeStamp another {@link TimeStamp}
     * @return new {@link TimeStamp}, which has the value equal to the sum of this and argument
     */
    public TimeStamp add(TimeStamp timeStamp) {
        return new TimeStamp(this.getValue() + timeStamp.getValue());
    }
    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * 
     * Creates a new {@link TimeStamp} with the value equal to System.currentTimeMillis()
     */
    public TimeStamp() {
        this.value = System.currentTimeMillis();
    }


    /**
     * Creates a new {@link TimeStamp} with the value equal to the argument
     * 
     * @param value the value of {@link TimeStamp} created
     */
    public TimeStamp(long value) {
        this.value = value;
    }

    /*--------------------*/
}
