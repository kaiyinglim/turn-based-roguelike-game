package game.states.mannequin;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.behaviours.AttackBehaviour;
import game.enums.WorkerAbility;
import game.loaders.StatefulMoonEnemy;
import game.interfaces.CreatureState;
import game.enums.StatefulCreatureStateId;
import game.loaders.WorkerSensing;

/**
 * Mannequin berserk: crowd returns to idle, isolated adjacent worker is attacked via
 * {@link AttackBehaviour}, no adjacent worker transitions to stalker.
 *
 * @author Lim Kai YIng
 * @version 25.0.2
 */
public final class MannequinBerserkState implements CreatureState {

    private final AttackBehaviour attackBehaviour = new AttackBehaviour(WorkerAbility.IS_WORKER);

    /**
     * {@inheritDoc}
     *
     * @return {@link StatefulCreatureStateId#MANNEQUIN_BERSERK}
     */
    @Override
    public StatefulCreatureStateId getStateId() {
        return StatefulCreatureStateId.MANNEQUIN_BERSERK;
    }

    /**
     * Berserk transitions: count workers on the eight neighbouring tiles
     * Transition conditions:
     * 1. {@code adjacentWorkers >= 2} transitions to idle;
     * 2. {@code adjacentWorkers == 0} transitions to stalker;
     * 3. {@code adjacentWorkers == 1} stays in berserk.
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

        // If there are no adjacent workers, transition to stalker to find new target.
        if (adjacentWorkers == 0) {
            return StatefulCreatureStateId.MANNEQUIN_STALKER;
        }

        // If there is exactly one adjacent worker, stay in berserk.
        return StatefulCreatureStateId.MANNEQUIN_BERSERK;
    }


    /**
     * While berserk, the Mannequin attacks an adjacent worker.
     * If no valid attack is available, it does nothing as a safety fallback.
     *
     * @param creature the stateful enemy performing the action
     * @param map      the map containing the creature
     * @return attack action if possible, otherwise {@link DoNothingAction}
     */
    @Override
    public Action getAction(StatefulMoonEnemy creature, GameMap map) {
        Location here = map.locationOf(creature);

        Action attack = attackBehaviour.operate(creature, here);

        if (attack != null) {
            return attack;
        }

        return new DoNothingAction();
    }
}
