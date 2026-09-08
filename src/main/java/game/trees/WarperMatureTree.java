package game.trees;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;

import java.util.List;

/**
 * A WarperMatureTree that represents the Mature tree stage of the WarperTree
 * which handles to warp the worker when it is on adjacent tiles
 * @author Heesu Kim
 */
public class WarperMatureTree extends WarperTreeStage {
    /**
     * The constructor of WarperMatureTree class
     */
    public WarperMatureTree() {
        super('W');
    }

    /**
     * no further grows from Mature stage of the WarperTree
     * @param age of the WarperTree Flora
     * @return this WarperMatureTree object
     */
    @Override
    public WarperTreeStage tryGrow(int age) {
        return this;
    }

    /**
     * Warps a worker on adjacent tiles to a random location of the map
     * @param floraLocation location of the WarperTree
     * @param actor an actor that entered the surroundings of the WarperTree
     * @return a boolean value whether it successfully warped a worker or not
     */
    @Override
    public boolean onActorAdjacent(Actor actor, Location floraLocation) {
        GameMap map = floraLocation.map();
        int radius = Math.max(
                map.getXRange().max() - map.getXRange().min(),
                map.getYRange().max() - map.getYRange().min()
        );
        List<Location> passable = floraLocation.getNearbyLocations(radius)
                .stream()
                .filter(loc -> loc.canActorEnter(actor))
                .toList();

        if (!passable.isEmpty()) {
            map.moveActor(actor, passable.get(random.nextInt(passable.size())));
            return true;
        }
        return false;
    }
}
