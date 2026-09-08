package game.grounds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.enums.StatusAbility;
import game.statuses.EMPShock;

/**
 * Hazardous environmental ground representing an Electric Field.
 *
 * This ground type is created by deployable systems such as Turrets and
 * Mines. It forms a damaging area-of-effect hazard that disrupts and
 * weakens entities standing on it by applying continuous damage and
 * status effects.
 *
 * The Electric Field contributes to battlefield control within the
 * autonomous tactical support system by creating persistent danger zones
 * that influence movement and positioning.
 *
 * @author Suzan Xuan Loh Palmer
 * @version 25.0.3
 */
public class ElectricField extends Ground {
    /**
     * Constructs an ElectricField tile.
     *
     * Initialises the ground type as a hazardous electric zone and enables
     * its special environmental ability.
     */
    public ElectricField() {
        super('E', "Electric Field");
        enableAbility(StatusAbility.ELECTRIC_FIELD);
    }
    /**
     * Updates the Electric Field each turn.
     *
     * Any actor standing on the tile takes damage and may receive the
     * EMPShock status effect, representing electronic disruption caused
     * by the field.
     *
     * @param location the location of this ground
     */
    @Override
    public void tick(Location location) {

        if (location.containsAnActor()) {

            Actor actor = location.getActor();
            actor.hurt(1);
            System.out.println(actor + " standing on Electric Field");

            if (!actor.hasStatus(EMPShock.class)) {
                System.out.println(actor + " standing on Electric Field");
                actor.addStatus(new EMPShock(2));
            }
        }
    }
}
