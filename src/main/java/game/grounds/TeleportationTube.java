package game.grounds;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.systems.RandomGround;
import game.actions.TeleportAction;
import game.interfaces.Teleportation;
import edu.monash.fit2099.engine.positions.Ground;
import java.util.Random;

/**
 * Get inside the tube and come to a whole new level or just a different place terminator style
 */
public class TeleportationTube extends Ground implements Teleportation {

    /**
     * The fire duration
     */
    private static final int FIRE_DURATION = 2;
    // (12, 2) in ship on deprecated moon 99
    // (22, 6) in building on deprecated moon 99
    // (6, 2) on 20-overflow
    /**
     * The specific coordinates of the teleportation tubes
     */
    private Integer[] x_coordinates = {12, 22, 6};
    private Integer[] y_coordinates = {2, 6, 2};
    /**
     * The constructor for teleportation tube
     */
    public TeleportationTube() {
        super('Φ', "Teleportation Tube");
    }

    @Override
    public String teleport(Actor actor, GameMap map, int x_coordinate, int y_coordinate) {
        Random random = new Random();
        boolean malfunction = false;
        //this is for a malfunction
        if (random.nextInt(2) == 0){
            malfunction = true;
            RandomGround coordinates = new RandomGround();
            coordinates.RandomAccessibleGround(map);
        }
        //This is to prevent from an exception occurring when you try to move an actor to the
        //same location it is already in.
        if (!map.at(x_coordinate, y_coordinate).equals(map.locationOf(actor))){
            map.moveActor(actor, map.at(x_coordinate, y_coordinate));
        }
        Location location = map.locationOf(actor);
        for (Exit exit : location.getExits()) {
            Location neighbour = exit.getDestination();
            if (neighbour.equals(location) || neighbour.canActorEnter(actor)) {
                Ground prevGround = neighbour.getGround();
                neighbour.setGround(new Fire(prevGround, FIRE_DURATION));
            }
        }
        if (x_coordinate == 2 && y_coordinate == 6){
            return "The teleport tube is partially under reconstruction, doesn't teleport to 20-overflow";
        } else if (malfunction){
            return "The teleport tube malfunctioned, you were teleported to (" + x_coordinate + ", " + y_coordinate + ")";
        }
        return "You teleported to " + "(" + x_coordinate + ", " + y_coordinate + ")";
    }

    public ActionList allowableActions(Actor actor, Location location, String direction) {
        ActionList actions = new ActionList();
        if (direction.isEmpty()) {
            for (int i = 0; i < x_coordinates.length; i++) {
                actions.add(new TeleportAction(this, this.x_coordinates[i], this.y_coordinates[i]));
            }
        }
        return actions;
    }
}
