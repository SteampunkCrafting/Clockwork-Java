package spc.clockwork.util;

import spc.clockwork.collections.GameWorld;
import spc.clockwork.graphics.mesh.Mesh;
import spc.clockwork.graphics.mesh.TriangleSetMesh;
import spc.clockwork.util.math.vector.Vector3f;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * A static class that provides methods to translate .obj
 * files into 3d models called {@link Mesh}
 *
 * @author wize
 * @version 0 (2018.03.31)
 */
public class ObjDecoder {
    /* ATTRIBUTES
    /*--------------------*/

    private static final String WARNING_MESH_WITHOUT_TRIANGLE_SET
            = "Warning: the mesh loaded does not contain the triangle set, because of some error occurred. \n" +
            "\tThe mesh \"%s\" can be rendered, but cannot be used for internal computations (such as collisions).";
    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/

    private static void processFaceVertex(IndexGroup indices,
                                          List<Vector3f> textCoordList,
                                          List<Vector3f> normList,
                                          List<Integer> indicesList,
                                          float[] texCoordArr,
                                          float[] normArr) {
        // Set index for vertex coordinates
        int posIndex = indices.indexPos;
        indicesList.add(posIndex);

        // Reorder texture coordinates
        if (indices.indexTextureCoord >= 0) {
            Vector3f textCoord = textCoordList.get(indices.indexTextureCoord);
            texCoordArr[posIndex * 2] = textCoord.x();
            texCoordArr[posIndex * 2 + 1] = 1 - textCoord.y();
        }

        // Reorder vectornormals
        if (indices.indexNormal >= 0) {
            Vector3f vecNorm = normList.get(indices.indexNormal);
            normArr[posIndex * 3] = vecNorm.x();
            normArr[posIndex * 3 + 1] = vecNorm.y();
            normArr[posIndex * 3 + 2] = vecNorm.z();
        }
    }


    private static Mesh reorderLists(GameWorld gameWorld,
                                     Class classpath,
                                     String filename,
                                     String description,
                                     List<Vector3f> posList,
                                     List<Vector3f> textCoordList,
                                     List<Vector3f> normList,
                                     List<Face> facesList) {
        List<Integer> indices = new ArrayList<>();
        // Create position array in the order it has been declared
        float[] posArr = new float[posList.size() * 3];
        int i = 0;
        for (Vector3f pos : posList) {
            posArr[i * 3] = pos.x();
            posArr[i * 3 + 1] = pos.y();
            posArr[i * 3 + 2] = pos.z();
            i++;
        }

        float[] textCoordArr = new float[posList.size() * 2];
        float[] normArr = new float[posList.size() * 3];

        for (Face face : facesList) {
            IndexGroup[] faceVertexIndices = face.getFaceVertexIndices();
            for (IndexGroup indValue : faceVertexIndices) {
                processFaceVertex(indValue, textCoordList, normList,
                        indices, textCoordArr, normArr);
            }
        }

        int[] indicesArr = indices.stream().mapToInt((Integer v) -> v).toArray();


        try{
            return new TriangleSetMesh(
                    gameWorld, posArr, textCoordArr, normArr, indicesArr, classpath, filename, description);
        } catch (Exception e) {
            System.err.println(
                    String.format(
                            WARNING_MESH_WITHOUT_TRIANGLE_SET,
                            classpath.getCanonicalName() + " : " + filename
                    )
            );
            return new Mesh(gameWorld, posArr, textCoordArr, normArr, indicesArr, classpath, filename, description);
        }
    }
    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    public static Mesh loadMesh(GameWorld gameWorld,
                                Class classpath,
                                String filename,
                                String description) throws IOException {
        return loadMesh(gameWorld, classpath, filename, description, Vector3f.VECTOR_111);
    }

    public static Mesh loadMesh(GameWorld gameWorld,
                                Class classpath,
                                String filename,
                                String description,
                                Vector3f scale) throws IOException {
        /* ---- INITIALIZATION ---- */
        ArrayList<Vector3f> vertices = new ArrayList<>();
        ArrayList<Vector3f> textures = new ArrayList<>();
        ArrayList<Vector3f> normals = new ArrayList<>();
        ArrayList<Face> faces = new ArrayList<>();
        BufferedReader sourceReader =
                new BufferedReader(new InputStreamReader(classpath.getResourceAsStream(filename)));


        /* ---- SPLITTING OBJ INTO LISTS ---- */
        String line;
        while((line = sourceReader.readLine()) != null) {
            String[] tokens = line.split("\\s+");
            switch (tokens[0]) {
                case "v":
                    //GEOMETRIC VERTEX
                    vertices.add(
                            new Vector3f(
                                    Float.parseFloat(tokens[1]) * scale.x(),
                                    Float.parseFloat(tokens[2]) * scale.y(),
                                    Float.parseFloat(tokens[3]) * scale.z()));
                    break;
                case "vt":
                    //TEXTURE COORDINATE
                    textures.add(
                            new Vector3f(
                                    Float.parseFloat(tokens[1]),
                                    Float.parseFloat(tokens[2]),
                                    0f));
                    break;
                case "vn":
                    //VERTEX NORMAL
                    normals.add(
                            new Vector3f(
                                    Float.parseFloat(tokens[1]),
                                    Float.parseFloat(tokens[2]),
                                    Float.parseFloat(tokens[3])));
                    break;
                case "f":
                    //FACE
                    faces.add(
                            new Face(
                                    tokens[1],
                                    tokens[2],
                                    tokens[3]));
                    break;
                default:
                    break;
            }
        }
        return reorderLists(gameWorld, classpath, filename, description, vertices, textures, normals, faces);
    }
    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/
    /*--------------------*/
}


class Face {

    /* ATTRIBUTES
    /*--------------------*/

    /** List of IndexGroup groups for a face triangle (3 vertices per face).*/
    private IndexGroup[] idxGroups = new IndexGroup[3];
    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/

    private IndexGroup parseLine(String line) {
        IndexGroup idxGroup = new IndexGroup();
        String[] tokenLines = line.split("/");
        int length = tokenLines.length;
        idxGroup.indexPos = Integer.parseInt(tokenLines[0]) - 1;

        if (length > 1) {
            // It can be empty if the obj does not define texture coords
            // In this case, we must use color, but normals are still required
            String textCoord = tokenLines[1];
            idxGroup.indexTextureCoord = (textCoord.length() > 0) ?
                    Integer.parseInt(textCoord) - 1 : IndexGroup.NO_VALUE;
            if (length > 2) {
                idxGroup.indexNormal = Integer.parseInt(tokenLines[2]) - 1;
            }
        }
        return idxGroup;
    }

    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    public IndexGroup[] getFaceVertexIndices() {
        return idxGroups;
    }

    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    public Face(String v1, String v2, String v3) {
        idxGroups = new IndexGroup[3];
        idxGroups[0] = parseLine(v1);
        idxGroups[1] = parseLine(v2);
        idxGroups[2] = parseLine(v3);
    }

    /*--------------------*/
}



class IndexGroup {
    public static final int NO_VALUE = -1;
    public int indexPos;
    public int indexTextureCoord;
    public int indexNormal;

    public IndexGroup() {
        indexPos = NO_VALUE;
        indexTextureCoord = NO_VALUE;
        indexNormal = NO_VALUE;
    }
}
