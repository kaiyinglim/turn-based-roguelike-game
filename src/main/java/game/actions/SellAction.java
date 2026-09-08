package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;

import game.loaders.ContractedWorker;
import game.interfaces.Sellable;

/**
 * Action for selling a sellable item to the Supercomputer.
 *
 * This action handles the complete selling transaction process,
 * including rewarding the worker with credits, removing the
 * sold item from the inventory, and applying any special
 * selling effects defined by the sellable item.
 *
 * @author Suzan Xuan Loh Palmer
 * @version 25.0.3
 */
public class SellAction extends Action {
    private final ContractedWorker worker;
    private final Item item;
    private final Sellable sellable;
    private final Location supercomputerLocation;

    /**
     * Constructs a sell action.
     *
     * @param worker the worker selling the item
     * @param item the item being sold
     * @param sellable the sellable behaviour of the item
     */
    public SellAction(ContractedWorker worker, Item item, Sellable sellable) {
        this(worker, item, sellable, null);
    }

    /**
     * Constructs a sell action made through a Supercomputer.
     *
     * @param worker the worker selling the item
     * @param item the item being sold
     * @param sellable the sellable behaviour of the item
     * @param supercomputerLocation location of the Supercomputer where this sale occurs
     */
    public SellAction(ContractedWorker worker, Item item, Sellable sellable, Location supercomputerLocation) {
        this.worker = worker;
        this.item = item;
        this.sellable = sellable;
        this.supercomputerLocation = supercomputerLocation;
    }

    /**
     * Executes the selling transaction.
     *
     * The worker receives credits based on the item's sell price,
     * the item is removed from the worker's inventory, and any
     * special selling effects are applied immediately.
     *
     * @param actor the actor performing the action
     * @param map the map the actor is on
     * @return a description of the transaction result
     */
    @Override
    public String execute(Actor actor, GameMap map) {

        int creditsEarned = sellable.getSellPrice();

        worker.addCredits(creditsEarned);
        worker.getInventory().remove(item);
        String effectResult = sellable.onSell(worker, map, supercomputerLocation);

        return worker + " sold " + item
                + " for " + creditsEarned + " worker credits.\n"
                + effectResult;
    }

    /**
     * Returns the menu description shown to the player.
     *
     * @param actor the actor viewing the menu
     * @return menu description for this action
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " sells " + item;
    }
}
