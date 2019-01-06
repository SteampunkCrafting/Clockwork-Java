package spc.clockwork.graphics.mesh;


import spc.clockwork.collections.GameWorld;
import spc.clockwork.util.ObjDecoder;
import spc.clockwork.util.math.vector.Vector3f;

/**
 * A 'static' class that stores default and most useful meshes
 *
 * @author wize
 * @version 1 (21 July 2018)
 */
public class Meshes {

    /** A quad mesh of size 1x1, which is used for rendering images */
    public static final Mesh QUAD;

    /** A basic cube mesh */
    public static final Mesh CUBE;

    /** A basic low-poly sphere mesh */
    public static final Mesh SPHERE;

    /** A low-poly monkey mesh */
    public static final Mesh MONKEY;


    /** Creates and returns a custom cuboid mesh of a scale given */
    public static Mesh newCustomCuboid(Vector3f scale) {
        return loadTempMesh("default_meshes/cube.obj", "A cuboid of scale" + scale.toString(), scale);
    }

    /** Creates and returns a custom ellipsoid mesh of a scale given */
    public static Mesh newCustomEllipsoid(Vector3f scale) {
        return loadTempMesh("default_meshes/sphere.obj", "An ellipsoid of scale " + scale.toString(), scale);
    }

    /** Creates and returns a custom quad mesh of a scale given */
    public static Mesh newCustomQuad(Vector3f scale) {
        return loadTempMesh("default_meshes/quad.obj", "A quad of scale " + scale.toString(), scale);
    }



    static {
        QUAD = loadDefaultMesh("default_meshes/quad.obj", "A basic quad mesh", Vector3f.VECTOR_111);
        CUBE = loadDefaultMesh("default_meshes/cube.obj", "A basic cube mesh", Vector3f.VECTOR_111);
        SPHERE = loadDefaultMesh("default_meshes/sphere.obj", "A basic low-poly sphere mesh", Vector3f.VECTOR_111);
        MONKEY = loadDefaultMesh("default_meshes/monkey.obj", "A low-poly monkey mesh", Vector3f.VECTOR_111);
    }


    /**
     * Loads the default mesh onto the constant {@link GameWorld} and returns it as a result
     * @param filename the filename of the mesh
     * @param description the description of the mesh
     * @return the mesh, loaded from the file
     */
    private static Mesh loadDefaultMesh(String filename, String description, Vector3f scale) {
        try {
            return ObjDecoder.loadMesh(GameWorld.getConst(), Mesh.class, filename, description, scale);
        } catch (Exception e) {
            System.err.println("Error loading default mesh " + filename);
            return null;
        }
    }

    /**
     * Loads the default mesh with a specified scale onto a temporary {@link GameWorld} and returns it as a result
     * @param filename the filename of the mesh
     * @param description the description of the mesh
     * @param scale the scale vector of the mesh
     * @return the mesh, loaded from the file
     */
    private static Mesh loadTempMesh(String filename, String description, Vector3f scale) {
        try {
            return ObjDecoder.loadMesh(GameWorld.getTemp(), Mesh.class, filename, description, scale);
        } catch (Exception e) {
            System.err.println("Error loading custom temporary mesh " + filename);
            return null;
        }
    }
}
