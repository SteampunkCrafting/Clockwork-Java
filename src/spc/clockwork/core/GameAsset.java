package spc.clockwork.core;


import spc.clockwork.collections.GameWorld;

/**
 * {@link GameAsset} is a representation of an abstract game resource
 * (e.g. model, texture, heightmap, sound -- a piece of content that was created outside the Clockwork)
 *
 * @author wize
 * @version 0 (4 June 2018)
 */
public abstract class GameAsset extends Identifiable {

    /* ATTRIBUTES
    /*--------------------*/

    /** The class, which is the root for the filePath */
    private final Class classPath;

    /** The path to the file, where this resource was encoded */
    private final String filePath;

    /** The description of a resource */
    private final String description;
    /*--------------------*/


    /* PRIVATE METHODS
    /*--------------------*/

    /**
     * An abstract method that is called upon the termination stage of the resource.
     * Is implemented non-empty by the subclass, if the termination requires to do something
     * (e.g. making an off-heap memory cleanup)
     */
    @Override
    protected abstract void onTerminate();
    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * The filePath parameter accessor
     * @return the filePath of this
     */
    public final String getFilePath() {
        return this.filePath;
    }

    /**
     * The classPath parameter accessor
     * @return the class that is the root for the filePath of this
     */
    public final Class getClassPath() {
        return this.classPath;
    }

    /**
     * The description parameter accessor
     * @return the description of this resource
     */
    public final String getDescription() {
        return this.description;
    }


    @Override
    public String toString() {
        return  (this.isTerminated() ? "(TERMINATED) " : "") + "RESOURCE\n" +
                "--------------------" +
                "TYPE: " + this.getClass().getCanonicalName() + "\n" +
                "FILE: " + this.getFilePath() + " from class " + this.getClassPath() +
                "DESCRIPTION: " + this.getDescription();
    }
    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * Default Resource constructor with an auto generated ID
     * @param classPath the classPath to the source
     * @param filePath the filePath to the source
     * @param description the resource description
     */
    public GameAsset(GameWorld gameWorld, Class classPath, String filePath, String description) {
        this(null, gameWorld, classPath, filePath, description);
    }


    /**
     * Default Resource constructor
     * @param id an object, which is a convertible to long number or a string representation of a hex long number
     * @param classPath the classPath to the source
     * @param filePath the filePath to the source
     * @param description the resource description
     */
    private GameAsset(Object id, GameWorld gameWorld, Class classPath, String filePath, String description) {
        super(id, gameWorld);
        this.classPath = classPath;
        this.filePath = filePath;
        this.description = description;
    }
    /*--------------------*/
}
