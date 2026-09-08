package game.deployables;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.DropAction;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.actions.BuyAction;
import game.actions.DeployAction;
import game.enums.ItemStatistics;
import game.enums.WorkerAbility;
import game.grounds.DestroyedTile;
import game.grounds.ElectricField;
import game.interfaces.Deployable;
import game.interfaces.Purchasable;
import game.loaders.ContractedWorker;
import game.statuses.EMPShock;

/**
 * Deployable Mine item.
 *
 * This item represents a purchasable Mine unit that can be carried
 * in the inventory and deployed onto the map. Once deployed, it remains
 * dormant until triggered by an entity entering its activation area.
 *
 * When triggered, the Mine applies environmental and status effects
 * by creating ElectricField hazards, converting terrain into a
 * DestroyedTile, and applying EMP-based disruption effects to nearby
 * entities. It functions as an area-denial deployable within the
 * autonomous tactical support system.
 *
 * @author Suzan Xuan Loh Palmer
 * @version 25.0.3
 */
public class Mine extends Item implements Deployable, Purchasable {

    private static final int PRICE = 25;
    private static final int WEIGHT = 5;

    private boolean armed = false;
    private Actor owner;
    private boolean ownerLeft = false;
    /**
     * Constructs a Mine item.
     *
     * Initialises item statistics such as weight and deployable status,
     * and marks the item as portable so it can be stored in inventory.
     */
    public Mine() {
        super("Mine", 'Q');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.addNewStatistic(ItemStatistics.DEPLOYABLE, new BaseStatistic(1));
        this.makePortable();
    }
    /**
     * Arms the mine so it becomes active and capable of triggering.
     */
    public void arm() {
        this.armed = true;
    }
    /**
     * Provides the deploy action for this mine.
     *
     * @param worker the worker deploying the mine
     * @return a DeployAction for this Mine item
     */
    @Override
    public Action getDeployAction(ContractedWorker worker) {
        return new DeployAction(this);
    }
    /**
     * Deploys the Mine onto the map.
     *
     * The item is removed from the inventory, placed on the current tile,
     * and armed immediately. It then monitors the tile for triggering events.
     *
     * @param worker the worker deploying the mine
     * @param map the game map
     * @return a message describing the deployment result
     */
    @Override
    public String deploy(ContractedWorker worker, GameMap map) {
        if (!worker.getInventory().remove(this)) {
            return worker + " fails to deploy a Mine.";
        }

        Location location = map.locationOf(worker);
        this.owner = worker;
        location.addItem(this);
        this.arm();

        return worker + " deploys a Mine.";
    }
    /**
     * Updates the Mine each turn.
     *
     * The Mine checks for valid triggering conditions. If an enemy entity
     * enters the tile (excluding the owner before leaving), the Mine is
     * detonated, applying environmental and terrain effects.
     *
     * @param location the current location of the Mine
     */
    @Override
    public void tick(Location location) {

        if (!armed) return;
        if (!location.containsAnActor()) {
            ownerLeft = true;
            return;
        }

        Actor actor = location.getActor();
        if (!ownerLeft && actor == owner) {
            return;
        }

        trigger(actor, location);
    }
    /**
     * Triggers the Mine explosion effect.
     *
     * Applies ElectricField to surrounding tiles, converts the current tile
     * into a DestroyedTile, and removes the Mine from the map.
     *
     * @param actor the actor triggering the explosion
     * @param location the location of the explosion
     * @return a message describing the trigger event
     */
    public String trigger(Actor actor, Location location) {
        if (!armed) {
            return "Mine is not armed.";
        }

        System.out.println(actor + " triggered a Mine explosion!");

        for (Exit exit : location.getExits()) {
            exit.getDestination().setGround(new ElectricField());
        }

        location.setGround(new DestroyedTile(3));
        location.removeItem(this);

        return actor + " triggered a Mine explosion!";
    }
    /**
     * Returns the purchase price of the Mine item.
     *
     * @return price in credits
     */
    @Override
    public int getPrice() {
        return PRICE;
    }
    /**
     * Handles the result of purchasing this Mine.
     *
     * @param actor the actor purchasing the item
     * @return confirmation message of purchase
     */
    @Override
    public String onPurchase(Actor actor) {
        return actor + " purchased a Mine for "
                + PRICE + " credits.";
    }
    /**
     * Returns the buy action for this Mine item.
     *
     * @param worker the worker purchasing the item
     * @return a BuyAction for this Mine item
     */
    @Override
    public Action getBuyAction(ContractedWorker worker) {
        return new BuyAction(worker, new Mine(), this);
    }
}