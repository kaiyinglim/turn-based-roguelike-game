package game.behaviours.drone;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.ElectricAttack;
import game.deployables.drone.Drone;
import game.enums.EnemyAbility;
import game.statuses.DetectionMarked;

/**
 * Behaviour implementation for Combat Drone autonomous units.
 *
 * The Combat Drone is an offensive autonomous system that scans
 * the surrounding area for hostile entities within range and
 * engages them using electric attacks. It prioritises targets
 * marked with DetectionMarked status and will otherwise engage
 * the nearest valid enemy target within its detection range.
 *
 * If no valid target is found, the drone performs no action.
 *
 * @author Suzan Xuan Loh Palmer
 * @version 25.0.3
 */
public class CombatDroneBehaviour extends DroneBehaviour {

    private static final int RANGE = 3;
    private static final int DAMAGE = 15;
    /**
     * Determines the action performed by the Combat Drone during its turn.
     *
     * The drone scans for hostile actors within range. If a valid target
     * is found, it performs an ElectricAttack. If no target is available,
     * it does nothing.
     *
     * @param drone the combat drone performing the action
     * @param map the game map containing all entities
     * @return the action the drone will perform this turn
     */
    @Override
    public Action getAction(Drone drone, GameMap map) {

        Location droneLocation = map.locationOf(drone);

        Actor target = findTarget(drone, droneLocation, map);

        if (target == null) {
            return new DoNothingAction();
        }

        return new ElectricAttack(target, DAMAGE);
    }
    /**
     * Finds the most suitable target for the Combat Drone.
     *
     * The drone searches within its attack range for hostile actors.
     * Priority is given to targets with the DetectionMarked status.
     * If no marked targets are found, the nearest enemy actor is selected.
     *
     * @param drone the combat drone searching for targets
     * @param droneLocation the current location of the drone
     * @param map the game map being scanned
     * @return the selected target actor, or null if none found
     */
    private Actor findTarget(Drone drone, Location droneLocation, GameMap map) {

        Actor nearestTarget = null;
        int nearestDistance = Integer.MAX_VALUE;

        for (int x : map.getXRange()) {
            for (int y : map.getYRange()) {

                Location current = map.at(x, y);

                if (!current.containsAnActor()) {
                    continue;
                }

                Actor actor = current.getActor();
                if (!actor.hasAbility(EnemyAbility.IS_ENEMY)) {
                    continue;
                }
                // now drone is in scope
                if (actor == drone) {
                    continue;
                }

                int distance =
                        Math.abs(current.x() - droneLocation.x())
                                + Math.abs(current.y() - droneLocation.y());

                if (distance > RANGE) {
                    continue;
                }

                // priority target
                if (actor.hasStatus(DetectionMarked.class)) {
                    return actor;
                }

                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    nearestTarget = actor;
                }
            }
        }

        return nearestTarget;
    }
}
