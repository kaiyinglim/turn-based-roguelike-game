package game.interfaces;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import game.loaders.ContractedWorker;
/**
 * Represents an item or entity that can be purchased from the Supercomputer.
 *
 * Implementing classes define their price, purchase effects, and the
 * action used to perform the purchase transaction.
 *
 * @author Suzan Xuan Loh Palmer
 * @version 25.0.3
 */
public interface Purchasable {
    /**
     * Returns the purchase cost of this item.
     *
     * @return item price in credits
     */
    int getPrice();

    /**
     * Applies effects after purchase.
     *
     * This may include healing, damage, status effects, or other
     * game-specific consequences triggered immediately upon buying.
     *
     * @param actor buyer
     * @return result description of the purchase effect
     */
    String onPurchase(Actor actor);
    /**
     * Returns the buy action associated with this purchasable item.
     *
     * @param worker the worker performing the purchase
     * @return buy action for this item
     */
    Action getBuyAction(ContractedWorker worker);
}
