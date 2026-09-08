package game.spawners;

import edu.monash.fit2099.engine.actors.Actor;
import game.effects.ParasiteSpawnEffect;
import game.effects.ScrapSnatcherSpawnEffect;
import game.effects.SlimeSpawnEffect;
import game.enemies.Parasite;
import game.enemies.ScrapSnatcher;
import game.enemies.Slime;
import game.interfaces.EnemySpawner;
import game.interfaces.SpawnEffect;

import java.util.Random;

/**
 * A generic spawner only found in moon that implements a spawner where enemy comes from.
 * @author FIT2099 Bryan Chan Zheng Lam
 */
public class OverflowVentSpawner implements EnemySpawner {

    /**
     * Random number generator used to determine which enemy to spawn.
     */
    private final Random random = new Random();

    /**
     * Remembers the last spawn effect and calls it for the specific creature
     */
    private SpawnEffect lastSpawnEffect;

    /**
     * Overrides the method in EnemySpawner interface.
     * Chooses which enemies to spawn.
     * Different enemies have different spawn effects
     * @return new parasite or slime enemy
     */
    @Override
    public Actor spawnEnemy() {
        int roll = random.nextInt(3);

        if (roll == 0) {
            lastSpawnEffect = new ParasiteSpawnEffect();
            return new Parasite();
        }
        else if (roll == 1){
            lastSpawnEffect = new SlimeSpawnEffect();
            return new Slime();
        }
        else {
            lastSpawnEffect = new ScrapSnatcherSpawnEffect();
            return new ScrapSnatcher();
        }
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
