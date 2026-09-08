package game.enemies;

import game.behaviours.ConsumeBehaviour;
import game.behaviours.WanderBehaviour;
import game.enums.EnemyAbility;
import game.loaders.MoonEnemy;

/**
 * A non-hostile, gluttonous slime, thats about it.
 */
public class Slime extends MoonEnemy {

    /**
     * The constructor of Slime class
     */
    public Slime() {
        super("Slime", '⍾', 25);
        this.enableAbility(EnemyAbility.IS_ENEMY);
        //Has its own behaviours
        this.behaviours.put(1, new ConsumeBehaviour());
        this.behaviours.put(999, new WanderBehaviour());
    }
}
