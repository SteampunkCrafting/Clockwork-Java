package spc.clockwork.gameobject.hitbox;


import com.sun.istack.internal.NotNull;
import spc.clockwork.collections.GameWorld;
import spc.clockwork.graphics.mesh.Mesh;
import spc.clockwork.graphics.mesh.TriangleSetMesh;
import spc.clockwork.util.math.geometry_3d.Triangle3D;

import java.util.Iterator;

/**
 * A special kind of {@link HitBox}, which has a form of a mesh, attached to it.
 *
 * @author wize
 * @version 0 (21 July 2018)
 */
public final class MeshBox extends HitBox {

    /* ATTRIBUTES
    /*--------------------*/
    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/
    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * Constructs and returns the new iterator over {@link Mesh} triangles
     * @return the new mesh triangles iterator
     */
    public Iterator<Triangle3D> triangleIterator() {
        return ((TriangleSetMesh)this.getMesh()).triangleIterator();
    }
    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * Default constructor of the {@link MeshBox}
     * @param gameWorld the {@link GameWorld} of this {@link MeshBox}
     * @param mesh the {@link Mesh}, which represents the form of this {@link MeshBox}
     */
    public MeshBox(@NotNull final GameWorld gameWorld, @NotNull final TriangleSetMesh mesh) {
        super(gameWorld);
        this.setMesh(mesh);
    }
    /*--------------------*/
}
