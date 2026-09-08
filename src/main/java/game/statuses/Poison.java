package game.statuses;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.actors.ActorStatistics;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.StatisticOperations;

/**
 * A class representing a status that can poison the players.
 */

public class Poison implements Status {

    /**
     * The amount of damage for one turn
     */
    private final int damagePerTurn;

    /**
     * The amount of duration left for burning to disappear
     */
    private int duration;

    /**
     * The constructor of Poison class
     * @param duration the amount of duration left for poison to disappear
     */
    public Poison(int duration, int damagePerTurn) {
        this.duration = duration;
        this.damagePerTurn = damagePerTurn;
    }

    /**
     * If entity has health, modify to decrease health with damage per turn.
     * Burning ticks will be on the player until end of duration.
     * @param currEntity the entity this status is attached to
     * @param location the location where poison is added to player
     */
    @Override
    public void tickStatus(GameEntity currEntity, Location location) {
        if (duration <= 0) {
            return;
        }
        if (currEntity.hasStatistic(ActorStatistics.HEALTH)) {
            currEntity.modifyStatistic(ActorStatistics.HEALTH, StatisticOperations.DECREASE, damagePerTurn);
        }
        duration--;
    }

    @Override
    public boolean isStatusActive() {
        return duration > 0;
    }
}
