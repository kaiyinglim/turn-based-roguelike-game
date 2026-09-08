package game.effects.stateful;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import game.loaders.WorkerSensing;
import game.interfaces.TransitionEffect;
import game.loaders.StatefulMoonEnemy;
import game.enums.StatefulCreatureStateId;
import game.statuses.Poison;

import java.util.List;

/**
 * On state entry, applies {@link Poison} to each adjacent worker.
 * Workers that already have a {@link Poison} status are skipped.
 *
 * @author Lim Kai YIng
 * @version 25.0.2
 */
public final class PoisonNearbyWorkersEffect implements TransitionEffect {

    private static final int MIN_POISON_TURNS = 1;

    private final int poisonTurns;

    /**
     * @param poisonTurns poison duration passed to {@link Poison}; clamped to at least {@link #MIN_POISON_TURNS}
     */
    public PoisonNearbyWorkersEffect(int poisonTurns) {
        this.poisonTurns = Math.max(MIN_POISON_TURNS, poisonTurns);
    }

    @Override
    public void apply(StatefulMoonEnemy creature, StatefulCreatureStateId fromState,
                      StatefulCreatureStateId toState, GameMap map) {
        if (!map.contains(creature)) {
            return;
        }

        // Get all adjacent workers
        List<Actor> adjacentWorkers = WorkerSensing.getAdjacentWorkers(creature, map);

        // Poison all workers that are not already poisoned
        int poisoned = 0;
        for (Actor worker : adjacentWorkers) {
            if (worker.hasStatus(Poison.class)) {
                continue;
            }
            // Add the poison status to the worker
            int damagePerTurn = 1;
            worker.addStatus(new Poison(poisonTurns, damagePerTurn));
            poisoned++;
        }
        
        if (poisoned > 0) {
            new Display().println(creature + " poisons " + poisoned
                    + " adjacent worker(s) for " + poisonTurns + " turn(s).");
        }
    }
}
