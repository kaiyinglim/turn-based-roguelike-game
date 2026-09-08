package game.grounds;

import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;

/**
 * Temporary environmental tile representing a destroyed area.
 *
 * This ground type is created when high-impact deployable effects
 * (such as Mines or Turrets) alter the terrain. It prevents actors
 * from entering and remains in place for a limited duration before
 * reverting to normal terrain.
 *
 * The DestroyedTile acts as a dynamic environmental obstacle within
 * the autonomous tactical support system, enabling persistent battlefield
 * modification and area denial effects.
 *
 * @author Suzan Xuan Loh Palmer
 * @version 25.0.3
 */
public class DestroyedTile extends Ground {

    private int remainingTurns;
    /**
     * Constructs a DestroyedTile with a fixed duration.
     *
     * @param duration number of turns before the tile reverts to normal floor
     */
    public DestroyedTile(int duration) {
        super('X', "Destroyed Tile");
        this.remainingTurns = duration;
    }
    /**
     * Determines whether an actor can enter this tile.
     *
     * Destroyed tiles are impassable by all actors.
     *
     * @param actor the actor attempting to enter
     * @return false (entry is blocked)
     */
    @Override
    public boolean canActorEnter(edu.monash.fit2099.engine.actors.Actor actor) {
        return false;
    }
    /**
     * Updates the tile each turn.
     *
     * Decreases the remaining duration and restores the tile to a normal
     * Floor once the duration expires.
     *
     * @param location the location of this ground
     */
    @Override
    public void tick(Location location) {
        remainingTurns--;

        if (remainingTurns <= 0) {
            location.setGround(new Floor());
        }
    }
}
