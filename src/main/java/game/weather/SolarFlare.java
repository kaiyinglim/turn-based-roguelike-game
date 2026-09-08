package game.weather;

import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.enums.WeatherCapability;

import java.util.List;

/**
 * Weather event that spreads fire from {@link WeatherCapability#SOLAR_SOURCE} tiles.
 * Bootstrap seeding when no sources exist is handled by {@link SolarIgnitionEffect}.
 *
 * @author Lim Kai Ying
 * @version 25.0.2
 */
public class SolarFlare extends WeatherEvent {

    private static final int DURATION = 3;

    /**
     * Creates a solar flare event with the default duration and ignition effect.
     */
    public SolarFlare() {
        super(new SolarIgnitionEffect(), DURATION);
    }

    /**
     * Returns the display name of this weather event.
     *
     * @return {@code "Solar Flare"}
     */
    @Override
    public String getName() {
        return "Solar Flare";
    }

    /**
     * Returns all SOLAR_SOURCE tiles on the map.
     *
     * @param map the map to scan for fire spread sources
     * @return locations that act as solar fire sources this tick
     */
    @Override
    protected List<Location> findTargets(GameMap map) {
        return scanMap(map, WeatherCapability.SOLAR_SOURCE);
    }
}
