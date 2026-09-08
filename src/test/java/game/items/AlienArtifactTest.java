package game.items;

import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.loaders.ContractedWorker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AlienArtifactTest {

    @Test
    void hasCorrectSellPrice() {
        AlienArtifact artifact = new AlienArtifact();

        assertEquals(200, artifact.getSellPrice());
    }

    @Test
    void hasCorrectDepositValue() {
        AlienArtifact artifact = new AlienArtifact();

        assertEquals(100, artifact.getDepositValue());
    }

    @Test
    void depositWithNoValidLocationsReturnsFailureMessage() {

        AlienArtifact artifact = new AlienArtifact();

        ContractedWorker worker =
                mock(ContractedWorker.class);

        GameMap map = mock(GameMap.class);

        when(map.getXRange())
                .thenReturn(new edu.monash.fit2099.engine.positions.NumberRange(0,0));

        when(map.getYRange())
                .thenReturn(new edu.monash.fit2099.engine.positions.NumberRange(0,0));

        Location location = mock(Location.class);

        when(map.at(0,0)).thenReturn(location);

        when(location.containsAnActor()).thenReturn(true);

        String result =
                artifact.onDeposit(worker, map, location);

        assertTrue(result.contains("no valid location"));
    }
}