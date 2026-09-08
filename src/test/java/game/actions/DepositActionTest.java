package game.actions;

import edu.monash.fit2099.engine.positions.Location;
import game.grounds.Supercomputer;
import game.items.AluminiumScrap;
import game.loaders.ContractedWorker;
import game.loaders.WeightLimitedInventory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DepositActionTest {

    @Test
    void depositRemovesItemFromInventory() {

        ContractedWorker worker =
                new ContractedWorker("Worker", 'ඞ', 100, new WeightLimitedInventory(50));

        AluminiumScrap scrap = new AluminiumScrap();

        worker.getInventory().add(scrap);

        Supercomputer supercomputer = new Supercomputer();

        DepositAction action =
                new DepositAction(
                        worker,
                        scrap,
                        supercomputer,
                        mock(Location.class));

        action.execute(worker, null);

        assertFalse(
                worker.getInventory().getItems().contains(scrap)
        );
    }

    @Test
    void depositReturnsQuotaStatus() {

        ContractedWorker worker =
                new ContractedWorker("Worker", 'ඞ', 100, new WeightLimitedInventory(50));

        AluminiumScrap scrap = new AluminiumScrap();

        worker.getInventory().add(scrap);

        Supercomputer supercomputer = new Supercomputer();

        DepositAction action =
                new DepositAction(
                        worker,
                        scrap,
                        supercomputer,
                        mock(Location.class));

        String result = action.execute(worker, null);

        assertTrue(result.contains("Company Rank"));
    }

    @Test
    void menuDescriptionContainsItemName() {

        ContractedWorker worker =
                new ContractedWorker("Worker", 'ඞ', 100,
                        new WeightLimitedInventory(50));

        AluminiumScrap scrap = new AluminiumScrap();

        DepositAction action =
                new DepositAction(
                        worker,
                        scrap,
                        new Supercomputer(),
                        mock(Location.class));

        String result =
                action.menuDescription(worker);

        assertTrue(result.contains("deposits"));
    }
}