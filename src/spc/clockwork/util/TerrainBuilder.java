package spc.clockwork.util;

import de.matthiasmann.twl.utils.PNGDecoder;
import spc.clockwork.collections.GameWorld;
import spc.clockwork.gameobject.entity.Entity;
import spc.clockwork.graphics.Material;
import spc.clockwork.graphics.Texture;
import spc.clockwork.graphics.mesh.Mesh;
import spc.clockwork.util.math.vector.Vector3f;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * The class is a provider of several static methods that allow the game developer
 * to create Meshes of a terrain with ease
 *
 * @author wize
 * @version 0 (29 May 2018)
 */
public class TerrainBuilder {

    /* ATTRIBUTES
    /*--------------------*/

    /**
     * The constants and methods for HeightMapMesh
     */
    private static class HeightMapMesh {
        /** The color of the highest (or lowest, if inverted) point for the heightmap image */
        private static final int HEIGHTMAP_IMAGE_MAX_COLOR = 256 * 256 * 256;

        /** Default mesh start-x */
        private static final float START_X = -0.5f;
        /** Default mesh start-z */
        private static final float START_Z = -0.5f;


        /**
         * Computes the height for
         * @param x current x coordinate
         * @param z current z coordinate
         * @param minY minimal map y coordinate
         * @param maxY maximal map y coordinate
         * @param width width of an image
         * @param colorBuffer an image as a byte buffer
         * @return the float value of y for mesh's current position
         */
        private static float getHeightFromColor(int x,
                                                int z,
                                                float minY,
                                                float maxY,
                                                int width,
                                                ByteBuffer colorBuffer) {
            byte r = colorBuffer.get(x * 4 + 0 + z * 4 * width);
            byte g = colorBuffer.get(x * 4 + 1 + z * 4 * width);
            byte b = colorBuffer.get(x * 4 + 2 + z * 4 * width);
            byte a = colorBuffer.get(x * 4 + 3 + z * 4 * width);
            int argb = ((0xFF & a) << 24) | ((0xFF & r) << 16)
                    | ((0xFF & g) << 8) | (0xFF & b);
            return minY
                    + Math.abs(maxY - minY)
                    * ((float) argb
                    / (float) HeightMapMesh.HEIGHTMAP_IMAGE_MAX_COLOR);
        }


