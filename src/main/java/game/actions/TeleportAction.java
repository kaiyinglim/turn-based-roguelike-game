package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.interfaces.Teleportation;

public class TeleportAction extends Action {
    /**
     * The teleport object that contains the specific teleport
     */
    private final Teleportation teleport;

    private final int x_coordinate;
    private final int y_coordinate;
    /**
     * Constructor for TeleportAction
     * @param teleport the teleport object that contains the specific teleport
     */
    public TeleportAction(Teleportation teleport, int x_coordinate, int y_coordinate) {
        this.teleport = teleport;
        this.x_coordinate = x_coordinate;
        this.y_coordinate = y_coordinate;
    }

    /**
     * Executes the teleport action, moving the actor to the specific x and y coordinates
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return a string describing what the action did
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        try{
        return teleport.teleport(actor, map, x_coordinate, y_coordinate);
    } catch (Exception e) {
            return actor + " cannot be teleported because " + e.getMessage();
        }
    }

    /**
     *
     * @param actor The actor performing the action.
     * @return a string describing what the action will do
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " " + "teleports to " + "(" + x_coordinate + ", " + y_coordinate + ")";
    }
}
