package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.weapons.Weapon;

/**
 * Class representing an action to attack Note that the attacker must have a
 * weapon, e.g., an intrinsic weapon or a weapon item. Otherwise, the execute
 * method will throw an error.
 *
 * Note: Taken from the demo/forest/AttackAction class.
 * @author Adrian Kristanto
 */
public class AttackAction extends Action {

    /**
     * The Actor that is to be attacked
     */
    private Actor target;

    /**
     * The direction of incoming attack.
     */
    private String direction;

    /**
     * Weapon used for the attack
     */
    private Weapon weapon;

    /**
     * The constructor of AttackAction class
     * @param target the target of the enemy
     * @param direction the direction between enemy and actor
     */
    public AttackAction(Actor target, String direction) {
        this.target = target;
        this.direction = direction;
    }

    /**
     * Executes when actor gets attack and if target is not conscious, outputs its description
     * else, just output when player gets attack.
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return the output string of the attack action
     */
    public String execute(Actor actor, GameMap map) {
        String result = actor.getIntrinsicWeapon().attack(actor, target, map);

        if (!target.isConscious()) {
            result += "\n" + target.unconscious(actor, map);
        }
        return result;
    }

    /**
     *
     * @param actor The actor performing the action.
     * @return the output string of the menu description
     */
    public String menuDescription(Actor actor) {
        return actor + " attacks " + target + " at " + direction + " with " + (weapon != null ? weapon : "Intrinsic Weapon");
    }
}
