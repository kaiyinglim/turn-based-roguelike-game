package game.grounds.doors;

import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import edu.monash.fit2099.engine.statistics.StatisticOperations;
import game.enums.ItemStatistics;
import game.grounds.Floor;
import game.enums.GroundStatistics;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.interfaces.Cuttable;
import game.items.AluminiumScrap;
import game.loaders.ContractedWorker;
/**
 * The Aluminium door has been standing there, waiting to do the only other thing it can
 * do except open and close. And that is to zap someone, why because it would be
 * funny.
 */

public class AluminiumDoor extends Door implements Cuttable {
    /**
     * The amount of damage the door does to the player
     */
    private static final int DAMAGE_AMOUNT = 2;
    private static final int EXPLOSION_DAMAGE = 100;
    private static final double EXPLOSION_CHANCE = 0.25;

    /**
     * The constructor of AluminiumDoor class
     */
    public AluminiumDoor() {
        super('=', "Aluminium Door", 1);
        this.addNewStatistic(ItemStatistics.CUTTABLE, new BaseStatistic(1));
    }

    /**
     * When unlocking the door, the door statistic is updated to 0 to show that its unlocked
     * and the player takes DAMAGE_AMOUNT damage.
     * @param actor the actor that unlocks the door
     */
    public void unlock(Actor actor) {
        this.modifyStatistic(GroundStatistics.LOCKED, StatisticOperations.UPDATE, 0);
        actor.hurt(DAMAGE_AMOUNT);
        System.out.println(actor + " has been zapped by the brutal aluminium door for " + DAMAGE_AMOUNT + " damage");
    }

    /**
     * Applies the effect of cutting through the Aluminium Door.
     * The door is replaced with a Floor and Aluminium Scrap is produced.
     * There is also a chance that the door explodes and damages
     * nearby actors.
     *
     * @param worker the worker performing the cutting action
     * @param map the map where the action occurs
     * @param targetLocation the location of the Aluminium Door
     * @return a description of the cutting outcome
     */
    @Override
    public String cutBy(ContractedWorker worker, GameMap map, Location targetLocation) {
        targetLocation.addItem(new AluminiumScrap());
        targetLocation.setGround(new Floor());

        StringBuilder result = new StringBuilder();
        result.append(worker)
                .append(" cut through the Aluminium Door. Aluminium Scrap drops on the floor.");

        if (Math.random() < EXPLOSION_CHANCE) {
            result.append("\nThe Aluminium Door blows up");

            for (Exit exit : targetLocation.getExits()) {
                Location surroundingLocation = exit.getDestination();

                if (surroundingLocation.containsAnActor()) {
                    Actor actor = surroundingLocation.getActor();

                    actor.hurt(EXPLOSION_DAMAGE);

                    result.append("\n")
                            .append(actor)
                            .append(" takes ")
                            .append(EXPLOSION_DAMAGE)
                            .append(" explosion damage.");

                    if (!actor.isConscious()) {
                        result.append("\n")
                                .append(actor.unconscious(map));
                    }
                }
            }
        }

        return result.toString();
    }
}
