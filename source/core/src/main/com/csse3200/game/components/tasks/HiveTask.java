package com.csse3200.game.components.tasks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.EnemyFactory;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Task for a Hive entity to periodically spawn bees after a specified waiting period.
 * Bees are spawned at random positions near the hive, and the task also handles
 * the disposal of the bees and the hive entity when the player gets close enough.
 * Requires an entity with a PhysicsMovementComponent.
 */
public class HiveTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(HiveTask.class);

    private final float waitTime; // Time to wait between bee spawns
    private final Entity target;  // The target that bees will chase
    private float elapsedTime = 0; // Tracks elapsed time for spawning
    private ArrayList<Entity> entities; // List of spawned bee entities

    /**
     * Creates a new HiveTask for the specified target entity and initializes
     * the waiting time for spawning bees.
     *
     * @param target The entity that the bees will target.
     */
    public HiveTask(Entity target) {
        this.target = target;
        waitTime = 15f; // Time between spawning bees (7 seconds)
    }

    @Override
    public int getPriority() {
        return 11; // Low priority task
    }

    /**
     * Starts the task by triggering the floating animation and initializing the list of bees.
     */
    @Override
    public void start() {
        super.start();
        owner.getEntity().getEvents().trigger("float");
        entities = new ArrayList<>();
    }

    /**
     * Updates the task every frame. It increments elapsed time to track when
     * to spawn a new bee. It also checks if the player is within a certain distance
     * of the hive, and if so, it disposes of the hive and all spawned bees.
     */
    @Override
    public void update() {
        System.out.println(elapsedTime);
        elapsedTime += Gdx.graphics.getDeltaTime(); // Increment elapsed time by the frame delta time

        float ownerX = owner.getEntity().getPosition().x;
        float ownerY = owner.getEntity().getPosition().y;

        float playerX = target.getPosition().x;
        float playerY = target.getPosition().y;

        System.out.println(Vector2.dst2(ownerX, ownerY, playerX, playerY));

        // If the player is close enough to the hive, dispose of the hive and all bees
        if (Vector2.dst2(ownerX, ownerY, playerX, playerY) < 10f) {
            for (Entity e : entities) {
                e.setEnabled(false);
                AnimationRenderComponent animationRenderComponent = e.getComponent(AnimationRenderComponent.class);
                animationRenderComponent.stopAnimation();
                e.specialDispose(); // Dispose of the bee entity
            }
            owner.getEntity().setEnabled(false); // Disable and dispose of the hive
            AnimationRenderComponent animationRenderComponent = owner.getEntity().getComponent(AnimationRenderComponent.class);
            animationRenderComponent.stopAnimation();
            owner.getEntity().specialDispose();
        }

        // Spawn a new bee after the specified waiting time
        if (elapsedTime > waitTime) {
            spawnBee(); // Spawn a bee when time exceeds the wait time
            elapsedTime = 0; // Reset elapsed time
        }

    }

    /**
     * Spawns a new bee entity at a random position near the hive and adds it to
     * the list of active bees. The bee is created via the EnemyFactory, and an event
     * is triggered to spawn it in the game world.
     */
    private void spawnBee() {
        Entity bee = EnemyFactory.createBee(target); // Create a new bee targeting the player

        entities.add(bee); // Add the bee to the list of active entities

        // Randomize the bee's spawn position around the hive
        Vector2 hivePos = owner.getEntity().getPosition();
        Vector2 spawnPos = RandomUtils.random(new Vector2(hivePos.x - 2, hivePos.y - 2), new Vector2(hivePos.x + 2, hivePos.y + 2));

        // Trigger an event to spawn the bee at the randomized position
        this.owner.getEntity().getEvents().trigger("spawnBee", bee, spawnPos);
    }
}

