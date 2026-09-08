package game.systems;

import edu.monash.fit2099.engine.positions.GameMap;
import game.loaders.BasicInventory;
import game.loaders.ContractedWorker;

import java.util.Random;

/**
 * A class to generate random coordinates that have a ground which an actor may walk on
 * so that no actor gets fused into a wall.
 */
public class RandomGround {
    /**
     * x and y coordinates respectively
     */
    private int x_cord;
    private int y_cord;

    /**
     * The constructor for the RandomGround class
     */
    public RandomGround(){
        x_cord = 0;
        y_cord = 0;
    }

    /**
     * returns the x coordinate
     * @return the x coordinate
     */
    public int getx_cord(){
        return x_cord;
    }

    /**
     * Returns the y coordinate
     * @return the y coordinate
     */
    public int gety_cord(){
        return y_cord;
    }

    /**
     * Finds a random ground that can be accessed by a contracted worker,
     * @param map the specific map of the game.
     */
    public void RandomAccessibleGround(GameMap map){
        boolean found_valid_location = false;
        Random random = new Random();
        ContractedWorker worker = new ContractedWorker("b", 'b', 0, new BasicInventory());
        do {
            int x_random = random.nextInt(map.getXRange().max() + 1);
            int y_random = random.nextInt(map.getYRange().max() + 1);
            if (map.at(x_random, y_random).getGround().canActorEnter(worker) && !map.at(x_random, y_random).containsAnActor()) {
                x_cord = x_random;
                y_cord = y_random;
                found_valid_location = true;
            }
        }
        while (!found_valid_location);
    }
}
