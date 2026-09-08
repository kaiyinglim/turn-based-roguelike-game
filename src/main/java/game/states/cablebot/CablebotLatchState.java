package game.states.cablebot;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.positions.GameMap;
import game.enemies.Cablebot;
import game.loaders.StatefulMoonEnemy;
import game.behaviours.LatchCableBehaviour;
import game.interfaces.CreatureState;
import game.enums.StatefulCreatureStateId;
import game.loaders.WorkerSensing;

/**
 * Cablebot attaches or strengthens cables on adjacent workers, then may transition to overload,
 * charge, or survey per the state transition priority table.
 *
 * @author Lim Kai YIng
 * @version 25.0.2
 */
public final class CablebotLatchState implements CreatureState {

    private final Behaviour<Cablebot, Action> latchBehaviour = new LatchCableBehaviour();

    /**
     * {@inheritDoc}
     *
     * @return {@link StatefulCreatureStateId#CABLEBOT_LATCH}
     */
    @Override
    public StatefulCreatureStateId getStateId() {
        return StatefulCreatureStateId.CABLEBOT_LATCH;
    }

    /**
     * Latch transitions (priority order):
     * 1. {@link StatefulCreatureStateId#CABLEBOT_OVERLOAD} if full charge and at least one cable
     * 2. {@link StatefulCreatureStateId#CABLEBOT_CHARGE} if not full charge, cables, no adjacent worker
     * 3. {@link StatefulCreatureStateId#CABLEBOT_SURVEY} if no adjacent worker and no cables
     * 4. {@link StatefulCreatureStateId#CABLEBOT_LATCH} if not full charge and adjacent worker(s)
     *
     * @param creature the stateful enemy evaluating its transition
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

        // If the charge is below the maximum and there are live cables and no adjacent workers, transition to charge.
        if (charge < maxCharge && attachedCableCount > 0 && adjacentWorkers == 0) {
            return StatefulCreatureStateId.CABLEBOT_CHARGE;
        }

        // If there are no adjacent workers and no live cables, transition to survey.
        if (adjacentWorkers == 0 && attachedCableCount == 0) {
            return StatefulCreatureStateId.CABLEBOT_SURVEY;
        }

        return StatefulCreatureStateId.CABLEBOT_LATCH;
    }

    /**
     * {@inheritDoc}
     * Latch behaviour: picks an adjacent worker and latches or strengthens a cable.
     *
     * @param creature the stateful enemy performing its turn
     * @param map      the map that contains the creature
     * @return latch action, or {@link DoNothingAction} if no adjacent worker is available
     */
    @Override
    public Action getAction(StatefulMoonEnemy creature, GameMap map) {
        Cablebot bot = (Cablebot) creature;
        Action action = latchBehaviour.operate(bot, map.locationOf(bot));
        return action != null ? action : new DoNothingAction();
    }
}
