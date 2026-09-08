package game.trees;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;
import game.behaviours.GrowingBehaviour;

/**
 * A WarperSapling that represents the Sapling stage of the WarperTree
 * which does notihng except to try to grow
 * @author Heesu Kim
 */
public class WarperSapling extends WarperTreeStage {

    private final GrowingBehaviour<WarperTreeStage> growingBehaviour;
    /**
     * The constructor of WarperSapling class
     */
    public WarperSapling() {
        super('w');
        this.growingBehaviour = new GrowingBehaviour<>(20, 0.25, WarperMatureTree::new);
    }

    /**
     * tries to grow every 20 turns with 25% chance, otherwise stays the same
     * @param age of the WarperTree Flora
     * @return either mature stage of WarperTreeStage or the same stage
     */
    @Override
    public WarperTreeStage tryGrow(int age) {
        return growingBehaviour.grow(age, this);
    }

    /**
     * does nothing when actor entered adjacent tiles of the Sapling stage
     * of the WarperTree and returns false
     * @param actor of the WarperTree Flora
     * @param floraLocation of the WarperTree Flora
     * @return always false
     */
    @Override
    public boolean onActorAdjacent(Actor actor, Location floraLocation) {
        return false;
    }
}
