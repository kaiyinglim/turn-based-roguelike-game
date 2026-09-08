package game.items;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.actions.DepositAction;
import game.enums.ItemStatistics;
import game.grounds.Supercomputer;
import game.interfaces.Depositable;
import game.loaders.ContractedWorker;

/**
 * A piece of discarded aluminium that can be deposited at a Supercomputer
 * for company credits. Depositing it may injure the worker due to its
 * sharp edges.
 * @author Heesu Kim
 */
public class AluminiumScrap extends Item implements Depositable {

    /**
     * Company credits earned when depositing the Aluminium Scrap.
     */
    private static final int DEPOSIT_VALUE = 50;

    /**
     * Weight of the Aluminium Scrap.
     */
    private static final int WEIGHT = 2;

    /**
     * Damage dealt if the worker is cut while depositing the scrap.
     */
    private static final int CUT_DAMAGE = 5;

    /**
     * Chance of being injured when depositing the scrap.
     */
    private static final double CUT_CHANCE = 0.2;

    /**
     * Constructor of the AluminiumScrap class.
     */
    public AluminiumScrap() {
        super("Aluminium Scrap", '%');
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(WEIGHT));
        this.addNewStatistic(ItemStatistics.DEPOSITABLE, new BaseStatistic(1));
        this.makePortable();
    }

    /**
     * Returns the company credit value of the Aluminium Scrap.
     *
     * @return the deposit value
     */
    @Override
    public int getDepositValue() {
        return DEPOSIT_VALUE;
    }

    /**
     * Applies the effect of depositing the Aluminium Scrap.
     * The worker may be injured by its sharp edges.
     *
     * @param worker the worker depositing the scrap
     * @param map the map where the action occurs
     * @param supercomputerLocation the location of the Supercomputer
     * @return a description of the deposit outcome
     */
    @Override
    public String onDeposit(ContractedWorker worker, GameMap map, Location supercomputerLocation) {
        if (Math.random() < CUT_CHANCE) {
            worker.hurt(CUT_DAMAGE);
            return "Jagged metal slices " + worker + " for " + CUT_DAMAGE + " damage.";
        }
        return worker + " shoves Aluminium Scrap into the deposit chute without injury.";
    }

    /**
     * Returns the deposit action associated with this Aluminium Scrap.
     *
     * @param worker the worker performing the action
     * @param supercomputer the Supercomputer receiving the deposit
     * @param supercomputerLocation the location of the Supercomputer
     * @return a DepositAction for this Aluminium Scrap
     */
    @Override
    public Action getDepositAction(ContractedWorker worker, Supercomputer supercomputer,
                                   Location supercomputerLocation) {
        return new DepositAction(worker, this, supercomputer, supercomputerLocation);
    }
}