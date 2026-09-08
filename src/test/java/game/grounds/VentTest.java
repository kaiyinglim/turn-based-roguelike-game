package game.grounds;

import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.loaders.ContractedWorker;
import game.loaders.WeightLimitedInventory;
import game.spawners.MatureUndeadSpawner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VentTest {

    @Test
    void cuttingVentAlwaysDropsIndustrialFan() {

        Vent vent = new Vent(new MatureUndeadSpawner());

        ContractedWorker worker =
                new ContractedWorker("Worker", 'ඞ', 100, new WeightLimitedInventory(50));

        Location location = mock(Location.class);
        GameMap map = mock(GameMap.class);

        when(location.getExits()).thenReturn(java.util.List.of());

        String result = vent.cutBy(worker, map, location);

        verify(location).addItem(any(game.items.IndustrialFan.class));
        verify(location).setGround(any(game.grounds.Floor.class));

        assertTrue(result.contains("Vent"));
    }

    @Test
    void cuttingVentReturnsUndeadMessage() {

        Vent vent = new Vent(new MatureUndeadSpawner());

        ContractedWorker worker =
                new ContractedWorker("Worker", 'ඞ', 100,
                        new WeightLimitedInventory(50));

        Location location = mock(Location.class);

        when(location.containsAnActor()).thenReturn(false);
        when(location.canActorEnter(any())).thenReturn(true);

        GameMap map = mock(GameMap.class);

        String result =
                vent.cutBy(worker, map, location);

        assertTrue(result.contains("Undead"));
    }
}