package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.interfaces.Consumable;

/**
 * ConsumeAction class is a class that represents Consume Action
 * @author FIT2099 Bryan Chan Zheng Lam
 */
public class ConsumeAction extends Action {

    /**
     * The consumable item to be consume/use when action is executed
     */
    private final Consumable consumable;

    /**
     * The constructor of FirstAidKit class
     * @param consumable the consumable item to be consumed
     */
    public ConsumeAction(Consumable consumable) { this.consumable = consumable; }

    /**
     * When executed, consume action is call by consumeBy()
     *
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return the description of the result of the consume action
     */
    public String execute(Actor actor, GameMap map) {
        return consumable.consumeBy(actor);
    }

    /**
     * Returns an output of consume action for display on menu
     * @param actor The actor performing the action.
     * @return a string describing this consume action
     */
    public String menuDescription(Actor actor) {
        return actor + " consumes " + consumable.getClass().getSimpleName();
    }

}
