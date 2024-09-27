package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a Water Boss entity's state and plays the animation when one
 * of the events is triggered.
 */
public class AirBossAnimationController extends Component {
    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("wanderLeft", this::animateWanderLeft);
        entity.getEvents().addListener("wanderRight", this::animateWanderRight);
        entity.getEvents().addListener("chaseLeft", this::animateChaseLeft);
        entity.getEvents().addListener("chaseRight", this::animateChaseRight);
    }

    private void animateWanderLeft() {
        animator.setFlipX(true);
        animator.startAnimation("wander");
    }

    private void animateWanderRight() {
        animator.setFlipX(false);
        animator.startAnimation("wander");
    }

    private void animateChaseLeft() {
        animator.setFlipX(true);
        animator.startAnimation("chase");
    }

    private void animateChaseRight() {
        animator.setFlipX(false);
        animator.startAnimation("chase");
    }
}
