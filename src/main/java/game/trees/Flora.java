package game.trees;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.enums.WorkerAbility;

/**
 * An abstract class that represents Flora that detects the surroundings
 * if a worker enters in each turn with handling an action once per turn
 * with an age
 * @author Heesu Kim
 */
public abstract class Flora extends Ground {
    /**
     * age of the Flora
     */
    protected int age;
    /**
     * contains a boolean value to check if flora acted this turn
     */
    private boolean actedThisTurn;

    /**
     * The constructor of Flora class
     * @param name of the Flora
     */
    public Flora(String name) {
        super('.', name);
        this.age = 0;
        this.actedThisTurn = false;
    }

    /**
     * Detects the surroundings if a worker enters in each turn and
     * performs the responsibility of each flora in each stage
     * @param location of the Flora
     */
    @Override
    public void tick(Location location) {
        age++;
        actedThisTurn = false;
        tryGrow();
        if (!hasActed()) {
            for (Exit exit : location.getExits()) {
                Location neighbour = exit.getDestination();
                if (neighbour.containsAnActor() && neighbour.getActor().hasAbility(WorkerAbility.IS_WORKER)) {
                    onActorEnteredAdjacentTile(location, neighbour.getActor());
                    break;
                }
            }
        }
    }

    /**
     * an abstract class that handles to grow to the next stage of the Flora
     */
    protected abstract void tryGrow();

    /**
     * an abstract class that handles the responsibility of the Flora in each stage
     * @param floraLocation location of the Flora
     * @param actor that entered the adjacent tiles of the Flora
     */
    protected abstract void onActorEnteredAdjacentTile(Location floraLocation, Actor actor);

    /**
     * Marks actedThisTurn to true if acted this turn and returns whether
     * it did mark or not
     * @return a boolean value whether it marked actedThisTurn true or not
     */
    protected boolean markActed() {
        if (actedThisTurn) return false;
        actedThisTurn = true;
        return true;
    }

    /**
     * Returns a boolean value whether it has acted this turn or not
     * @return a boolean value whether it has acted this turn or not
     */
    protected boolean hasActed() {
        return actedThisTurn;
    }
}