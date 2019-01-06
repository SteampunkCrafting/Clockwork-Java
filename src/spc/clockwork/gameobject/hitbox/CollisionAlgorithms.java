package spc.clockwork.gameobject.hitbox;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import spc.clockwork.util.math.geometry_3d.LineSegment3D;
import spc.clockwork.util.math.geometry_3d.Plane3D;
import spc.clockwork.util.math.geometry_3d.Triangle3D;
import spc.clockwork.util.math.vector.Vector3f;

import java.util.Iterator;

/**
 * Collision algorithms
 *
 * @author wize
 * @version 0 (20 Aug 2018)
 */
class CollisionAlgorithms {

    /**
     * Given a triangle, transformed into the space of the ellipsoid. (Where the ellipsoid is a unit sphere at vec3[0,0,0])
     * Check if the ellipsoid collides the triangle and, if so, determine the translation vector in the eSpace.
     * @param triangle the triangle to check collision with
     * @param cullBackFaces states whether to check only the triangles, which face in the direction of the sphere
     * @return the minimal penetration vector the ellipsoid into the triangle or null, if no collision occurred
     */
    static @Nullable Vector3f getEllipsoidIntoTrianglePenetration(@NotNull final Triangle3D triangle,
                                                                  final boolean cullBackFaces) {
        /* ---- STAGE 0: PREPARATIONS ---- */
        final Plane3D trianglePlane = new Plane3D(triangle);
        Vector3f closestContactPoint = null;


        /* ---- STAGE 1: CULLING ---- */
        if (cullBackFaces && !trianglePlane.isFrontFacingTo(Vector3f.ZERO_VECTOR.sub(trianglePlane.origin())))
            // WE ARE NOT INTERESTED IN THE FACES THAT ARE NOT LOOKING IN OUR DIRECTION
            return null;


        /* ---- STAGE 2: CHECKING COLLISION WITH THE TRIANGLE PLANE ---- */
        float signedDistToTrianglePlane = trianglePlane.getSignedDistanceTo(Vector3f.ZERO_VECTOR);
        if (Math.abs(signedDistToTrianglePlane) >= 1f) return null;


        /* ---- STAGE 3: CHECKING COLLISION WITH THE INSIDE OF A TRIANGLE ---- */
        Vector3f possibleContactPoint = trianglePlane.normal().negate().scale(signedDistToTrianglePlane);
        if(triangle.contains(possibleContactPoint)) closestContactPoint = possibleContactPoint;


        /* ---- STAGE 4: CHECKING COLLISION WITH THE EDGES OF A TRIANGLE ---- */
        Iterator<LineSegment3D> sideIterator = triangle.sideIterator();
        while(sideIterator.hasNext()) {
            final LineSegment3D side = sideIterator.next();
            final Vector3f projection;
            if(!((projection = side.getLine().project(Vector3f.ZERO_VECTOR)).length()
                    <= 1f && side.contains(projection))) continue;

            // THE SPHERE COLLIDES WITH THIS EDGE,
            // WE NEED TO CHECK IF THIS PROJECTION IS THE CLOSEST ONE TO CENTER
            closestContactPoint =
                    closestContactPoint == null || projection.length() < closestContactPoint.length() ?
                            projection : closestContactPoint;
        } return (closestContactPoint == null) ? null : closestContactPoint.normalize().sub(closestContactPoint);
    }
}
