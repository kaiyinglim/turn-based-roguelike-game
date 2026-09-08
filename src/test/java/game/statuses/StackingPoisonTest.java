package game.statuses;

import edu.monash.fit2099.engine.actors.ActorStatistics;
import edu.monash.fit2099.engine.actors.TestActor;
import edu.monash.fit2099.engine.positions.Location;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class StackingPoisonTest {

    @Test
    void testTickStatusDealsBaseDamageForSingleStack() {
        TestActor actor = new TestActor();
        StackingPoison poison = new StackingPoison(3, 2);
        Location location = mock(Location.class);
        int healthBefore = actor.getStatistic(ActorStatistics.HEALTH);

        poison.tickStatus(actor, location);

        assertEquals(healthBefore - 2, actor.getStatistic(ActorStatistics.HEALTH),
                "One stack should deal base damage on each tick.");
    }

    @Test
    void testStackIncreasesDamagePerTick() {
        TestActor actor = new TestActor();
        StackingPoison poison = new StackingPoison(3, 2);
        Location location = mock(Location.class);
        poison.stack();

        poison.tickStatus(actor, location);

        assertEquals(96, actor.getStatistic(ActorStatistics.HEALTH),
                "Two stacks should deal double the base damage.");
    }

    @Test
    void testExpiresAfterDurationTicks() {
        StackingPoison poison = new StackingPoison(2, 1);
        TestActor actor = new TestActor();
        Location location = mock(Location.class);

        poison.tickStatus(actor, location);
        poison.tickStatus(actor, location);

        assertFalse(poison.isStatusActive(), "Poison should expire after its duration elapses.");
    }
}
