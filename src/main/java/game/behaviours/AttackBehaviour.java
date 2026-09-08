package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.AttackAction;

/**
 * A class that represents the behaviour of the enemy attacking the entity
 * @author FIT2099 Bryan Chan Zheng Lam
 */
public class AttackBehaviour implements Behaviour<Actor, Action> {

    /**
     * The specific target type wgere the target must possess a specific type for it to attack
     */
    private final Enum<?> targetType;

    /**
     * The specific capability that must possess to execute this behaviour.
     * If set to null, actor can attack
     */
    private final Enum<?> requiredCapability;

    /**
     * The constructor of AttackBehaviour class
     * @param targetType the type of the target
     */
    public AttackBehaviour(Enum<?> targetType) {
        this.targetType = targetType;
        this.requiredCapability = null;
    }

    /**
     * The constructor of AttackBehaviour class
     * @param targetType the type of the target
     * @param requiredCapability the specific capability of the target
     */
    public AttackBehaviour(Enum<?> targetType, Enum<?> requiredCapability) {
        this.targetType = targetType;
        this.requiredCapability = requiredCapability;
    }

    /**
     * Returns a MoveAction to attacking player on the location its standing on, if possible.
     * If no movement is possible, returns null.
     *
     * @param actor The entity performing the behaviour
     * @param location The location of the current entity
     * @return an Action, or null if no MoveAction is possible
     */
    @Override
    public Action operate(Actor actor, Location location) {

        if (this.requiredCapability != null) {
            if (!actor.hasAbility(this.requiredCapability)) {
                return null;
            }
        }
        for (Exit exit : location.getExits()) {
            Location destination = exit.getDestination();

            // Check if there is an actor, and if that actor has the target trait
            if (destination.containsAnActor()) {
                Actor target = destination.getActor();
                if (target.hasAbility(this.targetType)) {
                    return new AttackAction(target, exit.getName());
                }
            }
        }
        return null;
    }
}
