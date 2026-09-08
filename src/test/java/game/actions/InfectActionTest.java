package game.actions;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.GameMap;
import game.enums.EnemyAbility;
import game.interfaces.Infectable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class InfectActionTest {

    private Actor mockParasite;
    private Actor mockTarget;
    private Infectable mockInfectable;
    private GameMap mockMap;
    private Status mockStatus; // We mock the status so we don't need a real InfectionStatus object
    private InfectAction infectAction;

    @BeforeEach
    void setUp() {
        // 1. Create all our fake engine objects
        mockParasite = mock(Actor.class);
        mockTarget = mock(Actor.class);
        mockInfectable = mock(Infectable.class);
        mockMap = mock(GameMap.class);
        mockStatus = mock(Status.class);

        // 2. Train the Infectable interface to return our fake status when asked
        when(mockInfectable.getInfectedStatus()).thenReturn(mockStatus);

        // 3. Instantiate the action we are testing
        infectAction = new InfectAction(mockParasite, mockTarget, mockInfectable);
    }

    @Test
    void testExecuteInfectsTargetAndKillsParasite() {
        // Arrange: Create a dummy actor to represent whoever is executing the action
        // (Even though the action internally uses 'parasite', the engine still requires an actor parameter)
        Actor actingActor = mock(Actor.class);

        // Act
        infectAction.execute(actingActor, mockMap);

        // Assert: Use verify() to check that the internal state-changing methods were called

        // 1. Check the target received the damage-over-time status object
        verify(mockTarget).addStatus(mockStatus);

        // 2. Check the target received the AI-altering RABID tag
        verify(mockTarget).enableAbility(EnemyAbility.IS_RABID);

        // 3. Check that the parasite successfully killed itself
        verify(mockParasite).unconscious(mockMap);
    }

    @Test
    void testExecuteReturnsSuccessMessage() {
        // Arrange
        Actor actingActor = mock(Actor.class);

        // Stub toString() so the message outputs actual names instead of Mockito memory addresses
        when(mockParasite.toString()).thenReturn("Facehugger");
        when(mockTarget.toString()).thenReturn("Scrap Snatcher");

        // Act
        String result = infectAction.execute(actingActor, mockMap);

        // Assert
        String expectedMessage = "Facehugger infects Scrap Snatcher! Facehugger dies!";
        assertEquals(expectedMessage, result, "The success message should match the exact expected string format.");
    }
}