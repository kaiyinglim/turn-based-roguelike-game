package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;

import java.util.ArrayList;
import java.util.Random;

/**
 * A class that represents the behaviour of the enemy wandering around the moon facility
 *
 * Note: Taken from demo/forest/WanderBehaviour class.
 * @author Riordan D. Alfredo
 */
public class WanderBehaviour implements Behaviour<Actor, Action> {

    /**
     * Random number generator to determine a random direction the enemy will choose to walk around.
     */
    private final Random random = new Random();

    /**
     * Returns a MoveAction to wander to a random location, if possible.
     * If no movement is possible, returns null.
     *
     * @param actor The entity performing the behaviour
     * @param location The location of the current entity
     * @return an Action, or null if no MoveAction is possible
     */
    @Override
    public Action operate(Actor actor, Location location) {
        ArrayList<Action> actions = new ArrayList<>();

        for (Exit exit : location.getExits()) {
            Location destination = exit.getDestination();
            if (destination.canActorEnter(actor)) {
                actions.add(exit.getDestination().getMoveAction(actor, "around", exit.getHotKey()));
            }
        }

        if (!actions.isEmpty()) {
            return actions.get(random.nextInt(actions.size()));
        }
        else {
            return null;
        }
    }
}
