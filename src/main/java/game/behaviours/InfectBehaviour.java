package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.interfaces.Infectable;

import java.util.Optional;

/**
 * A class that represents the behaviour of the enemy infecting the entity/item.
 * @author FIT2099 Bryan Chan Zheng Lam
 */
public class InfectBehaviour implements Behaviour<Actor, Action> {

    /**
     * Returns a InfectAction to infecting player on the location its standing on, if possible.
     * If no infection is possible, returns null.
     *
     * @param actor The entity performing the behaviour
     * @param location The location of the current entity
     * @return an Action, or null if no InfectAction is possible
     */
    @Override
    public Action operate(Actor actor, Location location) {
        for (Exit exit : location.getExits()) {
            Location destination = exit.getDestination();

            // check actors
            if (destination.containsAnActor()) {
                Actor target = destination.getActor();
                Optional<Infectable> infectable = target.asCapability(Infectable.class);
                if (infectable.isPresent()) {
                    return infectable.get().getInfectAction(actor, destination);
                }
            }

            // check items on ground
            for (Item item : destination.getItems()) {
                Optional<Infectable> infectable = item.asCapability(Infectable.class);
                if (infectable.isPresent()) {
                    return infectable.get().getInfectAction(actor, destination);
                }
            }
        }
        return null;
    }
}
