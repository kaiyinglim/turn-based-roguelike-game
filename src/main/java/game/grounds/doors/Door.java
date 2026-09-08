package game.grounds.doors;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.ActorStatistics;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import edu.monash.fit2099.engine.statistics.StatisticOperations;
import game.enums.GroundStatistics;
import game.items.AccessCard;

/**
 * Its primary purpose in the universe is to halt the progress of underpaid
 * {@code ContractedWorker}s until they can produce the correct rectangular
 * piece of plastic.
 */
public abstract class Door extends Ground {
    private int requiredLevel;
    /**
     * The constructor of Door class
     */
    public Door(char displayChar, String name, int requiredLevel) {
        super(displayChar, name);
        //1 = locked, 0 = unlocked
        this.addNewStatistic(GroundStatistics.LOCKED, new BaseStatistic(1));
        this.requiredLevel = requiredLevel;
    }
    /**
     * Checks whether the given access card has sufficient clearance
     * to unlock this door.
     *
     * @param card the access card attempting to unlock the door
     * @return true if the card clearance level is sufficient,
     *         false otherwise
     */
    public boolean canBeUnlockedBy(AccessCard card) {
        return card.getClearanceLevel() >= requiredLevel;
    }

    /**
     * When unlocking door, updates locked statistics to 0 which means 'unlocked'.
     * @param actor the actor that unlocks the door
     */
    public void unlock(Actor actor) {
        this.modifyStatistic(GroundStatistics.LOCKED, StatisticOperations.UPDATE, 0);

    }

    /**
     * Door is unlocked
     * @return true when LOCKED is 0, false when LOCKED is 1.
     */
    public boolean isUnlocked() {
        return this.getStatistic(GroundStatistics.LOCKED) == 0;
    }

    /**
     * if the door is unlocked, any actor can step into the door
     * @param actor the Actor to check
     * @return true if the door is unlocked, false otherwise.
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        return isUnlocked();
    }

    /**
     * The toString() method of the ground class
     * @return name of the ground class
     */
    @Override
    public String toString() {
        return super.toString();
    }
}
