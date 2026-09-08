package game.enemies;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.ActorStatistics;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.StatisticOperations;
import game.actions.InfectAction;
import game.behaviours.AttackBehaviour;
import game.behaviours.SnatchBehaviour;
import game.behaviours.WanderBehaviour;
import game.enums.EnemyAbility;
import game.enums.WorkerAbility;
import game.interfaces.Infectable;
import game.loaders.MoonEnemy;
import game.statuses.InfectedStatus;
import game.weapons.BareFist;

/**
 * An enemy that snatches depositable items from the ground up.
 * Be aware, when it gets infected...
 * @author FIT2099 Bryan Chan Zheng Lam
 */
public class ScrapSnatcher extends MoonEnemy implements Infectable {

    /**
     * The constructor of the ScrapSnatcher class
     */
    public ScrapSnatcher() {
        super("Scrap Snatcher", 's', 25);
        this.enableAbility(EnemyAbility.IS_ENEMY);
        this.enableAbility(EnemyAbility.IS_INFECTABLE);
        this.setIntrinsicWeapon(new BareFist());
        this.behaviours.clear();

        //has behaviours
        this.behaviours.put(1, new AttackBehaviour(WorkerAbility.IS_WORKER, EnemyAbility.IS_RABID));
        this.behaviours.put(2, new SnatchBehaviour());
        this.behaviours.put(999, new WanderBehaviour());
    }

    /**
     * Gets the infected status
     * @return a new InfectionStatus
     */
    @Override
    public Status getInfectedStatus() {
        return new InfectedStatus(ActorStatistics.HEALTH, StatisticOperations.DECREASE, 1,
                this + " looks rabid and takes 1 infection damage!",
                0, null
        );
    }

    /**
     * Gets the infect action when targeted by a parasite.
     * @param parasite a parasite that infects entities/items
     * @param location the location in which the infect action takes place
     * @return a new InfectAction
     */
    @Override
    public Action getInfectAction(Actor parasite, Location location) {
        return new InfectAction(parasite, this, this);
    }
}
