package game.spawners;

import edu.monash.fit2099.engine.actors.Actor;
import game.effects.ScrapSnatcherSpawnEffect;
import game.enemies.ScrapSnatcher;
import game.interfaces.EnemySpawner;
import game.interfaces.SpawnEffect;

/**
 * A Scrap snatcher spawner found in the moon that implements a spawner where enemy comes from.
 * @author FIT2099 Bryan Chan Zheng Lam
 */
public class ScrapSnatcherSpawner implements EnemySpawner {
    /**
     * Remembers the last spawn effect and calls it for the specific creature
     */
    private SpawnEffect lastSpawnEffect;

    /**
     * Overrides the method in EnemySpawner interface.
     * Chooses which enemies to spawn.
     * Different enemies have different spawn effects
     * @return new undead or slime enemy
     */
    @Override
    public Actor spawnEnemy() {
        lastSpawnEffect = new ScrapSnatcherSpawnEffect();
        return new ScrapSnatcher();
    }

    /**
     * Gets the spawn effect
     * @return the last spawn effect
     */
    @Override
    public SpawnEffect getSpawnEffect() {
        return lastSpawnEffect;
    }
}
