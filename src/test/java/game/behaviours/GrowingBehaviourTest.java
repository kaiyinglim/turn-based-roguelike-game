package game.behaviours;

import game.trees.FleshyTreeStage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GrowingBehaviourTest {

    private FleshyTreeStage mockCurrentStage;
    private FleshyTreeStage mockNextStage;
    private GrowingBehaviour<FleshyTreeStage> growingBehaviour;

    @BeforeEach
    void setUp() {
        // 1. Create our fake stages
        mockCurrentStage = mock(FleshyTreeStage.class);
        mockNextStage = mock(FleshyTreeStage.class);

        // 2. Instantiate the behaviour using a Supplier lambda "() ->"
        // This tells the behaviour: "When you are ready to grow, use this mockNextStage"
        growingBehaviour = new GrowingBehaviour<>(20, 25, () -> mockNextStage);
    }

    @Test
    void testDoesNotGrowWhenAgeIsTooLow() {
        // Arrange
        int currentAge = 5; // Not old enough to grow

        // Act
        FleshyTreeStage result = growingBehaviour.grow(currentAge, mockCurrentStage);

        // Assert
        assertEquals(mockCurrentStage, result,
                "Behaviour should return the current stage if the age requirement is not met.");
    }

    @Test
    void testGrowsToNextStageWhenConditionsAreMet() {
        // Arrange
        int readyAge = 20; // Exact age required to trigger growth

        // Act
        // (Note: If this relies on a 25% Math.random() chance, we loop to guarantee it hits for the test)
        FleshyTreeStage result = mockCurrentStage;
        for (int i = 0; i < 100; i++) {
            result = growingBehaviour.grow(readyAge, mockCurrentStage);
            if (result == mockNextStage) break; // It grew!
        }

        // Assert
        assertNotNull(result, "Behaviour should not return null.");
        assertEquals(mockNextStage, result,
                "Behaviour should successfully pull from the Supplier and return the next stage.");
    }
}