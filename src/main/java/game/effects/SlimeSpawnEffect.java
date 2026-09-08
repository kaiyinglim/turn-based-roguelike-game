package game.effects;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.enums.WorkerAbility;
import game.interfaces.SpawnEffect;

import java.util.ArrayList;

/**
 * A class that represents the spawn effect of the Slime when being spawned
 * @author FIT2099 Bryan Chan Zheng Lam
 */
public class SlimeSpawnEffect implements SpawnEffect {

    /**
     * Applies the spawn effect for slime where,
     * when slime spawns, all workers in its adjacent tiles,
     * will drop all the items they are carrying in the inventory onto the ground.
     * @param spawn the spawn where it took place
     * @param location the location where it took place
     */
    @Override
    public void applySpawnEffect(Actor spawn, Location location) {
        for (Exit exit : location.getExits()) {
            Location surroundingLocation = exit.getDestination();
            if (surroundingLocation.containsAnActor()) {
                Actor actor = surroundingLocation.getActor();
                if (actor.hasAbility(WorkerAbility.IS_WORKER)) {
                    // drop all items onto the ground
                    for (Item item : new ArrayList<>(actor.getInventory().getItems())) {
                        actor.getInventory().remove(item);
                        surroundingLocation.addItem(item);
                        new Display().println(actor + " drops " + item + "!");
                    }
                }
            }
        }
    }
}
