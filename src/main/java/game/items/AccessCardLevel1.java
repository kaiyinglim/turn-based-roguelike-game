package game.items;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import game.actions.BuyAction;
import game.loaders.ContractedWorker;
/**
 * Represents a Level 1 Access Card used to unlock basic security doors
 * (e.g. Aluminium doors) within the facility.
 *
 * This is the most basic clearance level card and provides minimal access.
 *
 * @author Suzan Xuan Loh Palmer
 * @version 25.0.3
 */
public class AccessCardLevel1 extends AccessCard {
    /**
     * Constructs a Level 1 Access Card.
     */
    public AccessCardLevel1() {
        super("Access Card Level 1", '▤', 1);
    }
    /**
     * Returns the clearance level of this access card.
     *
     * @return clearance level 1
     */
    @Override
    public int getClearanceLevel() {
        return 1;
    }
    /**
     * Returns the purchase price of this access card.
     *
     * @return price in credits
     */
    @Override
    public int getPrice() {
        return 50;
    }
    /**
     * Applies effects when this item is purchased.
     *
     * @param actor the buyer
     * @return description of purchase result
     */
    @Override
    public String onPurchase(Actor actor) {
        return actor + " purchased Access Card Level 1.";
    }
    /**
     * Returns the buy action associated with this access card.
     *
     * @param worker the worker purchasing the item
     * @return buy action for this item
     */
    @Override
    public Action getBuyAction(ContractedWorker worker) {
        return new BuyAction(worker, this, this);
    }
}
