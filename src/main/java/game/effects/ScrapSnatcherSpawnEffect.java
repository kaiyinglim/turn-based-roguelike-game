package game.effects;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.interfaces.SpawnEffect;
import game.loaders.RandomScrapGenerator;

/**
 * A class that represents the spawn effect of the Scrap Snatcher when being spawned.
 * @author FIT2099 Bryan Chan Zheng Lam
 */
public class ScrapSnatcherSpawnEffect implements SpawnEffect {

    /**
     * Applies the spawn effect for the Scrap Snatcher where,
     * when it spawns, exactly one random depositable resource drops on every adjacent tile.
     * @param spawn the entity being spawned
     * @param location the location where the spawn took place
     */
    @Override
    public void applySpawnEffect(Actor spawn, Location location) {
        new Display().println("A loot explosion occurs! Scrap scatters around the " + spawn + "!");

        for (Exit exit : location.getExits()) {
            Location adjacentLocation = exit.getDestination();
            adjacentLocation.addItem(RandomScrapGenerator.getRandomScrap());
        }
    }
}
