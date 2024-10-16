package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.areas.forest.ForestGameArea;
import com.csse3200.game.components.settingsmenu.UserSettings;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.services.AudioManager;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Represents a task for a Griffin entity that manages its movement towards a target
 * and allows it to shoot gusts of wind at the target.
 */
public class GriffinTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(GriffinTask.class);
    private final Entity target;
    private final int priority;
    private final float viewDistance;
    private final PhysicsEngine physics;
    private final DebugRenderer debugRenderer;
    private final RaycastHit hit = new RaycastHit();
    private MovementTask movementTask;

    // Shooting
    private final float waitTime; // Time to wait between firing
    private long lastShotTime;    // Time of the last shot
    private final float shootRange; // Range within which to shoot gust of wind
    private final GameTime timer; // Game timer for tracking shot intervals

    /**
     * Constructs a GriffinTask for controlling a Griffin entity.
     *
     * @param target       The target entity that the Griffin will focus on.
     * @param priority     The priority of this task, affecting when it runs.
     * @param viewDistance The distance within which the target can be viewed.
     * @param waitTime     The time to wait between firing gusts of wind.
     * @param shootRange   The range within which the Griffin can shoot gusts of wind.
     */
    public GriffinTask(Entity target, int priority, float viewDistance, float waitTime, float shootRange) {
        this.target = target;
        this.priority = priority;
        this.viewDistance = viewDistance;
        this.physics = ServiceLocator.getPhysicsService().getPhysics();
        this.debugRenderer = ServiceLocator.getRenderService().getDebug();

        this.waitTime = waitTime;
        this.shootRange = shootRange;
        this.timer = ServiceLocator.getTimeSource();
        this.lastShotTime = timer.getTime();
    }

    @Override
    public void start() {
        super.start();
        movementTask = new MovementTask(newPosition(true));
        movementTask.create(owner);
        movementTask.start();

        playTensionMusic();
        target.getEvents().trigger("startHealthBarBeating");
    }

    @Override
    public void update() {
        movementTask.setTarget(newPosition(false));
        movementTask.update();
        if (movementTask.getStatus() != Status.ACTIVE) {
            movementTask.start();
        }

        if (timer.getTime() - lastShotTime > waitTime || getDistanceToTarget() >= shootRange) {
            shootWindGust();
        }
    }

    @Override
    public void stop() {
        super.stop();
        movementTask.stop();

        stopTensionMusic();
        target.getEvents().trigger("stopHealthBarBeating");
    }

    @Override
    public int getPriority() {
        float dst = getDistanceToTarget();
        if (dst < viewDistance || isTargetVisible()) {
            return priority;
        }
        return -1;
    }

    /**
     * Calculates the distance from the Griffin to its target.
     *
     * @return The distance to the target.
     */
    private float getDistanceToTarget() {
        return owner.getEntity().getPosition().dst(target.getPosition());
    }

    /**
     * Checks if the target is visible to the Griffin.
     *
     * @return True if the target is visible, false otherwise.
     */
    private boolean isTargetVisible() {
        Vector2 from = owner.getEntity().getCenterPosition();
        Vector2 to = target.getCenterPosition();

        if (physics.raycast(from, to, PhysicsLayer.OBSTACLE, hit)) {
            debugRenderer.drawLine(from, hit.getPoint());
            return false;
        }
        debugRenderer.drawLine(from, to);
        return true;
    }

    /**
     * Calculates a new position for the Griffin based on its current position and the target's position.
     *
     * @param trigger Whether to trigger direction events based on the new position.
     * @return The calculated new position.
     */
    private Vector2 newPosition(boolean trigger) {
        Vector2 currentPos = owner.getEntity().getPosition();
        Vector2 targetPos = target.getPosition();

        float deltaX = currentPos.x - targetPos.x;
        float deltaY = currentPos.y - targetPos.y;
        Vector2 newPos = new Vector2(currentPos.x + deltaX, currentPos.y + deltaY);

        // Make sure that the new position is within the world boundaries
        newPos.x = Math.max(0, Math.min(newPos.x, ForestGameArea.MAP_SIZE.x));
        newPos.y = Math.max((float) ForestGameArea.MAP_SIZE.y / 3 * 2, Math.min(newPos.y, ForestGameArea.MAP_SIZE.y));

        if (trigger) {
            triggerDirection(newPos, owner.getEntity().getPosition());
        }

        return newPos;
    }

    /**
     * Triggers the direction event for the Griffin based on the target's position relative to its start position.
     *
     * @param targetPos The target position.
     * @param startPos  The Griffin's current position.
     */
    private void triggerDirection(Vector2 targetPos, Vector2 startPos) {
        float deltaX = targetPos.x - startPos.x;
        if (deltaX > 0) { // Moving Left
            this.owner.getEntity().getEvents().trigger("chaseLeft");
        } else { // Moving Right
            this.owner.getEntity().getEvents().trigger("chaseRight");
        }
    }

    /**
     * Initiates shooting a gust of wind at the target and updates the last shot time.
     */
    private void shootWindGust() {
        logger.debug("Shooting gust of wind at target");
        lastShotTime = timer.getTime();  // Update the time of the last shot
        owner.getEntity().getEvents().trigger("spawnWindGust", owner.getEntity()); // Trigger the event to shoot
    }

    /**
     * Plays the tension music to enhance the experience during the chase.
     */
    void playTensionMusic() {
        // Play the music using AudioManager
        AudioManager.stopMusic();
        AudioManager.playMusic("sounds/tension-air-boss.mp3", true);
    }

    /**
     * Stops playing the tension music and play the background music.
     */
    void stopTensionMusic() {
        // Stop the music using AudioManager
        AudioManager.stopMusic();

        // Get the selected music track from the user settings
        UserSettings.Settings settings = UserSettings.get();
        String selectedTrack = settings.selectedMusicTrack; // This will be "Track 1" or "Track 2"

        if (Objects.equals(selectedTrack, "Track 1")) {
            AudioManager.playMusic("sounds/BGM_03_mp3.mp3", true);
        } else if (Objects.equals(selectedTrack, "Track 2")) {
            AudioManager.playMusic("sounds/track_2.mp3", true);
        }
    }
}
