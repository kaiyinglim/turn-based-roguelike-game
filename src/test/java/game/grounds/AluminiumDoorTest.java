package game.grounds;

import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.grounds.doors.AluminiumDoor;
import game.items.AluminiumScrap;
import game.loaders.ContractedWorker;
import game.loaders.WeightLimitedInventory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AluminiumDoorTest {

    @Test
    void cuttingDoorDropsScrapAndReplacesGround() {

        AluminiumDoor door = new AluminiumDoor();

        ContractedWorker worker =
                new ContractedWorker("Worker", 'ඞ', 100, new WeightLimitedInventory(50));

        Location location = mock(Location.class);
        GameMap map = mock(GameMap.class);

        when(location.getExits()).thenReturn(java.util.List.of());
        when(location.getGround()).thenReturn(door);

        String result = door.cutBy(worker, map, location);

        verify(location).addItem(any(AluminiumScrap.class));
        verify(location).setGround(any(game.grounds.Floor.class));

        assertTrue(result.contains("Aluminium Door"));
    }
}