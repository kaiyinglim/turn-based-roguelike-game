package game.deployables.drone;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import game.actions.BuyAction;
import game.behaviours.drone.RepairDroneBehaviour;
import game.loaders.ContractedWorker;

/**
 * Autonomous Repair Drone unit.
 *
 * The Repair Drone is a support-based autonomous entity that restores
 * health to damaged allied units within its operational range. It uses
 * a RepairDroneBehaviour to locate valid allies and perform repairs each turn.
 *
 * This drone improves battlefield sustainability by maintaining the health
 * of deployed units and supporting long-term autonomous operations.
 *
 * @author Suzan Xuan Loh Palmer
 * @version 25.0.3
 */
public class RepairDrone extends Drone {
    /**
     * Constructs a Repair Drone.
     *
     * Initialises the drone with its name, display character, health,
     * and assigned repair behaviour controller.
     */
    public RepairDrone() {
        super(
                "Repair Drone",
                'R',
                40,
                new RepairDroneBehaviour()
        );
    }
}
