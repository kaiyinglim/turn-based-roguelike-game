package game.weather;

import edu.monash.fit2099.engine.positions.GameMap;

import java.util.List;

/**
 * The class to cause a weather event depending on a random locations daily precipitation for the
 * moon is dangerous and chaotic.
 */
public class MapWeather {
    /**
     * The precipitation class variable
     */
    private int precipitation;

    /**
     * The constructor for the MapWeather class
     */
    public MapWeather() {
        this.precipitation = 0;
    }

    /**
     * A tick that will be called for to see if there will be some type of weather occurring
     * @param maps the list of maps that the game contains
     */
    public void tick(List<GameMap> maps) {
        int precipitation_modifier = 3;
        for (GameMap map : maps) {
            WeatherData data = new WeatherData();
            precipitation = Math.round(data.getSum_of_rain()/precipitation_modifier);
            if (precipitation > 0) {
                Rain rain = new Rain(precipitation);
                rain.raining(map);
                System.out.println("It has rained a bit on the moon");
            }
        }
    }
}
