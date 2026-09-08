package game.statuses;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.actors.ActorStatistics;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.StatisticOperations;

/**
 * A class representing a status that can burn the players.
 */

public class Burning implements Status {

    /**
     * The amount of damage for one turn
     */
    private final int damagePerTurn;

    /**
     * The amount of duration left for burning to disappear
     */
    private int duration;

    /**
     * The constructor of Burning class
     * @param duration the amount of duration left for burning to disappear
     */
    public Burning(int duration, int damagePerTurn) {
        this.duration = duration;
        this.damagePerTurn = damagePerTurn;
    }

    /**
     * If entity has health, modify to decrease health with damage per turn.
     * Burning ticks will be on the player until end of duration.
     * @param currEntity the entity this status is attached to
     * @param location the location where burning is added to player
     */
    @Override
    public void tickStatus(GameEntity currEntity, Location location) {
        if (currEntity.hasStatistic(ActorStatistics.HEALTH)) {
            currEntity.modifyStatistic(ActorStatistics.HEALTH, StatisticOperations.DECREASE, damagePerTurn);
            duration--;
        }
    }

    /**
     * Indicates whether status is active using duration
     * @return true if duration is more than 0, false if less than or equal to 0
     */
    @Override
    public boolean isStatusActive() {
        return duration > 0;
    }
}
