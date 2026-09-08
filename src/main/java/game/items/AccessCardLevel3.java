package game.items;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import game.actions.BuyAction;
import game.loaders.ContractedWorker;
/**
 * Access Card Level 3 is the highest-tier security clearance card.
 * It allows the holder to unlock all doors in the facility.
 *
 * Purchasing this card is unpredictable due to predatory pricing algorithms:
 * there is a 50% chance that the Supercomputer will impose a hidden fee
 * of 50 additional credits after purchase.
 *
 * @author Suzan Xuan Loh Palmer
 * @version 25.0.3
 */
public class AccessCardLevel3 extends AccessCard {
    /**
     * Constructs an Access Card Level 3.
     */
    public AccessCardLevel3() {
        super("Access Card Level 3", '◐', 3);
    }
    /**
     * Returns the clearance level of this card.
     *
     * @return clearance level (3)
     */
    @Override
    public int getClearanceLevel() {
        return 3;
    }
    /**
     * Returns the purchase price of this card.
     *
     * @return price in credits (200)
     */
    @Override
    public int getPrice() {
        return 200;
    }
    /**
     * Applies purchase effects when buying this card.
     * There is a 50% chance of a hidden 50-credit surcharge.
     *
     * @param actor the buyer
     * @return description of the purchase outcome
     */
    @Override
    public String onPurchase(Actor actor) {

        ContractedWorker worker = (ContractedWorker) actor;

        String result = actor + " purchased Access Card Level 3.";

        if (Math.random() < 0.5) {
            worker.deductCredits(50);
            result += "\nHidden fee charged: 50 credits.";
        }

        return result;
    }
    /**
     * Creates a buy action for this item.
     *
     * @param worker the worker purchasing the card
     * @return BuyAction instance for this item
     */
    @Override
    public Action getBuyAction(ContractedWorker worker) {
        return new BuyAction(worker, this, this);
    }
}
