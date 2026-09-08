package game.interfaces;

import edu.monash.fit2099.engine.actors.Actor;

/**
 * EnemySpawner class is an interface class that represents the ability to spawn enemies
 * @author FIT2099 Bryan Chan Zheng Lam
 */
public interface EnemySpawner {
    /**
     * The method to spawn enemies
     * @return nothing as it is an interface
     */
    Actor spawnEnemy();

    /**
     * The method to get the spawn effect
     * @return nothing as it is an interface
     */
    SpawnEffect getSpawnEffect();
}
