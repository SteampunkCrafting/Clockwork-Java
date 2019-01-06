package spc.clockwork.collections;


import com.sun.istack.internal.NotNull;
import spc.clockwork.core.GameAsset;
import spc.clockwork.core.Identifiable;
import spc.clockwork.gameobject.GameObject;
import spc.clockwork.gameobject.ScriptedObject;
import spc.clockwork.graphics.layer.GameLayer;
import spc.clockwork.window.WindowManager;

import java.util.*;

public final class GameWorld {

    /* ATTRIBUTES
    /*--------------------*/

    /** A GameWorld for private static Identifiable objects that are used everywhere */
    private static final GameWorld CONSTANT_GAME_WORLD = new GameWorld(null);

    /** A GameWorld for temporary or short-living Identifiable objects */
    private static final GameWorld TEMPORARY_GAME_WORLD = new GameWorld(null);

    /** A collection of {@link spc.clockwork.gameobject.GameObject}s, based on maps and sets */
    private final GameObjectCollection gameObjectCollection;

    /** A collection of {@link spc.clockwork.core.GameAsset}s, based on maps and sets */
    private final GameAssetCollection gameAssetCollection;

    /** A collection of {@link spc.clockwork.graphics.layer.GameLayer}s, based on maps and sets */
    private final GameLayerCollection gameLayerCollection;

    /**
     * An order of the {@link spc.clockwork.graphics.layer.GameLayer} rendering for each window as the
     * {@link java.util.Map} of {@link Integer}s to {@link java.util.List}s,
     * where each map key is an ID for a window to be rendered, and a value is a list of layer names in a layer map
     */
    private final Map<Integer, List<String>> layerRenderQueues;

    /** The reference to the window manager of Clockwork instance */
    private final WindowManager windowManager;



    /** An error message, when an object passed cannot be held in this {@link GameWorld} */
    private static final String ERROR_ILLEGAL_OBJECT_GIVEN =
            "Error: the object given cannot be held in the GameWorld";
    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/

    /**
     * Gets the collection of game objects
     * @return the collection of game objects that belong to this game world
     */
    private GameObjectCollection getGameObjectCollection() {
        return this.gameObjectCollection;
    }

    /**
     * Gets the collection of game assets
     * @return the collection of game assets that belong to this game world
     */
    private GameAssetCollection getGameAssetCollection() {
        return this.gameAssetCollection;
    }

    /**
     * Gets the collection of game layers
     * @return the collection of game layers that belong to this game world
     */
    private GameLayerCollection getGameLayerCollection() {
        return this.gameLayerCollection;
    }

    /**
     * Gets the collection of game layer render queues
     * @return the collection of game layer render queues that belong to this game world
     */
    private Map<Integer, List<String>> getLayerRenderQueues() {
        return this.layerRenderQueues;
    }

    private void removeFromCollections(Identifiable object) {
        if(object instanceof GameObject) this.getGameObjectCollection().remove((GameObject) object);
        else if(object instanceof GameAsset) this.getGameAssetCollection().remove((GameAsset) object);
        else if(object instanceof GameLayer) this.getGameLayerCollection().remove((GameLayer) object);
    }

    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /* ---- STATIC ---- */
    /**
     * The constant game world getter
     * @return the instance of game world for constant objects/assets mainly provided by the Clockwork
     */
    public static GameWorld getConst() {
        return GameWorld.CONSTANT_GAME_WORLD;
    }

    /**
     * The temporary game world getter
     * {@link GameObject}s should be programmed to terminate the assets, situated in the temporary {@link GameWorld},
     * if they were created by them
     * @return the instance of game world for some temporary or one-object-use assets
     */
    public static GameWorld getTemp() {
        return GameWorld.TEMPORARY_GAME_WORLD;
    }



    /* ---- GETTER METHODS ---- */
    /* -- ELEMENT GETTER METHODS -- */
    /**
     * Gets the {@link GameObject}, which belongs to this {@link GameWorld}, by its name or null, if
     * the {@link GameObject} with this name does not exist.
     * @param name the name of the {@link GameObject}
     * @return the element of the collection with the name given or null,
     *         if such a name was not found in the set of {@link GameObject}s
     */
    public final GameObject getObject(@NotNull final String name) {
        return this.getGameObjectCollection().getElement(name);
    }

