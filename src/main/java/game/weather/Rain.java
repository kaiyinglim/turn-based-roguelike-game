package game.weather;

import edu.monash.fit2099.engine.positions.GameMap;
import game.systems.RandomGround;
import game.grounds.Puddle;

/**
 * The Rain class, will define the general features of how rain works on the map.
 */
public class Rain {
    /**
     * the number of puddles depending on the precipitation
     */
    private final int no_of_puddles;

    /**
     * Constructor for the Rain class
     * @param no_of_puddles is the number of puddles depending on precipitation
     */
    public Rain(int no_of_puddles) {
        this.no_of_puddles = no_of_puddles;
    }

    /**
     * Changes random grounds to a puddle depending on the amount of puddles,
     * and the puddle multiplier
     * @param map
     */
    public void raining(GameMap map){
        RandomGround randomGround = new RandomGround();
        for (int i = 0; i < no_of_puddles ; i++) {
            randomGround.RandomAccessibleGround(map);
            map.at(randomGround.getx_cord(), randomGround.gety_cord()).setGround(new Puddle());
        }
    }

}
