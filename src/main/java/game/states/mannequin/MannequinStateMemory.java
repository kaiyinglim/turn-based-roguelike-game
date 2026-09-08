package game.states.mannequin;

/**
 * Per-turn counters for mannequin idle loneliness and active no-pickup streaks.
 * {@link game.enemies.Mannequin} owns the fields; states read and update via this interface.
 *
 * @author Lim Kai YIng
 * @version 25.0.2
 */
public interface MannequinStateMemory {

    /**
     * @return turns spent in idle with no adjacent workers (lonely streak)
     */
    int getIdleLonelyTurns();

    /**
     * Adds one to the idle streak after a turn in idle with no adjacent workers.
     */
    void incrementIdleLonelyTurns();

    /**
     * Clears the idle streak, for example when a worker becomes adjacent.
     */
    void resetIdleLonelyTurns();

    /**
     * @return consecutive turns in collector behaviour where no item was picked up
     */
    int getNoPickupTurns();

    /**
     * Resets the no-pickup streak to zero after a pickup or on state entry rules.
     */
    void clearNoPickupTurns();

    /**
     * Adds one to the no-pickup streak when a collector turn ends without a pickup.
     */
    void incrementNoPickupTurns();
}
