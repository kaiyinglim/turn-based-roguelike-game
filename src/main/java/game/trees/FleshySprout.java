package game.trees;

import game.behaviours.GrowingBehaviour;
import game.interfaces.EnemySpawner;

/**
 * A sprout stage of the FleshyTree that spawns Slime when actor on adjacent tiles
 * @author Heesu Kim and Bryan Chan Zheng Lam
 */
public class FleshySprout extends FleshyTreeStage {

    /**
     * Spawner responsible for generating enemies
     */
    private final EnemySpawner spawner;

    /**
     * The behaviour that manages the turn-by-turn growth mechanics
     */
    private final GrowingBehaviour<FleshyTreeStage> growingBehaviour;
    /**
     * The constructor of FleshySprout class
     */
    public FleshySprout(EnemySpawner spawner, GrowingBehaviour<FleshyTreeStage> growingBehaviour) {
        super('y');
        //Directly use the exact spawner the markers asked for
        this.spawner = spawner;

        //Delegate the growth math to the new behavior
        this.growingBehaviour = growingBehaviour;
    }

    /**
     * tries to grow every 20 turns with 25% chance, otherwise stays the same
     * @param age of the FleshyTree Flora
     * @return either sapling stage of FleshyTreeStage or the same stage
     */
    @Override
    public FleshyTreeStage tryGrow(int age) {
        if (growingBehaviour != null) {
            return growingBehaviour.grow(age, this);
        }
        return this;
    }

    /**
     * Gets the enemy spawner associated with this flora stage
     * @return this spawner, indicating an enemy spawner for this stage
     */
    @Override
    public EnemySpawner getSpawner() {
        return this.spawner;
    }
}
