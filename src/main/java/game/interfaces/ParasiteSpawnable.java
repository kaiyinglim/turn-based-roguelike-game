package game.interfaces;

import edu.monash.fit2099.engine.GameEngineException;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.effects.ParasiteSpawnEffect;
import game.enemies.Parasite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ParasiteSpawnable class is an interface class that represents the ability to spawn enemies
 * @author FIT2099 Bryan Chan Zheng Lam
 */
public interface ParasiteSpawnable {

    /**
     * Default method in which it spawns Parasite by choosing a random surrounding location
     * Also, takes the Parasite spawn effect when Parasite spawns.
     * @param location the location in which the pqrasite spawns
     * @param message the output string description when spawning Parasite
     */
    default void spawnParasite(Location location, String message) {
        List<Exit> exits = new ArrayList<>(location.getExits());
        Collections.shuffle(exits);
        for (Exit exit : exits) {
            Location destination = exit.getDestination();
            if (!destination.containsAnActor()) {
                try {
                    Parasite parasite = new Parasite();
                    destination.map().addActor(parasite, destination);
                    new ParasiteSpawnEffect().applySpawnEffect(parasite, destination);
                    new Display().println(message);
                } catch (GameEngineException e) {
                    new Display().println(e.getMessage());
                }
                break;
            }
        }
    }
}
