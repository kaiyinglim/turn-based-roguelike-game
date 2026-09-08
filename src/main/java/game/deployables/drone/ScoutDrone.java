package game.deployables.drone;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import game.actions.BuyAction;
import game.behaviours.drone.ScoutDroneBehaviour;
import game.loaders.ContractedWorker;

/**
 * Autonomous Scout Drone unit.
 *
 * The Scout Drone is a reconnaissance-focused autonomous entity that detects
 * hostile actors within its range and applies the DetectionMarked status.
 * This improves the effectiveness of allied autonomous systems by enabling
 * prioritised targeting of marked enemies.
 *
 * The Scout Drone plays a support role in battlefield intelligence,
 * rather than direct combat, by improving situational awareness and
 * coordinating allied responses.
 *
 * @author Suzan Xuan Loh Palmer
 * @version 25.0.3
 */
public class ScoutDrone extends Drone {
    /**
     * Constructs a Scout Drone.
     *
     * Initialises the drone with its name, display character, health,
     * and assigned scouting behaviour controller.
     */
    public ScoutDrone() {
        super(
                "Scout Drone",
                'S',
                30,
                new ScoutDroneBehaviour()
        );
    }
}
