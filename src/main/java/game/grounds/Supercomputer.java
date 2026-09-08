package game.grounds;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.DropAction;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.positions.Exit;

import game.deployables.Mine;
import game.deployables.ShieldBeacon;
import game.deployables.Turret;
import game.deployables.drone.*;
import game.enums.ItemStatistics;
import game.interfaces.Deployable;
import game.interfaces.Depositable;
import game.interfaces.Purchasable;
import game.items.*;
import game.loaders.ContractedWorker;
import game.interfaces.Sellable;
import game.enums.WorkerAbility;
import game.systems.CompanyQuota;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * The Supercomputer allows workers to sell items and purchase equipment.
 *
 * Inside the armoured ship, workers interact with this terminal to trade items
 * and acquire equipment using credits. The Supercomputer enforces strict
 * economic rules on transactions.
 *
 * @author Suzan Xuan Loh Palmer
 * @version 25.0.3
 */
public class Supercomputer extends Ground {
    private final List<Purchasable> products = new ArrayList<>();
    private final List<Purchasable> deployableProducts = new ArrayList<>();
    //private final List<Deployable> deployables = new ArrayList<>();
    private final CompanyQuota companyQuota;

    /**
     * Constructs the Supercomputer ground.
     */
    public Supercomputer() {
        super('≡', "Supercomputer");
        this.companyQuota = new CompanyQuota();
        products.add(new FirstAidKit());
        products.add(new SterilisationBox());
        products.add(new AccessCardLevel1());
        products.add(new AccessCardLevel2());
        products.add(new AccessCardLevel3());
        products.add(new PlasmaCutter());

        deployableProducts.add(new Mine());
        deployableProducts.add(new Turret());
        deployableProducts.add(new ShieldBeacon());
        deployableProducts.add(new CombatDroneItem());
        deployableProducts.add(new ScoutDroneItem());
        deployableProducts.add(new RepairDroneItem());
    }

    @Override
    public void tick(Location location) {
        if (!hasWorkerOnMap(location)) {
            return;
        }
        if (companyQuota.advanceTurnAndCheckFailure()) {
            fireAdjacentWorkers(location);
        }
    }

    /**
     * Adds credits to the company quota when goods are sold through this terminal.
     *
     * @param amount credits earned for the company
     */
    public void addCompanyCredits(int amount) {
        companyQuota.addCompanyCredits(amount);
    }

    /**
     * Returns a short summary of the current quota cycle.
     *
     * @return quota status text
     */
    public String getQuotaStatus() {
        return companyQuota.statusSummary();
    }

    private void fireAdjacentWorkers(Location location) {
        Display display = new Display();
        display.println("The Supercomputer quota deadline has passed. Adjacent workers are fired.");
        for (Exit exit : location.getExits()) {
            Location neighbour = exit.getDestination();
            if (neighbour.containsAnActor() && neighbour.getActor().hasAbility(WorkerAbility.IS_WORKER)) {
                display.println(neighbour.getActor().unconscious(location.map()));
            }
        }
    }

    private boolean hasWorkerOnMap(Location location) {
        for (int x : location.map().getXRange()) {
            for (int y : location.map().getYRange()) {
                Location mapLocation = location.map().at(x, y);
                if (mapLocation.containsAnActor()
                        && mapLocation.getActor().hasAbility(WorkerAbility.IS_WORKER)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns transaction actions available to nearby workers.
     *
     * @param actor actor interacting with the Supercomputer
     * @param location location of the Supercomputer
     * @param direction direction of the Supercomputer relative to actor
     * @return list of allowable transaction actions
     */
    @Override
    public ActionList allowableActions(Actor actor, Location location, String direction) {

        ActionList actions = new ActionList();

        if (!companyQuota.isSupercomputerUsable()) {
            return actions;
        }

        //Only workers can use the Supercomputer
        if (!actor.hasAbility(WorkerAbility.IS_WORKER)) {
            return actions;
        }

        ContractedWorker worker = (ContractedWorker) actor;

        //Add sell and deposit actions for all sellable and depositable items
        for (Item item : worker.getInventory().getItems()) {
            actions.add(item.allowableActions(worker, location.map())); // drop + deploy come from here
            if (item.hasStatistic(ItemStatistics.SELLABLE)) {
                actions.add(((Sellable) item).getSellAction(worker, location));
            }
            if (item.hasStatistic(ItemStatistics.DEPOSITABLE)) {
                actions.add(((Depositable) item).getDepositAction(worker, this, location));
            }
            if (item.hasStatistic(ItemStatistics.DEPLOYABLE)) {
                actions.add(((Deployable) item).getDeployAction(worker));
            }
        }
        // Add purchasable items sold by the Supercomputer
        for (Purchasable product : products) {

            //Item item = (Item) product;

            actions.add(product.getBuyAction(worker));
        }
        for (Purchasable product : deployableProducts) {
            actions.add(product.getBuyAction(worker));
        }
        //for (Deployable deployable : deployables) {
            //actions.add(deployable.getDeployAction(worker));
        //}
        return actions;
    }

}