    /**
     * Gets the {@link GameAsset}, which belongs to this {@link GameWorld}, by its name or null, if
     * the {@link GameAsset} with this name does not exist.
     * @param name the name of the {@link GameAsset}
     * @return the element of the collection with the name given or null,
     *         if such a name was not found in the set of {@link GameAsset}s
     */
    public final GameAsset getAsset(@NotNull final String name) {
        return this.getGameAssetCollection().getElement(name);
    }

    /**
     * Gets the {@link GameLayer}, which belongs to this {@link GameWorld}, by its name or null, if
     * the {@link GameLayer} with this name does not exist.
     * @param name the name of the {@link GameLayer}
     * @return the element of the collection with the name given or null,
     *         if such a name was not found in the set of {@link GameLayer}s
     */
    public final GameLayer getLayer(@NotNull final String name) {
        return this.getGameLayerCollection().getElement(name);
    }

    /**
     * Gets a name of the object given, or null, if such an object is not a member of the collection
     * @param object the identifiable object, which could be a member of this {@link GameWorld}
     * @return the name of the object given, if it exists in the {@link GameWorld} (else returns null)
     */
    public final String getNameOf(@NotNull final Identifiable object) {
        if(object instanceof GameObject) return this.getGameObjectCollection().getNameOf((GameObject)object);
        if(object instanceof GameAsset) return this.getGameAssetCollection().getNameOf((GameAsset) object);
        if(object instanceof GameLayer) return this.getGameLayerCollection().getNameOf((GameLayer) object);
        return null;
    }

    /**
     * Checks if the {@link Identifiable} object is a member of this {@link GameWorld}
     * @param object the object to check
     * @return true, if the {@link GameWorld} contains this object, else return false
     */
    public final boolean contains(@NotNull final Identifiable object) {
        /* TO BE REMOVED */
        // if(object instanceof GameObject) return this.getGameObjectCollection().contains((GameObject)object);
        // if(object instanceof GameAsset) return this.getGameAssetCollection().contains((GameAsset) object);
        // if(object instanceof GameLayer) return this.getGameLayerCollection().contains((GameLayer) object);
        return this.getNameOf(object) != null;
    }

    /**
     * Checks if some element of this collection has a name given as argument
     * @param name the name to check
     * @return true, if the name is held by some object inside the {@link GameWorld}
     */
    public final boolean contains(@NotNull final String name) {
        return this.getObject(name) != null && this.getAsset(name) != null && this.getLayer(name) != null;
    }


    /* -- ITERATOR GETTER METHODS -- */
    /**
     * The queue iterator of the names of the layers in order
     * @return an {@link java.util.Iterator<String>} that contains a names of layers to draw in the required order
     */
    public final Iterator<String> layerQueueIterator(@NotNull final Integer windowID) {
        return this.getLayerRenderQueues().containsKey(windowID) ?
                this.getLayerRenderQueues().get(windowID).iterator() : Collections.emptyIterator();
    }

    /**
     * Creates and returns an iterator through all of the {@link ScriptedObject}s in the world
     * @return an iterator of all scripted objects inside a {@link GameWorld}
     */
    public final Iterator<ScriptedObject> scriptedObjectIterator() {
        return this.getGameObjectCollection().scriptedObjectIterator();
    }

    /* -- WINDOW MANAGER GETTER METHODS -- */

    /**
     * Gets the {@link WindowManager} of this clockwork
     * @return the main window manager instance, which allows to operate program's windows
     */
    public final WindowManager getWindowManager() {
        return this.windowManager;
    }



    /* ---- MUTATOR METHODS ---- */
    /* -- LAYER RENDER QUEUE -- */

    /**
     * Sets the layer render queue for a window at a given position in the window manager list.
     * @param windowPosition position of window in the window manager.
     * @param queue queue of render as the array of strings with its elements equal to names of layers.
     */
    public final void setLayerRenderQueue(final int windowPosition, String[] queue) {
        this.getLayerRenderQueues().put(windowPosition, Arrays.asList(queue));
    }

