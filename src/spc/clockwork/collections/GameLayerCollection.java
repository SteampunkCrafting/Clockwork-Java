package spc.clockwork.collections;


import spc.clockwork.graphics.layer.GameLayer;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * A collection of game layers
 *
 * @author wize
 * @version 0 (6 June 2018)
 */
public final class GameLayerCollection extends AbstractGameCollection<GameLayer> {

    /* ATTRIBUTES
    /*--------------------*/

    /** A set of layers */
    private final Set<GameLayer> gameLayers;


    private static Set<GameLayer> tempLayers;
    /*--------------------*/



    /* PRIVATE METHODS
    /*--------------------*/

    /**
     * Sets the power set up
     */
    @Override
    protected Collection<Collection> makePowerCollection() {
        Collection<Collection> powerSet = new LinkedList<>();
        powerSet.add(tempLayers = new LinkedHashSet<>());
        return powerSet;
    }

    /**
     * Adds the provided element to a corresponding subset
     *
     * @param element the element given
     */
    @Override
    protected void addElementToSubsets(GameLayer element) {
        this.gameLayers.add(element);
    }

    /*--------------------*/



    /* PUBLIC METHODS
    /*--------------------*/
    /*--------------------*/



    /* CLASS CONSTRUCTORS
    /*--------------------*/

    public GameLayerCollection() {
        super();
        this.gameLayers = tempLayers;
    }
    /*--------------------*/
}
