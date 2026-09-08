package game.enemies;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.ActorStatistics;
import edu.monash.fit2099.engine.actors.TestActor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.AttackAction;
import game.actions.SnatchAction;
import game.enums.EnemyAbility;
import game.enums.ItemStatistics;
import game.enums.WorkerAbility;
import game.statuses.InfectedStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScrapSnatcherTest {

    private ScrapSnatcher snatcher;
    private GameMap mockMap;
    private Location mockLocation;
    private ActionList mockActions;
    private Display mockDisplay;

    @BeforeEach
    void setUp() {
        // Instantiate the real ScrapSnatcher for testing
        snatcher = new ScrapSnatcher();

        // Mock the environment needed for playTurn()
        mockMap = mock(GameMap.class);
        mockLocation = mock(Location.class);
        mockActions = new ActionList();
        mockDisplay = mock(Display.class);

        // Tell the map where the snatcher is standing
        when(mockMap.locationOf(snatcher)).thenReturn(mockLocation);
    }

    @Test
    void testHealthySnatcherUsesSnatchAndWander() {
        // --- PART 1: TEST SNATCH BEHAVIOUR ---
        // Arrange: Place a depositable item on the floor
        Item mockItem = mock(Item.class);
        when(mockItem.hasStatistic(ItemStatistics.DEPOSITABLE)).thenReturn(true);
        when(mockLocation.getItems()).thenReturn(List.of(mockItem));

        // Act: Ask the snatcher what it wants to do
        Action snatchResult = snatcher.playTurn(mockActions, null, mockMap, mockDisplay);

        // Assert: It should choose to snatch the item
        assertNotNull(snatchResult, "Snatcher should return an action.");
        assertTrue(snatchResult instanceof SnatchAction, "Healthy Snatcher should prioritize SnatchAction when an item is present.");

        // --- PART 2: TEST WANDER BEHAVIOUR ---
        // Arrange: Remove the item from the floor so SnatchBehaviour aborts
        when(mockLocation.getItems()).thenReturn(List.of());

        // Setup a valid exit so WanderBehaviour has a place to walk to
        Exit mockExit = mock(Exit.class);
        Location mockDestination = mock(Location.class);
        when(mockLocation.getExits()).thenReturn(List.of(mockExit));
        when(mockExit.getDestination()).thenReturn(mockDestination);
        when(mockDestination.canActorEnter(snatcher)).thenReturn(true); // Ensure tile is walkable

        // Act
        Action wanderResult = snatcher.playTurn(mockActions, null, mockMap, mockDisplay);

        // Assert: It couldn't snatch, so it fell back to WanderBehaviour (which generates a MoveAction)
        assertNotNull(wanderResult, "Snatcher should return an action.");
        assertFalse(wanderResult instanceof SnatchAction, "Snatcher cannot snatch if there are no items.");
        assertFalse(wanderResult instanceof AttackAction, "Snatcher cannot attack if not rabid.");
        // If it isn't Snatching or Attacking, it successfully fell through to WanderBehaviour!
    }

    @Test
    void testInfectedSnatcherUsesAttackInsteadOfSnatch() {
        // Arrange:
        // 1. Infect the Snatcher manually for the test
        snatcher.enableAbility(EnemyAbility.IS_RABID);

        // 2. Put a depositable item on the floor (To prove it IGNORES it)
        Item mockItem = mock(Item.class);
        when(mockItem.hasStatistic(ItemStatistics.DEPOSITABLE)).thenReturn(true);
        when(mockLocation.getItems()).thenReturn(List.of(mockItem));

        // 3. Put a Worker on an adjacent tile
        Exit mockExit = mock(Exit.class);
        Location mockDestination = mock(Location.class);

        // FIX: Use the TestActor instead of mock(Actor.class)!
        TestActor workerTarget = new TestActor();

        // FIX: Give it the ability normally instead of using Mockito's when().thenReturn()
        workerTarget.enableAbility(WorkerAbility.IS_WORKER);

        when(mockLocation.getExits()).thenReturn(List.of(mockExit));
        when(mockExit.getDestination()).thenReturn(mockDestination);
        when(mockDestination.containsAnActor()).thenReturn(true);

        // Put our dummy worker on the destination tile
        when(mockDestination.getActor()).thenReturn(workerTarget);

        // Act: Ask the infected snatcher what it wants to do
        Action result = snatcher.playTurn(mockActions, null, mockMap, mockDisplay);

        // Assert: It should completely ignore the item on the floor and attack the worker!
        assertNotNull(result, "Infected Snatcher should return an action.");
        assertTrue(result instanceof AttackAction, "Infected Snatcher should prioritize AttackAction over SnatchAction.");
    }

    @Test
    void testInfectionCausesOneDamage() {
        // Arrange
        // Record the Snatcher's starting health
        int initialHealth = snatcher.getStatistic(ActorStatistics.HEALTH);

        // Retrieve the customized infection status and CAST it to InfectedStatus
        // so Java knows exactly what methods are available.
        InfectedStatus infection = (InfectedStatus) snatcher.getInfectedStatus();

        // Act
        // Apply the status to the actor
        snatcher.addStatus(infection);

        // Manually simulate 1 turn passing by explicitly calling tickStatus.
        // We pass both the snatcher AND the mockLocation required by your method signature.
        infection.tickStatus(snatcher, mockLocation);

        // Assert
        int currentHealth = snatcher.getStatistic(ActorStatistics.HEALTH);
        assertEquals(initialHealth - 1, currentHealth, "The Snatcher's health should decrease by exactly 1 when the infection ticks.");
    }
}