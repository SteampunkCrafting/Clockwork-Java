package spc.clockwork.gameobject;


import spc.clockwork.collections.GameWorld;
import spc.clockwork.core.Clockwork;
import spc.clockwork.core.ClockworkGameLogic;

/**
 * An interface that prescribes an action program to the instance of {@link spc.clockwork.gameobject.GameObject}
 * If a {@link spc.clockwork.gameobject.GameObject} implements this interface, then you do not have to setup and
 * process the {@link spc.clockwork.gameobject.GameObject} in the Game class (i.e. the implementation of
 * {@link ClockworkGameLogic})
 *
 * @author wize
 * @version 0 (7 June 2018)
 */
public interface ScriptedObject {

    /**
     * Prescribes the actions to be done by this object (with itself and with the {@link GameWorld} provided), when a
     * tick happens.
     * The responsibility of calling this method is outsourced to the {@link Clockwork} during the tick() process.
     * This method will be called after the {@link ClockworkGameLogic}'s onTick() was called
     */
     void onTick();


    /**
     * Prescribes the actions to be done by this object (with itself and with the {@link GameWorld} provided), when
     * the object is removed.
     * The responsibility of calling this method is outsourced to the {@link GameWorld} during the remove() process
     */
     void onTerminate();


    /**
     * Checks whether the script should be executed or not
     * @return true, if the script should be executed, else return false
     */
    boolean isActive();
}
