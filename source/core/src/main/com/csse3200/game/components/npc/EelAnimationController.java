package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a chicken entity's state and plays the animation when one
 * of the events is triggered.
 */
public class EelAnimationController extends Component {
    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        // Get the AnimationRenderComponent associated with the entity and store it in the animator field
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("runLeft", this::swim_left);
        entity.getEvents().addListener("runLeftUp", this::swim_up_left);
        entity.getEvents().addListener("runUp", this::swim_up);
        entity.getEvents().addListener("runRightUp", this::swim_up_right);
        entity.getEvents().addListener("runRight", this::swim_right);
        entity.getEvents().addListener("runRightDown", this::swim_down_right);
        entity.getEvents().addListener("runDown", this::swim_down);
        entity.getEvents().addListener("runLeftDown", this::swim_down_left);
    }

    private void swim_left() {
        animator.setFlipX(true);
        animator.startAnimation("swim_right");
    }

    private void swim_up_left() {
        animator.setFlipX(true);
        animator.startAnimation("swim_up_right");
    }

    private void swim_up() {
        animator.setFlipX(false);
        animator.startAnimation("swim_up");
    }

    private void swim_up_right() {
        animator.setFlipX(false);
        animator.startAnimation("swim_up_right");
    }

    private void swim_right() {
        animator.setFlipX(false);
        animator.startAnimation("swim_right");
    }

    private void swim_down_right() {
        animator.setFlipX(false);
        animator.startAnimation("swim_down_right");
    }

    private void swim_down() {
        animator.setFlipX(false);
        animator.startAnimation("swim_down");
    }

    private void swim_down_left() {
        animator.setFlipX(true);
        animator.startAnimation("swim_down_right");
    }
}
