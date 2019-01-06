package spc.clockwork.core;

import spc.clockwork.collections.GameWorld;
import spc.clockwork.gameobject.GameObject;
import spc.clockwork.window.WindowManager;

/**
 * The class that implements this interface is the main file of the game, which {@link Clockwork} runs.
 * The interface provides the game developer with a flexible way to initiate and manage the application running process
 * by means of programming {@link spc.clockwork.window.Window} behavior and sending the {@link GameObjectCollectionOld} to the
 * rendering systems.
 *
 * The game developer has an absolute freedom of changing the Game World state.
 */
public interface ClockworkGameLogic {

    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * Initiates the game state; is called before the first Clockwork tick
     * @param windowManager a window manager of this game
     */
    public void onStart(WindowManager windowManager);


    /**
     * Updates the game state; is called on each Clockwork tick
     * @param windowManager a window manager of Clockwork
     */
    public void onTick(WindowManager windowManager);


    /**
     * Gets the {@link GameWorld}, which will be read and/or processed
     * the method is called on each Clockwork render and/or tick for each window
     * the argument is the id of the window, given by manager (not the GLFW). You can forget about the argument,
     * if you have only one window
     * @return an ordered collection of {@link GameObject}s, as an instance of {@link GameWorld},
     */
    public GameWorld getGameWorld();


    /**
     * Checks, whether the next game tick will happen, or the Clockwork shall stop
     * @return true, if the Clockwork shall continue ticking, and false otherwise
     */
    public boolean shallTick();


    /*--------------------*/

}
