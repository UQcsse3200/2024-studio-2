package com.csse3200.game.components.tasks;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.areas.forest.ForestGameArea;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A task that allows an entity to chase the player and fire projectiles at the player when within range.
 * The Leviathan will play a heartbeat sound to create tension while chasing the player.
 */
public class LeviathanTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(LeviathanTask.class);

    // Parameters for task execution
    private final int priority;
    private final float viewDistance;
    private final float maxChaseDistance;
    private final float fireRange;
    private final float waitTime;

    // Target entity to chase
    private final Entity target;

    // Physics and rendering services
    private final PhysicsEngine physics;
    private final DebugRenderer debugRenderer;

    // Raycast hit for visibility checks
    private final RaycastHit hit = new RaycastHit();

    // Game timer for controlling firing intervals
    private final GameTime timer;
    private long lastShotTime;

    // Number of shots fired
    private int numShots = 0;

    // Movement task for chasing
    private MovementTask movementTask;

    // Heartbeat sound for tension
    private Music heartbeatSound;
    private static final String HEARTBEAT = "sounds/heartbeat.mp3";

    // Speed of the Leviathan
    private final Vector2 bossSpeed;

    // Direction of the chase: false for left, true for right
    private boolean chaseDir = false;

    /**
     * Constructs a LeviathanTask.
     *
     * @param target            The entity to chase.
     * @param priority          The task priority when chasing.
     * @param viewDistance      Maximum distance to start chasing.
     * @param maxChaseDistance  Maximum distance for the entity to chase before giving up.
     * @param fireRange         Distance within which the entity will start firing projectiles.
     * @param waitTime          Time between firing projectiles.
     */
    public LeviathanTask(Entity target, int priority, float viewDistance, float maxChaseDistance, float fireRange, float waitTime) {
        this.target = target;
        this.priority = priority;
        this.viewDistance = viewDistance;
        this.maxChaseDistance = maxChaseDistance;
        this.fireRange = fireRange;
        this.waitTime = waitTime;
        this.physics = ServiceLocator.getPhysicsService().getPhysics();
        this.debugRenderer = ServiceLocator.getRenderService().getDebug();
        this.timer = ServiceLocator.getTimeSource();
        this.bossSpeed = new Vector2(2.0f, 2.0f);
        this.lastShotTime = timer.getTime();
    }

    @Override
    public void start() {
        super.start();
        Vector2 currentPos = owner.getEntity().getPosition();
        Vector2 targetPos = target.getPosition();
        movementTask = new MovementTask(targetPos, bossSpeed);
        movementTask.create(owner);
        movementTask.start();

        playTensionSound();
        target.getEvents().trigger("startHealthBarBeating");

        // Trigger chase direction
        if (targetPos.x - currentPos.x < 0) {
            this.owner.getEntity().getEvents().trigger("chaseLeft");
        } else {
            this.owner.getEntity().getEvents().trigger("chaseRight");
        }
    }

    @Override
    public void update() {
        Vector2 targetPos = target.getPosition();
        Vector2 currentPos = owner.getEntity().getPosition();

        if (targetPos.x - currentPos.x < 0 && chaseDir) {
            chaseDir = false;
            this.owner.getEntity().getEvents().trigger("chaseLeft");
        } else if (targetPos.x - currentPos.x > 0 && !chaseDir){
            chaseDir = true;
            this.owner.getEntity().getEvents().trigger("chaseRight");
        }

        movementTask.setTarget(targetPos);
        movementTask.update();
        if (movementTask.getStatus() != Status.ACTIVE) {
            movementTask.start();
        }

        // Check if should shoot
        if ((getDistanceToTarget() <= fireRange && timer.getTime() - lastShotTime > waitTime) || numShots == 0) {
            startShooting();
        }
    }

    @Override
    public void stop() {
        super.stop();
        movementTask.stop();

        stopTensionSound();
        target.getEvents().trigger("stopHealthBarBeating");
    }

    @Override
    public int getPriority() {
        if (status == Status.ACTIVE) {
            return getActivePriority();
        }
        return getInactivePriority();
    }

    /**
     * Calculates the distance to the target entity.
     *
     * @return the distance to the target.
     */
    private float getDistanceToTarget() {
        return owner.getEntity().getPosition().dst(target.getPosition());
    }

    /**
     * Gets the priority while the task is active.
     *
     * @return the priority if the task is active, otherwise -1.
     */
    private int getActivePriority() {
        float dst = getDistanceToTarget();
        if (dst > maxChaseDistance || !isTargetVisible()) {
            return -1; // Too far, stop chasing
        }
        return priority;
    }

    /**
     * Gets the priority while the task is inactive.
     *
     * @return the priority if the task is inactive, otherwise -1.
     */
    private int getInactivePriority() {
        float dst = getDistanceToTarget();
        if (dst < viewDistance && isTargetVisible()) {
            return priority;
        }
        return -1;
    }

    /**
     * Checks if the target entity is visible, meaning there are no obstacles blocking the line of sight.
     *
     * @return true if the target is visible; false otherwise.
     */
    private boolean isTargetVisible() {
        Vector2 from = owner.getEntity().getCenterPosition();
        Vector2 to = target.getCenterPosition();

        // If there is an obstacle in the path to the player, not visible.
        if (physics.raycast(from, to, PhysicsLayer.OBSTACLE, hit)) {
            debugRenderer.drawLine(from, hit.point);
            return false;
        }
        debugRenderer.drawLine(from, to);
        return true;
    }

    /**
     * Initiates shooting projectiles at the target.
     */
    private void startShooting() {
        logger.debug("Shooting at target");
        lastShotTime = timer.getTime();
        numShots++;
        owner.getEntity().getEvents().trigger("spawnWaterSpiral", owner.getEntity());
    }

    /**
     * Plays the tension sound to enhance the experience during the chase.
     */
    private void playTensionSound() {
        if (heartbeatSound == null && ServiceLocator.getResourceService() != null) {
            heartbeatSound = ServiceLocator.getResourceService().getAsset(HEARTBEAT, Music.class);
            heartbeatSound.setLooping(true);
            heartbeatSound.setVolume(1.0f);
        }
        if (heartbeatSound != null) {
            ForestGameArea.puMusic();
            heartbeatSound.play();
        }
    }

    /**
     * Stops playing the tension sound.
     */
    private void stopTensionSound() {
        if (heartbeatSound != null) {
            ForestGameArea.pMusic();
            heartbeatSound.stop();
        }
    }
}
