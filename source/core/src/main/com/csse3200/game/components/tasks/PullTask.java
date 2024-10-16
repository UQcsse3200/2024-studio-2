package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.components.npc.FrogAnimationController;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wander around by moving a random position within a range of the starting position. Wait a little
 * bit between movements. Requires an entity with a PhysicsMovementComponent.
 */
public class PullTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(PullTask.class);

    private int priority;

    private Vector2 startPos;
    private MovementTask movementTask;
    private Task currentTask;
    private boolean isSpawned = false;
    private Entity target;
    private float viewDistance;
    private float pullDistance;
    protected final PhysicsEngine physics;
    protected final DebugRenderer debugRenderer;
    protected final RaycastHit hit = new RaycastHit();

    @Override
    public int getPriority() {
        if (getDistanceToTarget() < viewDistance && isTargetVisible()) {
            return priority;
        }
        return -1;
    }

    /**
     * @param pullDistance Distance within which the frog will be pulled toward the player.
     */
    public PullTask(int priority, Entity target, float viewDistance, float pullDistance) {
        this.priority = priority;
        this.target = target;
        this.viewDistance = viewDistance;
        this.pullDistance = pullDistance;
        physics = ServiceLocator.getPhysicsService().getPhysics();
        debugRenderer = ServiceLocator.getRenderService().getDebug();
    }

    private float getDistanceToTarget() {
        return owner.getEntity().getPosition().dst(target.getPosition());
    }

    private boolean isTargetVisible() {
        Vector2 from = owner.getEntity().getCenterPosition();
        Vector2 to = target.getCenterPosition();

        if (physics.raycast(from, to, PhysicsLayer.OBSTACLE, hit)) {
//            debugRenderer.drawLine(from, hit.getPoint());
            return false;
        }

        debugRenderer.drawLine(from, to);
        return true;
    }

    /**
     * Starts the frog behavior. If not spawned, triggers the spawn event.
     */
    @Override
    public void start() {
        super.start();
        if (!isSpawned) {
            logger.info("Triggering spawn event");
            this.owner.getEntity().getEvents().trigger("spawnStart");
            isSpawned = true;
        }
    }

    @Override
    public void update() {
        if (getDistanceToTarget() <= pullDistance) {
            pullToPlayer();
        }
    }

    private void pullToPlayer() {
        if (getDistanceToTarget() <= pullDistance) {
            logger.info("Pulling player to frog");

            // Get the positions of the frog and the player
            Vector2 frogPos = owner.getEntity().getPosition();
            Vector2 playerPos = target.getPosition();

            AnimationRenderComponent anim = owner.getEntity().getComponent(AnimationRenderComponent.class);

            // Determine the direction of the pull and trigger appropriate animations
            if(anim.isFinished()){
                if (playerPos.x > frogPos.x) {
                    // Player is to the left of the frog
                    owner.getEntity().getEvents().trigger("pullRight");  // Player is being pulled right towards frog
                } else {
                    // Player is to the right of the frog
                    owner.getEntity().getEvents().trigger("pullLeft");   // Player is being pulled left towards frog
                }
            }

            // Calculate the direction vector from player to frog
            Vector2 pullDirection = frogPos.cpy().sub(playerPos).nor(); // Normalized direction vector

            // Set the strength of the pull (you can adjust this value to control the pull speed)
            float pullStrength = 4.0f;

            // Apply movement to the player towards the frog
            Vector2 newPlayerPos = playerPos.add(pullDirection.scl(pullStrength * ServiceLocator.getTimeSource().getDeltaTime()));
            target.setPosition(newPlayerPos);
        }
    }
}