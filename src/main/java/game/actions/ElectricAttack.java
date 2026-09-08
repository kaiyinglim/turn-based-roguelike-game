package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.statuses.EMPShock;
import game.statuses.Shielded;

/**
 * Action that performs an electric attack on a target actor.
 *
 * This action allows an attacking entity to deal electrical damage
 * to a target actor. Damage may be reduced if the target has the
 * Shielded status. In addition, successful attacks may apply the
 * EMPShock status, temporarily disrupting the target's ability to act.
 *
 * @author Suzan Xuan Loh Palmer
 * @version 25.0.3
 */
public class ElectricAttack extends Action {

    private final Actor target;
    private final int damage;
    /**
     * Constructs an electric attack action.
     *
     * @param target the actor being attacked
     * @param damage the base damage of the attack
     */
    public ElectricAttack(Actor target, int damage) {
        this.target = target;
        this.damage = damage;
    }
    /**
     * Executes the electric attack.
     *
     * If the target is null or not conscious, the attack fails.
     * If the target has the Shielded status, damage is reduced.
     * Otherwise, full damage is applied. If the target is not
     * already EMP shocked, the EMPShock status is applied for a
     * limited duration.
     *
     * @param attacker the actor performing the attack
     * @param map the game map where the action occurs
     * @return a description of the attack result
     */
    @Override
    public String execute(Actor attacker, GameMap map) {

        if (target == null || !target.isConscious()) {
            return attacker + " has no valid target.";
        }

        int finalDamage = damage;

        if (target.hasStatus(Shielded.class)) {
            finalDamage /= 2;
        }

        target.hurt(finalDamage);

        if (!target.hasStatus(EMPShock.class)) {
            target.addStatus(new EMPShock(2));
        }

        return attacker + " electrocutes " + target;
    }
    /**
     * Returns the menu description shown to the player.
     *
     * @param actor the actor viewing the action menu
     * @return menu description for this action
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " shocks " + target;
    }
}
