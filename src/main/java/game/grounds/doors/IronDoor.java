package game.grounds.doors;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.StatisticOperations;
import game.enums.GroundStatistics;
import game.grounds.Fire;

public class IronDoor extends Door {
    /**
     * The duration of the fire
     */
    private static final int FIRE_DURATION = 2;

    /**
     * The variable to show if the door got just unlocked
     */
    private boolean JustUnlocked = false;
    /**
     * The constructor of IronDoor class
     */
    public IronDoor() {
        super('N', "Iron Door", 2);
    }

    /**
     * When unlocking the door, the locked statistic will be changed to 0 to indicate that it is unlocked,
     * the JustUnlocked variable will be set to true to show that the door has been just unlocked
     */
    public void unlock(Actor actor) {
        this.modifyStatistic(GroundStatistics.LOCKED, StatisticOperations.UPDATE, 0);
        JustUnlocked = true;
    }

    /**
     * The tick method is overridden to allow for the fire to spawn around the door on the ground.
     *
     * @param location The location of the Ground
     */
    @Override
    public void tick(Location location) {
        if (JustUnlocked) {
            Actor actor = null;
            Location actorLocation = null;
            for (Exit exit : location.getExits()) {
                Location neighbour = exit.getDestination();
                if (neighbour.containsAnActor()) {
                    actor = neighbour.getActor();
                    actorLocation = location.map().locationOf(actor);
                }
                if (neighbour.equals(actorLocation) || neighbour.canActorEnter(actor)) {
                    Ground prevGround = neighbour.getGround();
                    neighbour.setGround(new Fire(prevGround, FIRE_DURATION));
                }
            }
            JustUnlocked = false;
        }
    }
}

