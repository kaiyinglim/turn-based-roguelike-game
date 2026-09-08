package game.effects.stateful;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.interfaces.TransitionEffect;
import game.grounds.Fire;
import game.loaders.StatefulMoonEnemy;
import game.enums.StatefulCreatureStateId;

/**
 * Transition effect that ignites an area of nearby ground tiles with {@link Fire}.
 * The order in which tiles ignite is deterministic.
 * Intensity scales both the radius and the {@code fireDuration} stored on each new {@link Fire} tile.
 *
 * @author Lim Kai YIng
 * @version 25.0.2
 */
public final class BurnSurroundingsEffect implements TransitionEffect {

    /** Minimum accepted intensity; anything lower is increased. */
    private static final int MIN_INTENSITY = 1;

    /** Maximum accepted intensity; anything higher is reduced. */
    private static final int MAX_INTENSITY = 3;

    /** Radius used for low-intensity area (eight surrounding tiles). */
    private static final int SHORT_RADIUS = 1;

    /** Radius used for the strongest area (twenty-four surrounding tiles). */
    private static final int LONG_RADIUS = 2;

    /** Radius of the burn area around the creature. */
    private final int radius;

    /** Number of turns each newly-placed {@link Fire} tile will burn for. */
    private final int fireDuration;

    /**
     * Constructs a burn-surroundings effect scaled by {@code intensity}.
     * <p>
     * Mapping (after clamping to {@code [MIN_INTENSITY, MAX_INTENSITY]}):
     * <ul>
     *     <li>intensity 1: radius 1, fire duration 2</li>
     *     <li>intensity 2: radius 1, fire duration 3</li>
     *     <li>intensity 3: radius 2, fire duration 4</li>
     * </ul>
     *
     * @param intensity desired burst strength; clamped to the supported range
     */
    public BurnSurroundingsEffect(int intensity) {
        int clamped = Math.max(MIN_INTENSITY, Math.min(MAX_INTENSITY, intensity));
        this.radius = clamped >= MAX_INTENSITY ? LONG_RADIUS : SHORT_RADIUS;
        this.fireDuration = clamped + 1;
    }

    /**
     * Wraps eligible nearby ground tiles with {@link Fire}, preserving each tile's previous
     * ground so the terrain restores itself when the fire expires.
     *
     * @param creature  the enemy that just entered {@code toState}
     * @param fromState the behavioural mode being exited (unused)
     * @param toState   the behavioural mode being entered (unused)
     * @param map       the map hosting the creature
     */
    @Override
    public void apply(StatefulMoonEnemy creature, StatefulCreatureStateId fromState,
                      StatefulCreatureStateId toState, GameMap map) {
        if (!map.contains(creature)) {
            return;
        }

        Location origin = map.locationOf(creature);
        int ignited = 0;
        for (Location target : origin.getNearbyLocations(radius)) {
            Ground ground = target.getGround();

            // Skip tiles already on fire
            if (ground instanceof Fire) {
                continue;
            }
            // Skip impassable ground
            if (!ground.canActorEnter(creature)) {
                continue;
            }

            target.setGround(new Fire(ground, fireDuration));
            ignited++;
        }

        if (ignited > 0) {
            new Display().println(creature + " ignites " + ignited
                    + " surrounding tile(s) for " + fireDuration + " turn(s).");
        }
    }
}
