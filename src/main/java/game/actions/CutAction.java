package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.interfaces.Cuttable;
import game.loaders.ContractedWorker;

/**
 * An action that allows a ContractedWorker to cut a Cuttable target
 * using a Plasma Cutter.
 * @author Heesu Kim
 */
public class CutAction extends Action {

    /**
     * The worker performing the cutting action.
     */
    private final ContractedWorker worker;

    /**
     * The cuttable target being cut.
     */
    private final Cuttable target;

    /**
     * The location of the target on the map.
     */
    private final Location targetLocation;

    /**
     * Constructor of the CutAction class.
     *
     * @param worker the ContractedWorker performing the action
     * @param target the Cuttable target to be cut
     * @param targetLocation the location of the target
     */
    public CutAction(ContractedWorker worker, Cuttable target, Location targetLocation) {
        this.worker = worker;
        this.target = target;
        this.targetLocation = targetLocation;
    }

    /**
     * Executes the cutting action on the target.
     *
     * @param actor the actor performing the action
     * @param map the map where the action takes place
     * @return the result of cutting the target
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        return target.cutBy(worker, map, targetLocation);
    }

    /**
     * Returns the description shown in the menu.
     *
     * @param actor the actor performing the action
     * @return a string describing the cutting action
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " cuts " + target.toString() + " with Plasma Cutter";
    }
}