        /**
         * Computes the normal list for the terrain mesh, from the positions given
         * @param positions a float list of the positions
         * @param maxX width of the heightMap
         * @param maxZ height of the heightMap
         * @return an {@link ArrayList} of normals for the current terrain mesh
         */
        private static ArrayList<Float> computeNormals(ArrayList<Float> positions, int maxX, int maxZ) {
            Vector3f v0;
            Vector3f v1;
            Vector3f v2;
            Vector3f v3;
            Vector3f v4;
            Vector3f minusV0;
            Vector3f v12;
            Vector3f v23;
            Vector3f v34;
            Vector3f v41;
            Vector3f normal;
            int i0;
            int i1;
            int i2;
            int i3;
            int i4;
            
            ArrayList<Float> normals = new ArrayList<>();
            for (int row = 0; row < maxZ; row++) {
                for (int column = 0; column < maxX; column++) {
                    if (row <= 0 || row >= maxX - 1 || column <= 0 || column >= maxZ - 1) {
                        normals.add(0f);
                        normals.add(1f);
                        normals.add(0f);
                        continue;
                    }


                    i0 = row * maxX * 3 + column * 3;
                    v0 = new Vector3f(positions.get(i0), positions.get(i0+1), positions.get(i0+2));
                    minusV0 = v0.negate();

                    i1 = row * maxX * 3 + (column - 1) * 3;
                    v1 = new Vector3f(positions.get(i1), positions.get(i1+1), positions.get(i1+2));
                    v1 = v1.add(minusV0);

                    i2 = (row + 1) * maxX * 3 + column * 3;
                    v2 = new Vector3f(positions.get(i2), positions.get(i2+1), positions.get(i2+2));
                    v2 = v2.add(minusV0);

                    i3 = row * maxX * 3 + (column + 1) * 3;
                    v3 = new Vector3f(positions.get(i3), positions.get(i3+1), positions.get(i3+2));
                    v3 = v3.add(minusV0);

                    i4 = (row - 1) * maxX * 3 + column * 3;
                    v4 = new Vector3f(positions.get(i4), positions.get(i4+1), positions.get(i4+2));
                    v4 = v4.add(minusV0);


                    v12 = v1.cross(v2).normalize();
                    v23 = v2.cross(v3).normalize();
                    v34 = v3.cross(v4).normalize();
                    v41 = v4.cross(v1).normalize();


                    normal = v12.add(v23).add(v34).add(v41).normalize();
                    normals.add(normal.x());
                    normals.add(normal.y());
                    normals.add(normal.z());
                }
            }
            return normals;
        }
    }
    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/
    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * Creates an {@link Entity} with a {@link Mesh} generated from the heightMap image and
     *      with a {@link Texture}, assigned
     * @param gameWorld the game world of this terrain
     * @param minHeight the minimal Y value of the base mesh
     * @param maxHeight the maximal Y value of the base mesh
     * @param width the width (over x-axis) of the base mesh
     * @param length the length (over z-axis) of the base mesh
     * @param classPath current class path to getConst the heightmap and texture sources from
     * @param heightmapPath path to heightmap image from classPath
     * @param texturePath path to texture image from classPath
     * @param textureIncreaseX the texture increase over x
     * @param textureIncreaseY the texture increase over y
     * @param description the description of the
     * @return a new instance of Terrain {@link Entity}
     * @throws Exception when at least one of the sources is unreachable
     */
    public static Entity buildFromHeightmap(GameWorld gameWorld,
                                            float minHeight,
                                            float maxHeight,
                                            float width,
                                            float length,
                                            Class classPath,
                                            String heightmapPath,
                                            String texturePath,
                                            int textureIncreaseX,
                                            int textureIncreaseY,
                                            String description) throws Exception {

        /* ----  PREPARATION ---- */
        Entity terrain = new Entity(gameWorld);
        width /= 2;
        length /= 2;


        /* ---- TEXTURE DECODING ---- */
        Texture terrainTexture = new Texture(gameWorld, classPath, texturePath, null);


        /* ---- HEIGHTMAP DECODING ---- */
        PNGDecoder heightMap = new PNGDecoder(classPath.getResourceAsStream(heightmapPath));

        float incX = Math.abs(HeightMapMesh.START_X * 2) / (float) (heightMap.getWidth() - 1);
        float incZ = Math.abs(HeightMapMesh.START_Z * 2) / (float) (heightMap.getHeight() - 1);

        ByteBuffer heightMapBuffer = ByteBuffer.allocateDirect(4 * heightMap.getWidth() * heightMap.getHeight());
        heightMap.decode(heightMapBuffer, heightMap.getWidth() * 4, PNGDecoder.Format.RGBA);
        heightMapBuffer.flip();

        ArrayList<Float> positions = new ArrayList<>();
        ArrayList<Float> textureCoordinates = new ArrayList<>();
        ArrayList<Integer> indices = new ArrayList<>();

        for(int row = 0; row < heightMap.getHeight(); row++) {
            for(int column = 0; column < heightMap.getWidth(); column++) {

                /* -- ADDING POSITION -- */
                positions.add(HeightMapMesh.START_X + column * incX);
                positions.add(HeightMapMesh.getHeightFromColor(
                        column,
                        row,
                        minHeight,
                        maxHeight,
                        heightMap.getWidth(),
                        heightMapBuffer)); // y
                positions.add(HeightMapMesh.START_Z + row * incZ);

                /* -- SETTING TEX COORDINATE FOR THIS POSITION -- */
                textureCoordinates.add((float) textureIncreaseX * (float) column / (float) heightMap.getWidth());
                textureCoordinates.add((float) textureIncreaseY * (float) row / (float) heightMap.getHeight());
                //textureCoordinates.add(0f);
                //textureCoordinates.add(0f);


                /* -- SETTING INDICES -- */
                if (column < heightMap.getWidth() - 1 && row < heightMap.getHeight() - 1) {
                    int leftTop = row * heightMap.getWidth() + column;
                    int leftBottom = (row + 1) * heightMap.getWidth() + column;
                    int rightBottom = (row + 1) * heightMap.getWidth() + column + 1;
                    int rightTop = row * heightMap.getWidth() + column + 1;
                    indices.add(rightTop);
                    indices.add(leftTop);
                    indices.add(leftBottom);
                    indices.add(rightTop);
                    indices.add(leftBottom);
                    indices.add(rightBottom);
                }
            }
        }

        for(int i = 0; i < positions.size(); i += 3) {
            positions.set(i, positions.get(i) * width);
            positions.set(i + 2, positions.get(i+2) * length);
        }

        /* ---- SETTING NORMALS FOR THIS POSITIONS ---- */
        ArrayList<Float> normals = HeightMapMesh.computeNormals(positions, heightMap.getWidth(), heightMap.getHeight());

        Mesh terrainMesh = new Mesh(
                gameWorld,
                Utils.floatListToArray(positions),
                Utils.floatListToArray(textureCoordinates),
                Utils.floatListToArray(normals),
                Utils.intListToArray(indices),
                classPath,
                heightmapPath,
                description);


        /* ---- ENTITY ASSEMBLE AND RETURN ---- */
        terrain.setMaterial(new Material(gameWorld, terrainTexture));
        terrain.setMesh(terrainMesh);
        return terrain;
    }

    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/
    /*--------------------*/
}
