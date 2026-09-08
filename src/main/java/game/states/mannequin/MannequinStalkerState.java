package game.states.mannequin;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.behaviours.StalkBehaviour;
import game.enums.WorkerAbility;
import game.loaders.StatefulMoonEnemy;
import game.interfaces.CreatureState;
import game.enums.StatefulCreatureStateId;
import game.loaders.WorkerSensing;

/**
 * Mannequin stalker mode: transitions on adjacent worker count like other mannequin
 * states; while stalking, takes one greedy step toward the nearest worker.
 *
 * @author Lim Kai YIng
 * @version 25.0.2
 */
public final class MannequinStalkerState implements CreatureState {

    private final StalkBehaviour stalkBehaviour = new StalkBehaviour(WorkerAbility.IS_WORKER);

    /**
     * {@inheritDoc}
     *
     * @return {@link StatefulCreatureStateId#MANNEQUIN_STALKER}
     */
    @Override
    public StatefulCreatureStateId getStateId() {
        return StatefulCreatureStateId.MANNEQUIN_STALKER;
    }

    /**
     * Stalker transitions: count workers on the eight neighbouring tiles
     * Transition conditions:
     * 1. {@code adjacentWorkers >= 2} transitions to idle;
     * 2. {@code adjacentWorkers == 1} transitions to berserk;
     * 3. {@code adjacentWorkers == 0} stays in stalker.
     *
     * @param creature the stateful enemy
     * @param map      the map containing the creature
     * @return the state id to enter before this turn's ordinary action
     */
    @Override
    public StatefulCreatureStateId getNextState(StatefulMoonEnemy creature, GameMap map) {
        int adjacentWorkers = WorkerSensing.countAdjacentWorkers(creature, map);

        // If there are two or more adjacent workers, transition to idle.
        if (adjacentWorkers >= 2) {
            return StatefulCreatureStateId.MANNEQUIN_IDLE;
        }

        // If there is exactly one adjacent worker, transition to berserk.
        if (adjacentWorkers == 1) {
            return StatefulCreatureStateId.MANNEQUIN_BERSERK;
        }

        // If there are no adjacent workers, stay in stalker.
        return StatefulCreatureStateId.MANNEQUIN_STALKER;
    }

    /**
     * Moves one tile toward the nearest worker when possible; otherwise does nothing.
     *
     * @param creature the stateful enemy performing the action
     * @param map      the map containing the creature
     * @return a move toward the nearest worker or {@link DoNothingAction}
     */
    @Override
    public Action getAction(StatefulMoonEnemy creature, GameMap map) {
        Location here = map.locationOf(creature);
        Action move = stalkBehaviour.operate(creature, here);

        // if a move is found, return it, otherwise return a do nothing action
        return move != null ? move : new DoNothingAction();
    }
}
