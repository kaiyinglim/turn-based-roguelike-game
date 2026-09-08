package game.statuses;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.Location;

/**
 * Status effect that marks an entity as detected by scouting systems.
 *
 * DetectionMarked is applied by Scout Drones and improves target prioritisation
 * for autonomous systems such as Combat Drones and Turrets. Marked entities are
 * more likely to be selected as targets within range.
 *
 * The status decreases over time and expires after its duration ends.
 *
 * @author Suzan Xuan Loh Palmer
 * @version 25.0.3
 */
public class DetectionMarked implements Status {

    private int duration;
    /**
     * Constructor.
     *
     * @param duration duration of the mark in turns
     */
    public DetectionMarked(int duration) {
        this.duration = duration;
    }
    /**
     * Reduces the duration every tick.
     *
     * @param currEntity the entity carrying this status
     * @param location the entity's current location
     */
    @Override
    public void tickStatus(GameEntity currEntity, Location location) {

        if (duration > 0) {
            duration--;
        }
    }
    /**
     * Checks whether the status is still active.
     *
     * @return true if active, false otherwise
     */
    @Override
    public boolean isStatusActive() {
        return duration > 0;
    }
    /**
     * Returns the remaining duration for targeting logic.
     *
     * @return remaining turns
     */
    public int getDuration() {
        return duration;
    }
    /**
     * Returns a string representation of the DetectionMarked status.
     *
     * This includes the remaining duration for debugging and display purposes.
     *
     * @return formatted status string
     */
    @Override
    public String toString() {
        return "Detection Marked (" + duration + " turns remaining)";
    }
}

