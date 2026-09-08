package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Location;
import game.enums.ItemStatistics;

/**
 * A class that represents the behaviour of the enemy consuming items that players can consume.
 */
public class ConsumeBehaviour implements Behaviour<Actor, Action> {

    /**
     * Returns a MoveAction to consume item on the location its standing on, if possible.
     * If no movement is possible, returns null.
     *
     * @param actor The entity performing the behaviour
     * @param location The location of the current entity
     * @return an Action, or null if no MoveAction is possible
     */
    @Override
    public Action operate(Actor actor, Location location) {
        for (Item item : location.getItems()) {
            if (item.hasStatistic(ItemStatistics.CONSUMABLE)) {
                ActionList actions = item.allowableActions(actor, location.map());
                for (Action action : actions) {
                    location.removeItem(item);
                    return action;
                }
            }
        }
        return null;
    }
}
