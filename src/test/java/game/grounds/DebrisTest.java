package game.grounds;

import edu.monash.fit2099.engine.actors.TestActor;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DebrisTest {

    @Test
    void testBlocksActorEntry() {
        Debris debris = new Debris();
        TestActor actor = new TestActor();

        assertFalse(debris.canActorEnter(actor),
                "Debris should block actor movement.");
    }

    @Test
    void testDecaysToFloorAfterDuration() {
        Debris debris = new Debris(3);
        Location location = mock(Location.class);

        debris.tick(location);
        debris.tick(location);
        verify(location, never()).setGround(any(Ground.class));

        debris.tick(location);

        verify(location).setGround(any(Floor.class));
    }

    @Test
    void testSingleTurnDebrisDecaysImmediately() {
        Debris debris = new Debris(1);
        Location location = mock(Location.class);

        debris.tick(location);

        ArgumentCaptor<Ground> captor = ArgumentCaptor.forClass(Ground.class);
        verify(location).setGround(captor.capture());
        assertInstanceOf(Floor.class, captor.getValue(),
                "One-turn debris should decay on its first tick.");
    }
}
