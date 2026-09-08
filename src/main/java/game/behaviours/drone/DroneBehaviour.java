package game.behaviours.drone;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.positions.GameMap;
import game.deployables.drone.Drone;

/**
 * Abstract base class for all drone behaviours.
 *
 * DroneBehaviour defines the decision-making framework used by
 * autonomous drone entities. Each subclass implements a specific
 * behaviour strategy that determines how a drone acts each turn
 * based on its environment and available targets.
 *
 * This supports the autonomous system architecture by allowing
 * different drone types to share a common execution structure
 * while implementing specialised behaviour logic.
 *
 * @author Suzan Xuan Loh Palmer
 * @version 25.0.3
 */
public abstract class DroneBehaviour {

    /**
     * Determines the drone's action for the current turn.
     *
     * This method is called each tick to decide what action the
     * drone should perform based on the current game state.
     *
     * @param drone the drone performing the action
     * @param map the game map containing all entities and locations
     * @return the action the drone will execute this turn
     */
    public abstract Action getAction(Drone drone, GameMap map);
}
