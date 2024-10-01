package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events related to the Air Boss entity's state and plays the appropriate animation
 * when one of the events is triggered. The Air Boss has animations for wandering and chasing in both the
 * left and right directions.
 */
public class AirBossAnimationController extends Component {
    // Animation render component responsible for playing animations
    private AnimationRenderComponent animator;

    /**
     * Called when the component is created. Sets up listeners for the Air Boss' movement events
     * and links them to corresponding animation triggers.
     */
    @Override
    public void create() {
        super.create();
        // Retrieves the AnimationRenderComponent from the entity
        animator = this.entity.getComponent(AnimationRenderComponent.class);

        // Set up listeners for movement-related events and assign appropriate animations
        entity.getEvents().addListener("wanderLeft", this::animateWanderLeft);
        entity.getEvents().addListener("wanderRight", this::animateWanderRight);
        entity.getEvents().addListener("chaseLeft", this::animateChaseLeft);
        entity.getEvents().addListener("chaseRight", this::animateChaseRight);
    }

    /**
     * Plays the wander animation with the entity facing left by flipping the animation on the X-axis.
     */
    private void animateWanderLeft() {
        animator.setFlipX(true);
        animator.startAnimation("wander");
    }

    /**
     * Plays the wander animation with the entity facing right (default direction).
     */
    private void animateWanderRight() {
        animator.setFlipX(false);
        animator.startAnimation("wander");
    }

    /**
     * Plays the chase animation with the entity facing left by flipping the animation on the X-axis.
     */
    private void animateChaseLeft() {
        animator.setFlipX(true);
        animator.startAnimation("chase");
    }

    /**
     * Plays the chase animation with the entity facing right (default direction).
     */
    private void animateChaseRight() {
        animator.setFlipX(false);
        animator.startAnimation("chase");
    }
}
