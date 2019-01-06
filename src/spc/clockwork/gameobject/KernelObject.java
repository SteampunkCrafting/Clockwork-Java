package spc.clockwork.gameobject;


import spc.clockwork.collections.GameWorld;

/**
 * KernelObject is a special kind of {@link GameObject}, which is the singleton, an implicit center of the
 * {@link spc.clockwork.collections.GameWorld} and the object, which returns the identity transformation,
 * no matter what is its current position. This object has no parent and ignores its children.
 * By default, if the parent of any other {@link GameObject} becomes null, it is automatically set to {@link KernelObject}
 *
 * @author wize
 * @version 0 (28 June 2018)
 */
public final class KernelObject extends GameObject {

    /* ATTRIBUTES
    /*--------------------*/
    /** The reference to singleton */
    private static final KernelObject KERNEL_OBJECT = new KernelObject();

    /** The Kernel Object's ID */
    private static final Long KERNEL_OBJECT_ID = 0L;
    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/

    /**
     * Computes the inherited visible of this
     * NOTE: THE KERNEL OBJECT JUST RETURNS TRUE HERE
     * @return the inherited visible of this
     */
    @Override
    protected boolean getInheritedVisible() {
        return true;
    }

    /**
     * Adds the new child to the set of children
     * NOTE: KERNEL OBJECT DOES NOTHING HERE
     * @param child the new child
     */
    @Override
    protected void addChild(GameObject child) {}

    /**
     * Removes the child from the set of children
     * NOTE: KERNEL OBJECT DOES NOTHING HERE
     * @param child a child to remove
     */
    @Override
    protected void removeChild(GameObject child) {}
    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * The singleton getter
     * @return the singleton {@link KernelObject}
     */
    public static KernelObject get() {
        return KernelObject.KERNEL_OBJECT;
    }
    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    private KernelObject() {
        super(KERNEL_OBJECT_ID, GameWorld.getConst());
    }

    /**
     * Prescribes what happens to this object on the termination
     */
    @Override
    protected void onTerminate() {
    }
    /*--------------------*/
}
