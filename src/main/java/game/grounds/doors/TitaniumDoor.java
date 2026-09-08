package game.grounds.doors;

import edu.monash.fit2099.engine.statistics.StatisticOperations;
import game.enums.GroundStatistics;
import edu.monash.fit2099.engine.actors.Actor;

/**
 * The mighty titanium door holds steady against any threat, however to those that it finds worthy,
 * it opens itself up and blesses them.
 */
public class TitaniumDoor extends Door{
    /**
     * The amount healed by the door (the door worked quite hard to get its doctorate)
     */
    private static final int HEAL_AMOUNT = 5;
    /**
     * The constructor of TitaniumDoor class
     */
    public TitaniumDoor() {
        super('M', "Titanium Door", 3);
    }

    /**
     * When unlocking the door, the door statistic is updated to 0 to show that it's unlocked
     * and the player heals by HEAL_AMOUNT.
     * @param actor the actor that unlocks the door
     */
    public void unlock(Actor actor) {
        this.modifyStatistic(GroundStatistics.LOCKED, StatisticOperations.UPDATE, 0);
        actor.heal(HEAL_AMOUNT);
        System.out.println(actor + " has been healed by the kind titanium door for " + HEAL_AMOUNT + " HP");
    }
}