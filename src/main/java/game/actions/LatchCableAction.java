package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.states.cablebot.CableConnection;
import game.loaders.WorkerSensing;
import game.enemies.Cablebot;

/**
 * Attaches or strengthens an invisible {@link CableConnection} from a {@link Cablebot}
 * to an adjacent worker.
 */
public class LatchCableAction extends Action {

    private final Actor targetWorker;

    /**
     * @param targetWorker adjacent worker to latch onto (must satisfy {@link WorkerSensing#isWorkerAdjacentTo})
     */
    public LatchCableAction(Actor targetWorker) {
        this.targetWorker = targetWorker;
    }

    @Override
    public String execute(Actor actor, GameMap map) {
        Cablebot bot = (Cablebot) actor;
        if (!WorkerSensing.isWorkerAdjacentTo(bot, targetWorker, map)) {
            return bot + " fails to latch to " + targetWorker;
        }
        bot.addCableConnection(new CableConnection(targetWorker, CableConnection.DEFAULT_INITIAL_STRENGTH));
        return menuDescription(actor);
    }

    @Override
    public String menuDescription(Actor actor) {
        return actor + " latches a cable to " + targetWorker;
    }
}
