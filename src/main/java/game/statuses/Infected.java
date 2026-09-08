package game.statuses;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.Location;
import game.interfaces.ParasiteSpawnable;

/**
 * An abstract class representing a status that can infect the players.
 * @author FIT2099 Bryan Chan Zheng Lam
 */

public abstract class Infected implements Status, ParasiteSpawnable {

    /**
     * Called once per tick to update the status of the current entity
     * @param currEntity the entity this status is attached to
     * @param location the location of the entity
     */
    @Override
    public abstract void tickStatus(GameEntity currEntity, Location location);

    /**
     * Indicates whether this status is still active.
     * @return true when status is active, false when status is not active
     */
    @Override
    public boolean isStatusActive() {
        return true;
    }
}
