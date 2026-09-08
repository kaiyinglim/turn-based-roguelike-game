package game.items;

import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.loaders.ContractedWorker;
import game.loaders.WeightLimitedInventory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AluminiumScrapTest {

    @Test
    void hasCorrectDepositValue() {
        AluminiumScrap scrap = new AluminiumScrap();

        assertEquals(50, scrap.getDepositValue());
    }

    @Test
    void depositAlwaysReturnsMessage() {
        AluminiumScrap scrap = new AluminiumScrap();

        ContractedWorker worker =
                new ContractedWorker("Worker", 'ඞ', 100, new WeightLimitedInventory(50));

        String result =
                scrap.onDeposit(
                        worker,
                        mock(GameMap.class),
                        mock(Location.class));

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}