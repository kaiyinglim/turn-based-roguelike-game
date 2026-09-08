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
 * Deployable Combat Drone item.
 *
 * This item represents a purchasable and deployable Combat Drone unit.
 * It can be carried in the inventory, deployed onto the game map, and
 * later converted into an autonomous Combat Drone entity after deployment.
 *
 * The item tracks its owner and delays activation until the owner has
 * moved away from the deployment location, ensuring safe deployment
 * without immediate obstruction. Once activated, it spawns a Combat Drone
 * on the tile and removes itself from the environment.
 *
 * This class integrates both purchasing and deployment mechanics within
 * the autonomous tactical system.
 *
 * @author Suzan Xuan Loh Palmer
 * @version 25.0.3
 */
public class CombatDroneItem extends Item implements Deployable, Purchasable {
    private static final int PRICE = 50;
    private static final int WEIGHT = 5;
    private Actor owner;
    private boolean ownerLeft = false;
    /**
     * Constructs a Combat Drone item.
     *
     * Initialises item statistics such as weight and deployable status,
     * and marks the item as portable so it can be stored in inventory.
     */
    public CombatDroneItem() {
        super("Combat Drone", 'C');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.addNewStatistic(ItemStatistics.DEPLOYABLE, new BaseStatistic(1));
        this.makePortable();
    }
    /**
     * Returns the purchase price of the Combat Drone item.
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
        return actor + " purchased Combat Drone";
    }
    /**
     * Provides the deploy action for this item.
     *
     * @param worker the worker deploying the item
     * @return a DeployAction for this Combat Drone item
     */
    @Override
    public Action getDeployAction(ContractedWorker worker) {
        return new DeployAction(this);
    }
    /**
     * Deploys the Combat Drone item onto the map.
     *
     * The item is removed from the inventory and placed on the current tile.
     * Deployment does not immediately spawn the drone; activation occurs
     * after the owner leaves the tile.
     *
     * @param worker the worker deploying the item
     * @param map the game map
     * @return a message describing the deployment result
     */
    @Override
    public String deploy(ContractedWorker worker, GameMap map) {

        if (!worker.getInventory().remove(this)) {
            return worker + " fails to deploy Combat Drone.";
        }

        Location location = map.locationOf(worker);

        this.owner = worker;
        location.addItem(this);

        return worker + " deploys Combat Drone.";
    }
    /**
     * Returns the buy action for this Combat Drone item.
     *
     * This creates a BuyAction that handles purchasing the item,
     * including credit deduction and adding the item to the worker's inventory.
     *
     * @param worker the worker purchasing the item
     * @return a BuyAction for this Combat Drone item
     */
    @Override
    public Action getBuyAction(ContractedWorker worker) {
        return new BuyAction(worker, new CombatDroneItem(), this);
    }
    /**
     * Updates the deployment state each turn.
     *
     * The item waits until the owner leaves the tile before activating.
     * Once the tile is free of the owner, the Combat Drone is spawned
     * and the item is removed from the map.
     *
     * @param location the location of the item on the map
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

        CombatDrone drone = new CombatDrone();

        try {
            location.map().addActor(drone, location);
            location.removeItem(this);
        }
        catch (Exception e) {
            // keep waiting until tile becomes available
        }
    }
}
