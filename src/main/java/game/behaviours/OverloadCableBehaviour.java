package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.positions.Location;
import game.states.cablebot.CableConnection;
import game.actions.OverloadCableAction;
import game.enemies.Cablebot;

/**
 * Chooses the active cable connection to overload.
 * The connection attached for the longest time is selected. 
 * Ties are resolved by keeping the first connection in {@link Cablebot#getCableConnections()} order.
 */
public class OverloadCableBehaviour implements Behaviour<Cablebot, Action> {

    @Override
    public Action operate(Cablebot bot, Location location) {
        CableConnection chosen = null;
        int bestTurns = -1;

        // Find the connection attached for the longest time.   
        for (CableConnection c : bot.getCableConnections()) {
            int t = c.getTurnsAttached();
            if (t > bestTurns) {
                bestTurns = t;
                chosen = c;
            }
        }

        // If no connection is chosen, return a do-nothing action.
        if (chosen == null) {
            return new DoNothingAction();
        }

        // Otherwise, return an overload cable action for the chosen connection.
        return new OverloadCableAction(chosen);
    }
}
