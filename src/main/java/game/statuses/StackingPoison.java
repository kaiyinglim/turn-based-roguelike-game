package game.statuses;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.actors.ActorStatistics;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.StatisticOperations;

/**
 * A poison status whose damage increases each time the victim is re-exposed.
 * Re-exposure refreshes the duration and adds another stack.
 *
 * @author Lim Kai Ying
 * @version 25.0.2
 */
public class StackingPoison implements Status {

    private int stacks;
    private int duration;
    private final int baseDamage;
    private final int initialDuration;

    /**
     * Creates a new stacking poison with one stack.
     *
     * @param duration   number of turns the poison lasts before expiring
     * @param baseDamage damage dealt per stack on each tick
     */
    public StackingPoison(int duration, int baseDamage) {
        this.stacks = 1;
        this.duration = duration;
        this.initialDuration = duration;
        this.baseDamage = baseDamage;
    }

    /**
     * Increments the poison stack count and resets the remaining duration.
     */
    public void stack() {
        stacks++;
        duration = initialDuration; // reset timer on each re-exposure while in a pool
    }

    /**
     * Deals damage equal to {@code baseDamage * stacks} and decrements duration.
     *
     * @param currEntity the entity this status is attached to
     * @param location   the location of the affected entity
     */
    @Override
    public void tickStatus(GameEntity currEntity, Location location) {
        if (duration <= 0) {
            return;
        }
        if (currEntity.hasStatistic(ActorStatistics.HEALTH)) {
            // damage scales with how many turns the actor has stayed in toxic pools
            currEntity.modifyStatistic(ActorStatistics.HEALTH, StatisticOperations.DECREASE, baseDamage * stacks);
        }
        duration--;
    }

    /**
     * Indicates whether this poison is still active.
     *
     * @return true while duration remains, false once expired
     */
    @Override
    public boolean isStatusActive() {
        return duration > 0;
    }
}
