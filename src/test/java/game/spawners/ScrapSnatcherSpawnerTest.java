package game.spawners;

import edu.monash.fit2099.engine.actors.Actor;
import game.effects.ScrapSnatcherSpawnEffect;
import game.enemies.ScrapSnatcher;
import game.interfaces.SpawnEffect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScrapSnatcherSpawnerTest {

    private ScrapSnatcherSpawner spawner;

    @BeforeEach
    void setUp() {
        // Instantiate the real spawner we want to test
        spawner = new ScrapSnatcherSpawner();
    }

    @Test
    void testSpawnEnemyReturnsScrapSnatcher() {
        // Act: Tell the spawner to build an enemy
        Actor spawnedEnemy = spawner.spawnEnemy();

        // Assert: Verify it actually returned an object
        assertNotNull(spawnedEnemy, "The spawner should not return a null actor.");

        // Assert: Verify that the object it built is specifically a ScrapSnatcher
        assertTrue(spawnedEnemy instanceof ScrapSnatcher,
                "The spawned enemy must be an instance of ScrapSnatcher.");
    }

    @Test
    void testGetSpawnEffectReturnsCorrectEffectAfterSpawning() {
        // Arrange & Act:
        // We MUST call spawnEnemy() first!
        // In your spawner code, 'lastSpawnEffect' is only initialized when an enemy is spawned.
        spawner.spawnEnemy();

        // Now ask the spawner what effect it saved
        SpawnEffect effect = spawner.getSpawnEffect();

        // Assert: Verify it actually saved an effect
        assertNotNull(effect, "The spawn effect should not be null after an enemy has been spawned.");

        // Assert: Verify the saved effect is the correct loot-explosion effect
        assertTrue(effect instanceof ScrapSnatcherSpawnEffect,
                "The spawner should queue up a ScrapSnatcherSpawnEffect to be used.");
    }
}