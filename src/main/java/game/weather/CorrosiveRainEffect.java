package game.weather;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.enums.WeatherCapability;
import game.grounds.ToxicWaste;
import game.interfaces.WeatherEffect;
import game.statuses.StackingPoison;

import java.util.List;

/**
 * Converts targeted tiles to {@link ToxicWaste}, spreads corrosion to adjacent
 * {@link WeatherCapability#ACID_CORRODIBLE} neighbours, and applies stacking poison
 * to actors standing in toxic pools.
 *
 * @author Lim Kai Ying
 * @version 25.0.2
 */
public class CorrosiveRainEffect implements WeatherEffect {

    private static final int POISON_DURATION = 5;
    private static final int POISON_BASE_DAMAGE = 1;

    /**
     * Corrodes direct targets, spreads existing pools, then updates poison on exposed actors.
     *
     * @param targets locations selected by {@link AcidRain} this tick
     * @param map     the map being affected
     */
    @Override
    public void apply(List<Location> targets, GameMap map) {
        corrodeTargets(targets);       // direct hits from AcidRain targeting
        spreadCorrosion(map);          // pools grow into adjacent corrodible tiles
        applyStackingPoison(map);      // standing in a pool stacks or starts poison
    }

    /**
     * Replaces each selected tile with toxic waste.
     *
     * @param targets locations to corrode
     */
    private void corrodeTargets(List<Location> targets) {
        for (Location target : targets) {
            target.setGround(new ToxicWaste());
            new Display().println("Acid rain corroded " + target + " into toxic waste.");
        }
    }

    /**
     * For every toxic waste tile, converts adjacent acid-corrodible neighbours into new pools.
     *
     * @param map the map to scan for pools and spread targets
     */
    private void spreadCorrosion(GameMap map) {
        int spreadCount = 0;
        for (Location pool : WeatherEvent.scanMap(map, WeatherCapability.CORROSIVE_POOL)) {
            for (Exit exit : pool.getExits()) {
                Location neighbour = exit.getDestination();
                Ground ground = neighbour.getGround();
                // only untouched floor-like tiles convert; already-toxic neighbours are skipped
                if (ground.hasAbility(WeatherCapability.ACID_CORRODIBLE)) {
                    neighbour.setGround(new ToxicWaste());
                    spreadCount++;
                }
            }
        }
        if (spreadCount > 0) {
            new Display().println("Acid pools spread to " + spreadCount + " adjacent tile(s).");
        }
    }

    /**
     * Stacks or applies {@link StackingPoison} on every actor standing on toxic waste.
     *
     * @param map the map to scan for exposed actors
     */
    private void applyStackingPoison(GameMap map) {
        for (Location location : WeatherEvent.scanMap(map, WeatherCapability.CORROSIVE_POOL)) {
            if (!location.containsAnActor()) {
                continue;
            }
            Actor actor = location.getActor();
            List<StackingPoison> existing = actor.statusesOf(StackingPoison.class);
            if (!existing.isEmpty()) {
                existing.get(0).stack(); // more damage per tick, refresh duration
                new Display().println("Acid exposure intensified poison on " + actor + " at " + location + ".");
            } else {
                actor.addStatus(new StackingPoison(POISON_DURATION, POISON_BASE_DAMAGE));
                new Display().println("Acid rain poisoned " + actor + " at " + location + ".");
            }
        }
    }
}
