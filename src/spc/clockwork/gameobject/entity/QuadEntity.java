package spc.clockwork.gameobject.entity;

import spc.clockwork.collections.GameWorld;
import spc.clockwork.graphics.mesh.Mesh;
import spc.clockwork.graphics.mesh.Meshes;


/**
 * An entity, which has a Quad Mesh by default
 *
 * @author wize
 * @version 0 (28 May 2018)
 */
public class QuadEntity extends Entity {

    /* ATTRIBUTES
    /*--------------------*/
    /** The default Mesh of QuadEntity */
    private final Mesh DEFAULT_MESH = Meshes.QUAD;
    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * A constructor that creates an empty object with random id and default parameters
     *
     * @param gameWorld the game world to attach this object to
     */
    public QuadEntity(GameWorld gameWorld) {
        this(null, gameWorld);
    }

    /**
     * A constructor that creates an empty object with the specified id and default parameters
     *
     * @param id        an object, which is a convertible to long number or a string representation of a hex long number
     * @param gameWorld the game world to attach this object to
     */
    public QuadEntity(Object id, GameWorld gameWorld) {
        super(id, gameWorld);
        this.setMesh(DEFAULT_MESH);
    }

    /*--------------------*/
}
