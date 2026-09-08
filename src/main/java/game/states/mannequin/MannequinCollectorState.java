package game.states.mannequin;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.behaviours.CollectBehaviour;
import game.behaviours.WanderBehaviour;
import game.loaders.StatefulMoonEnemy;
import game.interfaces.CreatureState;
import game.enums.StatefulCreatureStateId;
import game.loaders.WorkerSensing;

/**
 * Mannequin collector state: wanders and prefers picking up portable items on any of
 * the eight adjacent ground tiles; transitions follow worker adjacency and a
 * no-pickup streak when no workers are adjacent.
 *
 * @author Lim Kai YIng
 * @version 25.0.2
 */
public class MannequinCollectorState implements CreatureState {

    /** Chooses a random legal step when collect behaviour finds nothing to pick up. */
    private final WanderBehaviour wander;
    /** Offers a pick-up action when a portable item lies on an adjacent ground tile. */
    private final CollectBehaviour collectBehaviour;

    /**
     * Constructs a mannequin collector state with default wander and collect behaviour.
     */
    public MannequinCollectorState() {
        this.wander = new WanderBehaviour();
        this.collectBehaviour = new CollectBehaviour();
    }
    /**
     * {@inheritDoc}
     *
     * @return {@link StatefulCreatureStateId#MANNEQUIN_COLLECTOR}
     */
    @Override
    public StatefulCreatureStateId getStateId() {
        return StatefulCreatureStateId.MANNEQUIN_COLLECTOR;
    }

    /**
     * Active transitions: count workers on the eight neighbouring tiles and no-pickup streak
     * <p>
     * Transition conditions:
     * 1. {@code adjacentWorkers >= 2} transitions to idle;
     * 2. {@code adjacentWorkers == 1} transitions to berserk;
     * 3. {@code adjacentWorkers == 0} and streak ≥ 3 transitions to stalker;
     * 4. {@code adjacentWorkers == 0} and streak fewer than 3 stays in collector.
     * <p>
     * @param creature the stateful enemy
     * @param map      the map containing the creature
     * @return the state id to enter before this turn's ordinary action
     */
    @Override
    public StatefulCreatureStateId getNextState(StatefulMoonEnemy creature, GameMap map) {
        MannequinStateMemory memory = (MannequinStateMemory) creature;
        int adjacentWorkers = WorkerSensing.countAdjacentWorkers(creature, map);

        // If there are two or more adjacent workers, transition to idle.
        if (adjacentWorkers >= 2) {
            return StatefulCreatureStateId.MANNEQUIN_IDLE;
        }

        // If there is exactly one adjacent worker, transition to berserk.
        if (adjacentWorkers == 1) {
            return StatefulCreatureStateId.MANNEQUIN_BERSERK;
        }

        // If there are no adjacent workers and the no-pickup counter is greater than or equal to 3, transition to stalker.
        if (adjacentWorkers == 0 && memory.getNoPickupTurns() >= 3) {
            return StatefulCreatureStateId.MANNEQUIN_STALKER;
        }

        // If there are no adjacent workers and the no-pickup counter is less than 3, stay in collector.
        return StatefulCreatureStateId.MANNEQUIN_COLLECTOR;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Prefers a neighbour pick-up when available, otherwise wanders or does nothing.
     * Clears the no-pickup streak on a pick-up choice; when no adjacent workers and the
     * chosen action is not a pick-up, increments the streak.
     */
    @Override
    public Action getAction(StatefulMoonEnemy creature, GameMap map) {
        MannequinStateMemory memory = (MannequinStateMemory) creature;
        Location here = map.locationOf(creature);
        Action pickup = collectBehaviour.operate(creature, here);

        // If a pickup action is available, clear the no-pickup counter and return the pickup action.
        if (pickup != null) {
            memory.clearNoPickupTurns();
            return pickup;
        }

        // If no pickup action is available, wander or do nothing.
        Action move = wander.operate(creature, here);
        Action chosen = move != null ? move : new DoNothingAction();

        // If there are no adjacent workers, increment the no-pickup counter.
        if (WorkerSensing.countAdjacentWorkers(creature, map) == 0) {
            memory.incrementNoPickupTurns();
        }
        return chosen;
    }
}
