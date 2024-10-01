package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a Joey entity's state and triggers the appropriate animation
 * when one of the events is activated. The Joey has animations for spawning, wandering, and chasing in
 * both left and right directions.
 */
public class JoeyAnimationController extends Component {
    // Animation render component responsible for playing animations
    private AnimationRenderComponent animator;

    /**
     * Called when the component is created. Initializes the animation controller by getting the
     * AnimationRenderComponent and setting up listeners for the Joey's movement and spawn events.
     */
    @Override
    public void create() {
        super.create();
        // Get the AnimationRenderComponent associated with the entity and store it in the animator field
        animator = this.entity.getComponent(AnimationRenderComponent.class);

        // Set up listeners for Joey's state-based events
        entity.getEvents().addListener("spawnStart", this::animateSpawn);
        entity.getEvents().addListener("wanderLeft", this::animateWanderLeft);
        entity.getEvents().addListener("wanderRight", this::animateWanderRight);
        entity.getEvents().addListener("chaseLeft", this::animateChaseLeft);
        entity.getEvents().addListener("chaseRight", this::animateChaseRight);
    }

    /**
     * Plays the spawn animation for Joey.
     */
    private void animateSpawn() {
        animator.startAnimation("spawn");
    }

    /**
     * Plays the wander animation with Joey facing left by flipping the animation on the X-axis.
     */
    private void animateWanderLeft() {
        animator.setFlipX(true);
        animator.startAnimation("wander");
    }

    /**
     * Plays the wander animation with Joey facing right (default direction).
     */
    private void animateWanderRight() {
        animator.setFlipX(false);
        animator.startAnimation("wander");
    }

    /**
     * Plays the chase animation with Joey facing left by flipping the animation on the X-axis.
     */
    private void animateChaseLeft() {
        animator.setFlipX(true);
        animator.startAnimation("chase");
    }

    /**
     * Plays the chase animation with Joey facing right (default direction).
     */
    private void animateChaseRight() {
        animator.setFlipX(false);
        animator.startAnimation("chase");
    }
}
