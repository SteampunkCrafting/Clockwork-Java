package spc.clockwork.graphics;

import de.matthiasmann.twl.utils.PNGDecoder;
import spc.clockwork.collections.GameWorld;
import spc.clockwork.core.GameAsset;
import spc.clockwork.gameobject.entity.Entity;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

/**
 * An immutable class that gets, decodes (with the help of a side library) a PNG image and loads it into the GPU
 * memory, providing a pointer to it. Can be assigned to the {@link Material} in order to be rendered on the
 * {@link Entity}
 *
 * @author wize
 * @version 1 (4 June 2018)
 */
public class Texture extends GameAsset {

    /* ATTRIBUTES
    /*--------------------*/

    /** A pointer to the texture in the GPU */
    private final int id;


    /* ---- GLOBAL TEXTURE PARAMETERS ---- */
    /** The number of bytes per image pixel */
    private static final int BYTES_PER_PIXEL = 4;
    /** The number of bytes per image component (1 chanel of 1 pixel) */
    private static final int BYTES_PER_COMPONENT = 1; //TODO: Understand why this is unused
    /** The default mipmap reduction of the texture, where 0 is no reduction at all */
    private static final int DEFAULT_MIPMAP_REDUCTION = 0;
    /** Default internal image format */
    private static final int DEFAULT_INTERNAL_IMAGE_FORMAT = GL_RGBA;
    /** Default image format */
    private static final int DEFAULT_IMAGE_FORMAT = GL_RGBA;
    /** Default image border */
    private static final int DEFAULT_IMAGE_BORDER = 0;
    /** Default pixel data type */
    private static final int DEFAULT_ELEMENT_TYPE = GL_UNSIGNED_BYTE;

    /** Texture width in pixels */
    private final int width;

    /** Texture height in pixels */
    private final int heigt;
    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/

    /**
     * An abstract method that is called upon the termination stage of the resource.
     * Is be overridden by the subclass, if the termination requires to do something
     * (e.g. making an off-heap memory cleanup)
     */
    @Override
    protected void onTerminate() {
        glDeleteTextures(this.id);
    }
    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * Binds the texture to the GPU
     */
    public void bind() {
        if (this.isTerminated()) {
            System.err.println("Warning, this texture was terminated");
            return;
        }
        glBindTexture(GL_TEXTURE_2D, this.id);
    }


    /**
     * Unbinds the texture from the GPU
     */
    public void unbind() {
        if (this.isTerminated()) {
            return;
        }
        glBindTexture(GL_TEXTURE_2D, 0);
    }


    /**
     * Image width getter
     */
    public int getWidth() {
        return this.width;
    }


    /**
     * Image height getter
     */
    public int getHeight() {
        return this.heigt;
    }

    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * A default constructor that takes the classpath and the address to the png image, then loads, decodes and stores
     * this image as a new instance of texture
     */
    public Texture(GameWorld gameWorld, Class classPath, String filename, String description) throws IOException {
        this(gameWorld, classPath.getResourceAsStream(filename), classPath, filename, description);
    }


    /**
     * A constructor that is used to create the texture out of the buffer
     */
    protected Texture(GameWorld gameWorld,
                      InputStream inputStream,
                      Class classPath,
                      String filename,
                      String description) throws IOException {
        super(gameWorld, classPath, filename, description);

        /* ---- Creating an instance of decoder and binding an image to it ---- */
        PNGDecoder image = new PNGDecoder(inputStream);

        /* ---- Filling in some parameters of the object ---- */
        this.width = image.getWidth();
        this.heigt = image.getHeight();

        /* ---- Buffering an image into a ByteBuffer ---- */
        ByteBuffer imageBuffer = ByteBuffer.allocateDirect(BYTES_PER_PIXEL * image.getWidth() * image.getHeight());
        image.decode(imageBuffer, image.getWidth() * BYTES_PER_PIXEL, PNGDecoder.Format.RGBA);
        imageBuffer.flip();

        /* ---- Creating a texture instance in the GPU ---- */
        this.id = glGenTextures();

        /* ---- Binding the texture and buffering the image into GPU ---- */
        glBindTexture(GL_TEXTURE_2D, this.id);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexImage2D(
                GL_TEXTURE_2D,
                DEFAULT_MIPMAP_REDUCTION,
                DEFAULT_INTERNAL_IMAGE_FORMAT,
                image.getWidth(),
                image.getHeight(),
                DEFAULT_IMAGE_BORDER,
                DEFAULT_IMAGE_FORMAT,
                DEFAULT_ELEMENT_TYPE,
                imageBuffer
        );

        /* ---- Setting up the texture rendering properties ---- */
        //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST); //TODO: Find out what this is for
        //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST); //TODO: Find out what this is for
        glGenerateMipmap(GL_TEXTURE_2D);

    }
    /*--------------------*/
}
