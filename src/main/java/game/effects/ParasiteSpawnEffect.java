package game.effects;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.ActorStatistics;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.StatisticOperations;
import game.enums.WorkerAbility;
import game.interfaces.SpawnEffect;

/**
 * A class that represents the spawn effect of the Parasite when being spawned
 * @author FIT2099 Bryan Chan Zheng Lam
 */
public class ParasiteSpawnEffect implements SpawnEffect {

    /**
     * The damage when parasite spawns
     */
    private static final int DAMAGE = 2;

    /**
     * Applies the spawn effect for parasite where,
     * when parasite spawns, all workers in adjacent tiles takes damage
     * @param spawn the spawn where it took place
     * @param location the location where it took place
     */
    @Override
    public void applySpawnEffect(Actor spawn, Location location) {
        for (Exit exit : location.getExits()) {
            Location surroundingLocation = exit.getDestination();
            if (surroundingLocation.containsAnActor()) {
                Actor actor = surroundingLocation.getActor();
                if (actor.hasAbility(WorkerAbility.IS_WORKER)) {
                    actor.modifyStatistic(ActorStatistics.HEALTH, StatisticOperations.DECREASE, DAMAGE);
                    new Display().println(actor + " takes " + DAMAGE
                                            + " damage from Parasite spawning!");
                }
            }
        }
    }
}
