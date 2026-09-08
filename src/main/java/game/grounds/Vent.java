package game.grounds;

import edu.monash.fit2099.engine.GameEngineException;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.effects.UndeadSpawnEffect;
import game.enemies.Undead;
import game.enums.ItemStatistics;
import game.enums.WorkerAbility;
import game.interfaces.Cuttable;
import game.interfaces.EnemySpawner;
import game.items.IndustrialFan;
import game.loaders.ContractedWorker;
import game.statuses.Poison;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Its purpose is to be a vent that spawns in enemies to the moon.
 * @author FIT2099 Bryan Chan Zheng Lam
 */
public class Vent extends Ground implements Cuttable {

    /**
     * The amount of turns it takes for enemies to spawn
     */
    private static final int SPAWN_TURNS = 2;

    /**
     * The count number for turns
     */
    private int turn_count = 0;

    /**
     * The amount of turns the poison has
     */
    private static final int POISON_TURNS = 5;
    /**
     * The spawner responsible for deciding which enemy to spawn
     * dependency injection via constructor, allowing different spawners to be used
     * without modifying Vent class
     */
    private final EnemySpawner spawner;

    /**
     * The constructor of the Vent class
     * @param spawner the spawn where enemy resides
     */
    public Vent(EnemySpawner spawner) {
        super('V', "Vent");
        this.spawner = spawner;
        this.addNewStatistic(ItemStatistics.CUTTABLE, new BaseStatistic(1));
    }

    /**
     * This method checks the amount of turns it takes for enemies to spawn
     * and will spawn a new enemy.
     * @param location The location of the Ground
     */
    @Override
    public void tick(Location location) {
        // check if worker is in adjacent tiles
        if (isWorkerNearby(location)) {
            spawnCreature(location);
        }
    }

    /**
     * Checks if a worker is adjacent to that specific location
     * @param location the location in which the worker is on
     * @return true if worker is in the surrounding area, false if worker is not in the surrounding area
     */
    public boolean isWorkerNearby(Location location) {
        for (Exit exit : location.getExits()) {
            Location surroundingLocation = exit.getDestination();
            if (surroundingLocation.containsAnActor()) {
                Actor actor = surroundingLocation.getActor();
                if (actor.hasAbility(WorkerAbility.IS_WORKER)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Spawns the creature from the vent,
     * When spawning applies the spawning effect,
     * Poisons all adjacent actors including the spawn creature
     * @param location the location of the spawn creature
     */
    public void spawnCreature(Location location) {
        List<Exit> exits = new ArrayList<>(location.getExits());
        Collections.shuffle(exits);
        for (Exit exit : exits) {
            Location surroundingLocation = exit.getDestination();
            if (!surroundingLocation.containsAnActor()) {
                try {
                    // spawn creature
                    Actor creature = spawner.spawnEnemy();
                    location.map().addActor(creature, surroundingLocation);
                    new Display().println("A " + creature
                            + " has spawned from the vent!");

                    // apply spawn effect
                    spawner.getSpawnEffect().applySpawnEffect(creature, surroundingLocation);

                    // poison all adjacent actors including spawned creature
                    poisonAdjacentActors(location);

                } catch (GameEngineException e) {
                    new Display().println(e.getMessage());
                }
                break;
            }
        }
    }

    /**
     * Poison all adjacent actors from the Vent
     * @param location the location in which it poisons the actors
     */
    public void poisonAdjacentActors(Location location) {
        for (Exit exit : location.getExits()) {
            Location surroundingLocation = exit.getDestination();
            if (surroundingLocation.containsAnActor()) {
                Actor actor = surroundingLocation.getActor();
                actor.addStatus(new Poison(POISON_TURNS, 1));
                new Display().println(actor + " is poisoned by the vent for "
                        + POISON_TURNS + " turns!");
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

    /**
     * Applies the effect of cutting open the Vent.
     * An Industrial Fan is produced and the Vent is replaced
     * with a Floor. An Undead may also emerge from the opening.
     *
     * @param worker the worker performing the cutting action
     * @param map the map where the action occurs
     * @param targetLocation the location of the Vent
     * @return a description of the cutting outcome
     */
    @Override
    public String cutBy(ContractedWorker worker, GameMap map, Location targetLocation) {
        targetLocation.addItem(new IndustrialFan());
        targetLocation.setGround(new Floor());

        Undead undead = new Undead();
        try {
            if (!targetLocation.containsAnActor() && targetLocation.canActorEnter(undead)) {
                map.addActor(undead, targetLocation);
                new UndeadSpawnEffect().applySpawnEffect(undead, targetLocation);
                return worker + " cut open the Vent. An Industrial Fan drops out and an Undead crawls from the opening.";
            }
        } catch (GameEngineException e) {
            return worker + " cut open the Vent. An Industrial Fan drops out, but the Undead could not spawn.";
        }
        return worker + " cut open the Vent. An Industrial Fan drops out, but there is no room for the Undead.";
    }
}
