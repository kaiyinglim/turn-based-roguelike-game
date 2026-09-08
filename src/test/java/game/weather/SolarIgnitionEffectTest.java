package game.weather;

import edu.monash.fit2099.engine.actors.TestActor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.enums.WeatherCapability;
import game.grounds.Fire;
import game.grounds.Floor;
import game.statuses.Burning;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SolarIgnitionEffectTest {

    private final SolarIgnitionEffect effect = new SolarIgnitionEffect();

    @Test
    void testSeedsFireWhenNoSolarSourcesExist() throws Exception {
        GameMap map = WeatherTestMapFactory.floorMap(
                "___",
                "___",
                "___"
        );

        effect.apply(List.of(), map);

        boolean seededSolarFire = false;
        for (int x : map.getXRange()) {
            for (int y : map.getYRange()) {
                Ground ground = map.at(x, y).getGround();
                if (ground.hasAbility(WeatherCapability.SOLAR_SOURCE)) {
                    seededSolarFire = true;
                    break;
                }
            }
        }

        assertTrue(seededSolarFire,
                "Solar flare should seed SOLAR_SOURCE fire when no sources exist.");
    }

    @Test
    void testSpreadsFireToAdjacentFlammableTile() throws Exception {
        GameMap map = WeatherTestMapFactory.floorMap(
                "___",
                "___"
        );
        Location source = WeatherTestMapFactory.at(map, 1, 0);
        Fire solarFire = new Fire(new Floor(), 5);
        solarFire.enableAbility(WeatherCapability.SOLAR_SOURCE);
        source.setGround(solarFire);
        Location adjacent = WeatherTestMapFactory.at(map, 1, 1);

        effect.apply(List.of(source), map);

        assertTrue(adjacent.getGround().hasAbility(WeatherCapability.SOLAR_SOURCE),
                "Solar fire should spread to adjacent flammable tiles.");
    }

    @Test
    void testSkipsAlreadySolarSourceTiles() throws Exception {
        GameMap map = WeatherTestMapFactory.floorMap("___");
        Location source = WeatherTestMapFactory.at(map, 0, 0);
        Fire sourceFire = new Fire(new Floor(), 5);
        sourceFire.enableAbility(WeatherCapability.SOLAR_SOURCE);
        source.setGround(sourceFire);

        Location adjacentSolar = WeatherTestMapFactory.at(map, 1, 0);
        Fire existingSolar = new Fire(new Floor(), 99);
        existingSolar.enableAbility(WeatherCapability.SOLAR_SOURCE);
        adjacentSolar.setGround(existingSolar);

        effect.apply(List.of(source), map);

        assertSame(existingSolar, adjacentSolar.getGround(),
                "Spread should not re-ignite tiles that are already solar sources.");
    }

    @Test
    void testBurnsActorOnIgnitedTile() throws Exception {
        GameMap map = WeatherTestMapFactory.floorMap("___");
        Location source = WeatherTestMapFactory.at(map, 0, 0);
        Fire solarFire = new Fire(new Floor(), 5);
        solarFire.enableAbility(WeatherCapability.SOLAR_SOURCE);
        source.setGround(solarFire);

        TestActor actor = new TestActor();
        WeatherTestMapFactory.addActor(map, 1, 0, actor);

        effect.apply(List.of(source), map);

        assertFalse(actor.statusesOf(Burning.class).isEmpty(),
                "Actors on ignited tiles should receive burning.");
    }
}
