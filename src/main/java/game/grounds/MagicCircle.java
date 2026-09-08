package game.grounds;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.TeleportAction;
import java.util.List;
import java.util.Random;
import game.interfaces.Teleportation;
import game.items.Flask;

import java.util.ArrayList;

public class MagicCircle extends Ground implements Teleportation {

    /**
     * The constructor for Magic Circle.
     */
    private static ArrayList<Integer> x_coordinates = new ArrayList<>();
    private static ArrayList<Integer> y_coordinates = new ArrayList<>();
    public MagicCircle() {
        super('◎', "Magic Circle");
    }

    /**
     * Teleports the actor a specific magic circle and randomly puts on a flask on the ground.
     * @param actor the Actor to be teleported
     * @param map the GameMap where the teleportation occurs
     * @param x_coordinate the x-coordinate of the magic circle
     * @param y_coordinate the y-coordinate of the magic circle
     * @return a message indicating the successful teleportation
     */
    public String teleport(Actor actor, GameMap map, int x_coordinate, int y_coordinate){
        Location previousLocation = map.locationOf(actor);
        //This is to prevent from an exception occurring when you try to move an actor to the
        //same location it is already in.
        if (!map.at(x_coordinate, y_coordinate).equals(previousLocation)){
            map.moveActor(actor, map.at(x_coordinate, y_coordinate));
        }
        Random random = new Random();
        List<Exit> exits = new ArrayList<>();
        for (Exit exit : map.at(x_coordinate, y_coordinate).getExits()) {
            Location neighbour = exit.getDestination();
            if (neighbour.canActorEnter(actor)) {
                exits.add(exit);
            }
        }
        Location flask_placement = exits.get(random.nextInt(exits.size()-1)).getDestination();
        flask_placement.addItem(new Flask());
        if (map.at(x_coordinate, y_coordinate).equals(previousLocation)){
            return "You got teleported to ... the same circle you entered from";
        }
        return "You teleported to " + "(" + x_coordinate + ", " + y_coordinate + ")";
    }

    /**
     * Used at initialization to find the coordinates of the magic circle
     * @param map
     */
    public void findCoordinates(GameMap map){
        for (int i = 0; i < map.getXRange().max() + 1; i++) {
            for (int j = 0; j < map.getYRange().max() + 1; j++) {
                if (map.at(i, j).getGround().getDisplayChar() == '◎') {
                    x_coordinates.add(i);
                    y_coordinates.add(j);
                }
            }
        }
    }

    /**
     * Gives the teleport action for a specific magic circle
     * @param actor the Actor acting
     * @param location the current Location
     * @param direction the direction of the Ground from the Actor
     * @return the list of allowable actions
     */
    @Override
    public ActionList allowableActions(Actor actor, Location location, String direction) {
        ActionList actions = new ActionList();
        if (direction.isEmpty() && !x_coordinates.isEmpty()) {
            Random random = new Random();
            int rand_coordinate = random.nextInt(x_coordinates.size());
            int x_coordinate = x_coordinates.get(rand_coordinate);
            int y_coordinate = y_coordinates.get(rand_coordinate);
            actions.add(new TeleportAction(this, x_coordinate, y_coordinate));
        }

        return actions;
    }
}
