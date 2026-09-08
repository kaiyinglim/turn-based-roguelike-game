package game.items;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.actions.ConsumeAction;
import game.actions.SellAction;
import game.enums.ItemStatistics;
import game.enums.WorkerAbility;
import game.interfaces.Consumable;
import game.interfaces.Sellable;
import game.loaders.ContractedWorker;
import game.statuses.Poison;

public class Apple extends Item implements Consumable, Sellable {

    /**
     * The amount of healing when eating a sterilise apple
     */
    private static final int HEAL_AMOUNT = 3;

    /**
     * The amount of turns the poison ticks.
     */
    private static final int POISON_TURNS = 5;

    /**
     * The constructor of Apple class
     */
    public Apple() {
        super("Apple", 'ó');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(1));
        //1 = Consumable, 0 = Non-Consumable
        this.addNewStatistic(ItemStatistics.CONSUMABLE, new BaseStatistic(1));
        this.addNewStatistic(ItemStatistics.SELLABLE, new BaseStatistic(1));
        this.makePortable();
    }

    /**
     * A method that checks if apple is sterilise or not,
     * and heals or poison depending on it.
     * @param actor the actor that consumes the item
     * @return a string that outputs the description of consuming the apple.
     */
    @Override
    public String consumeBy(Actor actor) {
        actor.getInventory().remove(this);
        if (actor.hasAbility(WorkerAbility.STERILISE)) {
            actor.heal(HEAL_AMOUNT);
                return actor + " eats the apple and heals " + HEAL_AMOUNT + " HP. ";
        }
        actor.addStatus(new Poison(POISON_TURNS, 2));
        return actor + " eats the apple and is poisoned for " + POISON_TURNS + " turns.";
    }

    /**
     * A list of actions that can be performed from its owner.
     * It will have an action that can consume the apple.
     *
     * @param owner the actor that owns the item
     * @param map the map where the actor is performing the action on
     * @return a list of allowable actions that apple can be performed from its owner.
     */
    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        ActionList actions = new ActionList();
        actions.add(new ConsumeAction(this));
        return actions;
    }
    /**
     * Returns the selling price of an Apple.
     * Apples always sell for a fixed price of 1 credit.
     *
     * @return selling price (1 credit)
     */
    @Override
    public int getSellPrice() {
        return 1;
    }
    /**
     * Applies effects when the Apple is sold to the Supercomputer.
     * If the worker does not have sterilisation ability, they are poisoned
     * for 2 turns. Otherwise, no negative effect is applied.
     *
     * @param worker the worker selling the Apple
     * @param map the game map where the transaction occurs
     * @return a description of the selling outcome
     */
    @Override
    public String onSell(ContractedWorker worker, GameMap map, Location supercomputerLocation) {
        if (!worker.hasAbility(WorkerAbility.STERILISE)) {
            worker.addStatus(new Poison(2, 2));
            return worker + " is poisoned for 2 turns.";
        }
        return worker + " sold an Apple.";
    }
    /**
     * Creates a sell action for this Apple item.
     *
     * @param worker the worker who is selling the item
     * @return a SellAction instance for this Apple
     */
    @Override
    public Action getSellAction(ContractedWorker worker, Location supercomputerLocation) {
        return new SellAction(worker, this, this, supercomputerLocation);
    }
}
