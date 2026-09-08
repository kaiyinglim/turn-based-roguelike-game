package game.enemies;

import game.effects.stateful.FreezeNearbyWorkersEffect;
import game.effects.stateful.PoisonNearbyWorkersEffect;
import game.effects.stateful.PullNearbyItemsEffect;
import game.enums.EnemyAbility;
import game.loaders.StatefulMoonEnemy;
import game.enums.StatefulCreatureStateId;
import game.states.mannequin.MannequinCollectorState;
import game.states.mannequin.MannequinBerserkState;
import game.states.mannequin.MannequinIdleState;
import game.states.mannequin.MannequinStateMemory;
import game.states.mannequin.MannequinStalkerState;
import game.weapons.MannequinGrip;

/**
 * Mannequin stateful moon enemy.
 *
 * @author Lim Kai YIng
 * @version 25.0.2
 */
public class Mannequin extends StatefulMoonEnemy implements MannequinStateMemory {

    private static final char DISPLAY_CHAR = 'M';
    private static final int DEFAULT_HIT_POINTS = 50;

    /** Turns adjacent workers stay {@link game.statuses.Frozen} when entering idle. */
    private static final int IDLE_ENTRY_FREEZE_TURNS = 2;
    /** Poison duration applied to adjacent workers when entering berserk. */
    private static final int BERSERK_ENTRY_POISON_TURNS = 3;

    /**
     * Counts turns spent in idle with no adjacent workers;
     * reset when any worker is adjacent or when transitioning to active or berserk.
     */
    private int idleLonelyTurns;

    /**
     * Counts consecutive turns where no item was picked up;
     * cleared on pickup or when leaving active pressure rules.
     */
    private int noPickupTurns;

    /**
     * Constructs a mannequin with default display stats and
     * registers idle, collector, berserk, and stalker states.
     */
    public Mannequin() {
        super("Mannequin", DISPLAY_CHAR, DEFAULT_HIT_POINTS);
        this.enableAbility(EnemyAbility.IS_ENEMY);
        this.setIntrinsicWeapon(new MannequinGrip());

        addState(new MannequinIdleState());
        addState(new MannequinCollectorState());
        addState(new MannequinStalkerState());
        addState(new MannequinBerserkState());
        setInitialState(StatefulCreatureStateId.MANNEQUIN_IDLE);

        addEnteringStateEffect(StatefulCreatureStateId.MANNEQUIN_IDLE, new FreezeNearbyWorkersEffect(IDLE_ENTRY_FREEZE_TURNS));
        PullNearbyItemsEffect pull = new PullNearbyItemsEffect();
        addEnteringStateEffect(StatefulCreatureStateId.MANNEQUIN_COLLECTOR, pull);
        addEnteringStateEffect(StatefulCreatureStateId.MANNEQUIN_STALKER, pull);
        addEnteringStateEffect(StatefulCreatureStateId.MANNEQUIN_BERSERK, new PoisonNearbyWorkersEffect(BERSERK_ENTRY_POISON_TURNS));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIdleLonelyTurns() {
        return idleLonelyTurns;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void incrementIdleLonelyTurns() {
        idleLonelyTurns++;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetIdleLonelyTurns() {
        idleLonelyTurns = 0;
    }

    /**
     * Resets both mannequin streak counters whenever a behavioural transition commits.
     *
     * @param newStateId the state id that was just entered
     */
    @Override
    protected void onEnteredState(StatefulCreatureStateId newStateId) {
        resetIdleLonelyTurns();
        clearNoPickupTurns();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNoPickupTurns() {
        return noPickupTurns;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearNoPickupTurns() {
        noPickupTurns = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void incrementNoPickupTurns() {
        noPickupTurns++;
    }
}
