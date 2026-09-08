package game.weather;

import edu.monash.fit2099.engine.positions.GameMap;
import game.interfaces.WeatherEffect;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WeatherControllerTest {

    @Test
    void testTicksActiveEventOnEveryMap() throws Exception {
        CountingEffect effect = new CountingEffect();
        WeatherEvent event = new TestWeatherEvent(effect, 5);
        WeatherController controller = new WeatherController(List.of(event));
        GameMap mapOne = WeatherTestMapFactory.floorMap("___");
        GameMap mapTwo = WeatherTestMapFactory.floorMap("___");

        controller.tick(List.of(mapOne, mapTwo));

        assertEquals(2, effect.applyCount,
                "Active weather should tick once on each map.");
    }

    @Test
    void testAdvancesToNextEventWhenDurationExpires() throws Exception {
        AcidRain acidRain = new AcidRain();
        MeteorStorm meteorStorm = new MeteorStorm();
        WeatherController controller = new WeatherController(List.of(acidRain, meteorStorm));
        GameMap map = WeatherTestMapFactory.floorMap("___");

        for (int i = 0; i < 3; i++) {
            controller.tick(List.of(map));
        }
        assertTrue(acidRain.isExpired(), "Acid rain should expire after three ticks.");

        controller.tick(List.of(map));
        assertFalse(meteorStorm.isExpired(), "Controller should advance to meteor storm.");
        assertEquals(2, meteorStorm.getTurnsRemaining(),
                "Meteor storm should tick once after becoming active.");
    }

    @Test
    void testEmptyRotationDoesNothing() {
        WeatherController controller = new WeatherController(List.of());
        GameMap mockMap = mock(GameMap.class);

        assertDoesNotThrow(() -> controller.tick(List.of(mockMap)));
        verifyNoInteractions(mockMap);
    }

    private static final class CountingEffect implements WeatherEffect {
        private int applyCount;

        @Override
        public void apply(List<edu.monash.fit2099.engine.positions.Location> targets, GameMap map) {
            applyCount++;
        }
    }

    private static final class TestWeatherEvent extends WeatherEvent {

        TestWeatherEvent(WeatherEffect effect, int duration) {
            super(effect, duration);
        }

        @Override
        protected List<edu.monash.fit2099.engine.positions.Location> findTargets(GameMap map) {
            return List.of();
        }

        @Override
        public String getName() {
            return "Test Weather";
        }
    }
}
