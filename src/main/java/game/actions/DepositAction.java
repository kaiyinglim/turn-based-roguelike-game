package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.grounds.Supercomputer;
import game.interfaces.Depositable;
import game.loaders.ContractedWorker;

/**
 * An action that allows a ContractedWorker to deposit a Depositable item
 * into a Supercomputer for company credits.
 * @author Heesu Kim
 */
public class DepositAction extends Action {

    /**
     * The worker performing the deposit action.
     */
    private final ContractedWorker worker;

    /**
     * The item that can be deposited.
     */
    private final Depositable depositable;

    /**
     * The Supercomputer receiving the deposited item.
     */
    private final Supercomputer supercomputer;

    /**
     * The location of the Supercomputer on the map.
     */
    private final Location supercomputerLocation;

    /**
     * Constructor of the DepositAction class.
     *
     * @param worker the ContractedWorker performing the action
     * @param depositable the item being deposited
     * @param supercomputer the Supercomputer receiving the deposit
     * @param supercomputerLocation the location of the Supercomputer
     */
    public DepositAction(ContractedWorker worker, Depositable depositable,
                         Supercomputer supercomputer, Location supercomputerLocation) {
        this.worker = worker;
        this.depositable = depositable;
        this.supercomputer = supercomputer;
        this.supercomputerLocation = supercomputerLocation;
    }

    /**
     * Executes the deposit action by transferring company credits,
     * removing the deposited item from the worker's inventory,
     * and triggering any deposit effects.
     *
     * @param actor the actor performing the action
     * @param map the map where the action takes place
     * @return the result of the deposit action
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        int companyCredits = depositable.getDepositValue();
        supercomputer.addCompanyCredits(companyCredits);
        worker.getInventory().remove((Item) depositable);

        return worker + " deposited " + depositable + " for " + companyCredits + " company credits.\n"
                + depositable.onDeposit(worker, map, supercomputerLocation) + "\n"
                + supercomputer.getQuotaStatus();
    }

    /**
     * Returns the description shown in the menu.
     *
     * @param actor the actor performing the action
     * @return a string describing the deposit action
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " deposits " + depositable;
    }
}