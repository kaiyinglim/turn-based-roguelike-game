package game.statuses;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.Location;

public class Shielded implements Status {
    private int duration;

    public Shielded(int duration) {
        this.duration = duration;
    }

    @Override
    public void tickStatus(GameEntity entity, Location location) {

        if (duration > 0) {
            duration--;
        }
    }

    @Override
    public boolean isStatusActive() {
        return duration > 0;
    }
    @Override
    public String toString() {
        return "Shielded (" + duration + " turns remaining)";
    }
}