package spc.clockwork.gameobject.hitbox;

import com.sun.istack.internal.NotNull;
import spc.clockwork.collections.GameWorld;
import spc.clockwork.util.math.vector.Vector3f;

/**
 * The {@link EllipsoidBox}, which has a pure spherical shape.
 * Can easily check the collisions with other {@link SphereBox}es
 * @author wize
 * @version 0 (21 July 2018)
 */
public class SphereBox extends EllipsoidBox {

    /* PRIVATE METHODS
    /*--------------------*/

    /**
     * Checks the collision with the sphere
     * @param sphere another sphere box
     * @return true, if the collision exists, else return false
     */
    private boolean collides(@NotNull final SphereBox sphere) {
        return this.getAbsolutePosition().negate().add(sphere.getAbsolutePosition()).length()
                <= this.getAbsoluteRadius() + sphere.getAbsoluteRadius();
    }
    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * Checks if this {@link HitBox} has an intersection with another of the {@link HitBox}es
     *
     * @param hitBox a {@link HitBox} to check the fact of collision with
     * @return true, if the collision between two boxes exists, else return false
     */
    @Override
    public boolean collides(@NotNull final HitBox hitBox) {
        if(hitBox instanceof SphereBox) return this.collides((SphereBox) hitBox);
        return super.collides(hitBox);
    }

    /**
     * Gets the global radius of the sphere box
     * @return the global radius of this
     */
    public float getAbsoluteRadius() {
        return this.getAbsoluteScale() * this.getRadius().x();
    }
    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * Default constructor
     * @param gameWorld the game world of this box
     * @param radius the radius of this sphere box
     */
    public SphereBox(@NotNull final GameWorld gameWorld, final float radius) {
        super(gameWorld, new Vector3f(radius));
    }

    /**
     * Default constructor of {@link SphereBox} with radius 1
     * @param gameWorld the game world of this
     */
    public SphereBox(@NotNull final GameWorld gameWorld) {
        this(gameWorld, 1f);
    }
    /*--------------------*/
}
