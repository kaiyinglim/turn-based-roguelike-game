package game.items;

import game.loaders.ContractedWorker;
import game.loaders.WeightLimitedInventory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlasmaCutterTest {

    @Test
    void correctPriceAndWeight() {
        PlasmaCutter cutter = new PlasmaCutter();

        assertEquals(50, cutter.getPrice());
    }

    @Test
    void purchaseWithoutEnoughCreditsReturnsMessage() {
        ContractedWorker worker =
                new ContractedWorker("Worker", 'ඞ', 100, new WeightLimitedInventory(50));

        PlasmaCutter cutter = new PlasmaCutter();

        String result = cutter.onPurchase(worker);

        assertTrue(result.contains("without enough credits"));
    }

    @Test
    void purchaseWithCreditsAppliesDamage() {
        ContractedWorker worker =
                new ContractedWorker("Worker", 'ඞ', 100, new WeightLimitedInventory(50));

        worker.addCredits(100);

        PlasmaCutter cutter = new PlasmaCutter();

        String result = cutter.onPurchase(worker);

        assertTrue(result.contains("takes 5 damage"));
    }

    @Test
    void purchaseWithExactlyFiftyCreditsSucceeds() {
        ContractedWorker worker =
                new ContractedWorker("Worker", 'ඞ', 100,
                        new WeightLimitedInventory(50));

        worker.addCredits(50);

        PlasmaCutter cutter = new PlasmaCutter();

        String result = cutter.onPurchase(worker);

        assertFalse(result.contains("without enough credits"));
    }

    @Test
    void purchaseWithFortyNineCreditsFails() {
        ContractedWorker worker =
                new ContractedWorker("Worker", 'ඞ', 100,
                        new WeightLimitedInventory(50));

        worker.addCredits(49);

        PlasmaCutter cutter = new PlasmaCutter();

        String result = cutter.onPurchase(worker);

        assertTrue(result.contains("without enough credits"));
    }
}