package game.states.cablebot;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.positions.GameMap;
import game.loaders.WorkerSensing;
import game.enemies.Cablebot;
import game.loaders.StatefulMoonEnemy;
import game.interfaces.CreatureState;
import game.enums.StatefulCreatureStateId;

/**
 * Cablebot's charge state.
 * In this state, Cablebot stays still and builds charge through existing cable connections.
 *
 * @author Lim Kai YIng
 * @version 25.0.2
 */
public final class CablebotChargeState implements CreatureState {

    /**
     * {@inheritDoc}
     *
     * @return {@link StatefulCreatureStateId#CABLEBOT_CHARGE}
     */
    @Override
    public StatefulCreatureStateId getStateId() {
        return StatefulCreatureStateId.CABLEBOT_CHARGE;
    }

    /**
     * Charge transitions (priority order):
     * <ol>
     *     <li>If charge is at maximum and there is at least one live cable, transition to overload.</li>
     *     <li>If at least one worker is adjacent, transition to latch.</li>
     *     <li>If there are no live cables and no adjacent workers, transition to survey.</li>
     *     <li>Otherwise, remain in charge.</li>
     * </ol>
     * <p>
     * Row 2 is not gated on {@code charge &lt; maxCharge} so full charge with no cables but an adjacent
     * worker still moves to latch (attach before overload).
     *
     * @param creature the stateful enemy (must be a {@link Cablebot})
     * @param map      the map that contains the creature
     * @return the state id to enter before this turn's ordinary action
     */
    @Override
    public StatefulCreatureStateId getNextState(StatefulMoonEnemy creature, GameMap map) {
        Cablebot bot = (Cablebot) creature; 
        int adjacentWorkers = WorkerSensing.countAdjacentWorkers(creature, map);
        int attachedCableCount = bot.getAttachedCableCount();
        int charge = bot.getCharge();
        int maxCharge = bot.getMaxCharge();

        // If the charge is at the maximum and there are live cables, transition to overload.
        if (charge >= maxCharge && attachedCableCount > 0) {
            return StatefulCreatureStateId.CABLEBOT_OVERLOAD;
        }

        // If there is at least one adjacent worker, transition to latch.
        if (adjacentWorkers >= 1) {
            return StatefulCreatureStateId.CABLEBOT_LATCH;
        }

        // If there are no live cables and no adjacent workers, transition to survey.
        if (attachedCableCount == 0) {
            return StatefulCreatureStateId.CABLEBOT_SURVEY;
        }

        // Otherwise, remain in charge.
        return StatefulCreatureStateId.CABLEBOT_CHARGE;
    }

    /**
     * Applies charge gain for this turn while remaining in charge (after any transition).
     * 
     * @param creature the stateful enemy performing its turn
     * @param map      the map that contains the creature
     * @return a do-nothing action (charge change is immediate, not carried by the action object)
     */
    @Override
    public Action getAction(StatefulMoonEnemy creature, GameMap map) {
        Cablebot bot = (Cablebot) creature;
        int n = bot.getAttachedCableCount();

        // If there are no live cables, return a do-nothing action.
        if (n == 0) {
            return new DoNothingAction();
        }

        // Calculate the charge gain.
        int gain = 1 + n;
        if (n >= 2) {
            gain += 1;
        }
        bot.increaseCharge(gain);
        return new DoNothingAction();
    }
}
