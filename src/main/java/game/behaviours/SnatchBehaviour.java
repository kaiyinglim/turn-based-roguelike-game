package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.SnatchAction;
import game.enums.EnemyAbility;
import game.enums.ItemStatistics;

/**
 * A class that represents the behaviour of the enemy snatching a depositable item on the ground.
 * Utilizes capability checking to toggle behavior based on infection state.
 * @author FIT2099 Bryan Chan Zheng Lam
 */
public class SnatchBehaviour implements Behaviour<Actor, Action> {

    /**
     * Returns a SnatchAction to snatch an item on the location its standing on, if possible.
     * If the actor is infected or no items are available, returns null.
     *
     * @param actor The entity performing the behaviour
     * @param location The location of the current entity
     * @return an Action, or null if no SnatchAction is possible
     */
    @Override
    public Action operate(Actor actor, Location location) {
        //Stops hoarding if infected
        if (actor.hasAbility(EnemyAbility.IS_RABID)) {
            return null;
        }
        //Snatch Item from ground if it is depositable
        for (Item item : location.getItems()) {
            if (item.hasStatistic(ItemStatistics.DEPOSITABLE)) {
                return new SnatchAction(item);
            }
        }
        return null;
    }
}