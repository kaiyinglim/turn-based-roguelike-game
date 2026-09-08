package game.interfaces;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.positions.GameMap;
import game.loaders.ContractedWorker;

/**
 * Interface representing an item or entity that can be deployed
 * onto the game map by a ContractedWorker.
 *
 * Deployable objects define how they are placed into the world
 * and what happens when deployment occurs. This allows different
 * systems such as drones, traps, and support structures to share
 * a common deployment contract while implementing unique behaviours.
 *
 * Part of the autonomous tactical support system, enabling dynamic
 * battlefield modification through deployable units.
 *
 * @author Suzan Xuan Loh Palmer
 * @version 25.0.3
 */
public interface Deployable {
    /**
     * Creates the deploy action for this deployable object.
     *
     * @param worker the worker performing the deployment
     * @return an Action that handles deployment
     */
    Action getDeployAction(ContractedWorker worker);
    /**
     * Executes the deployment of the object onto the map.
     *
     * This method defines how the deployable is removed from inventory
     * and placed into the game world, as well as any activation logic
     * that occurs upon deployment.
     *
     * @param worker the worker deploying the object
     * @param map the game map where deployment occurs
     * @return a message describing the result of deployment
     */
    String deploy(ContractedWorker worker, GameMap map);
}
