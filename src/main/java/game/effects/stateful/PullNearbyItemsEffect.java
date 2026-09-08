package game.effects.stateful;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.interfaces.TransitionEffect;
import game.loaders.StatefulMoonEnemy;
import game.enums.StatefulCreatureStateId;

import java.util.ArrayList;
import java.util.List;

/**
 * Transition effect that pulls the first portable item found from a nearby tile
 * onto the stateful creature's current tile.
 *
 * @author Lim Kai YIng
 * @version 25.0.2
 */
public final class PullNearbyItemsEffect implements TransitionEffect {

    /** Chebyshev distance 1: the eight neighbouring tiles around the creature. */
    private static final int NEIGHBOUR_RADIUS = 1;

    @Override
    public void apply(StatefulMoonEnemy creature,
                      StatefulCreatureStateId fromState,
                      StatefulCreatureStateId toState,
                      GameMap map) {

        if (!map.contains(creature)) {
            return;
        }

        // Get the location of the creature
        Location creatureLocation = map.locationOf(creature);

        int creatureX = creatureLocation.x();
        int creatureY = creatureLocation.y();

        // Scan neighbours in row-major (x then y) order so the chosen item is deterministic.
        for (int x = creatureX - NEIGHBOUR_RADIUS; x <= creatureX + NEIGHBOUR_RADIUS; x++) {
            for (int y = creatureY - NEIGHBOUR_RADIUS; y <= creatureY + NEIGHBOUR_RADIUS; y++) {

                if (x == creatureX && y == creatureY) {
                    continue; // Skip the creature's own cell
                }

                if (!map.getXRange().contains(x) || !map.getYRange().contains(y)) {
                    continue; // Skip tiles outside the map
                }

                Location nearbyLocation = map.at(x, y);

                // Copy the items so removing while iterating does not break the backing list.
                List<Item> items = new ArrayList<>(nearbyLocation.getItems());

                // Pick up the first item that can be picked up
                for (Item item : items) {
                    if (item.getPickUpAction(creature) != null) {
                        nearbyLocation.removeItem(item);
                        creatureLocation.addItem(item);

                        new Display().println(creature + " pulls " + item + " to its feet.");
                        return;
                    }
                }
            }
        }
    }
}
