package game.enemies;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import game.states.cablebot.CableConnection;
import game.effects.stateful.BurnSurroundingsEffect;
import game.effects.stateful.PoisonNearbyWorkersEffect;
import game.effects.stateful.PullNearbyItemsEffect;
import game.enums.EnemyAbility;
import game.enums.WorkerAbility;
import game.loaders.StatefulMoonEnemy;
import game.enums.StatefulCreatureStateId;
import game.states.cablebot.CablebotChargeState;
import game.states.cablebot.CablebotLatchState;
import game.states.cablebot.CablebotOverloadState;
import game.states.cablebot.CablebotSurveyState;

import java.util.ArrayList;
import java.util.List;

/**
 * Cablebot stateful moon enemy:
 * attaches invisible cable connections to adjacent workers,
 * charges through those connections, and overloads to disrupt worker inventories.
 *
 * @author Lim Kai YIng
 * @version 25.0.2
 */
public class Cablebot extends StatefulMoonEnemy {

    private static final char DISPLAY_CHAR = 'C';
    private static final int DEFAULT_HIT_POINTS = 50;

    /** Poison duration for adjacent workers when entering latch. */
    private static final int LATCH_ENTRY_POISON_TURNS = 5;
    /** {@link BurnSurroundingsEffect} intensity when entering charge. */
    private static final int CHARGE_ENTRY_BURN_INTENSITY = 1;
    /** {@link BurnSurroundingsEffect} intensity when entering overload. */
    private static final int OVERLOAD_ENTRY_BURN_INTENSITY = 2;

    private static final int MAX_CHARGE = 8;
    private static final int MIN_CHARGE = 0;

    /** Current charge level, clamped in {@link #increaseCharge} / {@link #decreaseCharge}. */
    private int charge;

    /** List of active cable connections to workers. */
    private final List<CableConnection> cableConnections = new ArrayList<>();

    /**
     * Constructs a Cablebot, registers survey, latch, overload, and charge states, and starts in survey.
     */
    public Cablebot() {
        super("Cablebot", DISPLAY_CHAR, DEFAULT_HIT_POINTS);
        this.enableAbility(EnemyAbility.IS_ENEMY);

        addState(new CablebotSurveyState());
        addState(new CablebotLatchState());
        addState(new CablebotChargeState());
        addState(new CablebotOverloadState());
        setInitialState(StatefulCreatureStateId.CABLEBOT_SURVEY);

        addEnteringStateEffect(StatefulCreatureStateId.CABLEBOT_SURVEY, new PullNearbyItemsEffect());
        addEnteringStateEffect(StatefulCreatureStateId.CABLEBOT_LATCH, new PoisonNearbyWorkersEffect(LATCH_ENTRY_POISON_TURNS));
        addEnteringStateEffect(StatefulCreatureStateId.CABLEBOT_CHARGE, new BurnSurroundingsEffect(CHARGE_ENTRY_BURN_INTENSITY));
        addEnteringStateEffect(StatefulCreatureStateId.CABLEBOT_OVERLOAD, new BurnSurroundingsEffect(OVERLOAD_ENTRY_BURN_INTENSITY));
    }

    /**
     * Advances attachment time on all cables, prunes invalid connections, 
     * then resolves transitions and returns the ordinary action.
     *
     * @param actions    menu actions
     * @param lastAction action taken on the previous turn
     * @param map        map containing this actor
     * @param display    display for messages
     * @return the action to execute this turn
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        tickConnections();
        removeBrokenConnections(map);
        return super.playTurn(actions, lastAction, map, display);
    }

    /**
     * Increments the number of turns each cable has been attached for every stored connection.
     */
    private void tickConnections() {
        for (CableConnection connection : cableConnections) {
            connection.tick();
        }
    }

    /**
     * @return current charge level
     */
    public int getCharge() {
        return charge;
    }

    /**
     * Increases charge by the given amount, capped at the maximum charge.
     *
     * @param amount non-negative charge to add
     */
    public void increaseCharge(int amount) {
        if (amount <= 0) {
            return;
        }
        charge = Math.min(MAX_CHARGE, charge + amount);
    }

    /**
     * Decreases charge by the given amount, stops at the minimum charge.
     *
     * @param amount non-negative charge to subtract
     */
    public void decreaseCharge(int amount) {
        if (amount <= 0) {
            return;
        }
        charge = Math.max(MIN_CHARGE, charge - amount);
    }

    /**
     * @return maximum charge this Cablebot can hold
     */
    public int getMaxCharge() {
        return MAX_CHARGE;
    }

    /**
     * @return unmodifiable snapshot of active cable connections (changes to this Cablebot's list
     *         after this call are not reflected in the returned list)
     */
    public List<CableConnection> getCableConnections() {
        return List.copyOf(cableConnections);
    }

    /**
     * Registers a cable to a worker. If a connection to the same target already exists, strengthens it.
     *
     * @param connection cable to register
     */
    public void addCableConnection(CableConnection connection) {
        if (connection == null) {
            return;
        }
        CableConnection existing = getConnectionTo(connection.getTarget());
        if (existing != null) {
            existing.strengthen();
            return;
        }
        cableConnections.add(connection);
    }

    /**
     * @param target worker to look up
     * @return the connection whose {@link CableConnection#getTarget()} is {@code target}, or {@code null}
     */
    public CableConnection getConnectionTo(Actor target) {
        for (CableConnection connection : cableConnections) {
            if (connection.getTarget() == target) {
                return connection;
            }
        }
        return null;
    }

    /**
     * @param target worker to test
     * @return true if this bot already has a cable to {@code target}
     */
    public boolean hasConnectionTo(Actor target) {
        return getConnectionTo(target) != null;
    }

    /**
     * Removes a cable instance from this bot (e.g. after overload breaks it).
     *
     * @param connection connection to remove; does nothing if null or not present
     */
    public void removeCableConnection(CableConnection connection) {
        if (connection != null) {
            cableConnections.remove(connection);
        }
    }

    /**
     * @return number of active cable connections after the last {@link #removeBrokenConnections}
     */
    public int getAttachedCableCount() {
        return cableConnections.size();
    }

    /**
     * Removes connections whose target is not on the map, is unconscious,
     * lacks the worker ability flag, or is structurally broken (strength depleted or strain threshold met).
     *
     * @param map map used to validate actor presence
     */
    public void removeBrokenConnections(GameMap map) {
        cableConnections.removeIf(connection -> {
            Actor target = connection.getTarget();
            return !map.contains(target)
                    || !target.isConscious()
                    || !target.hasAbility(WorkerAbility.IS_WORKER)
                    || connection.isBroken();
        });
    }
}
