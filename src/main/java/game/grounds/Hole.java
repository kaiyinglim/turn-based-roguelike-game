package game.grounds;

import edu.monash.fit2099.engine.GameEngineException;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.interfaces.EnemySpawner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Its purpose is to be a hole that spawns in enemies to the moon.
 * @author FIT2099 Bryan Chan Zheng Lam
 */
public class Hole extends Ground {

    /**
     * The amount of turns it takes for enemies to spawn
     */
    private static final int SPAWN_TURNS = 20;

    /**
     * The chance to expand the hole
     */
    private static final double CHANCE_TO_EXPAND = 0.01;

    /**
     * The count number for turns
     */
    private int turn_count = 0;

    /**
     * The spawner responsible for deciding which enemy to spawn
     * dependency injection via constructor, allowing different spawners to be used
     * without modifying Hole class
     */
    private final EnemySpawner spawner;

    /**
     * The constructor of the Hole class
     * @param spawner the spawn where enemy resides
     */
    public Hole(EnemySpawner spawner) {
        super('o', "Hole");
        this.spawner = spawner;
    }

    /**
     * This method checks the amount of turns it takes for enemies to spawn and will spawn a new enemy.
     * Applies a spawn effect for different creatures.
     * 1% chance to expand the hole every time a creature spawns.
     * @param location The location of the Ground
     */
    @Override
    public void tick(Location location) {
        turn_count++;
        if (turn_count >= SPAWN_TURNS && !location.containsAnActor()) {
            try {
                // spawn creature
                Actor creature = spawner.spawnEnemy();
                location.map().addActor(creature, location);
                new Display().println("A " + creature + " has spawned from the hole!");

                // apply spawn effect
                spawner.getSpawnEffect().applySpawnEffect(creature, location);

                // reset counter
                turn_count = 0;

                // 1% chance to expand hole to adjacent tile
                if (Math.random() < CHANCE_TO_EXPAND) {
                    expandHole(location);
                }

            } catch (GameEngineException e) {
                new Display().println(e.getMessage());
            }
        }
    }

    /**
     * Expands the Hole by choosing a random, available tile and creates a new Hole
     * @param location The location in which to spawn the Hole
     */
    public void expandHole(Location location) {
        List<Exit> exits = new ArrayList<>(location.getExits());
        Collections.shuffle(exits);
        for (Exit exit : exits) {
            Location destination = exit.getDestination();
            if (!destination.containsAnActor()) {
                // new hole inherits same spawner as parent
                destination.setGround(new Hole(spawner));
                new Display().println("The hole expands to " + destination + "!");
                break;
            }
        }
    }

    /**
     * Checks if actor can enter or not enter.
     * @param actor the Actor to check
     * @return true for actor to enter, false for actor to not enter
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        return false;
    }
}
