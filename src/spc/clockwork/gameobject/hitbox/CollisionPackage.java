package spc.clockwork.gameobject.hitbox;

import spc.clockwork.util.math.vector.Vector3f;

class CollisionPackage {

    /* ATTRIBUTES
    /*--------------------*/
    /* ---- ELLIPSOID (LOCAL) SPACE ---- */
    private Vector3f localVelocity;
    private Vector3f normalizedLocalVelocity;
    private Vector3f localBasePoint;
    private Vector3f localIntersectionPoint;
    /* ---- OTHER INFORMATION ---- */
    private boolean foundCollision;
    private float nearestDistance;
    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/
    /* ---- PACKAGE PRIVATE METHODS ---- */
    void foundCollision() {
        this.foundCollision = true;
    }
    void setNearestDistance(float nearestDistance) {
        this.nearestDistance = nearestDistance;
    }
    void setLocalIntersectionPoint(Vector3f localIntersectionPoint) {
        this.localIntersectionPoint = localIntersectionPoint;
    }

    boolean hasCollision() {
        return this.foundCollision;
    }

    float getNearestDistance() {
        return this.nearestDistance;
    }
    Vector3f getLocalBasePoint() {
        return this.localBasePoint;
    }
    Vector3f getLocalVelocity() {
        return localVelocity;
    }
    Vector3f getLocalIntersectionPoint() {
        return this.localIntersectionPoint;
    }

    void setLocalVelocity(Vector3f localVelocity) {
        this.localVelocity = localVelocity;
        this.normalizedLocalVelocity = localVelocity.normalize();
    }
    void setLocalBasePoint(Vector3f localBasePoint) {
        this.localBasePoint = localBasePoint;
    }
    void resetFoundCollision() {
        this.foundCollision = false;
    }

    /*--------------------*/
}
