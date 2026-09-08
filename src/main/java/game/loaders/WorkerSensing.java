package game.loaders;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.enums.WorkerAbility;

import java.util.ArrayList;
import java.util.List;

/**
 * Neutral helpers for sensing {@link WorkerAbility#IS_WORKER} actors around a moon enemy.
 */
public final class WorkerSensing {

    private WorkerSensing() {
    }

    /**
     * Counts workers on the eight map cells surrounding the creature
     * (Chebyshev distance 1, excluding its own cell).
     *
     * @param creature the enemy whose neighbours are scanned
     * @param map      map containing the creature
     * @return number of qualifying workers in the eight-neighbour ring
     */
    public static int countAdjacentWorkers(StatefulMoonEnemy creature, GameMap map) {
        return getAdjacentWorkers(creature, map).size();
    }

    /**
     * Lists workers on the eight cells surrounding the creature, in deterministic scan order:
     * for {@code dx} from {@code -1} to {@code 1} and {@code dy} from {@code -1} to {@code 1},
     * skipping the creature's own cell. Order is stable for tie-breaking in behaviours.
     *
     * @param creature the enemy whose neighbours are scanned
     * @param map      map containing the creature
     * @return workers adjacent to {@code creature}, never {@code null}
     */
    public static List<Actor> getAdjacentWorkers(StatefulMoonEnemy creature, GameMap map) {
        List<Actor> workers = new ArrayList<>();
        Location here = map.locationOf(creature);
        int cx = here.x();
        int cy = here.y();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                int nx = cx + dx;
                int ny = cy + dy;
                if (!map.getXRange().contains(nx) || !map.getYRange().contains(ny)) {
                    continue;
                }
                Location there = map.at(nx, ny);
                if (there.containsAnActor()) {
                    Actor actor = there.getActor();
                    if (actor.isConscious() && actor.hasAbility(WorkerAbility.IS_WORKER)) {
                        workers.add(actor);
                    }
                }
            }
        }
        return workers;
    }

    /**
     * @param creature moon enemy
     * @param worker   candidate worker
     * @param map      map containing both
     * @return true if {@code worker} is on the map, conscious, a worker, and Chebyshev-adjacent to {@code creature}
     */
    public static boolean isWorkerAdjacentTo(StatefulMoonEnemy creature, Actor worker, GameMap map) {
        if (worker == null || !map.contains(worker) || !worker.isConscious()
                || !worker.hasAbility(WorkerAbility.IS_WORKER)) {
            return false;
        }
        Location a = map.locationOf(creature);
        Location b = map.locationOf(worker);
        int distX = Math.abs(a.x() - b.x());
        int distY = Math.abs(a.y() - b.y());
        return distX <= 1 && distY <= 1 && !(distX == 0 && distY == 0);
    }
}
