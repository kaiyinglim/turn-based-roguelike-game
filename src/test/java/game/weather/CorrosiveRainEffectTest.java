package game.weather;

import edu.monash.fit2099.engine.actors.TestActor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.enums.WeatherCapability;
import game.grounds.ToxicWaste;
import game.statuses.StackingPoison;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CorrosiveRainEffectTest {

    private final CorrosiveRainEffect effect = new CorrosiveRainEffect();

    @Test
    void testCorrodesTargetFloorIntoToxicWaste() throws Exception {
        GameMap map = WeatherTestMapFactory.floorMap("___");
        Location target = WeatherTestMapFactory.at(map, 1, 0);

        effect.apply(List.of(target), map);

        assertInstanceOf(ToxicWaste.class, target.getGround());
        assertTrue(target.getGround().hasAbility(WeatherCapability.CORROSIVE_POOL));
    }

    @Test
    void testSpreadsCorrosionToAdjacentFloor() throws Exception {
        GameMap map = WeatherTestMapFactory.floorMap(
                "___",
                "___"
        );
        Location pool = WeatherTestMapFactory.at(map, 1, 0);
        pool.setGround(new ToxicWaste());
        Location neighbour = WeatherTestMapFactory.at(map, 1, 1);

        effect.apply(List.of(), map);

        assertInstanceOf(ToxicWaste.class, neighbour.getGround(),
                "Acid pools should spread into adjacent corrodible tiles.");
    }

    @Test
    void testAppliesStackingPoisonToActorOnPool() throws Exception {
        GameMap map = WeatherTestMapFactory.floorMap("___");
        Location pool = WeatherTestMapFactory.at(map, 1, 0);
        pool.setGround(new ToxicWaste());
        TestActor actor = new TestActor();
        WeatherTestMapFactory.addActor(map, 1, 0, actor);

        effect.apply(List.of(), map);

        assertFalse(actor.statusesOf(StackingPoison.class).isEmpty(),
                "Actors standing in toxic pools should receive stacking poison.");
    }

    @Test
    void testReExposureStacksPoison() throws Exception {
        GameMap map = WeatherTestMapFactory.floorMap("___");
        Location pool = WeatherTestMapFactory.at(map, 1, 0);
        pool.setGround(new ToxicWaste());
        TestActor actor = new TestActor();
        WeatherTestMapFactory.addActor(map, 1, 0, actor);

        effect.apply(List.of(), map);
        StackingPoison poison = actor.statusesOf(StackingPoison.class).get(0);
        poison.tickStatus(actor, pool);
        assertEquals(99, actor.getStatistic(
                edu.monash.fit2099.engine.actors.ActorStatistics.HEALTH));

        effect.apply(List.of(), map);
        poison.tickStatus(actor, pool);

        assertEquals(97, actor.getStatistic(
                edu.monash.fit2099.engine.actors.ActorStatistics.HEALTH),
                "Re-exposure should stack poison and increase damage per tick.");
    }
}
