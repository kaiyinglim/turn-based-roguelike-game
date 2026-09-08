package game.grounds;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.ConsumeAction;
import game.enums.WorkerAbility;
import game.interfaces.Consumable;
import game.statuses.Poison;

/**
 * A small, stationary body of mysterious liquid on the ground.
 * In a standard video game, this would just be water. On a deprecated moon
 * in the Eclipse Nebula, it could be anything from spilled engine coolant to
 * highly corrosive alien saliva. Step in it at your own risk.
 */
public class Puddle extends Ground implements Consumable {

    /**
     * The amount of healing when drinking puddle
     */
    private static final int HEAL_AMOUNT = 1;

    /**
     * The amount of turns for poison ticks
     */
    private static final int POISON_TURNS = 3;

    /**
     * The constructor of the Puddle class
     */
    public Puddle() {
        super('~', "Puddle");
    }

    /**
     * A method that checks if puddle is sterilise or not,
     * and poisons or heals hp when consuming it.
     * @param actor the actor that consumes the puddle
     * @return a string that outputs the description of consuming the puddle.
     */
    @Override
    public String consumeBy(Actor actor) {
        if (actor.hasAbility(WorkerAbility.STERILISE)) {
            actor.heal(HEAL_AMOUNT);
            return actor + " drinks from the puddle and heals " + HEAL_AMOUNT + " HP";
         }
        actor.addStatus(new Poison(POISON_TURNS, 1));
        return actor + " drinks from the puddle and is poisoned for " + POISON_TURNS + " turns";
    }

    /**
     * A list of actions that can be performed from its owner.
     * if the direction between the player and location is 0,
     * action will be added that the player can consume it.
     * @param actor the actor that owns the item
     * @param location the location of the ground
     * @param direction the direction between item and ground
     * @return a list of allowable actions that puddle can be performed from its owner.
     */
    @Override
    public ActionList allowableActions(Actor actor, Location location, String direction) {
        ActionList actions = new ActionList();
        if (direction.isEmpty()) {
            actions.add(new ConsumeAction(this));
        }
        return actions;
    }
}
