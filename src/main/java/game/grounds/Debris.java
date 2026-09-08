package game.grounds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;

/**
 * Impassable rubble left after a meteor destroys ground items. Decays back to Floor.
 *
 * @author Lim Kai Ying
 * @version 25.0.2
 */
public class Debris extends Ground {

    private static final int DEFAULT_DURATION = 5;

    private int turnsRemaining;

    /**
     * Creates debris that blocks movement and decays after the default duration.
     */
    public Debris() {
        this(DEFAULT_DURATION);
    }

    /**
     * Creates debris that blocks movement and decays after the given duration.
     *
     * @param duration number of turns before this tile restores to a floor
     */
    public Debris(int duration) {
        super('%', "Debris");
        this.turnsRemaining = duration;
    }

    /**
     * Returns false so actors cannot walk through debris.
     *
     * @param actor the actor 
     * @return always false
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        return false;
    }

    /**
     * Counts down remaining turns and restores a floor when debris expires.
     *
     * @param location the tile occupied by this debris
     */
    @Override
    public void tick(Location location) {
        if (--turnsRemaining <= 0) {
            location.setGround(new Floor());
        }
    }
}
