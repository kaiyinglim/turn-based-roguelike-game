package game.items;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.actions.SellAction;
import game.enums.ItemStatistics;
import game.enums.WorkerAbility;
import game.interfaces.Consumable;
import game.interfaces.Sellable;
import game.loaders.ContractedWorker;

import javax.xml.transform.stax.StAXResult;

/**
 * A class representing a piece of ancient technology.
 * It is extremely lightweight.
 */
public class FloppyDisk extends Item implements Sellable {
    private static final int SELL_PRICE = 1;
    private static final int GLITCH_FINE = 50;
    /**
     * The constructor of FloppyDisk class
     */
    public FloppyDisk() {
        super("Floppy Disk", '⊟');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(1));
        this.addNewStatistic(ItemStatistics.SELLABLE, new BaseStatistic(1));
        this.makePortable();
    }
    /**
     * Returns the selling price of the Floppy Disk.
     *
     * @return fixed sell price (1 credit)
     */
    @Override
    public int getSellPrice() {
        return SELL_PRICE;
    }
    /**
     * Applies effects when the Floppy Disk is sold.
     * There is a 50% chance the Supercomputer glitches and deducts
     * additional credits from the worker after the sale.
     *
     * @param worker the worker selling the Floppy Disk
     * @param map the game map where the transaction occurs
     * @return description of the selling outcome
     */
    @Override
    public String onSell(ContractedWorker worker, GameMap map, Location supercomputerLocation) {
        if (Math.random() < 0.5) {
            worker.deductCredits(GLITCH_FINE);

            return worker + " sold a Floppy Disk for " + SELL_PRICE + " credit, but the Supercomputer glitched and deducted " + GLITCH_FINE + " credits.";
        }
        return worker + " sold a Floppy Disk for " + SELL_PRICE + " credit.";
    }
    /**
     * Creates a sell action for this Floppy Disk.
     *
     * @param worker the worker performing the sale
     * @return a SellAction instance for this item
     */
    @Override
    public Action getSellAction(ContractedWorker worker, Location supercomputerLocation) {
        return new SellAction(worker, this, this, supercomputerLocation);
    }
    /**
     * Returns allowable actions for this Floppy Disk.
     * Workers are able to sell it via the Supercomputer.
     *
     * @param owner the actor who owns the item
     * @param map the game map where interaction occurs
     * @return list of possible actions (sell action if applicable)
     */
    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        return new ActionList();
    }
}
