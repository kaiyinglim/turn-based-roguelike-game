package game.actions;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.interfaces.Cuttable;
import game.loaders.ContractedWorker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CutActionTest {

    private ContractedWorker worker;
    private Cuttable cuttable;
    private Location location;
    private GameMap map;
    private CutAction cutAction;

    @BeforeEach
    void setUp() {

        worker = mock(ContractedWorker.class);
        cuttable = mock(Cuttable.class);
        location = mock(Location.class);
        map = mock(GameMap.class);

        cutAction = new CutAction(worker, cuttable, location);
    }

    @Test
    void executeCallsCuttableBehaviour() {

        Actor actor = mock(Actor.class);

        cutAction.execute(actor, map);

        verify(cuttable).cutBy(worker, map, location);
    }

    @Test
    void executeReturnsCuttableResult() {

        Actor actor = mock(Actor.class);

        when(cuttable.cutBy(worker, map, location))
                .thenReturn("Vent cut successfully");

        String result = cutAction.execute(actor, map);

        assertEquals("Vent cut successfully", result);
    }

    @Test
    void menuDescriptionContainsActorAndTarget() {

        Actor actor = mock(Actor.class);

        when(actor.toString()).thenReturn("Worker");
        when(cuttable.toString()).thenReturn("Vent");

        String result = cutAction.menuDescription(actor);

        assertEquals(
                "Worker cuts Vent with Plasma Cutter",
                result
        );
    }
}