    /* -- ADDING -- */
    /**
     * Adds the given {@link Identifiable} object to the {@link GameWorld}, giving it a custom name
     * @param name the custom name of the object
     * @param object the object's name
     */
    public final void add(String name, Identifiable object) {
        if(this.contains(name)) return;
        if(object instanceof GameObject) this.getGameObjectCollection().add(name, (GameObject)object);
        else if(object instanceof GameAsset) this.getGameAssetCollection().add(name, (GameAsset)object);
        else if(object instanceof GameLayer) this.getGameLayerCollection().add(name, (GameLayer)object);
        else throw new RuntimeException(ERROR_ILLEGAL_OBJECT_GIVEN + " " + object.toString());
    }

    /**
     * Adds the given {@link Identifiable} object to the {@link GameWorld}, giving it a default name (its id)
     * @param object the object's name
     */
    public final void add(Identifiable object) {
        if(this.contains(object)) return;
        this.add(object.getObjectIdentifier().toString(), object);
    }


    /* -- RENAMING -- */
    /**
     * Sets the new name to the object given, if it is contained in the game world, does nothing otherwise
     * @param object the collection member to set the name to
     * @param newName the new name to be set
     */
    public final void setName(Identifiable object, String newName) {
        if(!this.contains(object)) return;
        if(object instanceof GameObject) this.getGameObjectCollection().setName((GameObject)object, newName);
        else if(object instanceof GameAsset) this.getGameAssetCollection().setName((GameAsset)object, newName);
        else if(object instanceof GameLayer) this.getGameLayerCollection().setName((GameLayer)object, newName);
    }

    /**
     * Changes the old name to the new name to some object that already exists in the collection
     * This method will do nothing, if the caller attempts to overwrite the existing object (if the newName is busy)
     * @param oldName the old object name
     * @param newName the new object name
     */
    public final void rename(String oldName, String newName) {
        if(!this.contains(oldName)) return;
        this.getGameObjectCollection().rename(oldName, newName);
        this.getGameAssetCollection().rename(oldName, newName);
        this.getGameLayerCollection().rename(oldName, newName);
    }


    /* -- ADDING OBJECT TO LAYER OR REMOVING FROM IT -- */
    /**
     * Adds the {@link GameObject} to the layer with the name given
     * The object is added to the layer with the default id name
     * @param gameObject the {@link GameObject} to be added
     * @param layerName the {@link GameLayer} to be added to
     * @throws NullPointerException if the layerName is not occupied by the Layer instance
     */
    public final void addToLayer(GameObject gameObject, String layerName) {
        this.getLayer(layerName).add(gameObject.getObjectIdentifier().toString(), gameObject);
    }

    /**
     * Removes the {@link GameObject} from the layer with the name, given as argument
     * @param gameObject the {@link GameObject} to be added
     * @param layerName the {@link GameLayer} to be added to
     * @throws NullPointerException if the layerName is not occupied by the Layer instance
     */
    public final void removeFromLayer(GameObject gameObject, String layerName) {
        this.getLayer(layerName).remove(gameObject);
    }


    /* -- TERMINATING & REMOVING -- */
    /**
     * Terminates and removes the {@link Identifiable} object from the collection.
     * @param object the object to be removed.
     */
    public final void remove(Identifiable object) {
        if(!this.contains(object)) return;
        object.terminate();
        this.removeFromCollections(object);
        this.remove(object);
    }

    /**
     * Terminates and removes the {@link Identifiable} object from the collection by its name.
     * @param objectName the name of the object to terminate and remove
     */
    public final void remove(String objectName) {
        Identifiable object;
        if((object = this.getObject(objectName)) != null);
        else if ((object = this.getObject(objectName)) != null);
        else if ((object = this.getLayer(objectName)) != null);
        else return;
        this.remove(object);
    }
    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * Default constructor that creates an empty {@link GameWorld}
     */
    public GameWorld(WindowManager windowManager) {
        this.gameObjectCollection = new GameObjectCollection();
        this.gameAssetCollection = new GameAssetCollection();
        this.gameLayerCollection = new GameLayerCollection();
        this.layerRenderQueues = new HashMap<>();
        this.windowManager = windowManager;
    }
    /*--------------------*/
}