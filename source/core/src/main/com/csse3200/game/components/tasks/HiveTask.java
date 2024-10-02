package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.components.npc.FrogAnimationController;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.EnemyFactory;
import com.csse3200.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
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
    ArrayList<Entity> bees;

    /**
     * Creates a new HiveTask for the specified target entity.
     * Initializes the list of bees and the target.
     *
     * @param target The entity that the bees will target.
     */
    public HiveTask(Entity target) {
        this.target = target;
        bees = new ArrayList<Entity>();
    }
    
    @Override
    public int getPriority() {
        return 1; // Low priority task
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
        waitTask = new WaitTask(1.0f);
        waitTask.create(owner);
        waitTask.start();
    }

    /**
     * Updates the task, checking if any bees need to be spawned.
     * If no bees exist and the waiting task is active, it stops the waiting task and spawns a bee.
     * Otherwise, it starts a new waiting task.
     */
    @Override
    public void update() {
        if (bees.isEmpty() && waitTask.getStatus() == Status.ACTIVE) {
            waitTask.stop();
            spawnBee();
        } else{
            // Ensure some condition to stop waiting or spawn more bees
            startWaiting();
        }
    }

    /**
     * Spawns a new bee entity at the hive's position and adds it to the list of bees.
     * The bee is created via the EnemyFactory, and an event is triggered to spawn it at the hive's location.
     */
    private void spawnBee() {
        Entity bee = EnemyFactory.createBee(target);
        bees.add(bee);
        Vector2 pos = new Vector2(owner.getEntity().getPosition().x + 1, owner.getEntity().getPosition().y);
        this.owner.getEntity().getEvents().trigger("spawnBee", bee, pos);
        startWaiting();
    }
}
