package spc.clockwork.window;

import com.sun.istack.internal.NotNull;
import org.lwjgl.glfw.GLFW;
import spc.clockwork.collections.GameWorld;

import java.util.Iterator;

import static org.lwjgl.glfw.GLFW.*;

/**
 * A class that stores and controls the behavior of a window that belongs to the application
 * @author wize
 * @version 2 (12 June 2018)
 */
public class Window {
    /* ATTRIBUTES
    /*--------------------*/
    private static final String ERROR_WINDOW_ID_IS_NULL =
            "Error: the window was not properly initialized";

    /** A default window width in pixels */
    private static final int DEFAULT_WINDOW_WIDTH = 1024;
    /** A default window height in pixels */
    private static final int DEFAULT_WINDOW_HEIGHT = 768;
    /** A default window title */
    private static final String DEFAULT_WINDOW_TITLE = "Clockwork";


    /** A pointer to the window instance */
    public final long id;
    /** Window title */
    private String title;
    /** Window width */
    private int width;
    /** Window height */
    private int height;
    /** A {@link WindowManager} that is responsible for managing this window */
    private final WindowManager manager;
    /** A position of this {@link Window} in the {@link WindowManager} it is attached to */
    private final int positionInManager;


    /** Enum, which specifies the type of the cursor */
    public enum CursorType {
        UNLIMITED(GLFW_CURSOR_DISABLED),
        HIDDEN(GLFW_CURSOR_HIDDEN),
        NORMAL(GLFW_CURSOR_NORMAL);
        private final int value;
        /**
         * Default constructor
         * @param value the value of the enum
         */
        CursorType(int value) {
            this.value = value;
        }
        /**
         * Gets the value of the cursor type in terms of GLFW
         * @return the value of the cursor type
         */
        private int getValue() {
            return this.value;
        }
    }

    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/

    /**
     * Draws the rendered image in this window.
     * Is called in the last render stage.
     */
    private void drawRenderedImage() {
        glfwSwapBuffers(id);
    }

    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * Checks whether there was a request to close the window
     * @return true, if there was the request, false otherwise
     */
    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(this.id);
    }


    /**
     * Shows the window
     * 
     */
    public void show() {
        GLFW.glfwShowWindow(this.id);
    }


    /**
     * Hides the window
     */
    public void hide() {
        GLFW.glfwHideWindow(this.id);
    }


    // TODO: IMPLEMENT WINDOW GETTERS (SIZE, POSITION, MONITOR)..
    // TODO: IMPLEMENT WINDOW SETTERS (SIZE, POSITION, MONITOR)..


    /**
     * Renders an image in the window
     * @param gameWorld the {@link GameWorld} instance
     */
    public void render(GameWorld gameWorld) {

        /* ---- MAKING CONTEXT CURRENT ---- */
        if (GLFW.glfwGetCurrentContext() != this.id)
            GLFW.glfwMakeContextCurrent(this.id);

        /* ---- RENDERING ---- */
        Iterator<String> renderingQueue = gameWorld.layerQueueIterator(this.positionInManager);
        while(renderingQueue.hasNext())
            gameWorld.getLayer(renderingQueue.next()).render();
        this.drawRenderedImage();
    }


    /**
     * Terminates the window
     */
    public void terminate() {
        //TODO: IMPLEMENT WINDOW CLEANUP
        this.hide();
        GLFW.glfwDestroyWindow(this.id);
    }

    /**
     * Gets the window aspect ratio
     * @return window's aspect ratio
     */
    public float getAspectRatio() {
        return (float) this.getWidth() / (float) this.getHeight();
    }

    /**
     * Gets the window's title
     * @return title of this window
     */
    public String getTitle() {
        return this.title;
    }


    /**
     * Window width getter
     * @return this window width
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Window height getter
     * @return this window height
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Sets the window's title
     * @param title the new window title
     */
    public void setTitle(String title) {
        glfwSetWindowTitle(this.id, this.title = title);
    }

    /**
     * Sets the cursor type
     * @param type the type of the cursor: UNLIMITED, HIDDEN, NORMAL
     */
    public void setCursorType(CursorType type) {
        GLFW.glfwSetInputMode(this.id, GLFW_CURSOR, type.getValue());
    }
    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * Default window constructor
     */
    public Window(@NotNull final WindowManager manager) {

        /* ---- CREATING GLFW WINDOW ---- */
        this.id = GLFW.glfwCreateWindow(
                DEFAULT_WINDOW_WIDTH,
                DEFAULT_WINDOW_HEIGHT,
                DEFAULT_WINDOW_TITLE,
                0,
                0);
        if (this.id == 0)
            throw new IllegalStateException(ERROR_WINDOW_ID_IS_NULL);

        /* ---- FILLING THE WINDOW OBJECT WITH DATA ---- */
        this.width = DEFAULT_WINDOW_WIDTH;
        this.height = DEFAULT_WINDOW_HEIGHT;
        this.setTitle("");

        /* ---- ATTACHING THIS TO THE WINDOW MANAGER ---- */
        this.manager = manager;
        this.positionInManager = manager.size();
        this.manager.addWindow(this);
    }

    /*--------------------*/

}
