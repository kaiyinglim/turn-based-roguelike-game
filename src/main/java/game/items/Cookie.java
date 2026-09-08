package game.items;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.ActorStatistics;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import edu.monash.fit2099.engine.statistics.StatisticOperations;
import game.actions.ConsumeAction;
import game.actions.InfectAction;
import game.actions.SellAction;
import game.enums.EnemyAbility;
import game.enums.ItemStatistics;
import game.enums.WorkerAbility;
import game.interfaces.Consumable;
import game.interfaces.Infectable;
import game.interfaces.Sellable;
import game.loaders.ContractedWorker;
import game.statuses.InfectedStatus;

public class Cookie extends Item implements Consumable, Sellable, Infectable {

    /**
     * The max amount of uses the player can eat cookie
     */
    private static final int MAX_USES = 5;

    /**
     * The amount of healing per cookie
     */
    private static final int HEAL_AMOUNT = 1;

    /**
     * The remaining cookies left
     */
    private int cookiesRemaining = MAX_USES;

    /**
     * The constructor of Cookie class
     */
    public Cookie() {
        super("Cookie", '◍');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(2));
        this.addNewStatistic(ItemStatistics.CONSUMABLE, new BaseStatistic(1));
        this.enableAbility(ItemStatistics.IS_INFECTABLE);
        this.addNewStatistic(ItemStatistics.SELLABLE, new BaseStatistic(1));
        this.makePortable();
    }

    /**
     * A method that checks if cookie is sterilise or not,
     * and decrease max hp or heals hp when consuming it.
     * @param actor the actor that consumes the item
     * @return a string that outputs the description of consuming the cookie.
     */
    @Override
    public String consumeBy(Actor actor) {
        cookiesRemaining--;
        String result;
        if (actor.hasAbility(WorkerAbility.STERILISE)) {
            actor.heal(HEAL_AMOUNT);
            result = actor + " eats a cookie and heals " + HEAL_AMOUNT + " HP.";
        } else {
            actor.modifyStatisticMaximum(ActorStatistics.HEALTH, StatisticOperations.DECREASE, 1);
            result = actor + " eats a cookie and loses 1 HP.";
        }
        if (cookiesRemaining <= 0) {
            actor.getInventory().remove(this);
            result += " (All cookies eaten, item removed)";
        } else {
            result += " (" + cookiesRemaining + " cookies remaining)";
        }
        return result;
    }

    /**
     * Returns the allowable actions for the Cookie item.
     * The cookie can be consumed if it still has remaining uses,
     * and can also be sold if the owner is a worker.
     *
     * @param owner the actor who owns the cookie
     * @param map the game map where the actor is located
     * @return a list of possible actions (consume and/or sell)
     */
    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        ActionList actions = new ActionList();
        if (cookiesRemaining > 0) {
            actions.add(new ConsumeAction(this));
        }

        return actions;
    }
    /**
     * Returns the selling price of the Cookie.
     * The price depends on how many cookies remain in the pack.
     *
     * @return number of remaining cookies as the sell price
     */
    @Override
    public int getSellPrice() {
        return cookiesRemaining;
    }
    /**
     * Applies effects when the Cookie is sold.
     * The worker takes damage equal to the number of remaining cookies.
     *
     * @param worker the worker selling the cookies
     * @param map the game map where the transaction occurs
     * @return description of the selling outcome
     */
    @Override
    public String onSell(ContractedWorker worker, GameMap map, Location supercomputerLocation) {
        worker.hurt(cookiesRemaining);
        return worker + " sold Cookies and lost " + cookiesRemaining + " HP.";
    }
    /**
     * Creates a sell action for this Cookie item.
     *
     * @param worker the worker who is selling the item
     * @return a SellAction instance for this Cookie
     */
    @Override
    public Action getSellAction(ContractedWorker worker, Location supercomputerLocation) {
        return new SellAction(worker, this, this, supercomputerLocation);
    }

    /**
     * A list of actions that can be performed from its owner.
     * It will have an action that can infect the Cookie
     * @param location the location of the ground on which the item lies
     * @return a list of allowable actions that Cookie can be performed by the Parasite
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
        return new InfectedStatus(ItemStatistics.CONSUMABLE, StatisticOperations.DECREASE, 1,
                "Infected cookies content drains!",
                1, "A Parasite spawned from infected cookie!");
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
