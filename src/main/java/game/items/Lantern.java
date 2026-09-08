package game.items;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import edu.monash.fit2099.engine.statistics.StatisticOperations;
import game.actions.InfectAction;
import game.actions.SellAction;
import game.enums.EnemyAbility;
import game.enums.ItemStatistics;
import game.enums.WorkerAbility;
import game.grounds.Fire;
import game.interfaces.Infectable;
import game.interfaces.Sellable;
import game.loaders.ContractedWorker;
import game.statuses.Burning;
import game.statuses.InfectedStatus;

/**
 * A class representing a lantern which has an unstable fuel,
 * this fuel can randomly spill and cause a fire on a specific location
 * @author FIT2099 Bryan Chan Zheng Lam
 */
public class Lantern extends Item implements Sellable, Infectable {

    /**
     * The max amount of oil fuel in the lantern
     */
    private static final int MAX_OIL = 10;

    /**
     * The remaining oil left in the lantern
     */
    private int oil_remaining = MAX_OIL;

    /**
     * The constructor of Lantern class
     */
    private static final int PRICE_PER_OIL = 5;
    public Lantern() {
        super("Lantern", '&');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(7));
        this.enableAbility(ItemStatistics.OIL);
        this.enableAbility(ItemStatistics.IS_INFECTABLE);
        this.addNewStatistic(ItemStatistics.SELLABLE, new BaseStatistic(1));
        this.makePortable();
    }

    /**
     * If the remaining oil is more than 0 and it hits the 5% chance of oil spill,
     * then oil fuel loses 1 unit and creates fire on the current location.
     * @param currentLocation The location of the actor carrying this Item.
     * @param actor The actor carrying this Item.
     */
    @Override
    public void tick(Location currentLocation, Actor actor) {
        if (oil_remaining > 0 && Math.random() < 0.05) {
            oil_remaining--;
            Ground prevGround = currentLocation.getGround();
            int fireDuration = 5;
            currentLocation.setGround(new Fire(prevGround, fireDuration));
            new Display().println(actor + "'s lantern leaks, fire is now spread on " + currentLocation + ". ("
                                        + oil_remaining + " oil remaining left)");
        }
    }

    /**
     * Lantern will not leak oil when drop on ground
     * @param currentLocation The location of the ground on which we lie.
     */
    @Override
    public void tick(Location currentLocation) {
        //lantern oil does not leak when drop on ground
    }
    /**
     * Returns the selling price of the Lantern.
     * The price depends on the remaining oil inside the lantern.
     *
     * @return sell price based on remaining oil (oil_remaining * PRICE_PER_OIL)
     */
    @Override
    public int getSellPrice() {
        return oil_remaining * PRICE_PER_OIL;
    }
    /**
     * Applies effects when the Lantern is sold.
     * There is a 50% chance the worker is burned and receives a burning status.
     * There is also a 25% independent chance that fire spreads to surrounding tiles.
     *
     * @param worker the worker selling the Lantern
     * @param map the game map where the transaction occurs
     * @return description of the selling outcome and side effects
     */
    @Override
    public String onSell(ContractedWorker worker, GameMap map, Location supercomputerLocation) {
        String result = "";

        if (Math.random() < 0.5) {
            worker.addStatus(new Burning(3, 2));
            result += worker + " was burned by the Lantern.\n";
        }

        if (Math.random() < 0.25) {
            Location location = map.locationOf(worker);
            for (Exit exit : location.getExits()) {
                Location destination = exit.getDestination();
                Ground prevGround = destination.getGround();
                int fireDuartion = 5;
                destination.setGround(new Fire(prevGround, fireDuartion));
            }
        }
        result += "Fire spreads around the worker.\n";

        return result;
    }
    /**
     * Creates a sell action for this Lantern.
     *
     * @param worker the worker performing the sale
     * @return a SellAction instance for this item
     */
    @Override
    public Action getSellAction(ContractedWorker worker, Location supercomputerLocation) {
        return new SellAction(worker, this, this, supercomputerLocation);
    }
    /**
     * Returns allowable actions for this Lantern.
     * Workers can sell the Lantern via the Supercomputer.
     *
     * @param owner the actor who owns this item
     * @param map the game map where interaction occurs
     * @return list of possible actions (sell action if applicable)
     */
    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        return new ActionList();
    }

    /**
     * A list of actions that can be performed from its owner.
     * @param location the location of the ground on which the item lies
     * @return a list of allowable actions that Lantern can be performed by the Parasite
     */
    @Override
    public ActionList allowableActions(Location location) {
        ActionList actions = new ActionList();
        for (Exit exit : location.getExits()) {
            Location surroundingLocation = exit.getDestination();
            if (surroundingLocation.containsAnActor()) {
                Actor actor = surroundingLocation.getActor();
                // parasite is adjacent
                if (actor.hasAbility(EnemyAbility.IS_ENEMY)) {
                    actions.add(new InfectAction(actor, this, this));
                }
            }
        }
        return actions;
    }

    /**
     * Gets the infected status
     * @return a new InfectedWorker
     */
    @Override
    public Status getInfectedStatus() {
        return new InfectedStatus(ItemStatistics.OIL, StatisticOperations.DECREASE, 1,
                "Infected lantern oil drains!",
                0, null);
    }

    /**
     * Gets the infect action from infect actor action
     * @param parasite a parasite that infects entities/items
     * @param location the location in which the infect action takes place
     * @return a new InfectActorAction
     */
    @Override
    public Action getInfectAction(Actor parasite, Location location) {
        return new InfectAction(parasite, this, this);
    }
}
