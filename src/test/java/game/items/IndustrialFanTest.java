package game.items;

import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.loaders.ContractedWorker;
import game.loaders.WeightLimitedInventory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IndustrialFanTest {

    @Test
    void correctSellPrice() {
        IndustrialFan fan = new IndustrialFan();

        assertEquals(150, fan.getSellPrice());
    }

    @Test
    void correctDepositValue() {
        IndustrialFan fan = new IndustrialFan();

        assertEquals(10, fan.getDepositValue());
    }

    @Test
    void depositReturnsHealingMessage() {

        IndustrialFan fan = new IndustrialFan();

        ContractedWorker worker =
                new ContractedWorker("Worker", 'ඞ', 100, new WeightLimitedInventory(50));

        String result =
                fan.onDeposit(
                        worker,
                        mock(GameMap.class),
                        mock(Location.class));

        assertTrue(result.contains("heals 10 HP"));
    }

    @Test
    void sellReturnsSpawnMessage() {

        IndustrialFan fan = new IndustrialFan();

        ContractedWorker worker =
                mock(ContractedWorker.class);

        GameMap map = mock(GameMap.class);

        Location supercomputerLocation = mock(Location.class);
        Location destination = mock(Location.class);

        Exit exit = mock(Exit.class);

        when(exit.getDestination()).thenReturn(destination);
        when(supercomputerLocation.getExits())
                .thenReturn(java.util.List.of(exit));

        when(destination.containsAnActor()).thenReturn(false);
        when(destination.canActorEnter(any())).thenReturn(true);

        String result =
                fan.onSell(worker, map, supercomputerLocation);

        assertTrue(result.contains("Slime"));
    }
}