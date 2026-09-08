package game.loaders;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.ActorStatistics;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.displays.Menu;
import edu.monash.fit2099.engine.items.Inventory;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.StatisticOperations;
import game.actions.InfectAction;
import game.actions.UnlockDoorAction;
import game.deployables.Mine;
import game.enums.EnemyAbility;
import game.enums.ItemStatistics;
import game.enums.WorkerAbility;
import game.interfaces.Deployable;
import game.interfaces.Infectable;
import game.items.Flask;
import game.statuses.EMPShock;
import game.statuses.Frozen;
import game.statuses.InfectedStatus;

/**
 * This brave soul is capable of performing complex tasks such as picking up trash
 * off the floor, swiping plastic cards at stubborn doors, and drinking mystery
 * fluids to stay alive.
 */
public class ContractedWorker extends Actor implements Infectable {
    private int credits;

    public ContractedWorker(String name, char displayChar, int hitPoints, Inventory inventory) {
        super(name, displayChar, hitPoints, inventory);
        this.enableAbility(WorkerAbility.IS_WORKER);
        this.enableAbility(WorkerAbility.DEPLOY_UNITS);
        this.enableAbility(WorkerAbility.ALLY);
        this.credits = 0;
    }

    /**
     * Returns current credits
     *
     * @return worker credits
     */
    public int getCredits() {
        return credits;
    }

    /**
     * Adds credits up to the wallet limit of 1000
     *
     * @param amount credits to add
     */
    public void addCredits(int amount) {
        credits = Math.min(1000, credits + amount);
    }

    /**
     * Deducts credits from the worker
     *
     * @param amount credits to deduct
     */
    public void deductCredits(int amount) {
        credits -= amount;
    }

    /**
     * The playTurn method checks whether the current actor is unconscious due to environmental hazards.
     * Additionally, ut will also handle multi-turn actions by getting the subsequent action returned by the previous action.
     * Finally, it adds all possible actions that the actor can perform in the current turn and show it on the
     * console menu for the player to choose.
     *
     * @see UnlockDoorAction
     * @see Flask
     * @param actions collection of possible Actions for this Actor
     * @param lastAction The Action this Actor took last turn. Can do
     * interesting things in conjunction with Action.getNextAction()
     * @param map the map containing the Actor
     * @param display the I/O object to which messages may be written
     * @return the action that is chosen in the current turn
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        if (!this.isConscious()) {
            this.unconscious(map);
            return new DoNothingAction();
        }

        // If the worker is frozen, skip their turn
        if (this.hasStatus(Frozen.class)) {
            display.println(this + " is frozen and skips their turn.");
            return new DoNothingAction();
        }
        if (this.hasStatus(EMPShock.class)) {
            display.println(this + " is EMP shocked.");
            return new DoNothingAction();
        }

        // Handle multi-turn Actions
        if (lastAction.getNextAction() != null)
            return lastAction.getNextAction();

        // return/print the console menu
        Menu menu = new Menu(actions);
        return menu.showMenu(this, display);
    }
    /**
     * A list of actions that can be performed from its owner.
     * It will have an action that can infect the ContractedWorker
     * @param otherActor the Actor that performs the infection
     * @param direction String representing the direction of the other Actor
     * @param map current GameMap
     * @return a list of allowable actions that ContractedWorker can be performed by the Parasite
     */
    @Override
    public ActionList allowableActions(Actor otherActor, String direction, GameMap map) {
        ActionList actions = new ActionList();
        //parasite is adjacent
        if (otherActor.hasAbility(EnemyAbility.IS_ENEMY)) {
            actions.add(new InfectAction(otherActor, this, this));
        }
        return actions;
    }

    /**
     * Gets the infected status
     * @return a new InfectedWorker
     */
    @Override
    public Status getInfectedStatus() {
        return new InfectedStatus(ActorStatistics.HEALTH, StatisticOperations.DECREASE, 1,
                this + " takes 1 damage from infection", 5,
                "A Parasite spawned from the infected worker.");
    }

    /**
     * Gets the infect action from infect actor action
     * @param parasite a parasite that infects entities/items
     * @param location the location in which the infect action takes place
     * @return a new InfectActorAction
     */
    @Override
    public Action getInfectAction(Actor parasite, Location location) {
        return new InfectAction(parasite, this, this);
    }
}

