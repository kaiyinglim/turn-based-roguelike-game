package game.trees;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;

/**
 * A WarperTree of the Flora that holds a stage to represent the stage
 * of the WarperTree and handles to try to grow into the next stage
 * @author Heesu Kim
 */
public class WarperTree extends Flora {
    /**
     * Stage of the WarperTree
     */
    private WarperTreeStage stage;

    /**
     * The constructor of WarperTree class with an initial Sapling stage
     */
    public WarperTree() {
        super("Warper Tree");
        this.stage = new WarperSapling();
    }

    /**
     * Returns false because an Actor cannot enter WarperTree
     * @param actor the Actor who might be moving
     * @return false because an Actor cannot enter WarperTree
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        return false;
    }

    /**
     * Returns a character to display on the map of the game
     * @return a character to display on the map of the game
     */
    @Override
    public char getDisplayChar() {
        return stage.displayChar;
    }

    /**
     * Tries to grow into the next stage of the WarperTree
     */
    @Override
    protected void tryGrow() {
        WarperTreeStage next = stage.tryGrow(age);
        if (next != stage) {
            stage = next;
            markActed();
        }
    }

    /**
     * Calls onActorAdjacent method from the stage when the worker entered
     * its adjacent tiles of the WarperTree and also perform only once per turn
     * @param floraLocation location of the WarperTree
     * @param actor an actor that entered the surroundings of the WarperTree
     */
    @Override
    public void onActorEnteredAdjacentTile(Location floraLocation, Actor actor) {
        if (hasActed()) return;
        if (stage.onActorAdjacent(actor, floraLocation)) {
            markActed();
        }
    }
}