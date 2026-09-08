package game.weather;

import edu.monash.fit2099.engine.actors.TestActor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MeteorStormTest {

    @Test
    void testExcludesOccupiedMeteorTargets() throws Exception {
        ExposedMeteorStorm meteorStorm = new ExposedMeteorStorm();
        GameMap map = WeatherTestMapFactory.floorMap(
                "___",
                "___",
                "___"
        );
        WeatherTestMapFactory.addActor(map, 1, 1, new TestActor());

        List<Location> targets = meteorStorm.exposeTargets(map);

        for (Location target : targets) {
            assertFalse(target.containsAnActor(),
                    "Meteor storm must not target tiles occupied by actors.");
        }
    }

    @Test
    void testIncludesUnoccupiedMeteorTargets() throws Exception {
        ExposedMeteorStorm meteorStorm = new ExposedMeteorStorm();
        GameMap map = WeatherTestMapFactory.floorMap(
                "___",
                "___",
                "___"
        );

        List<Location> targets = meteorStorm.exposeTargets(map);

        assertFalse(targets.isEmpty(), "Unoccupied meteor targets should be selectable.");
        assertTrue(targets.stream().noneMatch(Location::containsAnActor));
    }

    @Test
    void testReturnsEmptyWhenNoValidTargets() throws Exception {
        ExposedMeteorStorm meteorStorm = new ExposedMeteorStorm();
        GameMap map = WeatherTestMapFactory.floorMap("_");
        WeatherTestMapFactory.addActor(map, 0, 0, new TestActor());

        List<Location> targets = meteorStorm.exposeTargets(map);

        assertTrue(targets.isEmpty(), "No valid targets when the only meteor tile is occupied.");
    }

    private static final class ExposedMeteorStorm extends MeteorStorm {
        List<Location> exposeTargets(GameMap map) {
            return findTargets(map);
        }
    }
}
