package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.SnatchAction;
import game.enemies.ScrapSnatcher;
import game.enums.EnemyAbility;
import game.enums.ItemStatistics;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SnatchBehaviourTest {

    @Test
    void testOperateReturnsSnatchActionForHealthyScrapSnatcher() {
        // 1. Spawn a healthy Snatcher (has IS_INFECTABLE, but NOT IS_RABID)
        ScrapSnatcher snatcher = new ScrapSnatcher();

        // 2. Setup the environment
        Location mockLocation = mock(Location.class);
        Item mockItem = mock(Item.class);
        when(mockItem.hasStatistic(ItemStatistics.DEPOSITABLE)).thenReturn(true);
        when(mockLocation.getItems()).thenReturn(List.of(mockItem));

        SnatchBehaviour snatchBehaviour = new SnatchBehaviour();

        // 3. Execute
        Action result = snatchBehaviour.operate(snatcher, mockLocation);

        // 4. Verify it successfully tries to pick up the item
        assertNotNull(result, "Behaviour should return an action when a depositable item is present.");
        assertTrue(result instanceof SnatchAction, "The returned action should be exactly a SnatchAction.");
    }

    @Test
    void testOperateReturnsNullWhenScrapSnatcherIsRabid() {
        // 1. Spawn a Snatcher and artificially infect it for the test
        ScrapSnatcher rabidSnatcher = new ScrapSnatcher();
        rabidSnatcher.enableAbility(EnemyAbility.IS_RABID);

        // 2. Setup the environment with a valid item
        Location mockLocation = mock(Location.class);
        Item mockItem = mock(Item.class);
        when(mockLocation.getItems()).thenReturn(List.of(mockItem)); // Doesn't matter if it's depositable now

        SnatchBehaviour snatchBehaviour = new SnatchBehaviour();

        // 3. Execute
        Action result = snatchBehaviour.operate(rabidSnatcher, mockLocation);

        // 4. Verify it ignores the item completely because it is rabid
        assertNull(result, "Behaviour should return null if the Scrap Snatcher is rabid.");
    }
}