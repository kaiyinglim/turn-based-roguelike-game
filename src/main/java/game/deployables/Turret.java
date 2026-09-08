package game.deployables;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.actions.BuyAction;
import game.actions.DeployAction;
import game.enums.EnemyAbility;
import game.enums.ItemStatistics;
import game.enums.StatusAbility;
import game.grounds.ElectricField;
import game.interfaces.Deployable;
import game.interfaces.Purchasable;
import game.loaders.ContractedWorker;
import game.statuses.DetectionMarked;

/**
 * Deployable Turret item.
 *
 * This item represents a purchasable Turret unit that can be carried
 * in the inventory and deployed onto the map. Once deployed, it remains
 * stationary and automatically scans for hostile entities within range.
 *
 * The Turret provides autonomous defensive support by prioritising
 * DetectionMarked targets and generating ElectricField hazards around
 * hostile units. It functions as a control-based defensive structure
 * within the autonomous tactical support system.
 *
 * @author Suzan Xuan Loh Palmer
 * @version 25.0.3
 */
public class Turret extends Item implements Deployable, Purchasable {

    private static final int PRICE = 25;
    private static final int RANGE = 3;
    private static final int WEIGHT = 5;
    /**
     * Constructs a Turret item.
     *
     * Initialises item statistics such as weight and deployable status,
     * and marks the item as portable so it can be stored in inventory.
     */
    public Turret() {
        super("Turret", 'T');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.addNewStatistic(ItemStatistics.DEPLOYABLE, new BaseStatistic(1));
        this.makePortable();
    }
    /**
     * Provides the deploy action for this Turret.
     *
     * @param worker the worker deploying the turret
     * @return a DeployAction for this Turret item
     */
    @Override
    public Action getDeployAction(ContractedWorker worker) {
        return new DeployAction(this);
    }
    /**
     * Deploys the Turret onto the map.
     *
     * The item is removed from the inventory and placed on the current tile.
     * Once deployed, it continuously scans for hostile targets and generates
     * ElectricField hazards around detected targets.
     *
     * @param worker the worker deploying the turret
     * @param map the game map
     * @return a message describing the deployment result
     */
    @Override
    public String deploy(ContractedWorker worker, GameMap map) {
        if (!worker.getInventory().remove(this)) {
            return worker + " fails to deploy a Turret.";
        }

        Location location = map.locationOf(worker);
        location.addItem(this);
        //worker.getInventory().remove(this);

        return worker + " deploys a Turret.";
    }
    /**
     * Updates the Turret each turn.
     *
     * The Turret scans for the nearest valid hostile target within range,
     * prioritising DetectionMarked entities, and generates ElectricField
     * hazards around the target location to control battlefield movement.
     *
     * @param location the current location of the Turret
     */
    @Override
    public void tick(Location location) {

        Actor target = findTarget(location);

        if (target == null) return;

        Location targetLocation = location.map().locationOf(target);
        createElectricField(targetLocation);
    }

    /**
     * Finds the highest priority hostile target within range.
     *
     * @param turretLocation the turret's current location
     * @return the selected target actor, or null if none found
     */
    private Actor findTarget(Location turretLocation) {

        Actor nearestTarget = null;
        int nearestDistance = Integer.MAX_VALUE;

        GameMap map = turretLocation.map();

        for (int x : map.getXRange()) {
            for (int y : map.getYRange()) {

                Location current = map.at(x, y);

                if (!current.containsAnActor()) continue;

                Actor actor = current.getActor();
                if (!actor.hasAbility(EnemyAbility.IS_ENEMY)) {
                    continue;
                }

                int distance =
                        Math.abs(x - turretLocation.x()) +
                                Math.abs(y - turretLocation.y());

                if (distance == 0 || distance > RANGE) continue;

                // PRIORITY: DetectionMarked
                if (actor.hasStatus(DetectionMarked.class)) {
                    return actor;
                }

                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    nearestTarget = actor;
                }
            }
        }

        return nearestTarget;
    }

    /**
     * Creates ElectricField hazards around a target location.
     *
     * @param targetLocation the location around which hazards are generated
     */
    private void createElectricField(Location targetLocation) {

        for (Exit exit : targetLocation.getExits()) {

            Location destination = exit.getDestination();

            if (!destination.getGround().hasAbility(StatusAbility.ELECTRIC_FIELD)) {
                destination.setGround(new ElectricField());
            }
        }
    }
    /**
     * Returns the purchase price of the Turret item.
     *
     * @return price in credits
     */
    @Override
    public int getPrice() {
        return PRICE;
    }
    /**
     * Handles the result of purchasing this Turret.
     *
     * @param actor the actor purchasing the item
     * @return confirmation message of purchase
     */
    @Override
    public String onPurchase(Actor actor) {
        return actor + " purchased a Turret for "
                + PRICE + " credits.";
    }
    /**
     * Returns the buy action for this Turret item.
     *
     * @param worker the worker purchasing the item
     * @return a BuyAction for this Turret item
     */
    @Override
    public Action getBuyAction(ContractedWorker worker) {
        return new BuyAction(worker, new Turret(), this);
    }
}