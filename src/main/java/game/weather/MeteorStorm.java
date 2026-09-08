package game.weather;

import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.enums.WeatherCapability;

import java.util.ArrayList;
import java.util.List;

/**
 * Weather event that strikes 3–5 unoccupied METEOR_TARGET tiles per tick.
 *
 * @author Lim Kai Ying
 * @version 25.0.2
 */
public class MeteorStorm extends WeatherEvent {

    private static final int DURATION = 3;
    private static final int MIN_IMPACTS_PER_TICK = 3;
    private static final int MAX_IMPACTS_PER_TICK = 5;

    /**
     * Creates a meteor storm event with the default duration and impact effect.
     */
    public MeteorStorm() {
        super(new MeteorImpactEffect(), DURATION);
    }

    /**
     * Returns the display name of this weather event.
     *
     * @return {@code "Meteor Storm"}
     */
    @Override
    public String getName() {
        return "Meteor Storm";
    }

    /**
     * Selects unoccupied METEOR_TARGET tiles for impact.
     *
     * @param map the map to scan for impact sites
     * @return a random subset of METEOR_TARGET tiles
     */
    @Override
    protected List<Location> findTargets(GameMap map) {
        List<Location> candidates = new ArrayList<>();
        for (Location location : scanMap(map, WeatherCapability.METEOR_TARGET)) {
            if (!location.containsAnActor()) {
                candidates.add(location);
            }
        }
        return selectRandomSubset(candidates, MIN_IMPACTS_PER_TICK, MAX_IMPACTS_PER_TICK);
    }
}
