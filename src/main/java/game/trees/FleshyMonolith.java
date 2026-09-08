package game.trees;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.enums.WorkerAbility;
import game.interfaces.EnemySpawner;

import java.util.List;

/**
 * A last stage of the FleshyTree that acts as a teleporter.
 * Once the tree reaches this stage, it ceases growth and gains the ability
 * to violently warp adjacent workers to a random passable location on the map.
 * @author FIT2099 Bryan Chan Zheng Lam
 */
public class FleshyMonolith extends FleshyTreeStage{

    /**
     * The constructor of the FleshyMonolith
     */
    public FleshyMonolith() {
        super('H');
    }

    /**
     * Attempts to grow but since it is at its final stage, it does not grow further.
     * @param age age of the FleshyTree
     * @return this current FleshyMonolith instance, as it cannot transition to a new stage
     */
    @Override
    public FleshyTreeStage tryGrow(int age) {
        return this; //Cannot grow further
    }

    /**
     * Gets the enemy spawner associated with this flora stage
     * @return null, indicating no enemy spawner is attached to this stage
     */
    @Override
    public EnemySpawner getSpawner() {
        return null; //Does not spawn enemies
    }

    /**
     * Overrides the default stage interaction to violently warp the worker adjacent to it
     * @param actor the actor interacting or adjacent to the FleshyMonolith
     * @param floraLocation the exact location of the FleshyMonolith on the map
     */
    @Override
    public void interactWithAdjacentActor(Actor actor, Location floraLocation) {
        if (!actor.hasAbility(WorkerAbility.IS_WORKER)) return;

        GameMap map = floraLocation.map();
        int radius = Math.max(map.getXRange().max() - map.getXRange().min(), map.getYRange().max() - map.getYRange().min());
        List<Location> passable = floraLocation.getNearbyLocations(radius).stream()
                .filter(loc -> loc.canActorEnter(actor))
                .toList();

        if (!passable.isEmpty()) {
            Location randomDestination = passable.get((int) (Math.random() * passable.size()));
            map.moveActor(actor, randomDestination);
            new Display().println(actor + " was violently warped by the Fleshy Monolith!");
        }
    }
}
