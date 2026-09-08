package game.enums;

/**
 * A class that represents the worker's ability to do something to the
 * items and grounds.
 * Currently, it has sterilise where the player can sterilise items or grounds.
 * Now, it tags itself as a worker to really make sure it is a worker.
 */
public enum WorkerAbility {
    STERILISE,
    IS_WORKER,
    IS_INFECTABLE,
    DEPLOY_UNITS,
    ALLY,
    TRIGGER_MINES
}
