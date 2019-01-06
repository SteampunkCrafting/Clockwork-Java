package spc.clockwork.collections;

import com.sun.istack.internal.NotNull;
import spc.clockwork.core.GameAsset;
import spc.clockwork.graphics.Material;
import spc.clockwork.graphics.Texture;
import spc.clockwork.graphics.mesh.Mesh;

import java.util.*;


/**
 * A collection of game resources
 * The collection is used to store the game resources in the {@link HashMap} and {@link HashSet} by categories
 * Is used for efficient random access by name and traverse
 * The collection prevents from making repetitions of the {@link GameAsset} inside
 *
 * The collection private methods "makePowerCollection()" and "addElementToSubsets()" must be reimplemented each time
 * the category set is changed.
 *
 * Please, do not forget to add/remove the corresponding getConst method to the instance of a category, when the category is
 * added/removed
 *
 * @author wize
 * @version 0 (4 June 2018)
 */
public final class GameAssetCollection extends AbstractGameCollection<GameAsset> {

    /* ATTRIBUTES
    /*--------------------*/

    /** A Set of all resources, that do not belong to defined below categories */
    private final Set<GameAsset> otherResources;

    /** A Set of meshes */
    private final Set<Mesh> meshes;

    /** A Set of materials */
    private final Set<Material> materials;

    /** A Set of textures */
    private final Set<Texture> textures;


    private static Set<GameAsset> tempResources;
    private static Set<Mesh> tempMeshes;
    private static Set<Material> tempMaterials;
    private static Set<Texture> tempTextures;
    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/

    /**
     * Sets the superset up
     */
    @Override
    protected Collection<Collection> makePowerCollection() {
        Collection<Collection> powerSet = new LinkedList<>();
        powerSet.add(tempMaterials = new LinkedHashSet<>());
        powerSet.add(tempMeshes = new LinkedHashSet<>());
        powerSet.add(tempResources = new LinkedHashSet<>());
        powerSet.add(tempTextures = new LinkedHashSet<>());
        return powerSet;
    }


    /**
     * Adds the provided element to a corresponding subset
     * @param element the element given
     */
    @Override
    protected void addElementToSubsets(GameAsset element) {
        if(element instanceof Mesh)
            this.meshes.add((Mesh) element);
        else if(element instanceof Material)
            this.materials.add((Material) element);
        else if(element instanceof Texture)
            this.textures.add((Texture) element);
        else otherResources.add(element);
    }
    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * Gets a {@link Mesh} from the collection
     * @param name name of the resource
     * @return the resource required or null, if it does not exist (or has another type)
     */
    public Mesh getMesh(@NotNull final String name) {
        try {
            return (Mesh) this.getElement(name);
        } catch (ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Gets a {@link Material} from the collection
     * @param name name of the resource
     * @return the resource required or null, if it does not exist (or has another type)
     */
    public Material getMaterial(@NotNull final String name) {
        try {
            return (Material) this.getElement(name);
        } catch (ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Gets a {@link Texture} from the collection
     * @param name name of the resource
     * @return the resource required or null, if it does not exist (or has another type)
     */
    public Texture getTexture(@NotNull final String name) {
        try {
            return (Texture) this.getElement(name);
        } catch (ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Terminates the resource and then removes it from the collection
     * @param name the resource name
     */
    public void terminateAndRemove(@NotNull final String name) {
        try {
            this.getElement(name).terminate();
        } catch (NullPointerException e) {
            return;
        }
        this.remove(name);
    }
    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * Default {@link GameAssetCollection} constructor
     */
    public GameAssetCollection() {
        super();
        this.otherResources = tempResources;
        this.materials = tempMaterials;
        this.meshes = tempMeshes;
        this.textures = tempTextures;
    }
    /*--------------------*/
}
