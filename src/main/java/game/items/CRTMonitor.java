package game.items;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.actions.SellAction;
import game.enums.ItemStatistics;
import game.enums.WorkerAbility;
import game.grounds.Fire;
import game.interfaces.Sellable;
import game.loaders.ContractedWorker;

/**
 * A class that represents a massive, incredibly heavy piece of archaic junk.
 */
public class CRTMonitor extends Item implements Sellable {
    private static final int SELL_PRICE = 25;
    private static final int HEAL_AMOUNT = 5;
    private static final int SHORT_DAMAGE = 2;
    /**
     * The constructor of CRTMonitor class
     */
    public CRTMonitor() {
        super("CRT Monitor",    '◙');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(30));
        this.addNewStatistic(ItemStatistics.SELLABLE, new BaseStatistic(1));
        this.makePortable();
    }
    /**
     * Returns the selling price of the CRT Monitor.
     *
     * @return fixed sell price (25 credits)
     */
    @Override
    public int getSellPrice() {
        return SELL_PRICE;
    }
    /**
     * Applies effects when the CRT Monitor is sold.
     * Selling it heals the worker due to relief from its weight,
     * but there is a 20% chance of electrical failure that causes damage
     * and spreads fire to surrounding tiles.
     *
     * @param worker the worker selling the CRT Monitor
     * @param map the game map where the transaction occurs
     * @return a description of the selling outcome and any side effects
     */
    @Override
    public String onSell(ContractedWorker worker, GameMap map, Location supercomputerLocation) {

        String result = worker + " sold a CRT Monitor for " + SELL_PRICE + " credits.\n";

        // immediate heal effect
        worker.heal(HEAL_AMOUNT);

        result += worker + " feels relieved and heals " + HEAL_AMOUNT + " HP.\n";

        // 20% failure chance
        if (Math.random() < 0.2) {

            worker.hurt(SHORT_DAMAGE);

            result += worker + " is shocked by the CRT short-circuit and takes "
                    + SHORT_DAMAGE + " damage!\n";

            Location location = map.locationOf(worker);

            for (Exit exit : location.getExits()) {
                Location adj = exit.getDestination();
                Ground prev = adj.getGround();
                int fireDuration = 5;
                adj.setGround(new Fire(prev, fireDuration));
            }

            result += "Fire spreads to surrounding tiles!";
        }

        return result;
    }
    /**
     * Creates a sell action for this CRT Monitor.
     *
     * @param worker the worker performing the sale
     * @return a SellAction instance for this item
     */
    @Override
    public Action getSellAction(ContractedWorker worker, Location supercomputerLocation) {
        return new SellAction(worker, this, this, supercomputerLocation);
    }
    /**
     * Returns allowable actions when interacting with this item.
     * Workers are able to sell the CRT Monitor via the Supercomputer.
     *
     * @param owner the actor who owns this item
     * @param map the game map where interaction occurs
     * @return list of possible actions (sell action if applicable)
     */
    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        return new ActionList();
    }
}
