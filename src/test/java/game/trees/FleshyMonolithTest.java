package game.trees;

import edu.monash.fit2099.engine.actors.TestActor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.positions.NumberRange;
import game.enums.WorkerAbility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FleshyMonolithTest {

    private FleshyMonolith monolith;

    @BeforeEach
    void setUp() {
        // Instantiate the real monolith for testing
        monolith = new FleshyMonolith();
    }

    @Test
    void testFleshyMonolithCannotGrowFurther() {
        // Act: Try to age the monolith by 99 turns
        FleshyTreeStage result = monolith.tryGrow(99);

        // Assert: It should return itself (this) because it is the final stage
        assertEquals(monolith, result, "Monolith should return itself when tryGrow is called, stopping further growth.");
    }

    @Test
    void testFleshyMonolithDoesNotSpawnEnemies() {
        // Assert: The spawner must be null
        assertNull(monolith.getSpawner(), "Monolith should return null for its spawner as it relies on warping instead of spawning.");
    }

    @Test
    void testWarpsWorkerToRandomLocation() {
        // Arrange
        // 1. Set up our dummy worker
        TestActor worker = new TestActor();
        worker.enableAbility(WorkerAbility.IS_WORKER);

        // 2. Set up the fake map and locations
        Location mockFloraLocation = mock(Location.class);
        GameMap mockMap = mock(GameMap.class);
        Location mockDestination = mock(Location.class);

        // Link the flora's location to our fake map
        when(mockFloraLocation.map()).thenReturn(mockMap);

        // 3. Prevent Math Crash: Mock the X and Y ranges of the map so the radius calculation works
        NumberRange mockRange = new NumberRange(0, 10);
        when(mockMap.getXRange()).thenReturn(mockRange);
        when(mockMap.getYRange()).thenReturn(mockRange);

        // 4. Set up the destination
        // Tell the flora location to return our fake destination when scanning for nearby tiles
        when(mockFloraLocation.getNearbyLocations(anyInt())).thenReturn(List.of(mockDestination));

        // Ensure the engine considers this destination a valid place for the worker to stand
        when(mockDestination.canActorEnter(worker)).thenReturn(true);

        // Act
        // Trigger the warp by forcing an interaction
        monolith.interactWithAdjacentActor(worker, mockFloraLocation);

        // Assert
        // Verify that the map was specifically instructed to move the worker to the destination
        verify(mockMap).moveActor(worker, mockDestination);
    }

    @Test
    void testIgnoresNonWorkers() {
        // Arrange
        // Set up an actor that does NOT have the WorkerAbility
        TestActor enemy = new TestActor();
        Location mockFloraLocation = mock(Location.class);
        GameMap mockMap = mock(GameMap.class);

        when(mockFloraLocation.map()).thenReturn(mockMap);

        // Act
        monolith.interactWithAdjacentActor(enemy, mockFloraLocation);

        // Assert
        // Verify that moveActor was NEVER called, proving the Monolith ignored the enemy
        verify(mockMap, never()).moveActor(any(), any());
    }
}