package game.behaviours.drone;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.ActorStatistics;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.deployables.drone.Drone;
import game.enums.WorkerAbility;

/**
 * Behaviour implementation for Repair Drone autonomous units.
 *
 * The Repair Drone is a support-oriented autonomous system that
 * scans the surrounding area for damaged allied entities within
 * range. When a valid target is found, it restores a portion of
 * their health before ending its turn.
 *
 * This behaviour prioritises sustaining allied forces rather than
 * engaging in combat or environmental interaction.
 *
 * If no damaged allies are found, the drone performs no action.
 *
 * @author Suzan Xuan Loh Palmer
 * @version 25.0.3
 */
public class RepairDroneBehaviour extends DroneBehaviour {

    private static final int RANGE = 3;
    private static final int REPAIR_AMOUNT = 10;
    /**
     * Determines the action performed by the Repair Drone during its turn.
     *
     * The drone searches for damaged allied actors within range. If a
     * valid target is found, it heals that actor by a fixed amount.
     * Otherwise, it does nothing.
     *
     * @param drone the repair drone performing the action
     * @param map the game map containing all entities
     * @return the action the drone will perform this turn
     */
    @Override
    public Action getAction(Drone drone, GameMap map) {

        Location droneLocation = map.locationOf(drone);

        Actor ally = findDamagedAlly(droneLocation, map);

        if (ally != null) {
            ally.heal(REPAIR_AMOUNT);
        }

        return new DoNothingAction();
    }
    /**
     * Finds a damaged allied actor within the drone's operational range.
     *
     * The method scans all map locations and selects the first allied actor
     * found within range whose health is below maximum.
     *
     * @param droneLocation the current location of the repair drone
     * @param map the game map being scanned
     * @return a damaged allied actor if found, otherwise null
     */
    private Actor findDamagedAlly(Location droneLocation, GameMap map) {

        Actor target = null;

        for (int x : map.getXRange()) {
            for (int y : map.getYRange()) {

                Location current = map.at(x, y);

                if (!current.containsAnActor()) {
                    continue;
                }

                Actor actor = current.getActor();

                if (!actor.hasAbility(WorkerAbility.ALLY)) {
                    continue;
                }

                int distance =
                        Math.abs(current.x() - droneLocation.x())
                                + Math.abs(current.y() - droneLocation.y());

                if (distance > RANGE) {
                    continue;
                }

                if (actor.getStatistic(ActorStatistics.HEALTH)
                        < actor.getMaximumStatistic(ActorStatistics.HEALTH)) {

                    target = actor;
                    break;
                }
            }
        }

        return target;
    }
}
