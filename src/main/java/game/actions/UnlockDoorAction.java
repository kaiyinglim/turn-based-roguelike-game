package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.grounds.doors.Door;

/**
 * The bureaucratic process of asking a piece of the environment for permission to pass.
 */
public class UnlockDoorAction extends Action {

    /**
     * The Door ground to be use when action is executed
     */
    private Door door;

    /**
     * The constructor of UnlockDoorAction class
     * @param door the door to be unlocked
     */
    public UnlockDoorAction(Door door) {
        this.door = door;
    }

    /**
     * When executed, it will search for a nearby door and unlock it.
     *
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return the description of the result of the action of opening a door
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        door.unlock(actor);
        return actor + " unlocks the " + door.toString();
    }

    /**
     * Returns an output of unlock door action for display on menu
     * @param actor The actor performing the action.
     * @return a string describing this unlock door action
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " unlocks " + door.toString();
    }
}
