package spc.clockwork.gameobject.entity;

import spc.clockwork.collections.GameWorld;
import spc.clockwork.gameobject.GameObject;
import spc.clockwork.graphics.Material;
import spc.clockwork.graphics.mesh.Mesh;
import spc.clockwork.graphics.mesh.Meshes;
import spc.clockwork.util.math.vector.Vector3f;

/**
 * Entity -- is a {@link GameObject} that has a model and material
 * @author wize
 * @version 0 (2018.03.29)
 */
public class Entity extends GameObject {

    /* ATTRIBUTES
    /*--------------------*/
    private static final Mesh DEFAULT_MESH = Meshes.SPHERE;
    private static final Material DEFAULT_MATERIAL = new Material(
            GameWorld.getConst(),
            Vector3f.VECTOR_111,
            Material.PolygonMode.FILL,
            "DEFAULT MATERIAL");

    private Mesh mesh;
    private Material material;
    /*--------------------*/


    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * Prescribes what happens to this object on the termination
     * NOTE: THE GENERAL ENTITY HAS NOTHING TO TERMINATE
     */
    @Override
    protected void onTerminate() {
        this.setMesh(null);
        this.setMaterial(null);
    }
    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * {@link Mesh} getter
     * @return Object's {@link Mesh}
     */
    public Mesh getMesh() {
        return this.mesh;
    }


    /**
     * {@link Material} accessor
     * @return Object's {@link Material}
     */
    public Material getMaterial() {
        return this.material;
    }


    /**
     * Checks, if this Entity has Mesh
     * @return true, if it has Mesh, false otherwise
     */
    public boolean hasMesh() {
        return this.getMesh() != null;
    }


    /**
     * Checks, if this Entity has Material
     * @return true, if it has Material, false otherwise
     */
    public boolean hasMaterial() {
        return this.getMaterial() != null;
    }


    /**
     * Checks, whether this Entity has Material with texture
     * @return return true, if the entity material exists and has texture
     */
    public boolean hasTexture() {
        return this.hasMaterial() && this.getMaterial().hasTexture();
    }


    /**
     * {@link Mesh} setter
     * @param mesh new object's mesh
     */
    public void setMesh(Mesh mesh) {
        if(this.getMesh() != null && this.getMesh().isTemporary()) this.getMesh().terminate();
        this.mesh = mesh;
    }


    /**
     * {@link Material} mutator
     * @param material Object's new {@link Material}
     */
    public void setMaterial(Material material) {
        if(this.getMaterial() != null && this.getMaterial().isTemporary()) this.getMaterial().terminate();
        this.material = material;
    }

    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * A constructor that creates an empty object with random id and default parameters
     *
     * @param gameWorld the game world to attach this object to
     */
    public Entity(GameWorld gameWorld) {
        this(null, gameWorld);
    }

    /**
     * A constructor that creates an empty object with the specified id and default parameters
     *
     * @param id        an object, which is a convertible to long number or a string representation of a hex long number
     * @param gameWorld the game world to attach this object to
     */
    public Entity(Object id, GameWorld gameWorld) {
        super(id, gameWorld);
        if(this.getMaterial() == null) this.setMaterial(DEFAULT_MATERIAL);
        if(this.getMesh() == null) this.setMesh(DEFAULT_MESH);
    }

    /*--------------------*/
}
