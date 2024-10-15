package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.EnemyFactory;
import com.csse3200.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Task for a Hive entity to spawn bees and control their behavior.
 * The Hive periodically spawns bees after a short waiting period.
 * Requires an entity with a PhysicsMovementComponent.
 */
public class HiveTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(HiveTask.class);

    private WaitTask waitTask;
    private final Entity target;
    private final ArrayList<Entity> bees;
    private int maxBees = 3;  // The number of bees to spawn

    /**
     * Creates a new HiveTask for the specified target entity.
     * Initializes the list of bees and the target.
     *
     * @param target The entity that the bees will target.
     */
    public HiveTask(Entity target) {
        this.target = target;
        this.bees = new ArrayList<>();
    }

    /**
     * Creates a new HiveTask for the specified target entity.
     * Initializes the list of bees and the target as well as setting max bees to spawn.
     *
     * @param target The entity that the bees will target.
     * @param maxBees the maximum number of bees that can be spawned at once.
     */
    public HiveTask(Entity target, int maxBees) {
        this.target = target;
        this.bees = new ArrayList<>();
        this.maxBees = maxBees;
    }

    @Override
    public int getPriority() {
        return 11; // Low priority task
    }

    /**
     * Checks if the entity has spawned yet, if not waits, else it wanders
     */
    @Override
    public void start() {
        super.start();
        owner.getEntity().getEvents().trigger("float");
        startWaiting();
    }

    /**
     * Begins the waiting process for the hive to spawn bees.
     */
    private void startWaiting() {
        if (waitTask == null || waitTask.getStatus() != Status.ACTIVE) {
            waitTask = new WaitTask(1.0f);
            waitTask.create(owner);
            waitTask.start();
        }
    }

    /**
     * Updates the task, checking if any bees need to be spawned or respawned.
     * If less than maxBees exist and the waiting task is active, it stops the waiting task and spawns a bee.
     * Otherwise, it starts a new waiting task.
     */
    @Override
    public void update() {
        // Remove any bees that have died or are no longer valid
        bees.removeIf(bee -> bee == null || bee.getComponent(CombatStatsComponent.class).getHealth() <= 0);

        // Check if we need to spawn more bees
        if (bees.size() < maxBees && waitTask != null && waitTask.getStatus() == Status.ACTIVE) {
            waitTask.stop();
            spawnBee();
        } else {
            startWaiting();
        }
    }

    /**
     * Spawns a new bee entity at a randomized position near the hive's location
     * and adds it to the list of bees. The bee is created via the EnemyFactory, and
     * an event is triggered to spawn it at the hive's location.
     */
    private void spawnBee() {
        Entity bee = EnemyFactory.createBee(target);
        bees.add(bee);

        // Randomize the spawn position slightly around the hive
        Vector2 hivePos = owner.getEntity().getPosition();
        Vector2 spawnPos = RandomUtils.random(new Vector2(hivePos.x - 2, hivePos.y - 2), new Vector2(hivePos.x + 2, hivePos.y + 2));

        this.owner.getEntity().getEvents().trigger("spawnBee", bee, spawnPos);

        logger.info("Bee spawned targeting: " + target + " at position: " + spawnPos);
    }
}
