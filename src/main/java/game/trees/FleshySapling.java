package game.trees;

import game.behaviours.GrowingBehaviour;
import game.interfaces.EnemySpawner;

/**
 * A sapling stage of the FleshyTree that does nothing
 * @author Heesu Kim and Bryan Chan Zheng Lam
 */
public class FleshySapling extends FleshyTreeStage {

    private final GrowingBehaviour<FleshyTreeStage> growingBehaviour;
    /**
     * The constructor of FleshySapling class
     */
    public FleshySapling(GrowingBehaviour<FleshyTreeStage> growingBehaviour) {
        super('v');
        //Delegate the growth math to the new behavior
        this.growingBehaviour = growingBehaviour;
    }

    /**
     * tries to grow every 25 turns with 50% chance, otherwise stays the same
     * @param age of the FleshyTree Flora
     * @return either mature stage of FleshyTreeStage or the same stage
     */
    @Override
    public FleshyTreeStage tryGrow(int age) {
        // The behavior handles the math and returns the new stage if successful
        if (growingBehaviour != null) {
            return growingBehaviour.grow(age, this);
        }
        return this;
    }

    /**
     * returns corresponding Spawner based on the stage of the FleshyTree
     * @return null since Sapling of FleshyTree does nothing
     */
    @Override
    public EnemySpawner getSpawner() {
        return null;
    }
}
