package game.weather;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;

import java.util.List;
import java.util.Objects;

/**
 * Rotates active weather events and ticks the current event on every map each turn.
 *
 * @author Lim Kai Ying
 * @version 25.0.2
 */
public class WeatherController {
    private final List<WeatherEvent> rotation;
    private int index = 0;
    private WeatherEvent active;

    /**
     * Creates a controller that cycles through the given events.
     *
     * @param rotation ordered list of weather events to rotate through
     */
    public WeatherController(List<WeatherEvent> rotation) {
        this.rotation = List.copyOf(Objects.requireNonNull(rotation));
    }

    /**
     * Advances the rotation when needed and ticks the active event on every map.
     *
     * @param maps all game maps to update this turn
     */
    public void tick(List<GameMap> maps) {
        if (rotation.isEmpty()) {
            return;
        }
        if (active == null || active.isExpired()) {
            advance();
        }
        for (GameMap map : maps) {
            active.tick(map);
        }
        new Display().println("Weather: " + active.getName() + " (" + active.getTurnsRemaining() + " turns left)");
    }

    /**
     * Selects the next event in the rotation as the active event.
     */
    private void advance() {
        active = rotation.get(index);
        index = (index + 1) % rotation.size();
    }
}
