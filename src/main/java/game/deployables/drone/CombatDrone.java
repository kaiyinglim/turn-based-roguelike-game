package game.deployables.drone;

import game.behaviours.drone.CombatDroneBehaviour;

/**
 * Autonomous Combat Drone unit.
 *
 * The Combat Drone is an offensive autonomous entity that engages
 * hostile targets within its operational range. It uses a predefined
 * CombatDroneBehaviour to locate enemies and execute electric attacks
 * without requiring player input.
 *
 * This unit provides sustained autonomous damage output and forms
 * part of the tactical deployment system.
 *
 * @author Suzan Xuan Loh Palmer
 * @version 25.0.3
 */
public class CombatDrone extends Drone {
    /**
     * Constructs a Combat Drone.
     *
     * The drone is initialised with a name, display character,
     * hp value, and its assigned behaviour controller.
     */
    public CombatDrone() {
        super(
                "Combat Drone",
                'C',
                50,
                new CombatDroneBehaviour()
        );
    }

}
