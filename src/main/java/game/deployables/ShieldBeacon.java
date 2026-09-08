package game.deployables;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.actions.BuyAction;
import game.actions.DeployAction;
import game.enums.ItemStatistics;
import game.enums.StatusAbility;
import game.enums.WorkerAbility;
import game.interfaces.Deployable;
import game.interfaces.Purchasable;
import game.loaders.ContractedWorker;
import game.statuses.Shielded;

/**
 * Deployable Shield Beacon item.
 *
 * This item represents a purchasable Shield Beacon unit that can be carried
 * in the inventory and deployed onto the map. Once deployed, it remains
 * stationary and emits a protective field that applies the Shielded status
 * to allied entities within range.
 *
 * The Shield Beacon provides defensive support by increasing survivability
 * of nearby allied units, forming part of the autonomous tactical support
 * system by creating controlled protective zones on the battlefield.
 *
 * @author Suzan Xuan Loh Palmer
 * @version 25.0.3
 */
public class ShieldBeacon extends Item implements Deployable, Purchasable {

    private static final int PRICE = 25;
    private static final int RANGE = 2;
    private static final int SHIELD_DURATION = 2;
    private static final int WEIGHT = 5;
    /**
     * Constructs a Shield Beacon item.
     *
     * Initialises item statistics such as weight and deployable status,
     * and marks the item as portable so it can be stored in inventory.
     */
    public ShieldBeacon() {
        super("Shield Beacon", 'B');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.addNewStatistic(ItemStatistics.DEPLOYABLE, new BaseStatistic(1));
        this.makePortable();
    }
    /**
     * Provides the deploy action for this Shield Beacon.
     *
     * @param worker the worker deploying the beacon
     * @return a DeployAction for this Shield Beacon item
     */
    @Override
    public Action getDeployAction(ContractedWorker worker) {
        return new DeployAction(this);
    }
    /**
     * Deploys the Shield Beacon onto the map.
     *
     * The item is removed from the inventory and placed on the current tile.
     * Once deployed, it continuously emits a protective aura that applies
     * the Shielded status to nearby allied actors.
     *
     * @param worker the worker deploying the beacon
     * @param map the game map
     * @return a message describing the deployment result
     */
    @Override
    public String deploy(ContractedWorker worker, GameMap map) {
        if (!worker.getInventory().remove(this)) {
            return worker + " fails to deploy a Shield Beacon.";
        }
        Location location = map.locationOf(worker);

        location.addItem(this);
        //worker.getInventory().remove(this);

        return worker + " deploys a Shield Beacon.";
    }
    /**
     * Updates the Shield Beacon effect each turn.
     *
     * Scans for allied actors within range and applies the Shielded status
     * for a limited duration, creating a defensive zone around the beacon.
     *
     * @param location the current location of the Shield Beacon
     */
    @Override
    public void tick(Location location) {

        GameMap map = location.map();

        for (int x : map.getXRange()) {
            for (int y : map.getYRange()) {

                Location current = map.at(x, y);

                if (!current.containsAnActor()) {
                    continue;
                }

                Actor actor = current.getActor();

                int distance =
                        Math.abs(current.x() - location.x())
                                + Math.abs(current.y() - location.y());

                if (distance > RANGE) {
                    continue;
                }

                applyShield(actor);
            }
        }
    }
    /**
     * Applies the Shielded status to allied actors.
     *
     * @param actor the actor receiving the shield effect
     */
    private void applyShield(Actor actor) {

        if (!actor.hasAbility(WorkerAbility.ALLY)) {
            return;
        }

        if (!actor.hasStatus(Shielded.class)) {
            //actor.enableAbility(StatusAbility.SHIELDED);
            actor.addStatus(new Shielded(SHIELD_DURATION));
        }
    }
    /**
     * Returns the purchase price of the Shield Beacon item.
     *
     * @return price in credits
     */
    @Override
    public int getPrice() {
        return PRICE;
    }
    /**
     * Handles the result of purchasing this Shield Beacon.
     *
     * @param actor the actor purchasing the item
     * @return confirmation message of purchase
     */
    @Override
    public String onPurchase(Actor actor) {
        return actor + " purchased a ShieldBeacon for "
                + PRICE + " credits.";
    }
    /**
     * Returns the buy action for this Shield Beacon item.
     *
     * @param worker the worker purchasing the item
     * @return a BuyAction for this Shield Beacon item
     */
    @Override
    public Action getBuyAction(ContractedWorker worker) {
        return new BuyAction(worker, new ShieldBeacon(), this);
    }
}