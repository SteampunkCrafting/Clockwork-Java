package spc.clockwork.graphics.mesh;


import org.lwjgl.system.MemoryUtil;
import spc.clockwork.collections.GameWorld;
import spc.clockwork.core.GameAsset;
import spc.clockwork.gameobject.entity.Entity;
import spc.clockwork.graphics.RenderSystem;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.memFree;

/**
 * {@link Mesh} is a set of vertices that forms a model of the {@link Entity}
 * This class is "immutable" (in fact, due to safety reasons, it is mutable) and keeps only pointers
 * to Vertex Buffer Objects and Vertex Array Object inside. The buffered model itself is stored in the GPU memory
 * for better performance.
 *
 * Since the object was successfully constructed, all the data about the mesh, required for rendering, is stored in the
 * memory of the GPU. We do not need to do anything else, but assign this model to different instances of
 * {@link Entity}.
 *
 * In order to render this model, the call of render() from the {@link RenderSystem} must be obtained.
 *
 * WARNING, calling render from somewhere else may cause the program crash because the {@link RenderSystem} makes
 * several preparations before the model rendering.
 *
 * When the model is not required anymore. It is reasonable to clear the occupied GPU memory with the method provided in
 * this class;
 *
 * @author wize
 * @version 2 (4 June 2018)
 */
public class Mesh extends GameAsset {


    /* ATTRIBUTES
    /*--------------------*/

    /* ---- CONSTANTS, WARNINGS AND ERRORS ---- */
    /** A warning that appears, when the mesh is marked as deleted and nevertheless was rendered*/
    private static final String WARNING_MESH_DELETED =
            "Warning: the mesh is deleted and thus cannot be rendered";


    /* ---- GENERAL MESH STATE ---- */
    /** Pointer to vertex buffer object of the mesh, which stores positions */
    private final int positionsVBO;
    /** Pointer to texture coordinate vertex buffer object */
    private final int textureCoordinatesVBO;
    /** Pointer to vertex buffer object of the mesh, which stores normals */
    private final int normalsVBO;
    /** Pointer to vertex buffer object of the mesh, which stores indices */
    private final int indicesVBO;
    /** Pointer to vertex array object of the mesh */
    private final int VAO;
    /** Size of the mesh */
    private final int vertexCount;
    /** Starting index of the mesh drawing process (used in glDrawArrays/glDrawElements) */
    private static final int STARTING_DRAW_INDEX = 0;
    /* States, whether the mesh is deleted or not */
    private boolean isDeleted;


    /* ---- POSITIONS ATTRIBUTE PARAMETERS ---- */
    /** Location of positions attribute. Must be also mentioned in shader programs */
    private static final int VAO_POSITIONS_LOCATION = 0;
    /** Total position vertex size (dimensions) (possible value range: 1~4) */
    private static final int VAO_POSITIONS_SIZE = 3;
    /** Type of position vertex */
    private static final int VAO_POSITIONS_TYPE = GL_FLOAT;
    /** Are position vertex elements normalized */
    private static final boolean VAO_POSITIONS_NORMALIZED = false;
    /** The offset between the end of this position vertex and the beginning of the new one */
    private static final int VAO_POSITIONS_STRIDE = 0;
    /** The pointer to the first component of the first generic position vertex */
    private static final int VAO_POSITIONS_POINTER_TO_FIRST = 0;



    /* ---- TEXTURE COORDINATES ATTRIBUTE PARAMETERS ---- */
    /** Location of texture coordinates attribute. Must be also mentioned in shader programs */
    private static final int VAO_TEXTURE_COORDINATES_LOCATION = 1;
    /** Total texture coordinates vertex size (dimensions) (possible value range: 1~4) */
    private static final int VAO_TEXTURE_COORDINATES_SIZE = 2;
    /** Type of texture coordinates vertex */
    private static final int VAO_TEXTURE_COORDINATES_TYPE = GL_FLOAT;
    /** Are texture coordinates vertex elements normalized */
    private static final boolean VAO_TEXTURE_COORDINATES_NORMALIZED = false;
    /** The offset between the end of this texture coordinates vertex and the beginning of the new one */
    private static final int VAO_TEXTURE_COORDINATES_STRIDE = 0;
    /** The pointer to the first component of the first generic TEXTURE_COORDINATE vertex */
    private static final int VAO_TEXTURE_COORDINATES_POINTER_TO_FIRST = 0;


