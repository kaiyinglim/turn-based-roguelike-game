package game.behaviours;

import java.util.function.Supplier;

/**
 * Encapsulates the logic for a plant or entity growing over time.
 */
public class GrowingBehaviour<T> {

    private final int targetTurns;
    private final double successRate;
    private final Supplier<T> nextStageSupplier;

    public GrowingBehaviour(int targetTurns, double successRate, Supplier<T> nextStageSupplier) {
        this.targetTurns = targetTurns;
        this.successRate = successRate;
        this.nextStageSupplier = nextStageSupplier;
    }

    /**
     * Executes the growth logic.
     * @param currentAge the current age of the entity
     * @param currentStage the entity's current stage to return if growth fails
     * @return the next stage if successful, otherwise the current stage
     */
    public T grow(int currentAge, T currentStage) {
        if (currentAge > 0 && currentAge % targetTurns == 0 && Math.random() < successRate) {
            return nextStageSupplier.get();
        }
        return currentStage;
    }
}