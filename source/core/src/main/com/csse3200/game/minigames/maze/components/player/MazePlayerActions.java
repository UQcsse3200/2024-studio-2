package com.csse3200.game.minigames.maze.components.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.components.Component;
import com.csse3200.game.particles.components.ParticleEffectComponent;
import com.csse3200.game.physics.components.PhysicsComponent;

/**
 * Action component for interacting with the player. Player events should be initialised in create()
 * and when triggered should call methods within this class.
 */
public class MazePlayerActions extends Component {

    // Players maximum speed, physics, walking direction and boolean for if the player is moving
    private static final Vector2 MAX_SPEED = new Vector2(1.6f, 1.6f); // Metres per second
    private PhysicsComponent physicsComponent;
    private Vector2 walkDirection = Vector2.Zero.cpy();
    private boolean moving = false;

    @Override
    public void create() {
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        entity.getEvents().addListener("walk", this::walk);
        entity.getEvents().addListener("walkStop", this::stopWalking);
    }

    /**
     * Makes bubbles if the player is moving
     */
    @Override
    public void update() {
        ParticleEffectComponent bubbles = entity.getComponent(ParticleEffectComponent.class);
        if (moving) {
            updateSpeed();
            bubbles.emit();
        } else {
            bubbles.allowCompletion();
        }
    }

    /**
     * Updates the speed of the player
     */
    private void updateSpeed() {
        Body body = physicsComponent.getBody();
        Vector2 velocity = body.getLinearVelocity();
        Vector2 desiredVelocity = walkDirection.cpy().scl(MAX_SPEED);
        // impulse = (desiredVel - currentVel) * mass
        Vector2 impulse = desiredVelocity.sub(velocity).scl(body.getMass());
        body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
    }

    /**
     * Moves the player towards a given direction.
     *
     * @param direction direction to move in
     */
    void walk(Vector2 direction) {
        this.walkDirection = direction;
        moving = true;
    }

    /**
     * Stops the player from walking.
     */
    void stopWalking() {
        this.walkDirection = Vector2.Zero.cpy();
        updateSpeed();
        moving = false;
    }
}
