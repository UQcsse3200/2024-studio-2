package com.csse3200.game.components.combat;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to an entity's state in combat and plays the animation when one
 * of the events is triggered.
 */
public class CombatAnimationController extends Component {
    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        // Get the AnimationRenderComponent associated with the entity and store it in the animator field
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("idleLeft", this::animateIdleLeft);
        entity.getEvents().addListener("idleRight", this::animateIdleRight);
        entity.getEvents().addListener("moveLeft", this::animateMoveLeft);
        entity.getEvents().addListener("moveRight", this::animateMoveRight);
        entity.getEvents().addListener("hurtLeft", this::animateHurtLeft);
        entity.getEvents().addListener("hurtRight", this::animateHurtRight);
    }

    private void animateIdleLeft() {
        animator.setFlipX(true);
        animator.startAnimation("combatIdle");
    }

    private void animateIdleRight() {
        animator.setFlipX(false);
        animator.startAnimation("combatIdle");
    }

    private void animateMoveLeft() {
        animator.setFlipX(true);
        animator.startAnimation("combatMove");
    }

    private void animateMoveRight() {
        animator.setFlipX(false);
        animator.startAnimation("combatMove");
    }

    private void animateHurtLeft() {
        animator.setFlipX(true);
        animator.startAnimation("combatHurt");
    }

    private void animateHurtRight() {
        animator.setFlipX(false);
        animator.startAnimation("combatHurt");
    }
}
