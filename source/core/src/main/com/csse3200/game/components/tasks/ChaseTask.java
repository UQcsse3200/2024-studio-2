package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.settingsmenu.UserSettings;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.minigames.maze.entities.mazenpc.MazeEntity;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.services.AudioManager;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.math.Vector2Utils;

import java.util.Objects;

/** Chases a target entity until they get too far away or line of sight is lost */
public class ChaseTask extends DefaultTask implements PriorityTask {
    protected final Entity target;
    protected final int priority;
    protected final float viewDistance;
    protected final float maxChaseDistance;
    protected final PhysicsEngine physics;
    protected final DebugRenderer debugRenderer;
    protected final RaycastHit hit = new RaycastHit();
    protected MovementTask movementTask;
    private final boolean isBoss;
    private final Vector2 bossSpeed;
    private boolean chaseDir = false; // 0 left, 1 right
    private Vector2 speed;
    
    /**
     * @param target The entity to chase.
     * @param priority Task priority when chasing (0 when not chasing).
     * @param viewDistance Maximum distance from the entity at which chasing can start.
     * @param maxChaseDistance Maximum distance from the entity while chasing before giving up.
     */
    public ChaseTask(Entity target, int priority, float viewDistance, float maxChaseDistance, Vector2 speed, boolean isBoss) {
        this.target = target;
        this.priority = priority;
        this.viewDistance = viewDistance;
        this.maxChaseDistance = maxChaseDistance;
        physics = ServiceLocator.getPhysicsService().getPhysics();
        debugRenderer = ServiceLocator.getRenderService().getDebug();
        bossSpeed = Vector2Utils.TWOHALF;
        this.isBoss = isBoss;
        this.speed = speed;
    }
    
    public boolean isBoss() {
        return isBoss;
    }
    
    /**
     * Moves the entity in the direction of the entities target (often the player)
     */
    @Override
    public void start() {
        super.start();
        
        // Set movementTask based on npc type
        Vector2 currentPos = owner.getEntity().getPosition();
        Vector2 targetPos = target.getPosition();
        // temporary fix as the behaviour of this was changed without changing the maze game.
        if (owner.getEntity() instanceof MazeEntity) {
            movementTask = new MovementTask(targetPos);
        } else {
            movementTask = this.isBoss ? new MovementTask(target.getPosition(), bossSpeed) :
                    new MovementTask(targetPos, speed);
        }
        movementTask.create(owner);
        movementTask.start();
        
        if (this.isBoss) {
            playTensionMusic();
            this.target.getEvents().trigger("startHealthBarBeating");
        }

        if (owner.getEntity().isNormalEnemy()) {
            owner.getEntity().getEvents().trigger("animate", targetPos, currentPos);
        } else if (targetPos.x - currentPos.x < 0) {
            this.owner.getEntity().getEvents().trigger("chaseLeft");
        } else {
            this.owner.getEntity().getEvents().trigger("chaseRight");
        }
    }

    /**
     * Plays the tension music to enhance the experience during the chase.
     */
    void playTensionMusic() {
        // Play the music using AudioManager
        AudioManager.stopMusic();
        AudioManager.playMusic("sounds/tension-land-boss.mp3", true);
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
    
    /**
     * Updates the direction in which the entity needs to move in, checks every
     * frame to see where the player is to determine this.
     */
    @Override
    public void update() {
        Vector2 currentPos = owner.getEntity().getPosition();
        Vector2 targetPos = target.getPosition();
        
        movementTask.setTarget(targetPos);
        movementTask.update();
        if (movementTask.getStatus() != Status.ACTIVE) {
            movementTask.start();
        }
        
        //handle animation, doesn't work as intended but I dont know how to fix it.
        if (targetPos.x - currentPos.x < 0 && chaseDir) {
            if (owner.getEntity().isNormalEnemy()) {
                owner.getEntity().getEvents().trigger("animate", targetPos, currentPos);
            } else {
                owner.getEntity().getEvents().trigger("chaseLeft");
            }
            chaseDir = false;
        } else if (targetPos.x - currentPos.x >= 0 && !chaseDir){
            if (owner.getEntity().isNormalEnemy()) {
                owner.getEntity().getEvents().trigger("animate", targetPos, currentPos);
            } else {
                owner.getEntity().getEvents().trigger("chaseRight");
            }
            chaseDir = true;
        }
    }
    
    public float getViewDistance() {
        return this.viewDistance;
    }
    
    @Override
    public void stop() {
        super.stop();
        movementTask.stop();
        
        if (this.isBoss) {
            stopTensionMusic();
            this.target.getEvents().trigger("stopHealthBarBeating");
        }
    }
    
    @Override
    public int getPriority() {
        if (getDistanceToTarget() < viewDistance && isTargetVisible()) {
            return priority;
        }
        return -1;
    }
    
    protected float getDistanceToTarget() {
        return owner.getEntity().getPosition().dst(target.getPosition());
    }
    
    protected boolean isTargetVisible() {
        Vector2 from = owner.getEntity().getCenterPosition();
        Vector2 to = target.getCenterPosition();
        
        // If there is an obstacle in the path to the player, not visible.
        if (physics.raycast(from, to, PhysicsLayer.OBSTACLE, hit)) {
            debugRenderer.drawLine(from, hit.getPoint());
            return false;
        }
        debugRenderer.drawLine(from, to);
        return true;
    }
}
