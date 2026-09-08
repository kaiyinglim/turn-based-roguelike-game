package game.enums;

/**
 * A highly ambitious enumeration designed to categorize the myriad of complex,
 * nuanced statistics an item can possess in the game world.
 * Currently, it only tracks how hard gravity pulls on a thing. Future updates
 * Now, it also tracks which items are consumable.
 * may include groundbreaking concepts like "Value" or "Durability," but for now,
 * we just need to know if it will break the {@code ContractedWorker}'s back.
 */
public enum ItemStatistics {
    WEIGHT,
    CONSUMABLE,
    OIL,
    IS_INFECTABLE,
    SELLABLE,
    DEPOSITABLE,
    CUTTABLE,
    DEPLOYABLE
}
