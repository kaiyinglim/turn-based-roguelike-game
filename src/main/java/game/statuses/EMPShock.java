package game.statuses;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.Location;

/**
 * Status effect that represents an electromagnetic disruption (EMP Shock).
 *
 * EMPShock disables or disrupts autonomous systems and reduces the effectiveness
 * of affected entities. It is commonly applied by electric-based attacks and
 * environmental hazards such as Electric Fields.
 *
 * The effect lasts for a limited number of turns and then expires automatically.
 *
 * @author Suzan Xuan Loh Palmer
 * @version 25.0.3
 */
public class EMPShock implements Status {

    private int duration;
    /**
     * Constructor.
     *
     * @param duration number of turns the effect remains active
     */
    public EMPShock(int duration) {
        this.duration = duration;
    }
    /**
     * Reduces the remaining duration every tick.
     *
     * @param currEntity the affected entity
     * @param location the entity's current location
     */
    @Override
    public void tickStatus(GameEntity currEntity, Location location) {

        if (duration > 0) {
            duration--;
        }
    }
    /**
     * Checks whether the EMP effect is still active.
     *
     * @return true if duration remains, false otherwise
     */
    @Override
    public boolean isStatusActive() {
        return duration > 0;
    }
    /**
     * Returns a string representation of the EMPShock status.
     *
     * Used for debugging and status display purposes.
     *
     * @return formatted status string
     */
    @Override
    public String toString() {
        return "EMP Shock (" + duration + " turns remaining)";
    }
}
