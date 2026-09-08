package game.weather;

import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.enums.WeatherCapability;

import java.util.List;

/**
 * Weather event that corrodes a random subset of ACID_CORRODIBLE tiles each tick
 * so acid pools spread gradually over time.
 *
 * @author Lim Kai Ying
 * @version 25.0.2
 */
public class AcidRain extends WeatherEvent {

    private static final int DURATION = 3;
    private static final int MIN_TARGETS_PER_TICK = 1;
    private static final int MAX_TARGETS_PER_TICK = 3;

    /**
     * Creates an acid rain event with the default duration and corrosive effect.
     */
    public AcidRain() {
        super(new CorrosiveRainEffect(), DURATION);
    }

    /**
     * Returns the display name of this weather event.
     *
     * @return {@code "Acid Rain"}
     */
    @Override
    public String getName() {
        return "Acid Rain";
    }

    /**
     * Selects a random subset of ACID_CORRODIBLE tiles each tick.
     *
     * @param map the map to scan for corrodeable ground
     * @return a random subset of ACID_CORRODIBLE tiles
     */
    @Override
    protected List<Location> findTargets(GameMap map) {
        List<Location> candidates = scanMap(map, WeatherCapability.ACID_CORRODIBLE);
        return selectRandomSubset(candidates, MIN_TARGETS_PER_TICK, MAX_TARGETS_PER_TICK);
    }
}
