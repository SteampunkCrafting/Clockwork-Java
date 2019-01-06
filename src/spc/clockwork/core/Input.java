package spc.clockwork.core;


import org.lwjgl.glfw.GLFW;
import spc.clockwork.util.math.vector.Vector2f;
import spc.clockwork.window.Window;

import java.util.HashSet;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

/**
 * This class handles the GLFW input and transforms it into a comfortable for the game developer structure
 * This class is only adopted for the mouse and keyboard usage and requires extensions to be provided,
 *     if any other input devices are needed.
 * Each Clockwork Input instance must be attached to the {@link spc.clockwork.window.Window} instance at the
 *     construction.
 * @author wize
 * @version 0 (2018.03.28)
 */
public class Input {
    /* ATTRIBUTES
    /*--------------------*/

    /** An error message text, if we receive an input from the wrong window */
    private static final String ERROR_WRONG_WINDOW_INPUT =
            "Error: This Input has received an input from the wrong window";


    /** A {@link spc.clockwork.window.Window}, to which this {@link Input} is attached */
    private final Window window;

    /** Current position of the cursor */
    private Vector2f cursorPosition;

    /** The difference between current cursor position and its position at the previous tick */
    private Vector2f cursorPositionOffset;

    /** States whether the cursor is within the window */
    private boolean isCursorInWindow;

    /** States whether the mouse was moved during this tick */
    private boolean mouseMoved;

    /** A {@link HashSet} of keys pressed */
    private final HashSet<Integer> keysPressed;

    /** A {@link HashSet} of mouse buttons pressed */
    private final HashSet<Integer> mouseButtonsPressed;

    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/

    /**
     * Checks, whether the {@link Input} has received invalid information about its window
     * 
     * @param windowID the window ID, given by the event handler
     * @return TRUE, if the input is NOT VALID, false otherwise
     */
    private boolean isInvalidInput(long windowID) {
        if (windowID == this.window.id)
            return false;
        System.err.println(ERROR_WRONG_WINDOW_INPUT);
        return true;
    }


    /* ---- CALLBACK METHODS ---- */
    /**
     * Updates the Map of pressed keys
     * 
     * @param windowID a window pointer
     * @param keyID a GLFW id for of a key
     * @param scancodeID a scancode of a key (is not used, but is required by contract)
     * @param actionID a GLFW id of actions (such as PRESS or RELEASE)
     * @param modsID unknown parameter
     */
    private void updateKeyboard(long windowID, int keyID, int scancodeID, int actionID, int modsID) {
        if(this.isInvalidInput(windowID))
            return;
        if (actionID == GLFW_PRESS)
            keysPressed.add(keyID);
        else if (actionID == GLFW_RELEASE)
            keysPressed.remove(keyID);
    }

    /**
     * Updates the Map of pressed mouse buttons
     * 
     * @param windowID a window pointer
     * @param keyID a GLFW id for of a key
     * @param actionID a GLFW id of actions (such as PRESS or RELEASE)
     * @param modsID unknown parameter
     */
    private void updateMouseButtons(long windowID, int keyID, int actionID, int modsID) {
        if(this.isInvalidInput(windowID))
            return;
        if (actionID == GLFW_PRESS)
            this.mouseButtonsPressed.add(keyID);
        else if (actionID == GLFW_RELEASE)
            this.mouseButtonsPressed.remove(keyID);
    }

    /**
     * updates cursor position
     * 
     * @param windowID the window id
     * @param cursorPositionX position of cursor x
     * @param cursorPositionY position of cursor y
     */
    private void updateCursorPos(long windowID, double cursorPositionX, double cursorPositionY) {
        this.mouseMoved = true;
        if(this.isInvalidInput(windowID))
            return;
        Vector2f cursorPosition = new Vector2f((float) cursorPositionX, (float) cursorPositionY);
        this.cursorPositionOffset = cursorPosition.sub(this.getCursorPosition());
        this.cursorPosition = cursorPosition;
    }

