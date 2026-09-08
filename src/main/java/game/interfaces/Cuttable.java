package game.interfaces;

import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.loaders.ContractedWorker;

public interface Cuttable {
    String cutBy(ContractedWorker worker, GameMap map, Location targetLocation);
}
