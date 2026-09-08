package game.spawners;

import edu.monash.fit2099.engine.actors.Actor;
import game.effects.UndeadSpawnEffect;
import game.enemies.Undead;
import game.interfaces.EnemySpawner;
import game.interfaces.SpawnEffect;

public class MatureUndeadSpawner implements EnemySpawner {
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
        lastSpawnEffect = new UndeadSpawnEffect();
        return new Undead();
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
