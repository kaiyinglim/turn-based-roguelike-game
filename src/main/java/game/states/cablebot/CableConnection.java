package game.states.cablebot;

import edu.monash.fit2099.engine.actors.Actor;

/**
 * Tracks an invisible cable from a {@link game.enemies.Cablebot} to a target worker.
 * It is an internal connection object stored inside Cablebot.
 */
public class CableConnection {

    /**
     * Minimum cable strength.
     */
    public static final int MIN_STRENGTH = 1;

    /**
     * Maximum cable strength.
     */
    private static final int MAX_STRENGTH = 3;

    /**
     * Strain level at which the cable breaks.
     */
    private static final int BREAKING_STRAIN = 3;

    /** Default strength when latch behaviour creates a new cable */
    public static final int DEFAULT_INITIAL_STRENGTH = MIN_STRENGTH;

    private final Actor target;
    private int strength;
    private int strain;
    private int turnsAttached;

    /**
     * Initializes a cable connection with the given target and strength.
     *
     * @param target   worker this cable is attached to
     * @param strength desired initial strength (clamped)
     */
    public CableConnection(Actor target, int strength) {
        this.target = target;
        this.strength = Math.max(MIN_STRENGTH, Math.min(MAX_STRENGTH, strength));
        this.strain = 0;
        this.turnsAttached = 0;
    }

    /**
     * @return the target worker this cable is attached to
     */
    public Actor getTarget() {
        return target;
    }

    /**
     * @return current cable strength
     */
    public int getStrength() {
        return strength;
    }

    /**
     * Increments strength by one up to {@link #MAX_STRENGTH}.
     */
    public void strengthen() {
        if (strength < MAX_STRENGTH) {
            strength++;
        }
    }

    /**
     * Decrements strength by one.
     */
    public void weaken() {
        strength--;
    }

    /**
     * Increments strain by one.
     */
    public void increaseStrain() {
        strain++;
    }

    /**
     * @return number of turns this cable has been attached
     */
    public int getTurnsAttached() {
        return turnsAttached;
    }

    /**
     * Increments the number of turns this cable has been attached.
     */
    public void tick() {
        turnsAttached++;
    }

    /**
     * @return true if strength is non-positive or strain has reached the break threshold
     */
    public boolean isBroken() {
        return strength <= 0 || strain >= BREAKING_STRAIN;
    }
}
