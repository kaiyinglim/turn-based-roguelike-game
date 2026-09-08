package game.trees;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;
import game.interfaces.EnemySpawner;

/**
 * A FleshyTreeStage that represents each stage holding common attributes among
 * the stages and abstract methods for consistency
 * @author Heesu Kim and Bryan Chan Zheng Lam
 */
public abstract class FleshyTreeStage {
    /**
     * a character to be displayed on the map
     */
    public final char displayChar;

    /**
     * The constructor of FleshyTreeStage class
     * @param displayChar a character to be displayed on the map
     */
    protected FleshyTreeStage(char displayChar) {
        this.displayChar = displayChar;
    }

    /**
     * an abstract method of FleshyTreeStage abstract class to grow
     * into the next stage in each stage
     * @param age age of the FleshyTree
     */
    public abstract FleshyTreeStage tryGrow(int age);
    /**
     * an abstract method of FleshyTreeStage abstract class to get
     * the spawner in each stage
     */
    public abstract EnemySpawner getSpawner();

    public void interactWithAdjacentActor(Actor actor, Location location) {
        // Default behavior does nothing. Override in special stages like FleshyMonolith
    }
}
