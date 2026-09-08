package game.enums;

/**
 * Capabilities tagged on Ground
 * so weather events can target terrain without referencing concrete ground types.
 *
 * @author Lim Kai Ying
 * @version 25.0.2
 */
public enum WeatherCapability {
    /** Ground that acid rain can convert to Toxic Waste. */
    ACID_CORRODIBLE,
    /** Unoccupied tiles that may receive a meteor impact. */
    METEOR_TARGET,
    /** Fire tiles that propagate flames during a solar flare. */
    SOLAR_SOURCE,
    /** Ground that can be ignited by spreading fire. */
    FLAMMABLE,
    /** Toxic pool that spreads acid corrosion and hosts stacking poison. */
    CORROSIVE_POOL
}
