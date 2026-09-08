package game.enemies;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.ActorStatistics;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.StatisticOperations;
import game.actions.InfectAction;
import game.behaviours.AttackBehaviour;
import game.behaviours.WanderBehaviour;
import game.enums.EnemyAbility;
import game.enums.WorkerAbility;
import game.interfaces.Infectable;
import game.loaders.MoonEnemy;
import game.statuses.InfectedStatus;
import game.weapons.BareFist;

/**
 * A hostile entity that wanders around the moon's facility
 * @author FIT2099 Bryan Chan Zheng Lam
 */
public class Undead extends MoonEnemy implements Infectable {
    /**
     * The constructor of the Undead class
     */
    public Undead() {
        super("Undead", 'Ѫ', 15);
        this.enableAbility(EnemyAbility.IS_ENEMY);
        this.enableAbility(EnemyAbility.IS_UNDEAD);
        this.enableAbility(EnemyAbility.IS_INFECTABLE);
        //Has a weapon
        this.setIntrinsicWeapon(new BareFist());
        //Has its own behaviours
        this.behaviours.put(1, new AttackBehaviour(WorkerAbility.IS_WORKER));
        this.behaviours.put(999, new WanderBehaviour());
    }

    /**
     * A list of actions that can be performed from its owner.
     * It will have an action that can infect the Undead
     * @param otherActor the Actor that performs the infection
     * @param direction String representing the direction of the other Actor
     * @param map current GameMap
     * @return a list of allowable actions that Undead can be performed by the Parasite
     */
    @Override
    public ActionList allowableActions(Actor otherActor, String direction, GameMap map) {
        ActionList actions = new ActionList();
        // parasite is adjacent
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
        return new InfectedStatus(ActorStatistics.HEALTH, StatisticOperations.UPDATE, 0,
                this + "'s alien biology rejects the dead. Instantly dies!",
                0, null); //no spawn rate as it never spawn when infected
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
