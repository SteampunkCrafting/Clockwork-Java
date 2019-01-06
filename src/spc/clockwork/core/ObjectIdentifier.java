package spc.clockwork.core;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * An identifier that, if is a part of an object
 * (originally, an instance of {@link GameAsset} or {@link spc.clockwork.gameobject.GameObject}),
 * can be used to check the fact, whether these objects are indeed the same one.
 *
 * @author wize
 * @version 0 (12 June 2018)
 */
public final class ObjectIdentifier {

    /* ATTRIBUTES
    /*--------------------*/

    /** An identification number of this identifier */
    private final long id;

    /** A state of the identifier's validity */
    private boolean isTerminated;

    /** An owner of this Identifier */
    private final Identifiable owner;

    /** The global set of currently used ids */
    private static final Set<Long> usedIDs = new HashSet<>();

    /** An error message, if the repeated {@link ObjectIdentifier} was constructed */
    private static final String ERROR_ID_ALREADY_EXISTS =
            "Error: attempted to construct an ObjectIdentifier with the same id number";
    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/

    /**
     * Generates a random long number, which does not exist in the set of the IDs used by other identifiers
     * @return a long number, which is not used by another {@link ObjectIdentifier}
     */
    private static long generateFreeIdNumber() {
        Random rand = new Random();
        long id;
        do {
            id = rand.nextLong();
        } while(ObjectIdentifier.usedIDs.contains(id));
        return id;
    }
    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * Checks, whether this identifier is valid
     * @return true, if the identifier is valid (i.e. is not terminated and attached to its owner), else return false
     */
    public boolean isValid() {
        return !this.isTerminated && this.getOwner().getObjectIdentifier() == this;
    }

    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "[id " + Long.toHexString(this.id) + "]";
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * <p>
     * The {@code equals} method implements an equivalence relation
     * on non-null object references:
     * <ul>
     * <li>It is <i>reflexive</i>: for any non-null reference value
     * {@code x}, {@code x.equals(x)} should return
     * {@code true}.
     * <li>It is <i>symmetric</i>: for any non-null reference values
     * {@code x} and {@code y}, {@code x.equals(y)}
     * should return {@code true} if and only if
     * {@code y.equals(x)} returns {@code true}.
     * <li>It is <i>transitive</i>: for any non-null reference values
     * {@code x}, {@code y}, and {@code z}, if
     * {@code x.equals(y)} returns {@code true} and
     * {@code y.equals(z)} returns {@code true}, then
     * {@code x.equals(z)} should return {@code true}.
     * <li>It is <i>consistent</i>: for any non-null reference values
     * {@code x} and {@code y}, multiple invocations of
     * {@code x.equals(y)} consistently return {@code true}
     * or consistently return {@code false}, provided no
     * information used in {@code equals} comparisons on the
     * objects is modified.
     * <li>For any non-null reference value {@code x},
     * {@code x.equals(null)} should return {@code false}.
     * </ul>
     * <p>
     * The {@code equals} method for class {@code Object} implements
     * the most discriminating possible equivalence relation on objects;
     * that is, for any non-null reference values {@code x} and
     * {@code y}, this method returns {@code true} if and only
     * if {@code x} and {@code y} refer to the same object
     * ({@code x == y} has the value {@code true}).
     * <p>
     * Note that it is generally necessary to override the {@code hashCode}
     * method whenever this method is overridden, so as to maintain the
     * general contract for the {@code hashCode} method, which states
     * that equal objects must have equal hash codes.
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     * argument; {@code false} otherwise.
     * @see #hashCode()
     * @see HashMap
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof ObjectIdentifier && this.id == ((ObjectIdentifier) obj).id;
    }

    /**
     * Terminates the identifier, making it invalid and freeing an id for another identifiers
     * The operation cannot be undone
     */
    public void terminate() {
        if (this.isTerminated) return;
        this.isTerminated = true;
        ObjectIdentifier.usedIDs.remove(this.id);
        this.getOwner().terminate();
    }

    /**
     * An owner accessor
     * @return an owner of this certificate
     */
    public Identifiable getOwner() {
        return this.owner;
    }

    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * A constructor that takes a {@link String}, which contains a long HEX number,
     * this will be parsed onto the numeric identifier's ID-num
     * @param id an identification string, written as a HEXADECIMAL number
     */
    public ObjectIdentifier(String id, Identifiable owner) {
        this(Long.decode(id), owner);
    }

    /**
     * The objectIdentifier constructor, which generates a new ID number for this Identifier
     * @throws RuntimeException, if the argument id is already used by another identifier
     */
    public ObjectIdentifier(Identifiable owner) {
        this(ObjectIdentifier.generateFreeIdNumber(), owner);
    }

    /**
     * The default Constructor, which gets an ID as a parameter
     * @param id the numeric identifier of this number
     * @throws RuntimeException, if the argument id is already used by another identifier
     */
    public ObjectIdentifier(long id, Identifiable owner) {
        if (ObjectIdentifier.usedIDs.contains(id)) throw new RuntimeException(ERROR_ID_ALREADY_EXISTS);
        this.owner = owner;
        this.id = id;
        this.isTerminated = false;
        ObjectIdentifier.usedIDs.add(id);
    }
    /*--------------------*/
}
