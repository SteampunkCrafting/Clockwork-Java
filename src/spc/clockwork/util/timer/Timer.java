package spc.clockwork.util.timer;


/**
 * Immutable class that stores {@link TimeStamp}s. Can be used to count time intervals.
 * Despite the fact that {@link TimeStamp} uses long type and measures time in milliseconds,
 * {@link Timer} operates with float type and measures time in seconds
 * @author wize
 * @version 0 (2018.03.25)
 */
public final class Timer {
    /* ATTRIBUTES
    /*--------------------*/

    /** The {@link TimeStamp} that stores the begin time of the current Timer */
    private final TimeStamp begin;
    /** The {@link TimeStamp} that stores the begin time of the current Timer */
    private final TimeStamp end;

    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/

    /**
     * Translates the time from milliseconds (expressed as long) to seconds (expressed as float)
     * @param millis the time in milliseconds
     * @return the floating-point number that is equal to number of seconds of argument
     */
    private static float translateLongMillisToFloatSeconds(long millis) {
        return (((float) millis) / 1000f);
    }


    /**
     * Translates the time from seconds (expressed as float) to milliseconds (expressed as long)
     * @param seconds the time in seconds
     * @return the long number that is equal to number of milliseconds of argument
     */
    private static long translateFloatSecondsToLongMillis(float seconds) {
        return (long) (seconds * 1000f);
    }

    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * Returns the current time in seconds
     * @return the current time in seconds as a float number
     */
    public static float currentTime() {
        return translateLongMillisToFloatSeconds(System.currentTimeMillis());
    }


    /**
     * Checks, whether the time interval of this {@link Timer} is up
     * @return true, if the timer is expired and false otherwise
     */
    public boolean isExpired() {
        return this.end.isEarlierThan(new TimeStamp());
    }


    /**
     * Checks, whether the time interval of this {@link Timer} is active
     * (i.e. the current time is between its begin and its end)
     * @return true, if the timer is active and false otherwise
     */
    public boolean isActive() {
        TimeStamp current = new TimeStamp();
        return this.begin.isLaterThan(current) && this.end.isEarlierThan(current);
    }


    /**
     * Gets the beginning of {@link Timer}
     * @return the beginning of {@link Timer} in seconds
     */
    public float beginsAt() {
        return translateLongMillisToFloatSeconds(this.begin.getValue());
    }


    /**
     * Gets the beginning of {@link Timer}
     * @return the beginning of {@link Timer} in seconds
     */
    public float endsAt() {
        return translateLongMillisToFloatSeconds(this.end.getValue());
    }


    /**
     * Gets the time since the beginning of {@link Timer}
     * The result is
     * @return the difference between the current time and the begin time (in seconds)
     */
    public float timePassed() {
        return translateLongMillisToFloatSeconds(
                new TimeStamp().difference(this.begin)
        );
    }


    /**
     * Gets the time till the end of {@link Timer}
     * @return the difference between the end time and the begin time (in seconds)
     */
    public float timeRemaining() {
        return translateLongMillisToFloatSeconds(
                this.end.difference(new TimeStamp())
        );
    }


    /**
     * Computes the length of the Timer interval
     * @return a difference between this begin and this end
     */
    public float length() {
        return this.end.difference(this.begin);
    }

    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * Creates a timer, which begins and ends at the exact time it was created
     */
    public Timer() {
        this.end = this.begin = new TimeStamp();
    }


    /**
     * Creates a timer.
     * If it has a positive or zero argument, then the value of the timer's end will be equal to current time + offset
     * @param offsetTime a timer offset time in seconds
     */
    public Timer(float offsetTime) {
        TimeStamp current = new TimeStamp();
        if (offsetTime < 0) {
            this.begin = new TimeStamp(translateFloatSecondsToLongMillis(offsetTime)).add(current);
            this.end = current;
        } else {
            this.end = new TimeStamp(translateFloatSecondsToLongMillis(offsetTime)).add(current);
            this.begin = current;
        }
    }


    /**
     * Creates a timer which begins at time0 and ends with time1 (or vice versa, if time0 > time1)
     * This can be combined with static method currentTime() in order to create custom timers with custom
     * begins and ends
     * @param time0 the first time point expressed in seconds
     * @param time1 the second time point expressed in seconds
     */
    public Timer(float time0, float time1) {
        if (time0 > time1) {
            this.end = new TimeStamp(translateFloatSecondsToLongMillis(time0));
            this.begin = new TimeStamp(translateFloatSecondsToLongMillis(time1));
        } else {
            this.begin = new TimeStamp(translateFloatSecondsToLongMillis(time0));
            this.end = new TimeStamp(translateFloatSecondsToLongMillis(time1));
        }
    }

    /*--------------------*/
}
