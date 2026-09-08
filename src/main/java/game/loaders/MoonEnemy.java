package game.loaders;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.behaviours.Behaviour;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;

import java.util.Map;
import java.util.TreeMap;

/**
 * This evil soul is capable of wandering around aimlessly until
 * it stumble upon a ContractedWorker where it will become hostile.
 * @author FIT2099 Bryan Chan Zheng Lam
 */
public abstract class MoonEnemy extends Actor {

    /**
     * A priority-ordered map for behaviours that enemy can perform
     * key reprsents priority, where lower key value indicates higher priority
     */
    protected final Map<Integer, Behaviour<Actor, Action>> behaviours = new TreeMap<>();

    /**
     * The constructor of MoonEnemy class
     * @param name the name of the moon enemy
     * @param displayChar the image of the moon enemy
     * @param hitPoints the amount of HP of moon enemy
     */
    public MoonEnemy(String name, char displayChar, int hitPoints) {
        super(name, displayChar, hitPoints, new BasicInventory());
    }

    /**
     * The playTurn method loops through behaviours in priority order, the behaviours will check its own methods
     * Then if action is not null, returns the first action found.
     * else, do nothing.
     *
     * @param actions collection of possible Actions for this Actor
     * @param lastAction The Action this Actor took last turn. Can do
     * interesting things in conjunction with Action.getNextAction()
     * @param map the map containing the Actor
     * @param display the I/O object to which messages may be written
     * @return action if not null, else return a do nothing action.
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {

        for (Behaviour<Actor, Action> behaviour : behaviours.values()) {
            Action action = behaviour.operate(this, map.locationOf(this));
            if (action != null) {
                return action;
            }
        }

        return new DoNothingAction();
    }
}
