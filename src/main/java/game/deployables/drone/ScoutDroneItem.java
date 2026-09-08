package game.deployables.drone;

import edu.monash.fit2099.engine.GameEngineException;
import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.actions.BuyAction;
import game.actions.DeployAction;
import game.enums.ItemStatistics;
import game.interfaces.Deployable;
import game.interfaces.Purchasable;
import game.loaders.ContractedWorker;

/**
 * Deployable Scout Drone item.
 *
 * The Scout Drone Item is a purchasable and deployable support unit that
 * allows workers to introduce reconnaissance capability onto the battlefield.
 * Once deployed and activated, it spawns a Scout Drone that marks hostile
 * entities with DetectionMarked, improving allied targeting efficiency.
 *
 * This item remains on the map until activation conditions are met, after
 * which it is replaced by an autonomous Scout Drone entity.
 *
 * @author Suzan Xuan Loh Palmer
 * @version 25.0.3
 */
public class ScoutDroneItem extends Item implements Deployable, Purchasable {
    private static final int PRICE = 50;
    private static final int WEIGHT = 5;
    private Actor owner;
    private boolean ownerLeft = false;
    /**
     * Constructs a Scout Drone item.
     *
     * Initialises item statistics such as weight and deployable status,
     * and marks the item as portable so it can be stored in inventory.
     */
    public ScoutDroneItem() {
        super("Scout Drone", 'S');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.addNewStatistic(ItemStatistics.DEPLOYABLE, new BaseStatistic(1));
        this.makePortable();
    }
    /**
     * Returns the purchase price of the Scout Drone item.
     *
     * @return the price in credits
     */
    @Override
    public int getPrice() {
        return PRICE;
    }
    /**
     * Handles the result of purchasing this item.
     *
     * @param actor the actor purchasing the item
     * @return confirmation message of purchase
     */
    @Override
    public String onPurchase(Actor actor) {
        return actor + " purchased Scout Drone";
    }
    /**
     * Provides the deploy action for this item.
     *
     * @param worker the worker deploying the item
     * @return a DeployAction for this Scout Drone item
     */
    @Override
    public Action getDeployAction(ContractedWorker worker) {
        return new DeployAction(this);
    }
    /**
     * Deploys the Scout Drone item onto the map.
     *
     * The item is removed from the inventory and placed on the current tile.
     * Activation is delayed until the owner leaves the tile.
     *
     * @param worker the worker deploying the item
     * @param map the game map
     * @return a message describing the deployment result
     */
    @Override
    public String deploy(ContractedWorker worker, GameMap map) {

        if (!worker.getInventory().remove(this)) {
            return worker + " fails to deploy Scout Drone.";
        }

        Location location = map.locationOf(worker);

        this.owner = worker;
        location.addItem(this);

        return worker + " deploys Scout Drone.";
    }
    /**
     * Returns the buy action for this Scout Drone item.
     *
     * This creates a BuyAction that handles purchasing the item,
     * including credit deduction and adding the item to the worker's inventory.
     *
     * @param worker the worker purchasing the item
     * @return a BuyAction for this Scout Drone item
     */
    @Override
    public Action getBuyAction(ContractedWorker worker) {
        return new BuyAction(worker, new ScoutDroneItem(), this);
    }
    /**
     * Updates the deployment state each turn.
     *
     * Once the owner leaves the tile, the Scout Drone is spawned and the
     * item is removed from the map. If the tile is not ready, the item
     * waits until deployment is possible.
     *
     * @param location the current location of the item
     */
    @Override
    public void tick(Location location) {

        if (!ownerLeft) {

            if (!location.containsAnActor()) {
                ownerLeft = true;
            }
            else if (location.getActor() != owner) {
                ownerLeft = true;
            }

            return;
        }

        ScoutDrone drone = new ScoutDrone();

        try {
            location.map().addActor(drone, location);
            location.removeItem(this);
        }
        catch (Exception e) {
            // keep waiting until tile becomes available
        }
    }
}
