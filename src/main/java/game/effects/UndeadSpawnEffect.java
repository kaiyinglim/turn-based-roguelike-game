package game.effects;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.ActorStatistics;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.StatisticOperations;
import game.interfaces.SpawnEffect;

/**
 * A class that represents the spawn effect of the Undead when being spawned
 * @author FIT2099 Bryan Chan Zheng Lam
 */
public class UndeadSpawnEffect implements SpawnEffect {

    /**
     * Applies the spawn effect for undead where,
     * when undead spawns, every creature in its adjacent tiles will,
     * increase the undead maximum health
     * @param spawn the spawn where it took place
     * @param location the location where it took place
     */
    @Override
    public void applySpawnEffect(Actor spawn, Location location) {
        for (Exit exit : location.getExits()) {
            Location surroundingLocation = exit.getDestination();
            if (surroundingLocation.containsAnActor()) {
                spawn.modifyStatisticMaximum(ActorStatistics.HEALTH, StatisticOperations.INCREASE, 1);
                new Display().println(spawn + " max health increased by 1."
                                        + " (" + spawn.getMaximumStatistic(ActorStatistics.HEALTH)
                                        + " max HP.");
            }
        }
    }
}
