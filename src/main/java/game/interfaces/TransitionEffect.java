package game.interfaces;

import edu.monash.fit2099.engine.positions.GameMap;
import game.loaders.StatefulMoonEnemy;
import game.enums.StatefulCreatureStateId;

/**
 * Effect executed when a {@link StatefulMoonEnemy} enters a new state.
 *
 * @author Lim Kai YIng
 * @version 25.0.2
 */
public interface TransitionEffect {

    /**
     * Applies the effects of entering {@code toState} from {@code fromState}.
     *
     * @param creature  the enemy undergoing the transition
     * @param fromState the behavioural mode being exited
     * @param toState   the behavioural mode being entered (redundant with lookup key but
     *                  kept for symmetric, self-documenting call sites)
     * @param map       the map hosting the creature
     */
    void apply(StatefulMoonEnemy creature, StatefulCreatureStateId fromState, StatefulCreatureStateId toState,
               GameMap map);
}
