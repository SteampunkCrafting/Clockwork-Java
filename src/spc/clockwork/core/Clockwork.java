package spc.clockwork.core;


import spc.clockwork.collections.GameWorld;
import spc.clockwork.gameobject.ScriptedObject;
import spc.clockwork.util.timer.Timer;
import spc.clockwork.window.WindowManager;

import java.util.Iterator;

import static org.lwjgl.opengl.GL11.*;

/**
 * The main class of the game engine; singleton
 * Initiates the main loop and the window manager
 *
 * Clockwork terminology:
 *  1. Tick -- an update of the game state
 *  2. Set the clock -- initiate world and start the main loop
 *
 * @author wize
 * @version 0 (2018.03.26)
 */
public final class Clockwork { //TODO: FINISH THE CLOCKWORK

    /* ATTRIBUTES
    /*--------------------*/

    /** Number of ticks per second; should be kept constant */
    private final float TICKS_PER_SECOND;
    /** Maximum number of frames per second */
    private final float MAX_FRAMES_PER_SECOND;
    /** Evaluation period */
    private final float EVALUATION_PERIOD;


    /** An error message text, if we construct the create for the second time */
    private static final String ERROR_CLOCKWORK_ALREADY_EXISTS =
            "Error: the Clockwork already exists. Returning the pointer to an existing create";
    /** An error message text, if we call create without construction */
    private static final String ERROR_CLOCKWORK_IS_NULL =
            "Warning: the pointer to Clockwork is null, because you did not construct the singleton yet";


    /** A pointer to singleton */
    private static Clockwork pointerToClockwork;
    /** An external {@link ClockworkGameLogic} interface implementation
     * (basically, pointer to the instance of the game that Clockwork executes)*/
    private final ClockworkGameLogic gameLogic;
    private final WindowManager windowManager;

    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/

    /**
     * A method that performs a single onTick (i.e. an update a world state)
     */
    private void onTick() {
        /* ---- PREPARATION ---- */
        final int windowManagerSize = this.windowManager.size();
        for(int i = 0; i < windowManagerSize; i++) this.windowManager.getInput(i).cleanup();

        /* ---- POLLING EVENTS ---- */
        this.windowManager.pollEvents();

        /* ---- PROCESSING MAIN GAME LOGIC TICK ---- */
        this.gameLogic.onTick(this.windowManager);

        /* ---- PROCESSING ALL AVAILABLE AND ACTIVE GAME OBJECT SCRIPTS ---- */
        GameWorld gameWorld = this.gameLogic.getGameWorld();
        Iterator<ScriptedObject> it = gameWorld.scriptedObjectIterator();
        ScriptedObject s;
        while(it.hasNext()) if((s = it.next()).isActive()) s.onTick();
    }


    /**
     * A method that performs a single renderLayer (i.e. a single frame drawing)
     */
    private void onRender() {
        GameWorld gameWorld = this.gameLogic.getGameWorld();
        for (int i = 0; i < this.windowManager.size(); i++) {
            if(this.windowManager.getWindow(i) != null)
                this.windowManager.getWindow(i).render(gameWorld);
        }
    }


    /**
     * A method that prints statistics of Clockwork (framerate, tickrate, etc...)
     */
    private void onEvaluation(int tps, int fps) {
        //TODO DESIGN AND IMPLEMENT FLEXIBLE EVALUATION
        //THE CODE BELOW IS USED FOR THE DEBUG PURPOSE
        //System.out.println("fps:" + fps);
        //System.out.println("tps:" + tps);
    }


    /**
     * A method that performs the termination stage of the Clockwork
     */
    private void onTermination() {
        windowManager.terminate();
    }


