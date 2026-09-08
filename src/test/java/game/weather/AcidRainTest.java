package game.weather;

import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.enums.WeatherCapability;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AcidRainTest {

    @Test
    void testFindTargetsOnlySelectsAcidCorrodibleTiles() throws Exception {
        ExposedAcidRain acidRain = new ExposedAcidRain();
        GameMap map = WeatherTestMapFactory.floorMap(
                "___",
                "___",
                "___"
        );

        List<Location> targets = acidRain.exposeTargets(map);

        assertFalse(targets.isEmpty(), "Floor tiles should be valid acid rain targets.");
        for (Location target : targets) {
            assertTrue(target.getGround().hasAbility(WeatherCapability.ACID_CORRODIBLE),
                    "Every target must be acid-corrodible.");
        }
    }

    @Test
    void testFindTargetsReturnsEmptyOnWallOnlyMap() throws Exception {
        ExposedAcidRain acidRain = new ExposedAcidRain();
        GameMap map = WeatherTestMapFactory.floorMap(
                "###",
                "###"
        );

        List<Location> targets = acidRain.exposeTargets(map);

        assertTrue(targets.isEmpty(), "Walls are not acid-corrodible targets.");
    }

    @Test
    void testTickDecrementsTurnsRemaining() throws Exception {
        AcidRain acidRain = new AcidRain();
        GameMap map = WeatherTestMapFactory.floorMap("___");

        assertEquals(3, acidRain.getTurnsRemaining());
        acidRain.tick(map);
        assertEquals(2, acidRain.getTurnsRemaining());

        acidRain.tick(map);
        acidRain.tick(map);
        assertTrue(acidRain.isExpired(), "Acid rain should expire after its duration.");
    }

    private static final class ExposedAcidRain extends AcidRain {
        List<Location> exposeTargets(GameMap map) {
            return findTargets(map);
        }
    }
}