    /* ---- NORMALS ATTRIBUTE PARAMETERS ---- */
    /** Location of normals attribute. Must be also mentioned in shader programs */
    private static final int VAO_NORMALS_LOCATION = 2;
    /** Total position vertex size (dimensions) (possible value range: 1~4) */
    private static final int VAO_NORMALS_SIZE = 3;
    /** Type of position vertex */
    private static final int VAO_NORMALS_TYPE = GL_FLOAT;
    /** Are position vertex elements normalized */
    private static final boolean VAO_NORMALS_NORMALIZED = false;
    /** The offset between the end of this position vertex and the beginning of the new one */
    private static final int VAO_NORMALS_STRIDE = 0;
    /** The pointer to the first component of the first generic position vertex */
    private static final int VAO_NORMALS_POINTER_TO_FIRST = 0;

    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/
    /**
     * Deletes the mesh from the GPU memory.
     */
    private void delete() {
        if(this.isDeleted) return;
        this.isDeleted = true;

        /* ---- DELETING VBOs ---- */
        glDeleteBuffers(new int[]{
                this.positionsVBO,
                this.textureCoordinatesVBO,
                this.normalsVBO,
                this.indicesVBO,
        });


        /* ---- DELETING VAO ---- */
        glDeleteVertexArrays(VAO);
    }

    @Override
    protected void onTerminate() {
        this.delete();
    }

    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * Draws this mesh.
     * Warning: this method must be called only by the {@link RenderSystem}, because it uses off-heap memory allocation
     * and may crush the app, if used incorrectly
     */
    public void render() {
        if (this.isDeleted()) {
            System.err.println(WARNING_MESH_DELETED);
            return;
        }


        /* ---- BINDING VAO ---- */
        glBindVertexArray(VAO);


        /* ---- BINDING IDX VBO ---- */
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.indicesVBO);


        /* ---- ACTIVATING ATTRIBUTES ---- */
        glEnableVertexAttribArray(VAO_POSITIONS_LOCATION);
        glEnableVertexAttribArray(VAO_TEXTURE_COORDINATES_LOCATION);
        glEnableVertexAttribArray(VAO_NORMALS_LOCATION);


        /* ---- DRAWING ELEMENTS ---- */
        glDrawElements(GL_TRIANGLES, this.getVertexCount(), GL_UNSIGNED_INT, STARTING_DRAW_INDEX);


