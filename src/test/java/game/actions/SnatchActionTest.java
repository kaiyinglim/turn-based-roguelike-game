package game.actions;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Inventory;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SnatchActionTest {

    private Actor mockActor;
    private GameMap mockMap;
    private Location mockLocation;
    private Item mockItem;
    private Inventory mockInventory;
    private SnatchAction snatchAction;

    @BeforeEach
    void setUp() {
        // 1. Create all our fake engine objects
        mockActor = mock(Actor.class);
        mockMap = mock(GameMap.class);
        mockLocation = mock(Location.class);
        mockItem = mock(Item.class);
        mockInventory = mock(Inventory.class);

        // 2. Train the map to return our fake location when asked where the actor is
        when(mockMap.locationOf(mockActor)).thenReturn(mockLocation);

        // 3. Train the actor to return our fake inventory so the test doesn't crash
        when(mockActor.getInventory()).thenReturn(mockInventory);

        // 4. Instantiate the action we are actually testing
        snatchAction = new SnatchAction(mockItem);
    }

    @Test
    void testExecuteRemovesItemFromMapAndAddsToInventory() {
        // Act
        snatchAction.execute(mockActor, mockMap);

        // Assert
        // Verify that the exact target item was removed from the tile
        verify(mockLocation).removeItem(mockItem);

        // Verify that the item was successfully placed into the actor's pockets
        verify(mockInventory).add(mockItem);
    }

    @Test
    void testExecuteReturnsSuccessMessage() {
        // Arrange
        // We need to give our mocks names so the returned String has actual words in it
        when(mockActor.toString()).thenReturn("Scrap Snatcher");
        when(mockItem.toString()).thenReturn("Aluminium Scrap");

        // Act
        String result = snatchAction.execute(mockActor, mockMap);

        // Assert
        // Check that the returned string matches the exact format of the execute method
        String expectedMessage = "Scrap Snatcher snatches the Aluminium Scrap from the ground!";
        assertEquals(expectedMessage, result, "The success message should match the expected format.");
    }
}