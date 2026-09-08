package game.states.mannequin;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.positions.GameMap;
import game.loaders.StatefulMoonEnemy;
import game.interfaces.CreatureState;
import game.enums.StatefulCreatureStateId;
import game.loaders.WorkerSensing;

/**
 * Mannequin idle state.
 *
 * @author Lim Kai YIng
 * @version 25.0.2
 */
public class MannequinIdleState implements CreatureState {

    /**
     * {@inheritDoc}
     *
     * @return {@link StatefulCreatureStateId#MANNEQUIN_IDLE}
     */
    @Override
    public StatefulCreatureStateId getStateId() {
        return StatefulCreatureStateId.MANNEQUIN_IDLE;
    }

    /**
     * Idle transitions: count workers on the eight neighbour tiles
     * If any are adjacent, reset the lonely-idle counter via {@link MannequinStateMemory}.
     * Then, in order:
     * 1. {@code adjacentWorkers >= 2} stays idle;
     * 2. {@code adjacentWorkers == 1} goes berserk;
     * 3. {@code adjacentWorkers == 0} increments the idle lonely turns counter and transitions to
     *    collector when it reaches 10, otherwise stays idle.
     * <p>
     *
     * @param creature the stateful enemy
     * @param map      the map containing the creature
     * @return the state id to enter before this turn's ordinary action
     */
    @Override
    public StatefulCreatureStateId getNextState(StatefulMoonEnemy creature, GameMap map) {
        MannequinStateMemory memory = (MannequinStateMemory) creature;
        int adjacentWorkers = WorkerSensing.countAdjacentWorkers(creature, map);

        // Any adjacent worker resets the lonely streak counter.
        if (adjacentWorkers > 0) {
            memory.resetIdleLonelyTurns();
        }

        // If there are two or more adjacent workers, stay idle.
        if (adjacentWorkers >= 2) {
            return StatefulCreatureStateId.MANNEQUIN_IDLE;
        }

        // If there is exactly one adjacent worker, transition to berserk.
        if (adjacentWorkers == 1) {
            return StatefulCreatureStateId.MANNEQUIN_BERSERK;
        }

        // Increment the lonely streak counter.
        memory.incrementIdleLonelyTurns();

        // If the lonely streak counter reaches 10, transition to active.
        if (memory.getIdleLonelyTurns() >= 10) {
            return StatefulCreatureStateId.MANNEQUIN_COLLECTOR;
        }

        // If the lonely streak counter is less than 10, stay idle.
        return StatefulCreatureStateId.MANNEQUIN_IDLE;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Idle mannequin performs no ordinary action beyond transition resolution.
     */
    @Override
    public Action getAction(StatefulMoonEnemy creature, GameMap map) {
        return new DoNothingAction();
    }
}
