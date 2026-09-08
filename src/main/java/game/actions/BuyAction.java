package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import game.interfaces.Purchasable;
import game.loaders.ContractedWorker;

/**
 * Action for purchasing a purchasable item from the Supercomputer.
 *
 * This action handles the complete purchase transaction process,
 * including validating credits, deducting payment, adding the
 * purchased item into the worker's inventory, and applying any
 * special purchase effects defined by the purchasable item.
 *
 * @author Suzan Xuan Loh Palmer
 * @version 25.0.3
 */
public class BuyAction extends Action {

    private final ContractedWorker worker;
    private final Item item;
    private final Purchasable purchasable;

    /**
     * Constructs a buy action.
     *
     * @param worker the worker buying the item
     * @param item the item being purchased
     * @param purchasable the purchasable behaviour of the item
     */
    public BuyAction(ContractedWorker worker, Item item, Purchasable purchasable) {
        this.worker = worker;
        this.item = item;
        this.purchasable = purchasable;
    }

    /**
     * Executes the purchase transaction.
     *
     * If the worker has insufficient credits, the purchasable item
     * handles the failed purchase outcome. Otherwise, the worker's
     * credits are deducted, the item is added into the inventory,
     * and any special purchase effects are applied immediately.
     *
     * @param actor the actor performing the action
     * @param map the map the actor is on
     * @return a description of the transaction result
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        worker.getInventory().add(item);
        int price = purchasable.getPrice();

        //Not enough credits
        if (worker.getCredits() < price) {
            return purchasable.onPurchase(worker);
        }

        //Deduct credits
        worker.deductCredits(price);

        //Add purchased item into inventory
        worker.getInventory().add(item);

        // Apply purchase effects
        String effectResult = purchasable.onPurchase(worker);

        return worker + " purchased " + item + " for " + price + " credits.\n" + effectResult;
    }

    /**
     * Returns the menu description shown to the player.
     *
     * @param actor the actor viewing the menu
     * @return menu description for this action
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " buys " + item;
    }
}
