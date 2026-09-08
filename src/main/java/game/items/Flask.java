package game.items;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.actions.ConsumeAction;
import game.enums.ItemStatistics;
import game.interfaces.Consumable;

/**
 * Due to severe budget cuts, the flask is only permitted to hold five (5)
 * mouthfuls of liquid per deployment. Employees are reminded not to consume
 * all five charges in a panic during a single encounter.
 *
 * @author Bryan Chan Zheng Lam
 */

public class Flask extends Item implements Consumable {

    //private attributes
    /**
     * The maximum uses the flask has
     */
    private static final int MAX_USES = 5;

    /**
     * The amount of healing when drinking it
     */
    private static final int HEAL_AMOUNT = 1;

    /**
     * The current total usable amount left
     */
    private int totalUsable = MAX_USES;

    /**
     * The constructor of the Flask class
     */
    public Flask() {
        super("Flask", 'u');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(3));
        this.makeNonPortable();
    }

    /**
     * When drinking flask, heal the player, if flask is not empty
     * @return the amount of healing
     */
    public int drink() {
        if(isEmpty()) {
            return 0;
        } else {
            totalUsable--;
            return HEAL_AMOUNT;
        }
    }

    /**
     * Flask is empty, flask is useless.
     * @return true if current usable amount left is <= 0, else false, if current usage is > 0
     */
    public boolean isEmpty() {
        return totalUsable <= 0;
    }

    /**
     * Gets the current total usable amount left
     * @return the current total usable amount left
     */
    public int getTotalUsable() {
        return totalUsable;
    }

    /**
     * A list of actions that can be performed from its owner.
     * If flask is not empty, it will have an action that the owner can use to drink and restore health.
     * If flask is empty, no actions will be added to the menu.
     *
     * @param owner the actor that owns the item
     * @param map the map where the actor is performing the action on
     * @return a list of allowable actions that flask can be performed from its owner.
     */
    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        ActionList actions = new ActionList();
        if (!isEmpty()) {
            actions.add(new ConsumeAction(this));
        }
        return actions;
    }

    /**
     * A method that checks if flask not empty, and player drinks and heals from it.
     * @param actor the actor that consumes the item
     * @return a string that outputs the description of consuming the bottle.
     */
    @Override
    public String consumeBy(Actor actor) {
        if (isEmpty()) {
            return actor + "'s flask is empty";
        }
        actor.heal(drink());
        return actor + " drinks " + this + " and heals for 1 point of health."
                + " (" + getTotalUsable() + " uses remaining left)";
    }

    /**
     * The toString() method of the item class
     * @return name of the item class
     */
    @Override
    public String toString() {
        return super.toString();
    }
}
