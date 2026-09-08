package game.trees;

import game.behaviours.GrowingBehaviour;
import game.interfaces.EnemySpawner;

/**
 * A mature stage of the FleshyTree that spawns Undead when actor on adjacent tiles
 * @author Heesu Kim and Bryan Chan Zheng Lam
 */
public class FleshyMatureTree extends FleshyTreeStage {

    /**
     * Spawner responsible for generating enemies
     */
    private final EnemySpawner spawner;

    /**
     * The behaviour that manages the turn-by-turn growth mechanics
     */
    private final GrowingBehaviour<FleshyTreeStage> growingBehaviour;

    /**
     * The constructor of FleshyMatureTree class
     */
    public FleshyMatureTree(EnemySpawner spawner, GrowingBehaviour<FleshyTreeStage> growingBehaviour) {
        super('Y');
        //Directly use the exact spawner the markers asked for
        this.spawner = spawner;
        this.growingBehaviour = growingBehaviour;
    }

    /**
     * Mo further grows from Mature stage of the FleshyTree
     * unless from the deprecated map, then it continues to grow
     * @param age of the FleshyTree Flora
     * @return this FleshyMatureTree object
     */
    @Override
    public FleshyTreeStage tryGrow(int age) {
        if (growingBehaviour != null) {
            return growingBehaviour.grow(age, this);
        }
        return this; // Stays mature forever
    }

    /**
     * Returns corresponding Spawner based on the stage of the FleshyTree
     * @return matureSpawner spawner that spawns in the Mature stage of the FleshyTree
     */
    @Override
    public EnemySpawner getSpawner() {
        return this.spawner;
    }
}
