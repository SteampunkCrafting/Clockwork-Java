package spc.clockwork.collections;


import com.sun.istack.internal.NotNull;
import spc.clockwork.core.Identifiable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

/**
 * An abstract map- and set-based collection
 * The GameCollections can contain only one instance of the same element
 * Each element has a unique String name
 * @param <E> An element of the collection of some type
 *
 * @author wize
 * @version 0 (4 June 2018)
 */
public abstract class AbstractGameCollection<E> {

    /* ATTRIBUTES
    /*--------------------*/

    /** A map of all items in this collection */
    private final Map<String, E> allElements;

    /** A reversed map of all keys in this collection */
    private final Map<E, String> allElementsReverse;

    /** A set of all defined item subsets in this collection */
    private final Collection<Collection> powerCollection;

    /** An error message, if a non-identifiable-object was added to the collection without name */
    private static final String ERROR_ADD_WITHOUT_NAME =
            "Error: The non-Identifiable element has been added to a collection without a name specified";
    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/

    /**
     * Sets the power collection up
     */
    protected abstract Collection<Collection> makePowerCollection();

    /**
     * Adds the provided element to a corresponding subset
     * @param element the element given
     */
    protected abstract void addElementToSubsets(E element);


    /**
     * Removes the element from the subsets
     * @param element the element to remove
     */
    private void removeElementFromSubsets(E element) {
        this.powerCollection.forEach(set ->  set.remove(element));
    }
    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * Checks if the element under this key exists
     * @param name the element name to check
     * @return true, if such an element exists, else false
     */
    public final boolean contains(@NotNull final String name) {
        return this.allElements.containsKey(name);
    }


    /**
     * Checks if the element exists
     * @param element the element to check
     * @return true, if such an element exists, else false
     */
    public final boolean contains(@NotNull final E element) {
        return this.allElementsReverse.containsKey(element);
    }


    /**
     * Adds an element to a collection, assigning a name to it
     * @param name name of the instance
     * @param element the element to add
     */
    public final void add(@NotNull final String name, @NotNull final E element) {
        if(this.contains(element)) return;
        this.allElements.put(name, element);
        this.allElementsReverse.put(element, name);
        this.addElementToSubsets(element);
    }


    /**
     * Adds an {@link Identifiable} element to a collection, assigning its id as its name
     * @throws RuntimeException if the element given is not an {@link Identifiable}
     * @param identifiableElement the element to add
     */
    public final void add(@NotNull final E identifiableElement) {
        if(!(identifiableElement instanceof Identifiable)) throw new RuntimeException(ERROR_ADD_WITHOUT_NAME);
        this.add(((Identifiable) identifiableElement).getObjectIdentifier().toString(), identifiableElement);
    }


    /**
     * Renames the element from the collection, if it exists (does nothing, if not)
     * @param oldName the old name of a resource
     * @param newName the new name of a resource
     */
    public final void rename(@NotNull final String oldName, @NotNull final String newName) {
        /* TO BE REMOVED */
        // if (!this.allElements.containsKey(oldName)) return;
        // this.allElements.put(newName, this.allElements.getConst(oldName));
        // this.allElements.remove(oldName);
        if (!this.contains(oldName) || this.contains(newName)) return;
        E element = this.allElements.get(oldName);
        this.allElementsReverse.replace(element, newName);
        this.allElements.put(newName, this.allElements.remove(oldName));
    }


    /**
     * Sets the name to an element in this collection, if it exists (does nothing, if not)
     * @param element the element of this collection
     * @param newName the new name of this collection
     */
    public final void setName(@NotNull final E element, @NotNull final String newName) {
        if (!this.contains(element) || this.contains(newName)) return;
        this.rename(this.getNameOf(element), newName);
    }


    /**
     * Gets an element from the collection
     * @param name name of the resource
     * @return the element required or null, if it does not exist
     */
    public final E getElement(@NotNull final String name) {
        return this.allElements.get(name);
    }

    /**
     * Gets a key of the element, if it exists in the collection
     * @param element a potential element of the collection
     * @return the string containing a name of the element of this collection or null, if there is no such element
     */
    public final String getNameOf(@NotNull final E element) {
        return this.allElementsReverse.get(element);
    }


    /**
     * Removes an element from the collection
     * @param name the element name
     */
    public final void remove(@NotNull final String name) {
        /* TO BE REMOVED */
        // if (!this.contains(name)) return;
        // this.removeElementFromSubsets(this.allElements.remove(name));

        if(name == null || !this.contains(name)) return;
        E element = this.allElements.remove(name);
        this.allElementsReverse.remove(element);
        this.removeElementFromSubsets(element);
    }

    /**
     * Removes an element from the collection
     * @param element the element to remove
     */
    public final void remove(@NotNull final E element) {
        /* TO BE REMOVED */
        // if(!this.contains(element)) return;
        // this.removeElementFromSubsets(element);
        // this.allElements.values().remove(element);
        this.remove(this.getNameOf(element));
    }

    /**
     * Applies the action to every element of the collection
     * @param action action to be performed for each element
     */
    public final void forEach(@NotNull final Consumer<E> action) {
        this.allElementsReverse.keySet().forEach(action);
    }

    /**
     * Gets an iterator over all elements in the collection
     * @return an iterator over {@link E}
     */
    public final Iterator<E> iterator() {
        return this.allElementsReverse.keySet().iterator();
    }

    /**
     * Gets an iterator over all names in the collection
     * @return an iterator over {@link String}
     */
    public final Iterator<String> nameIterator() {
        return this.allElements.keySet().iterator();
    }
    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * Default empty collection constructor
     */
    public AbstractGameCollection() {
        this.allElements = new HashMap<>();
        this.allElementsReverse = new HashMap<>();
        this.powerCollection = this.makePowerCollection();
    }
    /*--------------------*/
}
