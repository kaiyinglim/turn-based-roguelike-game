package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * SnatchAction class is a class that represents a Scrap Snatcher hoarding an item from the ground.
 * @author FIT2099 Bryan Chan Zheng Lam
 */
public class SnatchAction extends Action {

    /**
     * The item that is to be snatched
     */
    private final Item item;

    /**
     * The constructor of the SnatchAction class
     * @param item the item on the ground to be snatched
     */
    public SnatchAction(Item item) {
        this.item = item;
    }

    /**
     * Executes the snatch action, removing the item from the map and adding it to the actor's inventory.
     *
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return a string of the description of the actor snatching the item.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        map.locationOf(actor).removeItem(item);
        actor.getInventory().add(item);
        return actor + " snatches the " + item + " from the ground!";
    }

    /**
     * The description for the actor when it snatches the specific item
     * @param actor The actor performing the action.
     * @return the output string when the actor snatches the item.
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " snatches the " + item;
    }
}
