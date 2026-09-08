package game.deployables.drone;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.DeployAction;
import game.behaviours.drone.DroneBehaviour;
import game.interfaces.Deployable;
import game.interfaces.Purchasable;
import game.loaders.BasicInventory;
import game.loaders.ContractedWorker;
import game.statuses.EMPShock;

/**
 * Abstract base class for all autonomous drone entities.
 *
 * Drone defines the core structure and behaviour shared by all
 * autonomous units in the system. Each drone is controlled by a
 * DroneBehaviour implementation, which determines its actions each turn.
 *
 * Drones are capable of autonomous decision-making and can be affected
 * by status effects such as EMPShock, which may temporarily disable
 * their behaviour execution.
 *
 * This class forms the foundation of the autonomous deployment system,
 * allowing different drone types to share common logic while delegating
 * specialised behaviour to subclasses.
 *
 * @author Suzan Xuan Loh Palmer
 * @version 25.0.3
 */
public abstract class Drone extends Actor { //implements Deployable {

    /**
     * Behaviour responsible for autonomous actions.
     */
    protected final DroneBehaviour behaviour;

    /**
     * Constructs a Drone entity.
     *
     * @param name the name of the drone
     * @param displayChar the character used to represent the drone
     * @param hp the health points of the drone
     * @param behaviour the autonomous behaviour controller
     */
    public Drone(String name, char displayChar, int hp, DroneBehaviour behaviour) {
        super(name, displayChar, hp, new BasicInventory());
        this.behaviour = behaviour;
    }
    /**
     * Executes the drone's autonomous turn logic.
     *
     * If the drone is affected by EMPShock, it will be unable to act
     * and will instead perform no action. Otherwise, it delegates its
     * behaviour to its assigned DroneBehaviour implementation.
     *
     * @param actions available actions
     * @param lastAction the last action performed
     * @param map the game map containing all entities and locations
     * @param display the display interface
     * @return the action the drone will perform this turn
     */
    @Override
    public Action playTurn(ActionList actions,
                           Action lastAction,
                           GameMap map,
                           Display display) {
        if (this.hasStatus(EMPShock.class)) {
            return new DoNothingAction();
        }

        return behaviour.getAction(this, map);
    }
}