    /**
     * updates "If the cursor is inside the window"
     * 
     * @param windowID the window id
     * @param isInWindow the boolean variable
     */
    private void updateCursorEnter(long windowID, boolean isInWindow) {
        if(this.isInvalidInput(windowID))
            return;
        this.isCursorInWindow = isInWindow;
    }


    /* ---- CALLBACK SETUP METHODS ---- */
    /**
     * Sets the keyboard callback
     */
    private void setKeyCallback() {
        GLFW.glfwSetKeyCallback(
                this.window.id,
                this::updateKeyboard);
    }

    /**
     * Sets the mouse buttons callback
     * 
     */
    private void setMouseButtonsCallback() {
        GLFW.glfwSetMouseButtonCallback(
                this.window.id,
                this::updateMouseButtons
        );
    }

    /**
     * Sets cursor position callback
     * 
     */
    private void setCursorPosCallback() {
        GLFW.glfwSetCursorPosCallback(
                this.window.id,
                this::updateCursorPos
        );
    }

    /**
     * Sets cursor enter callback
     */
    private void setCursorEnterCallback() {
        GLFW.glfwSetCursorEnterCallback(
                this.window.id,
                this::updateCursorEnter
        );
    }

    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * Gets the current cursor position in the window space
     * @return the current cursor position
     */
    public Vector2f getCursorPosition() {
        return this.cursorPosition;
    }

    /**
     * Gets the difference between current and previous positions of the cursor
     * @return the cursor position offset in the window space
     */
    public Vector2f getCursorPositionOffset() {
        return this.mouseMoved ? this.cursorPositionOffset : Vector2f.ZERO_VECTOR;
    }

    /**
     * Checks whether the cursor is in window
     * @return true, if the cursor is in window, else return false
     */
    public boolean isCursorInWindow() {
        return this.isCursorInWindow;
    }

    /**
     * Checks if a key on a keyboard is pressed
     * 
     * @param keyID a GLFW key id
     * @return true, if the key is pressed, and false if the key is released
     */
    public boolean isKeyPressed(int keyID) {
        return keysPressed.contains(keyID);
    }

    /**
     * Checks if a button on a mouse is pressed
     * 
     * @param keyID a GLFW key id
     * @return true, if the button is pressed, and false if the button is released
     */
    public boolean isMousePressed(int keyID) {
        return mouseButtonsPressed.contains(keyID);
    }

    /**
     * Checks whether mouse was moved during this tick
     * @return true, if mouse was moved during this tick, else return false
     */
    public boolean isMouseMoved() {
        return this.mouseMoved;
    }

    /**
     * Checks a name for a key
     * 
     * @param keyID a GLFW key id
     * @return the key name as {@link String}
     */
    public String getKeyName(int keyID) {
        return GLFW.glfwGetKeyName(keyID, 0);
    }

    /**
     * Cleans up some things in the
     * Sets the Input::mouseMoved to false and the offset to zero vector
     * The method is called once per tick in its beginning
     */
    public void cleanup() {
        this.cursorPositionOffset = Vector2f.ZERO_VECTOR;
        this.mouseMoved = false;
    }

    public void terminate() {
        //TODO: IMPLEMENT INPUT TERMINATE
    }

    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * A standard constructor
     * @param window a reference to window, that will be attached to this
     */
    public Input(Window window) {
        this.window = window;
        this.keysPressed = new HashSet<>();
        this.mouseButtonsPressed = new HashSet<>();

        this.setKeyCallback();
        this.setCursorPosCallback();
        this.setCursorEnterCallback();
        this.setMouseButtonsCallback();

        double[] x = new double[1], y = new double[1];
        GLFW.glfwGetCursorPos(this.window.id, x, y);
        this.cursorPosition = new Vector2f((float)x[0], (float)y[0]);
    }

    /*--------------------*/
}
