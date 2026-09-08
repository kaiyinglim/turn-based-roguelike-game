package game.trees;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;
import java.util.Random;

/**
 * A WarperTreeStage that represents each stage holding common attributes among
 * the stages and abstract methods for consistency
 * @author Heesu Kim
 */
public abstract class WarperTreeStage {
    /**
     * a character to be displayed on the map
     */
    public final char displayChar;
    /**
     * a random object to calculate the chance
     */
    protected static final Random random = new Random();

    /**
     * The constructor of WarperTreeStage class
     * @param displayChar a character to be displayed on the map
     */
    protected WarperTreeStage(char displayChar) {
        this.displayChar = displayChar;
    }

    /**
     * an abstract method of WarperTreeStage abstract class to grow
     * into the next stage in each stage
     * @param age age of the FleshyTree
     */
    public abstract WarperTreeStage tryGrow(int age);
    /**
     * an abstract method of WarperTreeStage abstract class to perform
     * the responsibility in each stage of the WarperTree
     * @param actor that entered the adjacent tiles of the WarperTree
     * @param floraLocation a location of the Flora in the map
     */
    public abstract boolean onActorAdjacent(Actor actor, Location floraLocation);
}