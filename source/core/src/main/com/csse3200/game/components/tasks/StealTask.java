package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.areas.ForestGameArea;
import com.csse3200.game.areas.MapHandler;
import com.csse3200.game.components.npc.FrogAnimationController;
import com.csse3200.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.physics.raycast.RaycastHit;

import java.util.Map;

/**
 * A task for an entity to "steal" items, represented by dynamically placed entities in the game world.
 * The entity finds the closest item, moves toward it, disposes of it, and then returns to the original position.
 */
public class StealTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(StealTask.class);

    private final float waitTime;
    private Vector2 startPos;
    private MovementTask movementTask;
    private MovementTask returnTask;
    private WaitTask waitTask;
    private Task currentTask;
    private boolean isSpawned = false;
    private static final String LEFT = "wanderLeft";
    private static final String RIGHT = "wanderRight";
    private Map<Integer, Entity> items = null;
    protected final RaycastHit hit = new RaycastHit();
    Entity currentitem = null;
    Integer currentId = null;
    Vector2 origin;

    /**
     * Creates a StealTask that is responsible for finding, moving toward, and "stealing" items in the game world.
     *
     * @param items    A map of dynamically placed items in the game world with their corresponding IDs.
     * @param waitTime Time in seconds to wait before the entity moves again.
     */
    public StealTask(Map<Integer, Entity> items, float waitTime) {
        this.items = items;
        this.waitTime = waitTime;
    }

    @Override
    public int getPriority() {
        return 1; // Low priority task
    }

    /**
     * Starts the wandering task, which involves finding the nearest item and moving toward it.
     * The entity will change directions based on the relative position of the item.
     */
    private void startWandering() {
        startPos = owner.getEntity().getPosition();
        logger.debug("Starting wandering");
        Vector2 newPos = getClosestItem();
        movementTask = new MovementTask(newPos, 0.5F);
        movementTask.create(owner);
        movementTask.start();
        currentTask = movementTask;
        if (newPos.x - startPos.x < 0) {
            logger.debug("wandering right");
            this.owner.getEntity().getEvents().trigger(LEFT);
        } else {
            logger.debug("wandering left");
            this.owner.getEntity().getEvents().trigger(RIGHT);
        }
    }

    /**
     * Returns the entity to its original position after it successfully steals an item.
     * The entity will move toward the origin and trigger appropriate direction events.
     */
    private void startReturning() {
        startPos = owner.getEntity().getPosition();
        returnTask = new MovementTask(origin, 0.5F);
        returnTask.create(owner);
        returnTask.start();
        currentTask = returnTask;
        if (origin.x - startPos.x < 0) {
            logger.debug("wandering right");
            this.owner.getEntity().getEvents().trigger(LEFT);
        } else {
            logger.debug("wandering left");
            this.owner.getEntity().getEvents().trigger(RIGHT);
        }
    }

    /**
     * Finds the closest item within the specified range (viewDistance).
     * The method returns the position of the closest item and also updates the current item and its ID.
     *
     * @return The position of the closest item or null if no item is found.
     */
    private Vector2 getClosestItem() {
        Map<Integer, Entity> dynamicItems = ((ForestGameArea) MapHandler.getCurrentMap()).getDynamicItems();
        float closestDistance = 1000;  // Limit to viewDistance
        Entity nearestTarget = null;

        System.out.println("findNearestTarget");

        for (Map.Entry<Integer, Entity> entry : dynamicItems.entrySet()) {
            Integer itemId = entry.getKey();  // Get the ID (key) from the map
            Entity item = entry.getValue();   // Get the Entity (value) from the map

            System.out.println(item);
            float distance = getDistanceToTarget(item);

            // Check if the item is within viewDistance and is closer than the current closest target
            if (distance < closestDistance) {
                closestDistance = distance;
                nearestTarget = item;
                currentitem = nearestTarget;
                currentId = itemId;  // Set the currentId to the closest item's ID
            }
        }

        // Return the position of the nearest target, or handle the case if no target was found
        if (nearestTarget != null) {
            currentitem = nearestTarget;
            return nearestTarget.getPosition();
        } else {
            System.out.println("No target found within view distance");
            return null;  // Or return a default Vector2, e.g., new Vector2(0, 0)
        }
    }

    /**
     * Calculates the distance between the entity performing the task and the target item.
     *
     * @param item The target entity (item) to calculate the distance to.
     * @return The distance between the entity and the target item.
     */
    private float getDistanceToTarget(Entity item) {
        return owner.getEntity().getPosition().dst(item.getPosition());
    }

    /**
     * Starts the waiting phase where the entity pauses its movement for a specified amount of time.
     * This method triggers the spawn animation if available.
     */
    private void startWaiting() {
        logger.debug("Starting waiting");
        if (owner.getEntity().getComponent(FrogAnimationController.class) != null) {
            owner.getEntity().getEvents().trigger("spawnStart"); // plays still animation
        }
        waitTask = new WaitTask(waitTime);
        waitTask.create(owner);
        swapTask(waitTask);
    }

    /**
     * Swaps the current task being performed by the entity with a new task.
     *
     * @param newTask The new task to start.
     */
    private void swapTask(Task newTask) {
        if (currentTask != null) {
            currentTask.stop();
        }
        currentTask = newTask;
        currentTask.start();
    }
}
