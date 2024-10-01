package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a Kanga Boss entity's state and plays the corresponding
 * animation when one of the events is triggered. The Kanga Boss can wander or chase in both left
 * and right directions, and the appropriate animation will be played based on the direction.
 */
public class KangaBossAnimationController extends Component {
    // The animator responsible for rendering the animations for Kanga Boss.
    private AnimationRenderComponent animator;

    /**
     * Called when the component is created. This method initializes the animation controller,
     * retrieves the associated AnimationRenderComponent, and sets up event listeners for the
     * Kanga Boss's movement events.
     */
    @Override
    public void create() {
        super.create();
        // Get the AnimationRenderComponent associated with the Kanga Boss entity
        animator = this.entity.getComponent(AnimationRenderComponent.class);

        // Set up listeners for movement events to trigger the appropriate animations
        entity.getEvents().addListener("wanderLeft", this::animateWanderLeft);
        entity.getEvents().addListener("wanderRight", this::animateWanderRight);
        entity.getEvents().addListener("chaseLeft", this::animateChaseLeft);
        entity.getEvents().addListener("chaseRight", this::animateChaseRight);
    }

    /**
     * Plays the wander animation for Kanga Boss while facing left. The animation is flipped
     * horizontally to show left movement.
     */
    private void animateWanderLeft() {
        animator.setFlipX(true);
        animator.startAnimation("wander");
    }

    /**
     * Plays the wander animation for Kanga Boss while facing right (default direction).
     */
    private void animateWanderRight() {
        animator.setFlipX(false);
        animator.startAnimation("wander");
    }

    /**
     * Plays the chase animation for Kanga Boss while facing left. The animation is flipped
     * horizontally to show left movement.
     */
    private void animateChaseLeft() {
        animator.setFlipX(true);
        animator.startAnimation("chase");
    }

    /**
     * Plays the chase animation for Kanga Boss while facing right (default direction).
     */
    private void animateChaseRight() {
        animator.setFlipX(false);
        animator.startAnimation("chase");
    }
}
