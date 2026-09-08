package game.actions;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.enums.EnemyAbility;
import game.interfaces.Infectable;

/**
 * InfectItemAction class is a class that represents infection to any GameEntity (Actor or Item).
 * @author FIT2099 Bryan Chan Zheng Lam
 */
public class InfectAction extends Action {
    /**
     * The Actor that is the parasite
     */
    private final Actor parasite;

    /**
     * The GameEntity that is to be attacked
     */
    private final GameEntity target;

    /**
     * Infectable that is used for infecting
     */
    private final Infectable infectable;

    /**
     * The constructor of the InfectAction
     * @param parasite the parasite which is a creature
     * @param target the game entity in which the creature targets
     * @param infectable the infectable entity/item to be infected
     */
    public InfectAction(Actor parasite, GameEntity target, Infectable infectable) {
        this.parasite = parasite;
        this.target = target;
        this.infectable = infectable;
    }

    /**
     * Executes when actor/item gets infected and if target is not conscious, outputs its description
     *
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return a string of the description of infecting its target and parasite dying.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        // GameEntity safely handles the status addition for both Items and Actors
        target.addStatus(infectable.getInfectedStatus());
        target.enableAbility(EnemyAbility.IS_RABID);
        parasite.unconscious(map);
        return parasite + " infects " + target + "! " + parasite + " dies!";
    }

    /**
     * The description for the creature when it infects the specific target
     * @param actor The actor performing the action.
     * @return the output string when the creature infects the target.
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " infects " + target;
    }
}
