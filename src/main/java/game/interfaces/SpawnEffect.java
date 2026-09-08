package game.interfaces;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;

/**
 * SpawnEffect class is an interface class that represents an ability to spawn as an effect
 * @author FIT2099 Bryan Chan Zheng Lam
 */
public interface SpawnEffect {
    /**
     * The method in which others use to implement
     * Applies the spawn effect
     * @param spawn the spawn where it took place
     * @param location the location where it took place
     */
    void applySpawnEffect(Actor spawn, Location location);
}