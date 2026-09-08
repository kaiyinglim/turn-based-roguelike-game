package game.items;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.systems.RandomGround;
import game.actions.SellAction;
import game.actions.TeleportAction;
import game.enemies.Undead;
import game.enums.ItemStatistics;
import game.interfaces.Cuttable;
import game.grounds.ToxicWaste;
import game.interfaces.Sellable;
import game.interfaces.Teleportation;
import game.loaders.ContractedWorker;
import game.effects.UndeadSpawnEffect;
import game.statuses.Poison;

/**
 * These 3 cubes could represent anything,
 * past, present, and future.
 * Red, green, and blue.
 * Three random destinations on the map you could teleport too.
 */
public class AlienCubes extends Item implements Teleportation, Sellable, Cuttable {
    /**
     * The constructor for AlienCubes
     */

    public AlienCubes() {
        super("Alien Cubes", '◈');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(3));
        this.addNewStatistic(ItemStatistics.SELLABLE, new BaseStatistic(1));
        this.addNewStatistic(ItemStatistics.CUTTABLE, new BaseStatistic(1));
        this.makePortable();
    }

    /**
     * Teleports the actor to the specified coordinates
     *
     * @param actor        the actor that is teleporting
     * @param map          the GameMap where the teleportation occurs
     * @param x_coordinate the x-coordinate of the destination
     * @param y_coordinate the y-coordinate of the destination
     * @return a message indicating the successful teleportation
     */
    @Override
    public String teleport(Actor actor, GameMap map, int x_coordinate, int y_coordinate) {
        Location location = map.locationOf(actor);
        Location actorLocation = null;
        for (Exit exit : location.getExits()) {
            Location neighbour = exit.getDestination();
            if (neighbour.canActorEnter(actor)) {
                neighbour.setGround(new ToxicWaste());
            }
        }
        map.moveActor(actor, map.at(x_coordinate, y_coordinate));
        return actor + " teleports to " + "(" + x_coordinate + ", " + y_coordinate + ")";
    }

    /**
     * Gives the 3 possible destinations for the teleport using alien cubes
     *
     * @param owner the actor that owns the item
     * @param map   the map where the actor is performing the action on
     * @return the action list of the alien cubes item
     */
    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        ActionList actions = new ActionList();
        for (int i = 0; i <3; i++){
            RandomGround coordinates = new RandomGround();
            coordinates.RandomAccessibleGround(map);
            actions.add(new TeleportAction(this, coordinates.getx_cord(), coordinates.gety_cord()));
        }
        return actions;
    }

    /**
     * The sell price of the alien cubes
     *
     * @return the price in credits
     */
    @Override
    public int getSellPrice() {
        return 25;
    }

    /**
     * The sell action
     * @param worker seller
     * @return the sell action for the alien cubes
     */
    @Override
    public Action getSellAction(ContractedWorker worker, Location supercomputerLocation) {
        return new SellAction(worker, this, this, supercomputerLocation);
    }

    /**
     * The sale of Alien cubes causes an undead to spawn next to the player
     *
     * @param worker seller
     * @param map    the game map where the sale occurs
     * @return a message indicating the successful sale
     */
    @Override
    public String onSell(ContractedWorker worker, GameMap map, Location supercomputerLocation) {
        for (Exit exit : map.locationOf(worker).getExits()) {
            Location neighbour = exit.getDestination();
            Undead undead = new Undead();
            if (!neighbour.containsAnActor() && neighbour.canActorEnter(undead)) {
                try {
                    map.addActor(undead, neighbour);
                    UndeadSpawnEffect effect = new UndeadSpawnEffect();
                    effect.applySpawnEffect(undead, neighbour);
                    return worker + " sold the alien cubes and has awakened an undead creature.";
                } catch (Exception e) {
                    continue;
                }

            }
        }
        return worker + " sold the alien cubes and has awakened, nothing. How did you get yourself here";
    }

    /**
     * Applies the effect of cutting the Alien Cubes.
     * The cubes are removed, an Alien Artifact is created,
     * and the worker becomes poisoned.
     *
     * @param worker the worker performing the cutting action
     * @param map the map where the action occurs
     * @param targetLocation the location of the Alien Cubes
     * @return a description of the cutting outcome
     */
    @Override
    public String cutBy(ContractedWorker worker, GameMap map, Location targetLocation) {
        worker.getInventory().remove(this);
        targetLocation.addItem(new AlienArtifact());
        worker.addStatus(new Poison(5, 1));
        return worker + " cut the Alien Cubes into an Alien Artifact and is poisoned for 5 turns.";
    }
}
