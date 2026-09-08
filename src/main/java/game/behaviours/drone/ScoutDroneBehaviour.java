package game.behaviours.drone;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.deployables.drone.Drone;
import game.enums.EnemyAbility;
import game.enums.StatusAbility;
import game.statuses.DetectionMarked;

/**
 * Behaviour implementation for Scout Drone autonomous units.
 *
 * The Scout Drone is a reconnaissance-based autonomous system that
 * scans the surrounding area for hostile entities within range and
 * applies the DetectionMarked status. This improves targeting
 * efficiency for allied combat systems by marking enemies for
 * prioritised engagement.
 *
 * Once a target is successfully marked, the drone completes its
 * action for the turn and performs no further operations.
 *
 * If no valid targets are found, the drone performs no action.
 *
 * @author Suzan Xuan Loh Palmer
 * @version 25.0.3
 */
public class ScoutDroneBehaviour extends DroneBehaviour {

    private static final int RANGE = 5;
    private static final int MARK_DURATION = 3;
    /**
     * Determines the action performed by the Scout Drone during its turn.
     *
     * The drone scans for hostile actors within a defined range. If an
     * unmarked enemy is found, it applies the DetectionMarked status to
     * that actor and ends its turn. Only one target can be marked per turn.
     *
     * @param drone the scout drone performing the action
     * @param map the game map containing all entities
     * @return the action the drone will perform this turn
     */
    @Override
    public Action getAction(Drone drone, GameMap map) {

        Location droneLocation = map.locationOf(drone);

        for (int x : map.getXRange()) {
            for (int y : map.getYRange()) {

                Location current = map.at(x, y);

                if (!current.containsAnActor()) {
                    continue;
                }

                Actor actor = current.getActor();

                if (actor == drone) {
                    continue;
                }

                int distance =
                        Math.abs(current.x() - droneLocation.x())
                                + Math.abs(current.y() - droneLocation.y());

                if (distance > RANGE) {
                    continue;
                }
                if (!actor.hasAbility(EnemyAbility.IS_ENEMY)) {
                    continue;
                }
                if (!actor.hasStatus(DetectionMarked.class)) {

                    actor.enableAbility(StatusAbility.DETECTION_MARKED);
                    actor.addStatus(new DetectionMarked(MARK_DURATION));

                    return new DoNothingAction();
                }
            }
        }

        return new DoNothingAction();
    }
}
