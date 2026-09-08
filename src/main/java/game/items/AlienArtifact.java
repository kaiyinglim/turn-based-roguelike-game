package game.items;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.actions.DepositAction;
import game.actions.SellAction;
import game.enums.ItemStatistics;
import game.grounds.Supercomputer;
import game.interfaces.Depositable;
import game.interfaces.Sellable;
import game.loaders.ContractedWorker;
import game.statuses.Poison;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A mysterious artifact that can be sold or deposited at a Supercomputer.
 * Selling it may poison the worker, while depositing it causes the
 * Supercomputer to teleport the worker to a random valid location.
 * @author Heesu Kim
 */
public class AlienArtifact extends Item implements Sellable, Depositable {

    /**
     * Credits received when selling the Alien Artifact.
     */
    private static final int SELL_PRICE = 200;

    /**
     * Company credits earned when depositing the Alien Artifact.
     */
    private static final int DEPOSIT_VALUE = 100;

    /**
     * Weight of the Alien Artifact.
     */
    private static final int WEIGHT = 1;

    /**
     * Number of turns the poison effect lasts.
     */
    private static final int POISON_TURNS = 5;

    /**
     * Damage dealt by the poison effect each turn.
     */
    private static final int POISON_DAMAGE = 1;

    /**
     * Chance of being poisoned when selling the Alien Artifact.
     */
    private static final double POISON_CHANCE = 0.5;

    /**
     * Random number generator used for teleportation.
     */
    private static final Random RANDOM = new Random();

    /**
     * Constructor of the AlienArtifact class.
     */
    public AlienArtifact() {
        super("Alien Artifact", '?');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.addNewStatistic(ItemStatistics.SELLABLE, new BaseStatistic(1));
        this.addNewStatistic(ItemStatistics.DEPOSITABLE, new BaseStatistic(1));
        this.makePortable();
    }

    /**
     * Returns the sell price of the Alien Artifact.
     *
     * @return the sell price
     */
    @Override
    public int getSellPrice() {
        return SELL_PRICE;
    }

    /**
     * Applies the effect of selling the Alien Artifact.
     * The worker has a chance to be poisoned by the unstable artifact.
     *
     * @param worker the worker selling the artifact
     * @param map the map where the action occurs
     * @param supercomputerLocation the location of the Supercomputer
     * @return a description of the sell outcome
     */
    @Override
    public String onSell(ContractedWorker worker, GameMap map, Location supercomputerLocation) {
        if (Math.random() < POISON_CHANCE) {
            worker.addStatus(new Poison(POISON_TURNS, POISON_DAMAGE));
            return worker + " is poisoned by the unstable Alien Artifact for "
                    + POISON_TURNS + " turns.";
        }
        return worker + " safely hands over the unstable Alien Artifact.";
    }

    /**
     * Returns the sell action associated with this Alien Artifact.
     *
     * @param worker the worker performing the action
     * @param supercomputerLocation the location of the Supercomputer
     * @return a SellAction for this Alien Artifact
     */
    @Override
    public Action getSellAction(ContractedWorker worker, Location supercomputerLocation) {
        return new SellAction(worker, this, this, supercomputerLocation);
    }

    /**
     * Returns the company credit value of the Alien Artifact.
     *
     * @return the deposit value
     */
    @Override
    public int getDepositValue() {
        return DEPOSIT_VALUE;
    }

    /**
     * Applies the effect of depositing the Alien Artifact.
     * The Supercomputer teleports the worker to a random valid location.
     *
     * @param worker the worker depositing the artifact
     * @param map the map where the action occurs
     * @param supercomputerLocation the location of the Supercomputer
     * @return a description of the teleportation result
     */
    @Override
    public String onDeposit(ContractedWorker worker, GameMap map, Location supercomputerLocation) {
        List<Location> validLocations = new ArrayList<>();

        for (int x : map.getXRange()) {
            for (int y : map.getYRange()) {
                Location location = map.at(x, y);
                if (!location.containsAnActor() && location.canActorEnter(worker)) {
                    validLocations.add(location);
                }
            }
        }

        if (validLocations.isEmpty()) {
            return "The Supercomputer tries to teleport " + worker + ", but no valid location is available.";
        }

        Location destination = validLocations.get(RANDOM.nextInt(validLocations.size()));
        map.moveActor(worker, destination);

        return "The Supercomputer teleports " + worker + " to ("
                + destination.x() + ", " + destination.y() + ").";
    }

    /**
     * Returns the deposit action associated with this Alien Artifact.
     *
     * @param worker the worker performing the action
     * @param supercomputer the Supercomputer receiving the deposit
     * @param supercomputerLocation the location of the Supercomputer
     * @return a DepositAction for this Alien Artifact
     */
    @Override
    public Action getDepositAction(ContractedWorker worker, Supercomputer supercomputer,
                                   Location supercomputerLocation) {
        return new DepositAction(worker, this, supercomputer, supercomputerLocation);
    }
}