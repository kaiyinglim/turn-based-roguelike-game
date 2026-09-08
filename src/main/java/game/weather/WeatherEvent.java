package game.weather;

import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.enums.WeatherCapability;
import game.interfaces.WeatherEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Abstract weather lifecycle: find targets each tick and delegate world mutation to a
 * {@link WeatherEffect}.
 *
 * @author Lim Kai Ying
 * @version 25.0.2
 */
public abstract class WeatherEvent {

    private static final Random random = new Random();

    /** The effect executed each tick against selected targets. */
    protected final WeatherEffect effect;
    /** Turns left before this event expires and the controller advances. */
    protected int turnsRemaining;

    /**
     * Creates a weather event with the given effect and duration.
     *
     * @param effect   the effect to run each tick
     * @param duration number of turns this event remains active
     */
    protected WeatherEvent(WeatherEffect effect, int duration) {
        this.effect = effect;
        this.turnsRemaining = duration;
    }

    /**
     * Runs one turn of this event: find targets, apply the effect, decrement duration.
     *
     * @param map the map to affect this tick
     */
    public final void tick(GameMap map) {
        List<Location> targets = findTargets(map);
        effect.apply(targets, map);
        turnsRemaining--;
    }

    /**
     * Selects the locations this event will affect on the given map.
     *
     * @param map the map to scan for targets
     * @return locations passed to {@link WeatherEffect#apply}
     */
    protected abstract List<Location> findTargets(GameMap map);

    /**
     * Returns whether this event has no turns remaining.
     *
     * @return {@code true} when {@code turnsRemaining <= 0}
     */
    public boolean isExpired() {
        return turnsRemaining <= 0;
    }

    /**
     * Returns how many turns remain before this event expires.
     *
     * @return turns remaining (may be zero or negative after the final tick)
     */
    public int getTurnsRemaining() {
        return turnsRemaining;
    }

    /**
     * Returns the display name of this weather event.
     *
     * @return a human-readable name for logging and UI
     */
    public abstract String getName();

    /**
     * Scans the map for all locations whose ground has the given capability.
     *
     * @param map        the map to scan
     * @param capability the capability to match
     * @return all matching locations on the map
     */
    protected static List<Location> scanMap(GameMap map, WeatherCapability capability) {
        List<Location> matches = new ArrayList<>();
        for (int x : map.getXRange()) {
            for (int y : map.getYRange()) {
                Location location = map.at(x, y);
                if (location.getGround().hasAbility(capability)) {
                    matches.add(location);
                }
            }
        }
        return matches;
    }

    /**
     * Shuffles the given candidates and returns a random subset within the inclusive size range.
     *
     * @param candidates locations to choose from
     * @param min        minimum number of targets to select
     * @param max        maximum number of targets to select
     * @return a shuffled sublist of {@code candidates}, or an empty list when none are available
     */
    protected static List<Location> selectRandomSubset(List<Location> candidates, int min, int max) {
        if (candidates.isEmpty()) {
            return List.of();
        }
        List<Location> shuffled = new ArrayList<>(candidates);
        Collections.shuffle(shuffled, random);
        int count = Math.min(shuffled.size(), random.nextInt(max - min + 1) + min);
        return shuffled.subList(0, count);
    }
}
