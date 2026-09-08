package edu.monash.fit2099.engine.actors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Inventory;
import edu.monash.fit2099.engine.positions.GameMap;

import java.util.ArrayList;
import static org.mockito.Mockito.*;

public class TestActor extends Actor {

    public TestActor() {
        super("Test Dummy", 'T', 100, mock(Inventory.class));
        when(this.getInventory().getItems()).thenReturn(new ArrayList<>());
    }

    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        return null;
    }
}