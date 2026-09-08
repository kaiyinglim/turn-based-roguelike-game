package game.grounds;

import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.statuses.Burning;

public class Fire extends Ground {

    private static final int BURN_TURNS = 5;
    private int turns_remaining;
    private Ground prevGround;

    //inject prevground so it knows what ground to restore
    public Fire(Ground prevGround, int fireDuration) {
        super('^', "Fire");
        this.turns_remaining = fireDuration;
        this.prevGround = prevGround;
    }

    @Override
    public void tick(Location location) {
        if (location.containsAnActor()) {
            location.getActor().addStatus(new Burning(BURN_TURNS,1));
        }

        if (turns_remaining > 0) {
            turns_remaining--;
        } else {
            location.setGround(prevGround);
        }
    }
}
