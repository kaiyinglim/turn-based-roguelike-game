package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.PickUpNearbyItemAction;

import java.util.ArrayList;

/**
 * Scans the eight neighbour cells around an actor for a portable item on the ground.
 * {@link Item#getPickUpAction(Actor)} is used only as a probe for whether the actor may
 * pick the item up; the returned action is {@link PickUpNearbyItemAction} so removal
 * happens on the correct tile.
 */
public class CollectBehaviour implements Behaviour<Actor, Action> {

    /**
     * Returns a pick-up action for the first portable item in the eight-neighbouring tiles.
     *
     * @param actor    the actor that would pick up
     * @param location the actor's current location (must not be null)
     * @return a pick-up action, or null
     */ 
    @Override
    public Action operate(Actor actor, Location location) {
        GameMap map = location.map();
        int cx = location.x();
        int cy = location.y();

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
                for (Item item : new ArrayList<>(there.getItems())) {
                    if (item.getPickUpAction(actor) != null) {
                        return new PickUpNearbyItemAction(item, there);
                    }
                }
            }
        }
        return null;
    }
}