        /* ---- UNBINDING IDX VBO ---- */
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);


        /* ---- DEACTIVATING ATTRIBUTES ---- */
        glDisableVertexAttribArray(VAO_POSITIONS_LOCATION);
        glDisableVertexAttribArray(VAO_TEXTURE_COORDINATES_LOCATION);
        glDisableVertexAttribArray(VAO_NORMALS_LOCATION);


        /* ---- UNBINDING VAO ---- */
        glBindVertexArray(0);
    }


    /**
     * Vertex count accessor
     * @return number of position vertices of this {@link Mesh}
     */
    public int getVertexCount() {
        return this.vertexCount;
    }

    /**
     * Checks, whether the Mesh was deleted from the GPU memory
     * @return true, if it was deleted, false otherwise
     */
    public boolean isDeleted() {
        return this.isDeleted;
    }

    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/


    /**
     * A constructor of Mesh, which does not require file names
     * @param positions a dumped array of positions
     * @param textureCoordinates a dumped array of texture coordinates
     * @param normals a dumped array of normals
     * @param indices an array of indices
     * @param description description of the {@link GameAsset}
     */
    public Mesh(GameWorld gameWorld,
                float[] positions,
                float[] textureCoordinates,
                float[] normals,
                int[] indices,
                String description) {
        this(gameWorld, positions, textureCoordinates, normals, indices, null, null, description);
    }



    /**
     * A standard constructor of Mesh, used by model decoders to create meshes from files, pre-generated by 3d editors
     * @param gameWorld the GameWorld of this
     * @param positions a dumped array of positions
     * @param textureCoordinates a dumped array of texture coordinates
     * @param normals a dumped array of normals
     * @param indices an array of indices
     * @param classPath the path to the root class
     * @param filePath the path to the file, which stores the encoded copy of this model
     * @param description description of the {@link GameAsset}
     */
    public Mesh(GameWorld gameWorld,
                float[] positions,
                float[] textureCoordinates,
                float[] normals,
                int[] indices,
                Class classPath,
                String filePath,
                String description) {
        super(gameWorld, classPath, filePath, description);

        /* ---- SETTING NAME TO THE MESH ---- */
        this.setName(this.getFilePath());

        /* ---- DETERMINING MESH SIZE ---- */
        this.vertexCount = indices.length;


        /* ---- GPU OBJECTS CREATION ---- */
        /* -- VAO -- */
        this.VAO = glGenVertexArrays();
        /* -- positionsVBO -- */
        this.positionsVBO = glGenBuffers();
        /* -- textureCoordinatesVBO -- */
        this.textureCoordinatesVBO = glGenBuffers();
        /* -- normalsVBO -- */
        this.normalsVBO = glGenBuffers();
        /* -- indicesVBO -- */
        this.indicesVBO = glGenBuffers();



        /* ---- BUFFERING DATA INTO VBOs ---- */
        /* -- positionsVBO -- */
        glBindBuffer(GL_ARRAY_BUFFER, this.positionsVBO);
        FloatBuffer positionsBuffer = MemoryUtil.memAllocFloat(positions.length);
        positionsBuffer.put(positions).flip();
        glBufferData(GL_ARRAY_BUFFER, positionsBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        memFree(positionsBuffer);
        /* -- textureCoordinatesVBO -- */
        glBindBuffer(GL_ARRAY_BUFFER, this.textureCoordinatesVBO);
        FloatBuffer textureCoordinatesBuffer = MemoryUtil.memAllocFloat(textureCoordinates.length);
        textureCoordinatesBuffer.put(textureCoordinates).flip();
        glBufferData(GL_ARRAY_BUFFER, textureCoordinatesBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        memFree(textureCoordinatesBuffer);
        /* -- normalsVBO -- */
        glBindBuffer(GL_ARRAY_BUFFER, this.normalsVBO);
        FloatBuffer normalsBuffer = MemoryUtil.memAllocFloat(normals.length);
        normalsBuffer.put(normals).flip();
        glBufferData(GL_ARRAY_BUFFER, normalsBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        memFree(normalsBuffer);
        /* -- indicesVBO -- */
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.indicesVBO);
        IntBuffer indicesBuffer = MemoryUtil.memAllocInt(indices.length);
        indicesBuffer.put(indices).flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        memFree(indicesBuffer);


        /* ---- VAO ATTRIBUTES SETUP ---- */
        /* -- binding VAO -- */
        glBindVertexArray(VAO);
        /* -- setting positions -- */
        glBindBuffer(GL_ARRAY_BUFFER, this.positionsVBO);
        glVertexAttribPointer(
                VAO_POSITIONS_LOCATION,
                VAO_POSITIONS_SIZE,
                VAO_POSITIONS_TYPE,
                VAO_POSITIONS_NORMALIZED,
                VAO_POSITIONS_STRIDE,
                VAO_POSITIONS_POINTER_TO_FIRST
        );
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        /* -- setting textureCoordinates -- */
        glBindBuffer(GL_ARRAY_BUFFER, this.textureCoordinatesVBO);
        glVertexAttribPointer(
                VAO_TEXTURE_COORDINATES_LOCATION,
                VAO_TEXTURE_COORDINATES_SIZE,
                VAO_TEXTURE_COORDINATES_TYPE,
                VAO_TEXTURE_COORDINATES_NORMALIZED,
                VAO_TEXTURE_COORDINATES_STRIDE,
                VAO_TEXTURE_COORDINATES_POINTER_TO_FIRST
        );
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        /* -- setting normals -- */
        glBindBuffer(GL_ARRAY_BUFFER, this.normalsVBO);
        glVertexAttribPointer(
                VAO_NORMALS_LOCATION,
                VAO_NORMALS_SIZE,
                VAO_NORMALS_TYPE,
                VAO_NORMALS_NORMALIZED,
                VAO_NORMALS_STRIDE,
                VAO_NORMALS_POINTER_TO_FIRST
        );
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        /* -- unbinding VAO -- */
        glBindVertexArray(0);
    }
    /*--------------------*/
}
