package game.trees;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.interfaces.EnemySpawner;

/**
 * A FleshyTree of the Flora that holds a stage and spawners for some stages
 * and handles to spawn enemies on adjacent tiles
 * @author Heesu Kim and Bryan Chan Zheng Lam
 */
public class FleshyTree extends Flora {
    /**
     * stage of the FleshyTree
     */
    private FleshyTreeStage stage;

    /**
     * The constructor of FleshySapling class
     */
    public FleshyTree(FleshyTreeStage initialStage) {
        super("Fleshy Tree");
        this.stage = initialStage;
    }

    /**
     * Returns false because an Actor cannot enter FleshyTree
     * @param actor the Actor who might be moving
     * @return false because an Actor cannot enter FleshyTree
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
     * Tries to grow into the next stage of the FleshyTree
     */
    @Override
    protected void tryGrow() {
        FleshyTreeStage next = stage.tryGrow(age);
        if (next != stage) {
            stage = next;
            markActed();
        }
    }

    /**
     * Spawns an enemy in the surroundings if it can enter and no actor standing there
     * @param floraLocation location of the FleshyTree
     * @param actor an actor that entered the surroundings of the FleshyTree
     */
    @Override
    public void onActorEnteredAdjacentTile(Location floraLocation, Actor actor) {
        if (hasActed()) return;

        //Stage-specific logic. (eg. teleport)
        stage.interactWithAdjacentActor(actor, floraLocation);

        //Spawning logic
        EnemySpawner spawner = stage.getSpawner();
        if (spawner != null) {
            if (markActed()) {
                Actor spawned = spawner.spawnEnemy();
                for (Exit exit : floraLocation.getExits()) {
                    Location neighbour = exit.getDestination();
                    if (!neighbour.containsAnActor() && neighbour.getGround().canActorEnter(spawned)) {
                        try {
                            neighbour.addActor(spawned);
                            spawner.getSpawnEffect().applySpawnEffect(spawned, floraLocation);
                            break;
                        } catch (Exception ignored) {
                        }
                    }
                }
            }
        }
    }
}