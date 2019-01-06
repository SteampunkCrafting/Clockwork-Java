package spc.clockwork.core;


import spc.clockwork.collections.GameWorld;

import java.util.HashMap;

public abstract class Identifiable {

    /* ATTRIBUTES
    /*--------------------*/

    private final ObjectIdentifier objectIdentifier;
    private final GameWorld gameWorld;
    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/

    /**
     * Prescribes what happens to this object on the termination
     */
    protected abstract void onTerminate();

    /**
     * Makes a new {@link ObjectIdentifier}.
     * If the id is not attached to the object, which has called this method, instantly becomes invalid
     * @param id an id number of a new {@link ObjectIdentifier}. Is generated, if set to null.
     * @return the new {@link ObjectIdentifier}, which considers this as its owner
     */
    private ObjectIdentifier makeObjectIdentifier(Object id) {
        return (id == null ? new ObjectIdentifier(this) :
                (id instanceof Number ? new ObjectIdentifier(((Number) id).longValue(), this) :
                        (new ObjectIdentifier(id.toString(), this))));
    }


    @Override
    protected final void finalize() throws Throwable {
        this.terminate();
        super.finalize();
    }

    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /* ---- OBJECT IDENTIFIER AND VALIDATION ---- */
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
    public final boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     * Gets the {@link ObjectIdentifier} of this
     * @return the identifier of this
     */
    public final ObjectIdentifier getObjectIdentifier() {
        return this.objectIdentifier;
    }

    /**
     * Checks, whether this object is valid
     * @return true, if the object's {@link ObjectIdentifier} is valid and the {@link ObjectIdentifier}'s owner is this.
     */
    public final boolean isValidIdentifiable() {
        return this.getObjectIdentifier().isValid() && this.getObjectIdentifier().getOwner() == this;
    }


    /* ---- TERMINATION ---- */
    /**
     * Terminates the object.
     * Deletes the fact of its presence in all of the in-game structures it belongs to.
     * Clears the off-heap memory that it occupies.
     * Additionally does whatever the abstract onTerminate() method prescribes
     */
    public final void terminate() {
        if (this.isTerminated()) return;
        this.onTerminate();
        this.getObjectIdentifier().terminate();
        this.getGameWorld().remove(this);
    }

    /**
     * Checks if this is terminated
     * @return true, if this is terminated, else false
     */
    public final boolean isTerminated() {
        return !this.isValidIdentifiable();
    }


    /* ---- GAME WORLD ---- */
    /**
     * The {@link spc.clockwork.collections.GameWorld} that this identifiable object is attached to
     * @return the GameWorld of this object
     */
    public final GameWorld getGameWorld() {
        return this.gameWorld;
    }

    /**
     * Gets the name of this in the attached GameWorld
     * @return the name of this identifiable in the GameWorld
     */
    public final String getName() {
        return this.getGameWorld().getNameOf(this);
    }

    /**
     * Sets the name of this object in its GameWorld.
     * Performs no operation, if the name is currently busy
     * @param newName the new name of the object
     */
    public final void setName(final String newName) {
        this.getGameWorld().setName(this, newName);
    }

    /**
     * Sets the name of the object to its default one
     */
    public final void resetName() {
        this.setName(this.getObjectIdentifier().toString());
    }

    /**
     * Checks whether the {@link Identifiable} is temporary
     * Every identifiable from the temporary game world is temporary
     * @return true, if this identifiable is temporary, else return false
     */
    public final boolean isTemporary() {
        return this.getGameWorld() == GameWorld.getTemp();
    }

    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    public Identifiable(Object id, GameWorld gameWorld) {
        /* ---- SETTING INTERNAL STATE ---- */
        this.objectIdentifier = this.makeObjectIdentifier(id);
        this.gameWorld = gameWorld;

        /* ---- ADDING THIS TO GAME WORLD ---- */
        if(this.getGameWorld() == null) return;
        this.getGameWorld().add(this.getObjectIdentifier().toString(), this);
    }
    /*--------------------*/
}