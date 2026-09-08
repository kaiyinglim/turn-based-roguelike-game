package game.items;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.actions.BuyAction;
import game.enums.ItemStatistics;
import game.enums.WorkerAbility;
import game.interfaces.Purchasable;
import game.loaders.ContractedWorker;

import java.util.List;

/**
 * A class representing a box that can sterilise anything consumable, items and grounds.
 * Only one sterilisation box in the ship.
 *
 * @author Bryan Chan Zheng Lam
 */

public class SterilisationBox extends Item implements Purchasable {
    private static final int PRICE = 750;
    /**
     * The constructor of SterilisationBox class
     */
    public SterilisationBox() {
        super("Sterilisation Box", '▣');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(7));
        this.enableAbility(WorkerAbility.STERILISE);
        this.makePortable();
    }

    /**
     * The toString() method of the item class
     * @return name of the item class
     */
    @Override
    public String toString() {
        return super.toString();
    }
    /**
     * Returns the purchase price of the Sterilisation Box.
     *
     * @return price in credits (750)
     */
    @Override
    public int getPrice() {
        return PRICE;
    }
    /**
     * Applies effects when the Sterilisation Box is purchased.
     * The box emits radiation that causes one random item in the
     * buyer's inventory to be permanently erased. If the inventory
     * is empty, only the purchase message is returned.
     *
     * @param actor the actor purchasing the item
     * @return a description of the purchase outcome and side effects
     */
    @Override
    public String onPurchase(Actor actor) {

        ContractedWorker worker = (ContractedWorker) actor;

        //Remove one random item from inventory
        List<Item> items = worker.getInventory().getItems();

        if (!items.isEmpty()) {

            int randomIndex = (int) (Math.random() * items.size());

            Item removedItem = items.get(randomIndex);

            worker.getInventory().remove(removedItem);

            return worker + " purchased a Sterilisation Box for "
                    + PRICE + " credits.\n"
                    + removedItem + " was erased by radiation.";
        }

        return worker + " purchased a Sterilisation Box for "
                + PRICE + " credits.";
    }
    /**
     * Creates a buy action for this Sterilisation Box.
     *
     * @param worker the worker purchasing the item
     * @return a BuyAction instance for this item
     */
    public Action getBuyAction(ContractedWorker worker) {
        return new BuyAction(worker, this, this);
    }
}
