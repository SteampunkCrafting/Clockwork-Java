package spc.clockwork.window;

import org.lwjgl.opengl.GL;
import spc.clockwork.core.Input;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


/**
 * A class that is responsible for setting up the GLFW, holding windows and
 * @author wize
 * @version 0 (2018.03.28)
 */
public class WindowManager {

    /* ATTRIBUTES
    /*--------------------*/

    /** An error message text, if we cannot initRender GLFW */
    private static final String ERROR_CANNOT_INIT_GLFW =
            "Error: Failed to initialize GLFW (Window management System)";
    /** An error message text, if we have different list of inputs and windows sizes */
    private static final String ERROR_DIFFERENT_INPUT_WINDOW_LIST_SIZES =
            "Error: Different window and input sizes";


    /** Windows that are controlled by this manager */
    private ArrayList<Window> windows;
    /** Inputs of windows. There must be 1to1 index correspondence between windows and inputs */
    private ArrayList<Input> inputs;

    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    public int size() {
        if (windows.size() == inputs.size())
            return windows.size();
        System.out.println(ERROR_DIFFERENT_INPUT_WINDOW_LIST_SIZES);
        return windows.size();
    }


    /**
     * Adds new window and creates a new {@link Input} for it
     * @param window
     */
    public void addWindow(Window window) {
        this.windows.add(window);
        this.inputs.add(new Input(window));
    }


    /**
     * Terminates the window and its input at positionInList and sets their values to null
     * 
     * @param positionInList a position in list
     */
    public void destroyWindow(int positionInList) {
        if (this.inputs.get(positionInList) != null)
            this.inputs.get(positionInList).terminate();
        if (this.windows.get(positionInList) != null)
            this.windows.get(positionInList).terminate();

        this.inputs.set(positionInList, null);
        this.windows.set(positionInList, null);
    }


    /**
     * Window getter
     * @param positionInList an index of an {@link ArrayList}
     * @return an instance of {@link Window} or null
     */
    public Window getWindow(int positionInList) {
        return (this.windows.size() > positionInList) ? windows.get(positionInList) : null;
    }


    public Input getInput(int positionInList) {
        return (this.inputs.size() > positionInList) ? inputs.get(positionInList) : null;
    }


    /**
     * Polls events of inputs and draws windows
     */
    public void pollEvents() {
        glfwPollEvents();
    }


    /**
     * Terminates all windows, then terminates GLFW
     */
    public void terminate() {
        for (int i = 0; i < windows.size(); i++) {
            this.destroyWindow(i);
        }
        org.lwjgl.glfw.GLFW.glfwTerminate();
    }


    /**
     * gets the main window out
     * @return the first (main) window
     */
    public Window mainWin() {
        return this.getWindow(0);
    }


    /**
     * gets the main input out
     * @return the first (main) window input
     */
    public Input mainInp() {
        return this.getInput(0);
    }
    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * Default WindowManager constructor
     */
    public WindowManager() {
        /* INITIALIZATION OF FIELDS */
        this.windows = new ArrayList<>();
        this.inputs = new ArrayList<>();

        /* INITIALIZATION OF GLFW */
        if (!glfwInit()) throw new IllegalStateException(ERROR_CANNOT_INIT_GLFW);

        /* GLOBAL WINDOW HINTS SETUP */
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwSwapInterval(1);

        /* ADDITION OF THE FIRST WINDOW */
        new Window(this); // Addition to a window manager happens in the window constructor

        /* INITIAL RENDERING SETUP */
        glfwMakeContextCurrent(this.getWindow(0).id);
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
    }
    /*--------------------*/

}
