package game.weather;

import edu.monash.fit2099.engine.actors.TestActor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.grounds.Debris;
import game.grounds.Fire;
import game.grounds.Floor;
import game.statuses.Burning;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class MeteorImpactEffectTest {

    private final MeteorImpactEffect effect = new MeteorImpactEffect();

    @Test
    void testImpactIgnitesTargetTile() throws Exception {
        GameMap map = WeatherTestMapFactory.floorMap("___");
        Location impact = WeatherTestMapFactory.at(map, 1, 0);

        effect.apply(List.of(impact), map);

        assertInstanceOf(Fire.class, impact.getGround());
    }

    @Test
    void testBlastAppliesBurningToActor() throws Exception {
        GameMap map = WeatherTestMapFactory.floorMap("___");
        Location impact = WeatherTestMapFactory.at(map, 1, 0);
        TestActor actor = new TestActor();
        WeatherTestMapFactory.addActor(map, 1, 0, actor);

        effect.apply(List.of(impact), map);

        assertFalse(actor.statusesOf(Burning.class).isEmpty(),
                "Actors in the blast radius should receive burning.");
    }

    @Test
    void testKnocksActorToOnlyAdjacentExit() {
        GameMap map = mock(GameMap.class);
        Location impact = mock(Location.class);
        Location escape = mock(Location.class);
        Exit exit = mock(Exit.class);
        TestActor actor = new TestActor();

        when(impact.containsAnActor()).thenReturn(true);
        when(impact.getActor()).thenReturn(actor);
        when(impact.getGround()).thenReturn(new Floor());
        when(impact.getItems()).thenReturn(List.of());
        when(impact.getNearbyLocations(anyInt())).thenReturn(List.of());
        when(impact.getExits()).thenReturn(List.of(exit));
        when(exit.getDestination()).thenReturn(escape);
        when(escape.canActorEnter(actor)).thenReturn(true);

        effect.apply(List.of(impact), map);

        verify(map).moveActor(actor, escape);
    }

    @Test
    void testDestroysGroundItemsIntoDebris() throws Exception {
        GameMap map = WeatherTestMapFactory.floorMap("___");
        Location blastTile = WeatherTestMapFactory.at(map, 2, 0);
        Item groundItem = mock(Item.class);
        blastTile.addItem(groundItem);

        effect.apply(List.of(WeatherTestMapFactory.at(map, 1, 0)), map);

        assertTrue(blastTile.getItems().isEmpty(), "Ground items in the blast should be destroyed.");
        assertInstanceOf(Debris.class, blastTile.getGround(),
                "Tiles with destroyed items should become impassable debris.");
    }
}
