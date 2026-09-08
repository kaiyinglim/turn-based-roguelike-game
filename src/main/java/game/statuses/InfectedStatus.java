package game.statuses;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.statistics.StatisticOperations;

public class InfectedStatus extends Infected {
    private final Enum<?> targetStatistic;
    private final StatisticOperations operation;
    private final int value;
    private final String tickMessage;

    private final int spawnRate;
    private final String spawnMessage;
    private int spawnCounter = 0;

    public InfectedStatus(Enum<?> targetStatistic, StatisticOperations operation, int value,
                           String tickMessage, int spawnRate, String spawnMessage) {
        this.targetStatistic = targetStatistic;
        this.operation = operation;
        this.value = value;
        this.tickMessage = tickMessage;
        this.spawnRate = spawnRate;
        this.spawnMessage = spawnMessage;

    }
    @Override
    public void tickStatus(GameEntity currEntity, Location location) {
        if (currEntity.hasStatistic(targetStatistic)) {
            // Apply the specific damage/drain
            currEntity.modifyStatistic(targetStatistic, operation, value);
            new Display().println(tickMessage);

            // Handles parasite spawning (if spawnRate > 0)
            if (spawnRate > 0) {
                spawnCounter++;
                if (spawnCounter >= spawnRate) {
                    spawnParasite(location, spawnMessage);
                    spawnCounter = 0; // Reset counter
                }
            }
        }
    }
}
