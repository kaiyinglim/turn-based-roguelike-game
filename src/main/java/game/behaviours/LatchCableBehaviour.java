package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.ActorStatistics;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.loaders.WorkerSensing;
import game.actions.LatchCableAction;
import game.enums.WorkerAbility;
import game.enemies.Cablebot;

/**
 * Picks a deterministic adjacent worker and returns a {@link LatchCableAction} 
 * to attach or strengthen a cable via {@link Cablebot#addCableConnection}.
 * <p>
 * Scoring: {@code missingHealth + isolatedWorkerBonus - alreadyAttachedPenalty}. Ties keep the first worker
 * in {@link WorkerSensing#getAdjacentWorkers} scan order.
 */
public class LatchCableBehaviour implements Behaviour<Cablebot, Action> {

    private static final int ISOLATED_BONUS = 1;
    private static final int ALREADY_ATTACHED_PENALTY = 2; // to prefer workers not already cabled to

    @Override
    public Action operate(Cablebot bot, Location location) {
        GameMap map = location.map();
        int bestScore = Integer.MIN_VALUE;
        Actor chosen = null;
        for (Actor worker : WorkerSensing.getAdjacentWorkers(bot, map)) {
            int score = scoreWorker(bot, worker, map);
            if (score > bestScore) {
                bestScore = score;
                chosen = worker;
            }
        }
        if (chosen == null) {
            return new DoNothingAction();
        }
        return new LatchCableAction(chosen);
    }

    private static int scoreWorker(Cablebot bot, Actor worker, GameMap map) {
        int maxHp = worker.getMaximumStatistic(ActorStatistics.HEALTH);
        int hp = worker.getStatistic(ActorStatistics.HEALTH);
        int missingHealth = maxHp - hp;
        int isolatedBonus = countAdjacentWorkers(worker, map) == 0 ? ISOLATED_BONUS : 0;
        int penalty = bot.hasConnectionTo(worker) ? ALREADY_ATTACHED_PENALTY : 0;
        return missingHealth + isolatedBonus - penalty;
    }

    /**
     * Counts other workers in the eight-neighbour ring around {@code worker} (excluding {@code worker}'s cell).
     */
    private static int countAdjacentWorkers(Actor worker, GameMap map) {
        Location here = map.locationOf(worker);
        int cx = here.x();
        int cy = here.y();
        int count = 0;
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
                if (map.at(nx, ny).containsAnActor()) {
                    Actor a = map.at(nx, ny).getActor();
                    if (a != worker && a.hasAbility(WorkerAbility.IS_WORKER)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
}
