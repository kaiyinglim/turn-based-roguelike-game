package game.items;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.ActorStatistics;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import edu.monash.fit2099.engine.statistics.StatisticOperations;
import game.actions.BuyAction;
import game.actions.ConsumeAction;
import game.enums.ItemStatistics;
import game.interfaces.Consumable;
import game.interfaces.Purchasable;
import game.loaders.ContractedWorker;

/**
 * A class representing a first aid kit.
 * Its primary function is to save a player when that player is low on health.
 * Once use, it will undergo a long cooldown, so players have to use it carefully.
 *
 * @author Bryan Chan Zheng Lam
 */
public class FirstAidKit extends Item implements Consumable, Purchasable {

    /**
     * Starting cooldown timer, after first aid kit was first used.
     */
    private static final int COOLDOWN_TIMER = 20;

    /**
     * The number of turns left, after first aid kit is used
     */
    private int cooldown_remaining = 0;

    /**
     * The constructor of FirstAidKit class
     */
    public FirstAidKit() {
        super("First Aid Kit", '+');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(25));
        this.addNewStatistic(ItemStatistics.CONSUMABLE, new BaseStatistic(1));
        this.makePortable();
    }

    /**
     * When the player uses the first aid kit, increase player's max health
     * and restore health points fully.
     * Cooldown timer starts.
     * @param actor the actor that gets its stats modify
     */
    public void use(Actor actor) {
        actor.modifyStatisticMaximum(ActorStatistics.HEALTH, StatisticOperations.INCREASE, 1);
        cooldown_remaining = COOLDOWN_TIMER;
    }

    /**
     * When timer reaches 0, first aid kit can be used again
     * @return true when timer hits 0, false when timer is not 0
     */
    public boolean isReady() {
        return cooldown_remaining <= 0;
    }

    /**
     * Gets the current cooldown timer
     * @return current cooldown timer
     */
    public int getCooldownTimer() {
        return COOLDOWN_TIMER;
    }

    /**
     * If the cooldown timer has not reached 0, decrement by 1.
     * @param currentLocation The location of the actor carrying this Item.
     * @param actor The actor carrying this Item.
     */
    @Override
    public void tick(Location currentLocation, Actor actor) {
        //item on cooldown only when carried
        if (cooldown_remaining > 0) {
            cooldown_remaining--;
        }
    }

    /**
     * Freeze the cooldown timer when first aid kit is on the ground.
     * @param currentLocation The location of the ground on which we lie.
     */
    @Override
    public void tick(Location currentLocation) {
        //do nothing - freeze tick when on ground
    }

    /**
     * A list of actions that can be performed from its owner.
     * If first aid kit is ready to be used, it will have an action that the owner can use to restore health.
     * If first aid kit had been used, no actions will be added to the menu.
     *
     * @param owner the owner that owns the item
     * @param map the map where the actor is performing the action on
     * @return a list of allowable actions that first aid kit can be performed from its owner.
     */
    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        ActionList actions = new ActionList();
        if (isReady()) {
            actions.add(new ConsumeAction(this));
        }
        return actions;
    }

    @Override
    public String consumeBy(Actor actor) {
        use(actor);
        return actor + " uses " + this + " and increase max health by 1.\n" +
                actor + " is now at full health.\n" +
                "(Next available use in: " + getCooldownTimer() + " turns)";
    }

    /**
     * The toString() method of the item class
     * @return name of the item class
     */
    @Override
    public String toString() {
        return super.toString();
    }
    /**
     * Returns the purchase price of the First Aid Kit.
     *
     * @return price in credits (1000)
     */
    @Override
    public int getPrice() {
        return 1000;
    }
    /**
     * Applies effects when the First Aid Kit is purchased.
     * If the worker does not have enough credits, they are killed instantly
     * by the Supercomputer as punishment for insufficient funds.
     * Otherwise, the item is added to the worker's inventory.
     *
     * @param actor the actor attempting to purchase the item
     * @return a description of the purchase outcome
     */
    @Override
    public String onPurchase(Actor actor) {

        ContractedWorker worker = (ContractedWorker) actor;

        if (worker.getCredits() < getPrice()) {
            worker.hurt(worker.getStatistic(ActorStatistics.HEALTH));

            return worker + " tried to purchase a First Aid Kit without enough credits "
                    + "and was killed by the Supercomputer.";
        }

        //worker.deductCredits(getPrice());
        worker.getInventory().add(this);

        return worker + " purchased a First Aid Kit for " + getPrice() + " credits.";
    }
    /**
     * Creates a buy action for this First Aid Kit.
     *
     * @param worker the worker purchasing the item
     * @return a BuyAction instance for this item
     */
    public Action getBuyAction(ContractedWorker worker) {
        return new BuyAction(worker, this, this);
    }
}
