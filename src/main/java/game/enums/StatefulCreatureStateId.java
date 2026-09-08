package game.enums;

/**
 * Identifies concrete behavioural states for {@link game.loaders.StatefulMoonEnemy}
 * subclasses. Each enum constant belongs to exactly one creature family so that
 * state maps and transition keys remain unambiguous across the moon facility.
 *
 * @author Lim Kai YIng
 * @version 25.0.2
 */
public enum StatefulCreatureStateId {

    // Mannequin family
    /**
     * Mannequin is frozen in place.
     */
    MANNEQUIN_IDLE,
    /**
     * Mannequin wanders and collects portable items from adjacent ground tiles.
     */
    MANNEQUIN_COLLECTOR,
    /**
     * Mannequin moves toward the nearest worker.
     */
    MANNEQUIN_STALKER,
    /**
     * Mannequin attacks when exactly one worker occupies an adjacent tile.
     */
    MANNEQUIN_BERSERK,

    // Cablebot family
    /**
     * Cablebot waits and senses adjacent workers and cable/charge status.
     */
    CABLEBOT_SURVEY,
    /**
     * Cablebot latches cables onto adjacent workers.
     */
    CABLEBOT_LATCH,
    /**
     * Cablebot builds charge through active cable connections.
     */
    CABLEBOT_CHARGE,
    /**
     * Cablebot overloads through cables to disrupt workers.
     */
    CABLEBOT_OVERLOAD
}
