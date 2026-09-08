package game.statuses;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.Location;

/**
 * Status that prevents normal turn resolution for a limited number of turns.
 * {@link game.loaders.ContractedWorker} honours this in {@code playTurn}.
 */
public class Frozen implements Status {

    private int duration;

    /**
     * @param duration number of turns the freeze lasts
     */
    public Frozen(int duration) {
        this.duration = Math.max(0, duration);
    }

    /**
     * If the duration is greater than 0, decrement the duration.
     * 
     * @param currEntity the entity this status is attached to
     * @param location the location of the entity
     */
    @Override
    public void tickStatus(GameEntity currEntity, Location location) {
        if (duration > 0) {
            duration--;
        }
    }

    /**
     * Indicates whether the status is active.
     * 
     * @return true if the duration is greater than 0, false otherwise
     */
    @Override
    public boolean isStatusActive() {
        return duration > 0;
    }
}
