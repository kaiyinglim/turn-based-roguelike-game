package game.enemies;

import game.behaviours.InfectBehaviour;
import game.behaviours.WanderBehaviour;
import game.enums.EnemyAbility;
import game.loaders.MoonEnemy;

/**
 * A highly invasive entity that infects a player or item around the moon's facility
 * @author FIT2099 Bryan Chan Zheng Lam
 */
public class Parasite extends MoonEnemy {

    /**
     * The constructor of Parasite class
     */
    public Parasite() {
        super("Parasite", 'x', 30);
        this.enableAbility(EnemyAbility.IS_ENEMY);
        //Has its own behaviours
        this.behaviours.put(1, new InfectBehaviour());
        this.behaviours.put(999, new WanderBehaviour());
    }
}
