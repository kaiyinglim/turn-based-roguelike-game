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
import game.actions.BuyAction;
import game.actions.CutAction;
import game.enums.ItemStatistics;
import game.enums.WorkerAbility;
import game.interfaces.Cuttable;
import game.interfaces.Purchasable;
import game.loaders.ContractedWorker;
import game.statuses.Burning;

/**
 * A purchasable cutting tool that allows workers to cut cuttable
 * objects such as grounds and items. Purchasing it causes damage
 * and inflicts a burning effect on the worker.
 * @author Heesu Kim
 */
public class PlasmaCutter extends Item implements Purchasable {

    /**
     * Price required to purchase the Plasma Cutter.
     */
    private static final int PRICE = 50;

    /**
     * Weight of the Plasma Cutter.
     */
    private static final int WEIGHT = 7;

    /**
     * Damage taken when purchasing the Plasma Cutter.
     */
    private static final int PURCHASE_DAMAGE = 5;

    /**
     * Number of turns the burning effect lasts.
     */
    private static final int BURN_TURNS = 5;

    /**
     * Damage dealt by the burning effect each turn.
     */
    private static final int BURN_DAMAGE = 1;

    /**
     * Constructor of the PlasmaCutter class.
     */
    public PlasmaCutter() {
        super("Plasma Cutter", '>');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.makePortable();
    }

    /**
     * Returns the actions available when the Plasma Cutter is carried.
     * Allows workers to cut nearby cuttable grounds and cuttable items
     * in their inventory.
     *
     * @param owner the actor carrying the Plasma Cutter
     * @param map the map containing the actor
     * @return a list of allowable cutting actions
     */
    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        ActionList actions = new ActionList();

        // if not a worker holding PlasmaCutter
        if (!owner.hasAbility(WorkerAbility.IS_WORKER)) {
            return actions;
        }

        ContractedWorker worker = (ContractedWorker) owner;
        Location here = map.locationOf(owner);

        // check AluminiumDoor and Vent in surroundings of the worker
        for (Exit exit : here.getExits()) {
            Location destination = exit.getDestination();
            Ground ground = destination.getGround();

            if (ground.hasStatistic(ItemStatistics.CUTTABLE)) {
                actions.add(new CutAction(worker, ((Cuttable) ground), destination));
            }
        }

        // check AlienCubes in the worker's inventory
        for (Item item : owner.getInventory().getItems()) {
            if (item.hasStatistic(ItemStatistics.CUTTABLE)) {
                actions.add(new CutAction(worker, ((Cuttable) item), here));
            }
        }

        return actions;
    }

    /**
     * Returns the purchase price of the Plasma Cutter.
     *
     * @return the purchase price
     */
    @Override
    public int getPrice() {
        return PRICE;
    }

    /**
     * Applies the effects of purchasing the Plasma Cutter.
     * The worker takes damage and gains the Burning status effect.
     *
     * @param actor the actor purchasing the Plasma Cutter
     * @return a description of the purchase outcome
     */
    @Override
    public String onPurchase(Actor actor) {
        ContractedWorker worker = (ContractedWorker) actor;

        if (worker.getCredits() < getPrice()) {
            return worker + " tried to purchase a Plasma Cutter without enough credits";
        } else {
            actor.hurt(PURCHASE_DAMAGE);
            actor.addStatus(new Burning(BURN_TURNS, BURN_DAMAGE));

            return actor + " is scorched by the searing Plasma Cutter and takes "
                    + PURCHASE_DAMAGE + " damage, then burns for "
                    + BURN_TURNS + " turns.";
        }
    }

    /**
     * Returns the buy action associated with the Plasma Cutter.
     *
     * @param worker the worker purchasing the Plasma Cutter
     * @return a BuyAction for the Plasma Cutter
     */
    @Override
    public Action getBuyAction(ContractedWorker worker) {
        return new BuyAction(worker, new PlasmaCutter(), this);
    }
}