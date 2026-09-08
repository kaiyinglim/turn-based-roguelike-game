package game.interfaces;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.loaders.ContractedWorker;

public interface Depositable {
    int getDepositValue();
    String onDeposit(ContractedWorker worker, GameMap map, Location supercomputerLocation);
    Action getDepositAction(ContractedWorker worker, game.grounds.Supercomputer supercomputer, Location supercomputerLocation);
}
