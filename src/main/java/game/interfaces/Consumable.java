package game.interfaces;

import edu.monash.fit2099.engine.actors.Actor;

/**
 * Consumable class is an interface class that represents an ability to consume
 * @author FIT2099 Bryan Chan
 */
public interface Consumable {
    public String consumeBy(Actor actor);
}
