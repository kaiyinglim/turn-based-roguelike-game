package game.loaders;

import edu.monash.fit2099.engine.GameEngineException;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.World;
import game.weather.MapWeather;
import game.weather.AcidRain;
import game.weather.MeteorStorm;
import game.weather.SolarFlare;
import game.weather.WeatherController;
import game.behaviours.GrowingBehaviour;
import game.grounds.doors.AluminiumDoor;
import game.grounds.doors.IronDoor;
import game.grounds.doors.TitaniumDoor;
import game.spawners.DeprecatedHoleSpawner;
import game.grounds.*;
import game.items.*;
import game.grounds.ToxicWaste;
import game.spawners.MatureUndeadSpawner;
import game.spawners.ScrapSnatcherSpawner;
import game.trees.FleshyMatureTree;
import game.trees.FleshyMonolith;
import game.trees.FleshySprout;
import game.trees.FleshyTree;

import java.util.Arrays;
import java.util.List;

/**
 * This class handles the miracle of creation, translating a bunch of periods
 * and hashtags into a sprawling, functional sci-fi facility.
 */
public class EclipseNebula extends World {
    private WeatherController weatherController;
    private MapWeather mapWeather;

    public EclipseNebula(Display display) {
        super(display);
    }

    /**
     * Ticks weather before map and actor turns so terrain changes are visible on the next draw.
     */
    @Override
    protected void gameLoop() throws GameEngineException {
        weatherController.tick(gameMaps);
        mapWeather.tick(gameMaps);
        super.gameLoop();
    }

    /**
     * Initialise maps, actors, items, and grounds of the game world.
     * @throws Exception in case if anything goes wrong...
     */
    public void initialise() throws Exception {
        DefaultGroundCreator groundCreator = new DefaultGroundCreator();
        groundCreator.registerGround('.', Dirt::new);
        groundCreator.registerGround('#', Wall::new);
        groundCreator.registerGround('~', Puddle::new);
        groundCreator.registerGround('_', Floor::new);
        groundCreator.registerGround('=', AluminiumDoor::new);
        groundCreator.registerGround('◎', MagicCircle::new);
        groundCreator.registerGround('N', IronDoor::new);
        groundCreator.registerGround('M', TitaniumDoor::new);
        groundCreator.registerGround('≈', ToxicWaste::new);
        groundCreator.registerGround('Φ', TeleportationTube::new);
        groundCreator.registerGround('y', () -> new FleshyTree(
                new FleshySprout(
                        new MatureUndeadSpawner(),
                        new GrowingBehaviour<>(20, 0.25,() -> new FleshyMatureTree(
                                new ScrapSnatcherSpawner(),
                                new GrowingBehaviour<>(35, 0.50, FleshyMonolith::new)
                        ))
                )
        ));

        List<String> moon99Deprecated = Arrays.asList(
                "y...................########################################",
                "...#######..........#__________________#___________________#",
                "...#_____#..Φ.......=__________________M___________________#",
                "...#_____=...~......#__________________#___________________#",
                "...#_____#..~~~.....########=#####=#####___#############___#",
                "...#######.~~~~.....#______#_#_________#___N___________#___#",
                ".........~~~~.......#_Φ____#_#_________#####___________#####",
                "....................#______=_#_________#___________________#",
                "......~.............#______#_#_________#___________________#",
                ".....~~~............#______#_###########___#############___#",
                ".....~..............#______#___________#___#___________#___#",
                "....................=______#___________=___=___________=___#",
                "....................#______#############___#############___#",
                ".........~~~~.......#______#___________#####################",
                "........~~~~~~......#______#___________=___________________#",
                ".........~~~~.......#______#___________#___________________#",
                "....................#______#############___#############___#",
                "....................#______#___________#___#___________#___#",
                "..~.................#______=___________=___=___________=___#",
                "....................########################################"
        );

        GameMap moon99DeprecatedMap = new GameMap("99-Deprecated", groundCreator, moon99Deprecated);
        this.addGameMap(moon99DeprecatedMap);
        moon99DeprecatedMap.at(10, 2).setGround(new Supercomputer());

        //add spawner to moon facility
        moon99DeprecatedMap.at(26,15).setGround(new Hole(new DeprecatedHoleSpawner()));
        moon99DeprecatedMap.at(50,15).setGround(new Hole(new DeprecatedHoleSpawner()));

        //add items to moon facility
        moon99DeprecatedMap.at(25,3).addItem(new Apple());
        moon99DeprecatedMap.at(45, 2).addItem(new Cookie());
        moon99DeprecatedMap.at(31, 6).addItem(new Lantern());
        moon99DeprecatedMap.at(25,8).addItem(new FloppyDisk());
        moon99DeprecatedMap.at(45,15).addItem(new CRTMonitor());
        moon99DeprecatedMap.at(8,4).addItem(new AlienCubes());

        //Create inventories for each player
        AccessCard level1 = new AccessCardLevel1();
        AccessCard level2 = new AccessCardLevel2();
        AccessCard level3 = new AccessCardLevel3();
        FirstAidKit firstAidKit = new FirstAidKit();
        SterilisationBox sterilisationBox = new SterilisationBox();

        WeightLimitedInventory inventory1 = new WeightLimitedInventory(50);
        inventory1.add(new Flask());
        inventory1.add(new AlienArtifact());
        inventory1.add(level1);

        WeightLimitedInventory inventory2 = new WeightLimitedInventory(50);
        inventory2.add(new Flask());
        inventory2.add(firstAidKit);
        inventory2.add(level2);

        WeightLimitedInventory inventory3 = new WeightLimitedInventory(50);
        inventory3.add(new Flask());
        inventory3.add(sterilisationBox);
        inventory3.add(level3);

        WeightLimitedInventory inventory4 = new WeightLimitedInventory(50);
        inventory4.add(new Flask());

        WeightLimitedInventory inventory5 = new WeightLimitedInventory(50);
        inventory5.add(new Flask());

        // BEHOLD, LOCAL MULTIPLAYER!!!
        ContractedWorker contractedWorker1 = new ContractedWorker("#1 Bob", 'ඞ', 10, inventory1);
        ContractedWorker contractedWorker2 = new ContractedWorker("#2 Tom", 'ඞ', 10, inventory2);
        ContractedWorker contractedWorker3 = new ContractedWorker("#3 Sarah", 'ඞ', 10, inventory3);
        ContractedWorker contractedWorker4 = new ContractedWorker("#4 Julie", 'ඞ', 10, inventory4);
        ContractedWorker contractedWorker5 = new ContractedWorker("#5 Rick", 'ඞ', 10, inventory5);
        this.addPlayer(contractedWorker1, moon99DeprecatedMap.at(1, 1));
        this.addPlayer(contractedWorker2, moon99DeprecatedMap.at(7, 2));
        this.addPlayer(contractedWorker3, moon99DeprecatedMap.at(8, 2));
        this.addPlayer(contractedWorker4, moon99DeprecatedMap.at(6, 4));
        this.addPlayer(contractedWorker5, moon99DeprecatedMap.at(8, 4));

        weatherController = new WeatherController(
                List.of(new AcidRain(), new MeteorStorm(), new SolarFlare()));
        mapWeather = new MapWeather();
    }
}