    /**
     * Sets the global parameters for the engine working process
     */
    private void setGlobalParameters() {
        System.setProperty("java.awt.headless", "true");
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/

    /**
     * A method that returns or initiates Clockwork
     * @param gameLogic a class that stores the game state and describes all of the actions inside it
     * @return the first instance of Clockwork, which was created since the program beginning
     */
    public static Clockwork create(ClockworkGameLogic gameLogic) {
        if (pointerToClockwork == null)
            return pointerToClockwork = new Clockwork(gameLogic);
        System.err.println("\n" + ERROR_CLOCKWORK_ALREADY_EXISTS);
        return pointerToClockwork;
    }


    /**
     * A method that returns an already existing Clockwork singleton
     * @return a Clockwork singleton or null, if there is no Clockwork (will also make a warning, if so)
     */
    public static Clockwork get() {
        if (pointerToClockwork == null)
            System.err.println(ERROR_CLOCKWORK_IS_NULL);
        return pointerToClockwork;
    }


    /**
     * A method that starts the {@link Clockwork}'s main loop
     */
    public void setTheClock() {

        /* ----- INITIALIZATION STAGE ----- */
        /* GLOBAL PARAMETERS */
        this.setGlobalParameters();

        /* CONSTANTS */
        final float TICK_PERIOD = 1f / TICKS_PER_SECOND; // FOR SETTING TIMERS
        final float MIN_SECONDS_TILL_NEXT_RENDER = 1f / MAX_FRAMES_PER_SECOND; // FOR SETTING TIMERS

        /* VARIABLE DECLARATION */
        int tickPerSecondCounter; // HOW MANY TICKS HAPPENED SINCE THE BEGINNING OF THIS SECOND
        int framePerSecondCounter; // HOW MANY FRAMES DRAWN SINCE THE BEGINNING OF THIS SECOND
        float tickBalanceCounter; // HOW MANY TICKS REQUIRED TO PROCESS, TO KEEP THEIR QUANTITY OVER TIME CONSTANT
        Timer evaluationTimer; // COUNTDOWN TILL NEXT EVALUATION
        Timer tickTimer; // COUNTDOWN TILL NEXT TICK
        Timer renderTimer; // COUNTDOWN TILL NEXT RENDER

        /* VARIABLE ASSIGNMENT */
        tickPerSecondCounter = 0;
        framePerSecondCounter = 0;
        tickBalanceCounter = 0f;
        evaluationTimer = new Timer(EVALUATION_PERIOD);
        tickTimer = new Timer(TICK_PERIOD);
        renderTimer = new Timer(MIN_SECONDS_TILL_NEXT_RENDER);

        /* ClockworkGameLogic */
        gameLogic.onStart(this.windowManager);


        /* ----- LOOP STAGE ----- */
        while (gameLogic.shallTick()) {

            /* TICK STAGE */
            if (tickTimer.isExpired()) {
                tickBalanceCounter -= tickTimer.timeRemaining() * TICKS_PER_SECOND;
                tickBalanceCounter++; // TODO: (CRUTCH FIX) FIND OUT WHY INCREMENT IS NEEDED HERE
                tickTimer = new Timer(TICK_PERIOD);
            }
            while (tickBalanceCounter >= 1f) {
                tickBalanceCounter--;
                onTick();
                tickPerSecondCounter++;
            }


            /* RENDERING STAGE */
            if (renderTimer.isExpired()) {
                renderTimer = new Timer(MIN_SECONDS_TILL_NEXT_RENDER);
                this.onRender();
                framePerSecondCounter++;
            }


            /* EVALUATION STAGE */
            if (evaluationTimer.isExpired()) {
                this.onEvaluation(tickPerSecondCounter, framePerSecondCounter);
                evaluationTimer = new Timer(EVALUATION_PERIOD);
                framePerSecondCounter = 0;
                tickPerSecondCounter = 0;
            }
        }

        /* ----- TERMINATION STAGE ----- */
        this.onTermination();
    }

    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    /**
     * Initiates the create and stores it in a static constant
     * @param gameLogic a class that implements the ClockworkGameLogic
     */
    private Clockwork(ClockworkGameLogic gameLogic) {
        /* -- INITIALIZING GAME LOGIC AND SINGLETON -- */
        this.gameLogic = gameLogic;
        Clockwork.pointerToClockwork = this;
        MAX_FRAMES_PER_SECOND = 60f;
        TICKS_PER_SECOND = 60f;
        EVALUATION_PERIOD = 1f;

        /* -- CREATING WINDOW MANAGER AND THE FIRST WINDOW WITH IT -- */
        windowManager = new WindowManager();
    }
    /*--------------------*/
}
