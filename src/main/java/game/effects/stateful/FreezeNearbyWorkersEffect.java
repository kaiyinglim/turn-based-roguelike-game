package game.effects.stateful;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import game.loaders.WorkerSensing;
import game.interfaces.TransitionEffect;
import game.loaders.StatefulMoonEnemy;
import game.enums.StatefulCreatureStateId;
import game.statuses.Frozen;

import java.util.List;

/**
 * On entering a state, applies {@link Frozen} to every conscious adjacent worker.
 * Workers already frozen are skipped.
 *
 * @author Lim Kai YIng
 * @version 25.0.2
 */
public final class FreezeNearbyWorkersEffect implements TransitionEffect {

    private static final int MIN_FREEZE_TURNS = 1;

    private final int freezeTurns;

    /**
     * @param freezeTurns how many turns each newly frozen worker remains frozen
     */
    public FreezeNearbyWorkersEffect(int freezeTurns) {
        this.freezeTurns = Math.max(MIN_FREEZE_TURNS, freezeTurns);
    }

    /**
     * Applies the freeze nearby workers effect to the creature.
     * 
     * @param creature the creature to apply the effect to
     * @param fromState the state the creature is entering
     * @param toState the state the creature is entering
     * @param map the map the creature is on
     */
    @Override
    public void apply(StatefulMoonEnemy creature, StatefulCreatureStateId fromState,
                      StatefulCreatureStateId toState, GameMap map) {
        if (!map.contains(creature)) {
            return;
        }

        // Get all workers adjacent to the creature
        List<Actor> adjacentWorkers = WorkerSensing.getAdjacentWorkers(creature, map);

        // Freeze all workers that are not already frozen
        int frozenCount = 0;
        for (Actor worker : adjacentWorkers) {
            // Skip workers that are already frozen
            if (worker.hasStatus(Frozen.class)) {
                continue;
            }
            // Add the frozen status to the worker
            worker.addStatus(new Frozen(freezeTurns));
            frozenCount++;
        }

        if (frozenCount > 0) {
            new Display().println(creature + " freezes " + frozenCount
                    + " adjacent worker(s) for " + freezeTurns + " turn(s).");
        }
    }
}
