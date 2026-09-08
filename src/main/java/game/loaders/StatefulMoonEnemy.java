package game.loaders;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import game.interfaces.TransitionEffect;
import game.interfaces.CreatureState;
import game.enums.StatefulCreatureStateId;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Moon enemy driven each turn by a fixed set of {@link CreatureState} behavioural modes.
 * The active mode selects the next mode; optional entry {@link TransitionEffect}s run when the mode
 * changes; then that mode's ordinary action is returned.
 *
 * @author Lim Kai YIng
 * @version 25.0.2
 */
public abstract class StatefulMoonEnemy extends MoonEnemy {

    private StatefulCreatureStateId currentStateId;
    private Map<StatefulCreatureStateId, CreatureState> states;
    private Map<StatefulCreatureStateId, TransitionEffect> enteringStateEffects;

    /**
     * Constructs a StatefulMoonEnemy with the supplied name, display character, and hit points.
     * Initializes the states and entering-state effects maps.
     *
     * @param name        display name of the enemy
     * @param displayChar display character of the enemy
     * @param hitPoints   hit points of the enemy
     */
    protected StatefulMoonEnemy(String name, char displayChar, int hitPoints) {
        super(name, displayChar, hitPoints);
        this.states = new HashMap<>();
        this.enteringStateEffects = new EnumMap<>(StatefulCreatureStateId.class);
    }

    /**
     * Adds a state to the enemy's state map with the state's identifier as the key.
     * If the state is null, or already registered, an exception is thrown.
     * 
     * @param state the state to add to the enemy's state map
     */
    protected void addState(CreatureState state) {
        if (state == null) {
            throw new IllegalArgumentException("State cannot be null");
        }
        if (states.containsKey(state.getStateId())) {
            throw new IllegalArgumentException("State already registered");
        }
        states.put(state.getStateId(), state);
    }

    /**
     * Sets the initial state of the creature.
     *
     * @param stateId initial state identifier
     */
    protected void setInitialState(StatefulCreatureStateId stateId) {
        if (stateId == null) {
            throw new IllegalArgumentException("State ID cannot be null");
        }
        if (!states.containsKey(stateId)) {
            throw new IllegalArgumentException("State not registered");
        }
        this.currentStateId = stateId;
    }

    /**
     * Registers an effect run when the creature enters {@code enteredState}.
     * At most one effect per entered state id is allowed.
     *
     * @param enteredState behavioural state being entered
     * @param effect       effect executed on entry to that state
     */
    protected void addEnteringStateEffect(StatefulCreatureStateId enteredState, TransitionEffect effect) {
        if (enteredState == null || effect == null) {
            throw new IllegalArgumentException("Entered state and effect must be non-null");
        }
        if (!states.containsKey(enteredState)) {
            throw new IllegalArgumentException("Entering state effect only allowed for registered states: " + enteredState);
        }
        if (enteringStateEffects.containsKey(enteredState)) {
            throw new IllegalArgumentException("Entering state effect already registered");
        }
        enteringStateEffects.put(enteredState, effect);
    }

    /**
     * Text form including behavioural state: {@code Name(STATE_ENUM)}, e.g.
     * {@code Mannequin(MANNEQUIN_IDLE)}. If no state is set yet, delegates to {@link Actor#toString()}.
     *
     * @return display name with current {@link StatefulCreatureStateId}, or the default actor string
     */
    @Override
    public String toString() {
        if (currentStateId == null) {
            return super.toString();
        }
        return name + "(" + currentStateId + ")";
    }

    /**
     * Hook invoked once after a transition when {@code currentStateId} has changed.
     *
     * @param newStateId the state id that was just entered
     */
    protected void onEnteredState(StatefulCreatureStateId newStateId) {
    }

    /**
     * Resolves this turn's behavioural mode: evaluate {@code getNextState}; if the mode changes,
     * apply the entry effect keyed by the new mode (when present), commit the new mode,
     * call {@link #onEnteredState(StatefulCreatureStateId)}, then return the resolved
     * mode's ordinary action.
     *
     * @param actions    menu actions (unused for AI enemies)
     * @param lastAction previously executed action
     * @param map        the map containing the Actor
     * @param display    the I/O object to which messages may be written
     * @return the action selected for this turn
     * @throws IllegalStateException if no initial state was set, the current state is missing
     *                                 from the state table, {@code getNextState} returned
     *                                 {@code null} or an unregistered id, or the resolved
     *                                 state object is missing
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        if (currentStateId == null) {
            throw new IllegalStateException(getClass().getSimpleName() + " has no current state (setInitialState was not called)");
        }
        StatefulCreatureStateId activeId = currentStateId;

        CreatureState currentState = states.get(activeId);
        if (currentState == null) {
            throw new IllegalStateException(getClass().getSimpleName() + " has current state " + activeId + " but no registered CreatureState");
        }

        StatefulCreatureStateId nextStateId = currentState.getNextState(this, map);
        if (nextStateId == null || !states.containsKey(nextStateId)) {
            throw new IllegalStateException(getClass().getSimpleName() + " resolved invalid next state: " + nextStateId);
        }

        if (nextStateId != activeId) {
            TransitionEffect effect = enteringStateEffects.get(nextStateId);
            if (effect != null) {
                effect.apply(this, activeId, nextStateId, map);
            }
            currentStateId = nextStateId;
            onEnteredState(nextStateId);
        }

        CreatureState resolvedState = states.get(currentStateId);
        if (resolvedState == null) {
            throw new IllegalStateException(getClass().getSimpleName() + " missing resolved state for " + currentStateId);
        }
        return resolvedState.getAction(this, map);
    }
}
