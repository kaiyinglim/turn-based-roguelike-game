package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import game.states.cablebot.CableConnection;
import game.enums.ItemStatistics;
import game.enums.WorkerAbility;
import game.enemies.Cablebot;

import java.util.ArrayList;

/**
 * Represents Cablebot overloading one active {@link CableConnection}.
 * The connected worker may be forced to drop their heaviest portable item. 
 * Cablebot then spends charge, weakens the cable, increases its strain, and removes the connection if it becomes broken.
 */
public class OverloadCableAction extends Action {

    private final CableConnection connection;

    /**
     * @param connection live cable to overload through (must still be stored on the {@link Cablebot})
     */
    public OverloadCableAction(CableConnection connection) {
        this.connection = connection;
    }

    @Override
    public String execute(Actor actor, GameMap map) {
        Cablebot bot = (Cablebot) actor;
        if (!bot.getCableConnections().contains(connection)) {
            return bot + " has no such cable";
        }

        Actor worker = connection.getTarget();
        if (!map.contains(worker) || !worker.isConscious() || !worker.hasAbility(WorkerAbility.IS_WORKER)) {
            bot.removeCableConnection(connection);
            return bot + " overload severs an invalid cable";
        }

        Item toDrop = selectHeaviestPortable(worker);
        String dropMsg;
        if (toDrop != null && worker.getInventory().remove(toDrop)) {
            map.locationOf(worker).addItem(toDrop);
            dropMsg = worker + " is forced to drop " + toDrop;
        } else {
            dropMsg = worker + " has no portable item to drop";
        }

        int cost = 1 + connection.getStrength();
        bot.decreaseCharge(cost);
        connection.weaken();
        connection.increaseStrain();
        if (connection.isBroken()) {
            bot.removeCableConnection(connection);
        }

        return dropMsg + "; " + bot + " overloads a cable (" + cost + " charge)";
    }

    /**
     * Selects the heaviest portable item carried by {@code worker}.
     * Ties keep the first in inventory iteration order.
     * 
     * @param worker the worker to select the heaviest portable item from
     * @return the heaviest portable item carried by the worker
     */
    private static Item selectHeaviestPortable(Actor worker) {
        Item best = null;
        int bestWeight = Integer.MIN_VALUE;
        for (Item item : new ArrayList<>(worker.getInventory().getItems())) {
            if (item.getDropAction(worker) == null || !item.hasStatistic(ItemStatistics.WEIGHT)) {
                continue;
            }
            int w = item.getStatistic(ItemStatistics.WEIGHT);
            if (w > bestWeight) {
                bestWeight = w;
                best = item;
            }
        }
        return best;
    }

/**
 * Returns a description of the action for the menu.
 * 
 * @param actor the actor performing the action
 * @return a description of the action
 */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " overloads a cable connection";
    }
}
