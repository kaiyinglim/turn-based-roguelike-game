package game.states.cablebot;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.positions.GameMap;
import game.behaviours.OverloadCableBehaviour;
import game.enemies.Cablebot;
import game.loaders.StatefulMoonEnemy;
import game.interfaces.CreatureState;
import game.enums.StatefulCreatureStateId;

/**
 * Cablebot spends stored charge through live cables to disrupt workers (forced drops, weakened cables).
 *
 * @author Lim Kai YIng
 * @version 25.0.2
 */
public final class CablebotOverloadState implements CreatureState {

    private final Behaviour<Cablebot, Action> overloadBehaviour = new OverloadCableBehaviour();

    /**
     * {@inheritDoc}
     *
     * @return {@link StatefulCreatureStateId#CABLEBOT_OVERLOAD}
     */
    @Override
    public StatefulCreatureStateId getStateId() {
        return StatefulCreatureStateId.CABLEBOT_OVERLOAD;
    }

    /**
     * Overload transitions (priority order):
     * <ol>
     *     <li>{@link StatefulCreatureStateId#CABLEBOT_CHARGE} if charge is zero and at least one cable remains</li>
     *     <li>{@link StatefulCreatureStateId#CABLEBOT_SURVEY} if there are no live cables</li>
     *     <li>Otherwise remain {@link StatefulCreatureStateId#CABLEBOT_OVERLOAD}</li>
     * </ol>
     *
     * @param creature the stateful enemy (must be a {@link Cablebot})
     * @param map      the map that contains the creature
     * @return the state id to enter before this turn's ordinary action
     */
    @Override
    public StatefulCreatureStateId getNextState(StatefulMoonEnemy creature, GameMap map) {
        Cablebot bot = (Cablebot) creature;
        int attachedCableCount = bot.getAttachedCableCount();
        int charge = bot.getCharge();

        if (charge == 0 && attachedCableCount > 0) {
            return StatefulCreatureStateId.CABLEBOT_CHARGE;
        }
        if (attachedCableCount == 0) {
            return StatefulCreatureStateId.CABLEBOT_SURVEY;
        }
        return StatefulCreatureStateId.CABLEBOT_OVERLOAD;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Delegates to {@link OverloadCableBehaviour} to pick a cable and perform overload (drop, charge cost, weaken).
     *
     * @param creature the stateful enemy performing its turn
     * @param map      the map that contains the creature
     * @return overload action or {@link DoNothingAction} if there is no cable to overload
     */
    @Override
    public Action getAction(StatefulMoonEnemy creature, GameMap map) {
        Cablebot bot = (Cablebot) creature;
        Action action = overloadBehaviour.operate(bot, map.locationOf(bot));
        return action != null ? action : new DoNothingAction();
    }
}
