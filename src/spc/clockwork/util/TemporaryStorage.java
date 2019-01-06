package spc.clockwork.util;

import com.sun.istack.internal.NotNull;

/**
 * The temporary storage of some object. Can be outdated or up-to-date.
 * @param <E> some element to be stored.
 */
public abstract class TemporaryStorage<E> {

    /* ATTRIBUTES
    /*--------------------*/

    /** The element of this storage */
    private E element;
    /** The up-to-date state of the element inside the storage */
    private boolean isActual;
    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/

    /**
     * Checks whether this
     * @return
     */
    private boolean isActual() {
        return this.isActual;
    }

    /**
     * Sets new element value and sets its state as 'up-to-date'
     * @param updatedElement the new element instance
     */
    private void updateElement(@NotNull final E updatedElement) {
        this.element = updatedElement;
        this.isActual = true;
    }

    /**
     * Gets the updated element from the subclass
     * @return the updated element, which will be kept in the storage
     */
    protected abstract E getUpdatedElement();
    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * Sets the outdated status for the element of this storage
     */
    public final void setOutdated() {
        this.isActual = false;
    }

    /**
     * Gets the up-to-date element of this storage
     * @return the up-to-date element of this storage
     */
    public final E getElement() {
        if(this.isActual()) return this.element;
        this.updateElement(this.getUpdatedElement());
        return this.getElement();
    }
    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * Default constructor that creates an outdated empty temporary storage
     */
    public TemporaryStorage() {
        this.setOutdated();
    }
    /*--------------------*/
}
