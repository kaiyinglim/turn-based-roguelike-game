package game.weather;

import edu.monash.fit2099.engine.GameEngineException;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.positions.World;
import game.grounds.Floor;
import game.grounds.Wall;

import java.util.Arrays;
import java.util.List;

/**
 * Builds small deterministic {@link GameMap} instances for REQ4 weather tests.
 */
final class WeatherTestMapFactory {

    private WeatherTestMapFactory() {
    }

    static GameMap floorMap(String... rows) throws GameEngineException {
        DefaultGroundCreator creator = new DefaultGroundCreator();
        creator.registerGround('_', Floor::new);
        creator.registerGround('#', Wall::new);
        List<String> lines = Arrays.asList(rows);
        GameMap map = new GameMap("weather-test", creator, lines);
        TestWorld world = new TestWorld();
        world.addGameMap(map);
        return map;
    }

    static void addActor(GameMap map, int x, int y, Actor actor) throws GameEngineException {
        map.addActor(actor, map.at(x, y));
    }

    static Location at(GameMap map, int x, int y) {
        return map.at(x, y);
    }

    private static final class TestWorld extends World {
        TestWorld() {
            super(new Display());
        }
    }
}
