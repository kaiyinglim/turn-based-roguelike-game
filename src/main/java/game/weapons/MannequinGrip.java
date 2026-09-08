package game.weapons;

import edu.monash.fit2099.engine.weapons.IntrinsicWeapon;

/**
 * Intrinsic weapon used by the Mannequin in berserk state.
 * It has a guaranteed hit rate and high damage so an isolated worker becomes unconscious.
 */
public class MannequinGrip extends IntrinsicWeapon {

    /**
     * Constructs the Mannequin's berserk intrinsic weapon.
     */
    public MannequinGrip() {
        super(9999, "crushes", 100, "mannequin grip");
    }
}