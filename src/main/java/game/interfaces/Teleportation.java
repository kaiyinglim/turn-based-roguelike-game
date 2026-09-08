package game.interfaces;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * Teleport class is an interface class that represents an ability to teleport
 * @author FIT2099 Vladimir Gayvoronskiy
 */
public interface Teleportation {
    /**
     * The method that will teleport the actor
     * @return the message of the teleportation occurring.
     */
    public String teleport(Actor actor, GameMap map, int x_coordinate, int y_coordinate);
}
