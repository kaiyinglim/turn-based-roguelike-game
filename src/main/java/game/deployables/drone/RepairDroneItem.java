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
 * Deployable Repair Drone item.
 *
 * This item represents a purchasable Repair Drone unit that can be carried
 * in the inventory and deployed onto the map. After deployment, the item
 * remains on the tile until the owner leaves, at which point it activates
 * and spawns an autonomous Repair Drone entity.
 *
 * The Repair Drone provides sustained support by healing allied units
 * within range, forming part of the autonomous tactical support system.
 *
 * @author Suzan Xuan Loh Palmer
 * @version 25.0.3
 */
public class RepairDroneItem extends Item implements Deployable, Purchasable {
    private static final int PRICE = 50;
    private static final int WEIGHT = 5;
    private Actor owner;
    private boolean ownerLeft = false;
    /**
     * Constructs a Repair Drone item.
     *
     * Initialises item statistics such as weight and deployable status,
     * and marks the item as portable so it can be stored in inventory.
     */
    public RepairDroneItem() {
        super("Repair Drone", 'R');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.addNewStatistic(ItemStatistics.DEPLOYABLE, new BaseStatistic(1));
        this.makePortable();
    }
    /**
     * Returns the purchase price of the Repair Drone item.
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
        return actor + " purchased Repair Drone";
    }
    /**
     * Provides the deploy action for this item.
     *
     * @param worker the worker deploying the item
     * @return a DeployAction for this Repair Drone item
     */
    @Override
    public Action getDeployAction(ContractedWorker worker) {
        return new DeployAction(this);
    }
    /**
     * Deploys the Repair Drone item onto the map.
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
            return worker + " fails to deploy Repair Drone.";
        }

        Location location = map.locationOf(worker);

        this.owner = worker;
        location.addItem(this);

        return worker + " deploys Repair Drone.";
    }
    /**
     * Returns the buy action for this Repair Drone item.
     *
     * This creates a BuyAction that handles purchasing the item,
     * including credit deduction and adding the item to the worker's inventory.
     *
     * @param worker the worker purchasing the item
     * @return a BuyAction for this Repair Drone item
     */
    @Override
    public Action getBuyAction(ContractedWorker worker) {
        return new BuyAction(worker, new RepairDroneItem(), this);
    }
    /**
     * Updates the deployment state each turn.
     *
     * Once the owner leaves the tile, the Repair Drone is spawned and the
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

        RepairDrone drone = new RepairDrone();

        try {
            location.map().addActor(drone, location);
            location.removeItem(this);
        }
        catch (Exception e) {
            // keep waiting until tile becomes available
        }
    }
}
