package game.interfaces;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.loaders.ContractedWorker;
/**
 * Represents an item or object that can be sold to the Supercomputer.
 *
 * Implementing classes define the selling price, side effects triggered
 * upon selling, and the action used to perform the sale.
 *
 * @author Suzan Xuan Loh Palmer
 * @version 25.0.3
 */
public interface Sellable {
    /**
     * Returns the selling price of this item
     *
     * @return credits earned from selling
     */
    int getSellPrice();

    /**
     * Applies side effects when sold.
     *
     * @param worker seller
     * @param map the game map where the sale occurs
     * @return result description of the selling effect
     */
    String onSell(ContractedWorker worker, GameMap map, Location supercomputerLocation);

    /**
     * Returns the sell action for this item.
     *
     * @param worker seller
     * @return sell action
     */
    Action getSellAction(ContractedWorker worker, Location supercomputerLocation);
}
