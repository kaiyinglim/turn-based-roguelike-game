package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;

/**
 * Picks up an item from a known ground tile. 
 * Use this when the item is not on {@link GameMap#locationOf(Actor)} 
 */
public class PickUpNearbyItemAction extends Action {

    private final Item item;
    private final Location itemLocation;

    /**
     * @param item          the item to pick up
     * @param itemLocation  the tile where the item currently sits
     */
    public PickUpNearbyItemAction(Item item, Location itemLocation) {
        this.item = item;
        this.itemLocation = itemLocation;
    }

    /**
     * Removes the item from {@link #itemLocation} and adds it to the actor's inventory if possible.
     *
     * @param actor the actor performing the pickup
     * @param map   the game map
     * @return a short outcome message for the UI log
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        if (!itemLocation.getItems().contains(item)) {
            return String.format("%s fails to pick up %s", actor, item);
        }
        if (actor.getInventory().add(item)) {
            itemLocation.removeItem(item);
            return menuDescription(actor);
        }
        return String.format("%s fails to pick up %s", actor, item);
    }

    /**
     * @param actor the actor performing the pickup
     * @return a description of the action suitable for the menu
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " picks up the " + item;
    }
}
