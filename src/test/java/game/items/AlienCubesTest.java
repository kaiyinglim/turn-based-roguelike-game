package game.items;

import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.loaders.ContractedWorker;
import game.loaders.WeightLimitedInventory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AlienCubesTest {

    @Test
    void cuttingCubesCreatesArtifactAndPoisonsWorker() {

        AlienCubes cubes = new AlienCubes();

        ContractedWorker worker =
                new ContractedWorker("Worker", 'ඞ', 100, new WeightLimitedInventory(50));

        Location location = mock(Location.class);
        GameMap map = mock(GameMap.class);

        String result = cubes.cutBy(worker, map, location);

        verify(location).addItem(any(AlienArtifact.class));
        assertTrue(result.contains("Alien Artifact"));
    }

    @Test
    void cuttingCubesRemovesCubeFromInventory() {

        AlienCubes cubes = new AlienCubes();

        ContractedWorker worker =
                new ContractedWorker("Worker", 'ඞ', 100,
                        new WeightLimitedInventory(50));

        worker.getInventory().add(cubes);

        Location location = mock(Location.class);
        GameMap map = mock(GameMap.class);

        cubes.cutBy(worker, map, location);

        assertFalse(
                worker.getInventory().getItems().contains(cubes)
        );
    }

    @Test
    void cuttingCubesReturnsPoisonMessage() {

        AlienCubes cubes = new AlienCubes();

        ContractedWorker worker =
                new ContractedWorker("Worker", 'ඞ', 100,
                        new WeightLimitedInventory(50));

        Location location = mock(Location.class);
        GameMap map = mock(GameMap.class);

        String result =
                cubes.cutBy(worker, map, location);

        assertTrue(result.contains("poison"));
    }
}