package game.weapons;

import edu.monash.fit2099.engine.weapons.IntrinsicWeapon;

/**
 * The class represents a humanoid creature that can use its bare fist as a weapon
 */
public class BareFist extends IntrinsicWeapon {

    /**
     * The constructor of BareFist class
     */
    public BareFist() {
        super(1, "punches", 10, "bare fist");
    }
}
