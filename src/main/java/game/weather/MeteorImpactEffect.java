package game.weather;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.grounds.Debris;
import game.grounds.Fire;
import game.interfaces.WeatherEffect;
import game.statuses.Burning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Strikes each impact site with fire, burns and displaces actors in the blast radius,
 * and destroys ground items by replacing their tiles with impassable Debris.
 *
 * @author Lim Kai Ying
 * @version 25.0.2
 */
public class MeteorImpactEffect implements WeatherEffect {

    private static final int BLAST_RADIUS = 1;
    private static final int METEOR_FIRE_DURATION = 4;
    private static final int BURNING_DURATION = 5;
    private static final int BURNING_DAMAGE = 1;

    private static final Random random = new Random();

    /**
     * Applies a primary fire impact and blast effects to each selected meteor target.
     *
     * @param targets unoccupied impact sites chosen by {@link MeteorStorm}
     * @param map     the map being affected
     */
    @Override
    public void apply(List<Location> targets, GameMap map) {
        for (Location impact : targets) {
            applyImpact(impact, map);
        }
    }

    /**
     * Ignites the impact tile, then applies blast effects on the impact site and nearby tiles.
     *
     * @param impact the meteor strike location
     * @param map    the map 
     */
    private void applyImpact(Location impact, GameMap map) {
        // remove items before fire so the crater tile is not left as debris
        clearGroundItems(impact);
        Ground previousGround = impact.getGround();
        impact.setGround(new Fire(previousGround, METEOR_FIRE_DURATION));
        new Display().println("Meteor struck " + impact + ", setting the tile on fire.");

        // blast hits the crater and every tile within BLAST_RADIUS
        affectBlastTile(impact, map);
        for (Location nearby : impact.getNearbyLocations(BLAST_RADIUS)) {
            affectBlastTile(nearby, map);
        }
    }

    /**
     * Applies burning and knockback to actors; destroys ground items into debris on other tiles.
     *
     * @param tile the tile within the blast (impact centre or neighbour)
     * @param map  the map used for actor displacement
     */
    private void affectBlastTile(Location tile, GameMap map) {
        if (tile.containsAnActor()) {
            Actor actor = tile.getActor();
            actor.addStatus(new Burning(BURNING_DURATION, BURNING_DAMAGE));
            if (knockToRandomAdjacent(actor, tile, map)) {
                new Display().println("Meteor blast burned " + actor + " at " + tile + " and knocked them aside.");
            } else {
                new Display().println("Meteor blast burned " + actor + " at " + tile + ".");
            }
        }

        // ground loot is destroyed and the tile becomes impassable debris
        if (!tile.getItems().isEmpty()) {
            clearGroundItems(tile);
            tile.setGround(new Debris());
            new Display().println("Meteor destroyed items at " + tile + ", leaving debris.");
        }
    }

    /**
     * Removes every item lying on the given tile.
     *
     * @param location the tile whose ground items should be destroyed
     */
    private void clearGroundItems(Location location) {
        // copy list so removeItem does not mutate while iterating
        for (Item item : new ArrayList<>(location.getItems())) {
            location.removeItem(item);
        }
    }

    /**
     * Moves the actor to a random adjacent passable, unoccupied tile when one exists.
     *
     * @param actor    the actor to displace
     * @param current  the actor's current location within the blast
     * @param map      the map used to perform the move
     * @return true if the actor was moved to an adjacent tile
     */
    private boolean knockToRandomAdjacent(Actor actor, Location current, GameMap map) {
        List<Location> destinations = new ArrayList<>();
        for (Exit exit : current.getExits()) {
            Location adjacent = exit.getDestination();
            // only move to passable and unoccupied tiles
            if (adjacent.canActorEnter(actor)) {
                destinations.add(adjacent);
            }
        }
        // if no destinations, do not move
        if (destinations.isEmpty()) {
            return false;
        }
        // shuffle the destinations to randomize the move
        Collections.shuffle(destinations, random);
        // move to the first destination
        map.moveActor(actor, destinations.get(0));
        return true;
    }
}
