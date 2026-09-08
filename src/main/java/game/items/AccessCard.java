package game.items;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.BaseStatistic;
import game.grounds.doors.Door;
import game.enums.ItemStatistics;
import game.actions.UnlockDoorAction;
import game.interfaces.Purchasable;

/**
 * A class representing a small rectangular piece of plastic that holds entirely
 * too much power over your ability to walk through doors.
 * Its primary function is to beep happily when the player has clearance, and beep
 * angrily when they don't.
 * Essential for progressing the plot,
 *
 * @author Adrian Kristanto
 * @author Bryan Chan Zheng Lam
 */
public abstract class AccessCard extends Item implements Purchasable {

    /**
     * The constructor of AccessCard class
     */
    public AccessCard(String name, char displayChar, int weight) {
        super(name, displayChar);
        this.addNewStatistic(ItemStatistics.WEIGHT, new BaseStatistic(1));
        this.makePortable();
    }
    /**
     * Returns the clearance level of this access card.
     * The clearance level determines which doors the card is able to unlock.
     *
     * @return the clearance level as an integer value
     */
    public abstract int getClearanceLevel();

    /**
     * A list of actions that can be performed from its owner.
     * If access card has a neighbour called door: '=',
     * it will have an action that the owner can use to unlock door.
     * If access card has no neighbours called door: '=', no actions will be added to the menu.
     *
     * @param owner the owner that owns the item
     * @param map the map where the actor is performing the action on
     * @return a list of allowable actions that first aid kit can be performed from its owner.
     */
    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        ActionList actions = new ActionList();
        Location location = map.locationOf(owner);
        for (Exit exit : location.getExits()) {
            Location surroundingLocation = exit.getDestination();
            Ground surroundingGround = surroundingLocation.getGround();
            if (surroundingGround.getDisplayChar() == '=') {
                Door door = (Door) surroundingGround;
                if (!door.isUnlocked() && door.canBeUnlockedBy(this)) {
                    actions.add(new UnlockDoorAction(door));
                }
            }
        }
        return actions;
    }

    /**
     * The toString() method of the item class
     * @return name of the item class
     */
    @Override
    public String toString() {
        return super.toString();
    }
}
