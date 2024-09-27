package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import static java.lang.Math.atan2;

/** Chases a target entity until they get too far away or line of sight is lost */
public class ProjectileMovementTask extends DefaultTask implements PriorityTask {
    protected final int priority;
    protected final PhysicsEngine physics;
    public MovementTask movementTask;
    private Vector2 targetPosition;
    
    /**
     * @param target The entity to chase.
     * @param priority Task priority when chasing (0 when not chasing).
     */
    public ProjectileMovementTask(Entity target, int priority) {
        this.targetPosition = target.getPosition();
        this.priority = priority;
        physics = ServiceLocator.getPhysicsService().getPhysics();
    }
    
    /**
     * Moves the entity in the direction of the entities target (often the player)
     */
    @Override
    public void start() {
        super.start();
        movementTask = new MovementTask(targetPosition);
        movementTask.create(owner);
        movementTask.start();
        
        animate();
        
    }
    
    private void animate(){
        //default if projectile doesn't have animations
        owner.getEntity().getEvents().trigger("ProjectileMove");
        
        //if it directional animations
        Vector2 direction = movementTask.getMovementComponent().getDirection();
        double angle = atan2(direction.y, direction.x);
        if (direction.x >= 0) {
            if (angle > 9*Math.PI/24) {
                owner.getEntity().getEvents().trigger("up");
            } else if (angle > 3*Math.PI/24) {
                owner.getEntity().getEvents().trigger("rightUp");
            } else if (angle > -3*Math.PI/24) {
                owner.getEntity().getEvents().trigger("right");
            } else if (angle > -9*Math.PI/24) {
                owner.getEntity().getEvents().trigger("rightDown");
            } else {
                owner.getEntity().getEvents().trigger("down");
            }
        } else {
            if (angle > 9*Math.PI/24) {
                owner.getEntity().getEvents().trigger("down");
            } else if (angle > 3*Math.PI/24) {
                owner.getEntity().getEvents().trigger("leftDown");
            } else if (angle > -3*Math.PI/24) {
                owner.getEntity().getEvents().trigger("left");
            } else if (angle > -9*Math.PI/24) {
                owner.getEntity().getEvents().trigger("leftUp");
            } else {
                owner.getEntity().getEvents().trigger("up");
            }
        }
    }
    /**
     * Updates the direction in which the entity needs to move in, checks every
     * frame to see where the player is to determine this.
     */
    @Override
    public void update() {
        if (movementTask.getStatus() != Status.ACTIVE) {
            owner.getEntity().setEnabled(false);
            AnimationRenderComponent animationRenderComponent = owner.getEntity().getComponent(AnimationRenderComponent.class);
            animationRenderComponent.stopAnimation();
            owner.getEntity().specialDispose();
        }
        movementTask.update();
    }
    
    @Override
    public int getPriority() {
        return priority;
    }
}
