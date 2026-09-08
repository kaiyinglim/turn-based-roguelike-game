package game.states.cablebot;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.positions.GameMap;
import game.enemies.Cablebot;
import game.loaders.StatefulMoonEnemy;
import game.interfaces.CreatureState;
import game.enums.StatefulCreatureStateId;
import game.loaders.WorkerSensing;

/**
 * Cablebot's survey state.
 * In this state, Cablebot waits and checks whether it should latch onto a worker
 * or start charging through existing cable connections.
 *
 * @author Lim Kai YIng
 * @version 25.0.2
 */
public final class CablebotSurveyState implements CreatureState {

    /**
     * {@inheritDoc}
     *
     * @return the state id for this state
     */
    @Override
    public StatefulCreatureStateId getStateId() {
        return StatefulCreatureStateId.CABLEBOT_SURVEY;
    }

    /**
     * Survey transitions (priority order):
     * 1. If at least one worker is adjacent, transition to latch.
     * 2. If no worker is adjacent but live cables exist, transition to charge.
     * 3. Otherwise, remain in survey.
     *
     * @param creature the stateful enemy
     * @param map      the map containing the creature
     * @return the state id to enter before this turn's ordinary action
     */
    @Override
    public StatefulCreatureStateId getNextState(StatefulMoonEnemy creature, GameMap map) {
        Cablebot bot = (Cablebot) creature;
        int adjacentWorkers = WorkerSensing.countAdjacentWorkers(creature, map);
        int attachedCableCount = bot.getAttachedCableCount();

        // If there is at least one adjacent worker, transition to latch.
        if (adjacentWorkers >= 1) {
            return StatefulCreatureStateId.CABLEBOT_LATCH;
        }

        // If there are live cables and the charge is below the maximum, transition to charge.
        if (adjacentWorkers == 0 && attachedCableCount > 0) {
            return StatefulCreatureStateId.CABLEBOT_CHARGE;
        }

        // If there are no adjacent workers and no live cables, remain in survey.
        return StatefulCreatureStateId.CABLEBOT_SURVEY;
    }

    /**
     * Cablebot does not perform an ordinary action while surveying.

     * @param creature the stateful enemy performing its turn
     * @param map      the map that contains the creature
     * @return a do-nothing action
     */
    @Override
    public Action getAction(StatefulMoonEnemy creature, GameMap map) {
        return new DoNothingAction();
    }
}
