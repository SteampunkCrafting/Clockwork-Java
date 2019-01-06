package spc.clockwork.collections;

import spc.clockwork.gameobject.Camera;
import spc.clockwork.gameobject.GameObject;
import spc.clockwork.gameobject.ScriptedObject;
import spc.clockwork.gameobject.entity.Entity;
import spc.clockwork.gameobject.light.Light;

import java.util.*;

public final class GameObjectCollection extends AbstractGameCollection<GameObject> {

    /* ATTRIBUTES
    /*--------------------*/

    private final Set<GameObject> otherObjects;

    private final Set<Entity> entities;
    private final Set<Light> lights;
    private final Set<Camera> cameras;
    private final Set<ScriptedObject> scriptedObjects;


    private static Set<Entity> tempEntities;
    private static Set<Light> tempLights;
    private static Set<Camera> tempCameras;
    private static Set<ScriptedObject> tempScriptedObjects;
    private static Set<GameObject> tempOthers;
    /*--------------------*/





    /* PRIVATE METHODS
    /*--------------------*/

    /**
     * Sets the power set up
     */
    @Override
    protected Collection<Collection> makePowerCollection() {
        Collection<Collection> powerSet = new LinkedList<>();
        powerSet.add(tempEntities = new LinkedHashSet<>());
        powerSet.add(tempLights = new LinkedHashSet<>());
        powerSet.add(tempCameras = new LinkedHashSet<>());
        powerSet.add(tempOthers = new LinkedHashSet<>());
        powerSet.add(tempScriptedObjects = new LinkedHashSet<>());
        return powerSet;
    }


    /**
     * Adds the provided element to a corresponding subset
     * @param element the element given
     */
    @Override
    protected void addElementToSubsets(GameObject element) {
        if(element instanceof Entity)
            this.entities.add((Entity) element);
        else if(element instanceof Light)
            this.lights.add((Light) element);
        else if(element instanceof Camera)
            this.cameras.add((Camera) element);
        else otherObjects.add(element);


        if (element instanceof ScriptedObject)
            this.scriptedObjects.add((ScriptedObject) element);
    }


    /**
     * Creates and returns an iterator through all instances of {@link Entity} in the collection
     * @return an entity iterator
     */
    public Iterator<Entity> entityIterator() {
        return this.entities.iterator();
    }


    /**
     * Creates and returns an iterator through all instances of {@link Light} in the collection
     * @return a light iterator
     */
    public Iterator<Light> lightIterator() {
        return this.lights.iterator();
    }


    /**
     * Creates and returns an iterator through all instances of {@link ScriptedObject} in the collection
     * @return a script game objects iterator
     */
    public Iterator<ScriptedObject> scriptedObjectIterator() {
        return this.scriptedObjects.iterator();
    }

    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/
    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * Default constructor
     */
    public GameObjectCollection() {
        super();
        this.entities = tempEntities;
        this.cameras = tempCameras;
        this.lights = tempLights;
        this.scriptedObjects = tempScriptedObjects;
        this.otherObjects = tempOthers;
    }
    /*--------------------*/
}
