package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.enums.WorkerAbility;
import game.interfaces.Deployable;
import game.loaders.ContractedWorker;

/**
 * Action for deploying a deployable item from the worker's inventory.
 *
 * This action handles the deployment process of autonomous units,
 * structures, and devices onto the game map. It verifies whether
 * the actor has permission to deploy units, then delegates the
 * deployment logic to the deployable item, which defines its own
 * behaviour when placed on the map.
 *
 * @author Suzan Xuan Loh Palmer
 * @version 25.0.3
 */
public class DeployAction extends Action {

    private final Deployable deployable;
    /**
     * Constructs a deploy action.
     *
     * @param deployable the deployable item being deployed
     */
    public DeployAction(Deployable deployable) {
        this.deployable = deployable;
    }
    /**
     * Executes the deployment process.
     *
     * If the actor does not have permission to deploy units,
     * the action is rejected. Otherwise, the deployable item
     * is deployed onto the map at the worker's current location,
     * triggering its defined deployment behaviour.
     *
     * @param actor the actor performing the action
     * @param map the map the actor is on
     * @return a description of the deployment result
     */
    @Override
    public String execute(Actor actor, GameMap map) {

        if (!actor.hasAbility(WorkerAbility.DEPLOY_UNITS)) {
            return actor + " cannot deploy units.";
        }

        ContractedWorker worker = (ContractedWorker) actor;
        return deployable.deploy(worker, map);
    }
    /**
     * Returns the menu description shown to the player.
     *
     * @param actor the actor viewing the action menu
     * @return menu description for this action
     */
    @Override
    public String menuDescription(Actor actor) {
        return "Deploy " + deployable.toString();
    }
}
