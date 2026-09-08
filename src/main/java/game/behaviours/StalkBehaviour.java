package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.MoveActorAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.enums.WorkerAbility;

/**
 * Greedy stalking: nearest map actor with the given {@link WorkerAbility}, 
 * then one step that shortens Manhattan distance to the target.    
 */
public class StalkBehaviour implements Behaviour<Actor, Action> {

    /** Ability that marks valid targets */
    private final WorkerAbility targetType;

    /**
     * Constructs a stalk behaviour that pursues actors with the given ability.
     *
     * @param targetType ability that marks valid targets
     */
    public StalkBehaviour(WorkerAbility targetType) {
        this.targetType = targetType;
    }

    /**
     * Resolves a single move toward the nearest target actor on {@code location}'s map.
     *
     * @param actor    the actor performing the stalk behaviour
     * @param location the actor's current location
     * @return a {@link MoveActorAction} when a move that shortens distance to target is found, otherwise {@code null}
     */
    @Override
    public Action operate(Actor actor, Location location) {
        // check if actor is on a valid map
        GameMap map = location.map();
        if (!map.contains(actor)) {
            return null;
        }

        // find nearest target
        Actor target = findNearestTarget(actor, map);
        if (target == null) {
            return null;
        }

        // get current and target locations
        Location here = map.locationOf(actor);
        Location goal = map.locationOf(target);
        int currentDistance = manhattan(here, goal);

        // try each neighbour and keep a move that shortens distance to target.
        for (Exit exit : here.getExits()) {
            Location destination = exit.getDestination();
            if (destination.canActorEnter(actor)) {
                int newDistance = manhattan(destination, goal);
                if (newDistance < currentDistance) {
                    return new MoveActorAction(destination, exit.getName());
                }
            }
        }
        // no move found
        return null;
    }

    /**
     * Finds the nearest actor with {@link #targetType} on this map, using the Manhattan distance
     * Scan is row-major ({@code x} ascending, then {@code y}); only
     * strictly smaller distances replace the current best, so equal-distance actors keep the first hit.
     *
     * @param self the actor performing the stalk behaviour
     * @param map  the map to search
     * @return the nearest target actor, or {@code null} if none
     */
    private Actor findNearestTarget(Actor self, GameMap map) {
        Location selfLoc = map.locationOf(self);
        Actor nearest = null;
        int bestDistance = Integer.MAX_VALUE;

        for (int x : map.getXRange()) {
            for (int y : map.getYRange()) {
                Location cell = map.at(x, y);
                if (!cell.containsAnActor()) {
                    continue;
                }
                Actor other = cell.getActor();
                // ignore self and non-target actors
                if (other == self || !other.hasAbility(targetType) || !map.contains(other)) {
                    continue;
                }
                // update best distance and nearest actor if this one is closer
                int d = manhattan(selfLoc, cell);
                if (d < bestDistance) {
                    bestDistance = d;
                    nearest = other;
                }
            }
        }
        return nearest;
    }


    /**
     * Manhattan distance between two locations.
     * Note: Taken from demo/mars/FollowBehaviour class ({@code distance} helper).
     *
     * @param a the first location
     * @param b the second location
     * @return the Manhattan distance between the two locations
     */
    private static int manhattan(Location a, Location b) {
        return Math.abs(a.x() - b.x()) + Math.abs(a.y() - b.y());
    }
}
