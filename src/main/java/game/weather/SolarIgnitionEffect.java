package game.weather;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.enums.WeatherCapability;
import game.grounds.Fire;
import game.interfaces.WeatherEffect;
import game.statuses.Burning;

import java.util.List;

/**
 * Spreads SOLAR_SOURCE tagged fire to adjacent FLAMMABLE tiles. 
 * Seeds initial fires when no sources exist.
 * New fire tiles gain SOLAR_SOURCE, causing the spread radius to grow outward each tick.
 *
 * @author Lim Kai Ying
 * @version 25.0.2
 */
public class SolarIgnitionEffect implements WeatherEffect {

    private static final int MIN_SEED_FIRES = 1;
    private static final int MAX_SEED_FIRES = 2;
    private static final int SEED_FIRE_DURATION = 5;
    private static final int SPREAD_FIRE_DURATION = 5;
    private static final int BURNING_DURATION = 5;
    private static final int BURNING_DAMAGE = 1;

    /**
     * Seeds solar fires when no sources exist, then spreads from each source tile.
     *
     * @param targets locations selected by {@link SolarFlare}; may be empty on first tick
     * @param map     the map being affected
     */
    @Override
    public void apply(List<Location> targets, GameMap map) {
        List<Location> sources = targets;

        // first tick: no SOLAR_SOURCE tiles exist yet, seed initial fires
        if (sources.isEmpty()) {
            seedSolarSources(map);
            // re-scan so the seeded tiles are included as sources this tick
            sources = WeatherEvent.scanMap(map, WeatherCapability.SOLAR_SOURCE);
        }

        // spread outward from every current source
        int spreadCount = 0;
        for (Location source : sources) {
            spreadCount += spreadFromSource(source);
        }
        if (spreadCount > 0) {
            new Display().println("Solar flare spread fire to " + spreadCount + " tile(s).");
        }
    }

    /**
     * Ignites a random subset of FLAMMABLE tiles with SOLAR_SOURCE tagged fire.
     *
     * @param map the map on which to place seed fires
     */
    private void seedSolarSources(GameMap map) {
        // find all flammable tiles as seeding candidates
        List<Location> flammable = WeatherEvent.scanMap(map, WeatherCapability.FLAMMABLE);
        List<Location> seeds = WeatherEvent.selectRandomSubset(flammable, MIN_SEED_FIRES, MAX_SEED_FIRES);
        for (Location location : seeds) {
            placeSolarFire(location, SEED_FIRE_DURATION, true);
        }
    }

    /**
     * Spreads solar-tagged fire to adjacent FLAMMABLE tiles that are not already solar fire sources.
     *
     * @param source the solar fire tile spreading flames this tick
     * @return number of adjacent tiles newly ignited this tick
     */
    private int spreadFromSource(Location source) {
        int spreadCount = 0;
        for (Exit exit : source.getExits()) {
            Location adjacent = exit.getDestination();
            Ground ground = adjacent.getGround();

            // skip non-flammable tiles
            if (!ground.hasAbility(WeatherCapability.FLAMMABLE)) {
                continue;
            }

            // skip tiles already on solar fire to avoid resetting their duration
            if (ground.hasAbility(WeatherCapability.SOLAR_SOURCE)) {
                continue;
            }

            placeSolarFire(adjacent, SPREAD_FIRE_DURATION, false);
            spreadCount++;
        }
        return spreadCount;
    }

    /**
     * Replaces the ground at the given location with solar-tagged fire and
     * applies Burning status to any actor standing there.
     *
     * @param location     the tile to ignite
     * @param fireDuration number of turns the new fire lasts
     * @param seeded       whether this fire was created by initial solar seeding
     */
    private void placeSolarFire(Location location, int fireDuration, boolean seeded) {
        Ground existing = location.getGround();

        // create fire, tag it as a solar source so next tick it spreads further
        Fire fire = new Fire(existing, fireDuration);
        fire.enableAbility(WeatherCapability.SOLAR_SOURCE);
        location.setGround(fire);

        if (seeded) {
            new Display().println("Solar flare ignited " + location + ".");
        }

        // burn any actor caught on the ignited tile
        if (location.containsAnActor()) {
            location.getActor().addStatus(new Burning(BURNING_DURATION, BURNING_DAMAGE));
            new Display().println("Solar flare burned " + location.getActor() + " at " + location + ".");
        }
    }
}
