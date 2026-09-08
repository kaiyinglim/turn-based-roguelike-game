package game.loaders;

import edu.monash.fit2099.engine.items.Item;
import game.items.AlienArtifact;
import game.items.AluminiumScrap;
import game.items.IndustrialFan;

/**
 * A factory class that generates random depositable items for the Snatcher's loot explosion.
 * @author FIT2099 Bryan Chan Zheng Lam
 */
public class RandomScrapGenerator {
    /**
     * Generates a random depositable scrap item.
     * @return a random Item (AluminiumScrap, IndustrialFan, or AlienArtifact)
     */
    public static Item getRandomScrap() {
        double chance = Math.random();
        if (chance < 0.33) {
            return new AluminiumScrap();
        } else if (chance < 0.66) {
            return new IndustrialFan();
        } else {
            return new AlienArtifact();
        }
    }
}
