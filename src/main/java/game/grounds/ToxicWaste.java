package game.grounds;

import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.enums.WeatherCapability;

/**
 * The sour candy that destroys a persons mouth. The sour candy that
 * dissolves the tongue's taste receptors, that is Toxic waste...
 * Wait a minute sorry, this is just toxic sludge on the ground, not too dissimilar to the sour
 * candy known as toxic waste.
 */
public class ToxicWaste extends Ground {

    /**
     * The constructor for ToxicWaste
     */
    public ToxicWaste() {
        super('≈', "Toxic Waste");
        this.enableAbility(WeatherCapability.CORROSIVE_POOL);
    }

    /**
     * Damages the unfortunate actor standing on the toxic waste.
     * @param location The location of the Ground
     */
    @Override
    public void tick(Location location){
        if (location.containsAnActor()){
            int damage_per_turn = 1;
            location.getActor().hurt(damage_per_turn);
        }

    }}
