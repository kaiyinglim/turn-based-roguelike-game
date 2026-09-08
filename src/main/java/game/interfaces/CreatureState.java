package game.interfaces;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.positions.GameMap;
import game.enums.StatefulCreatureStateId;
import game.loaders.StatefulMoonEnemy;

/**
 * Encapsulates deterministic transition rules and per-turn behaviour for one
 * behavioural mode of a {@link StatefulMoonEnemy}. Implementations follow the
 * Single Responsibility Principle by owning only the logic for their own state.
 *
 * @author Lim Kai YIng
 * @version 25.0.2
 */
public interface CreatureState {

    /**
     * Returns the identifier used to register this state inside the creature's
     * state table.
     *
     * @return the stable enum key for this state
     */
    StatefulCreatureStateId getStateId();

    /**
     * Computes the next behavioural state based on the current map context.
     * Implementations must be deterministic and must not use randomness to pick
     * among valid successor states.
     *
     * @param creature the stateful enemy evaluating its transition
     * @param map      the map that contains the creature
     * @return the identifier of the state to enter before executing this turn's
     *         ordinary action
     */
    StatefulCreatureStateId getNextState(StatefulMoonEnemy creature, GameMap map);

    /**
     * Produces the ordinary (non-transition) action for the creature while it
     * remains in this state after any transition effects have already fired.
     *
     * @param creature the stateful enemy performing its turn
     * @param map      the map that contains the creature
     * @return the action the engine should execute for this actor this turn
     */
    Action getAction(StatefulMoonEnemy creature, GameMap map);
}
