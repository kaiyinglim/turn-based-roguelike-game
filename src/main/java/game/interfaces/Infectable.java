package game.interfaces;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.Location;

/**
 * Infectable class is an interface class that represents an ability to infect
 * @author FIT2099 Bryan Chan Zheng Lam
 */
public interface Infectable {
    /**
     * Returns the status effect applied when the entity is infected by a Parasite.
     * Each infectable target returns its own infected status.
     *
     * @return the infected status to apply to the entity
     */
    Status getInfectedStatus();

    /**
     * The method to get the infect actions
     * @param parasite The parasite performing the behaviour
     * @param location The location of the current entity
     * @return an Action, or null if no InfectAction is possible
     */
    Action getInfectAction(Actor parasite, Location location);
}
