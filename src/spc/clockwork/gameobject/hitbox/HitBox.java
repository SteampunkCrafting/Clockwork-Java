package spc.clockwork.gameobject.hitbox;

import com.sun.istack.internal.NotNull;
import spc.clockwork.collections.GameWorld;
import spc.clockwork.gameobject.entity.Entity;
import spc.clockwork.graphics.Material;
import spc.clockwork.util.math.vector.Vector3f;

/**
 * A {@link HitBox} is a subclass of an {@link Entity} that represents a 'box' with some physical parameters.
 * A {@link HitBox} is able to find out, if it intersects or collides with some other {@link HitBox}.
 *
 * @author wize
 * @version 0 (29 June 2018)
 */
public abstract class HitBox extends Entity {

    /* ATTRIBUTES
    /*--------------------*/

    /** An error message, when the non-implemented collision detection was called */
    private static final String ERROR_COLLISION_CHECK_NOT_IMPLEMENTED =
            "Error: %s <--> %s type of collision detection is not supported";

    /** Default material of the HitBox {@link HitBox} */
    private static final Material DEFAULT_HIT_BOX_MATERIAL = new Material(
            GameWorld.getConst(),
            Vector3f.RIGHT_VECTOR,
            Material.PolygonMode.LINE,
            "Default material of the HitBox objects"
    );

    /** A state of the {@link HitBox} that determines whether it is busy to be called or not */
    private boolean isCurrentlyBusyCheckingCollision;
    /*--------------------*/


    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * Checks if this {@link HitBox} has an intersection with another of the {@link HitBox}es
     * @param hitBox a {@link HitBox} to check the fact of collision with
     * @return true, if the collision between two boxes exists, else return false
     */
    public boolean collides(@NotNull final HitBox hitBox) {
        if(hitBox.isCurrentlyBusyCheckingCollision)
            throw new RuntimeException(String.format(
                        ERROR_COLLISION_CHECK_NOT_IMPLEMENTED,
                        this.getClass().getCanonicalName(),
                        hitBox.getClass().getCanonicalName()));
        this.isCurrentlyBusyCheckingCollision = true;
        boolean collision = hitBox.collides(this);
        this.isCurrentlyBusyCheckingCollision = false;
        return collision;
    }
    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * Default constructor
     * @param gameWorld the {@link GameWorld} of this object
     */
    public HitBox(final GameWorld gameWorld) {
        super(null, gameWorld);
        this.isCurrentlyBusyCheckingCollision = false;
        this.setMaterial(DEFAULT_HIT_BOX_MATERIAL);
    }
    /*--------------------*/
}
