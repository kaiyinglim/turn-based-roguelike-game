package game.items;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import game.actions.BuyAction;
import game.loaders.ContractedWorker;
/**
 * Access Card Level 2 is a medium-tier security clearance card.
 * It allows the holder to unlock higher-level doors than Level 1.
 *
 * Purchasing this card causes the Supercomputer to extract blood,
 * dealing 5 damage to the buyer immediately.
 *
 * @author Suzan Xuan Loh Palmer
 * @version 25.0.3
 */
public class AccessCardLevel2 extends AccessCard {
    /**
     * Constructs an Access Card Level 2.
     */
    public AccessCardLevel2() {
        super("Access Card Level 2", 'α', 2);
    }
    /**
     * Returns the clearance level of this card.
     *
     * @return clearance level (2)
     */
    @Override
    public int getClearanceLevel() {
        return 2;
    }
    /**
     * Returns the purchase price of this card.
     *
     * @return price in credits (100)
     */
    @Override
    public int getPrice() {
        return 100;
    }
    /**
     * Applies the side effects when this card is purchased.
     * The buyer takes 5 damage due to blood extraction.
     *
     * @param actor the buyer
     * @return description of the purchase effect
     */
    @Override
    public String onPurchase(Actor actor) {

        actor.hurt(5);

        return actor + " purchased Access Card Level 2 "
                + "and lost 5 HP from blood extraction.";
    }
    /**
     * Creates a buy action for this item.
     *
     * @param worker the worker purchasing the item
     * @return a BuyAction instance
     */
    @Override
    public Action getBuyAction(ContractedWorker worker) {
        return new BuyAction(worker, this, this);
    }
}
