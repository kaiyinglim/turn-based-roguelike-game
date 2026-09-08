package game.interfaces;

import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.weather.WeatherEvent;

import java.util.List;

/**
 * Abstraction for weather-driven mutations (terrain, status, AoE, spread).
 *
 * @author Lim Kai Ying
 * @version 25.0.2
 */
public interface WeatherEffect {

    /**
     * Applies this effect to the given target tiles on the map.
     *
     * @param targets locations selected by the active {@link WeatherEvent}
     * @param map     the map being affected
     */
    void apply(List<Location> targets, GameMap map);
}
