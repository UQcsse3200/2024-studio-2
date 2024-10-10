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
        entity.getEvents().addListener("runLeft", this::swimLeft);
        entity.getEvents().addListener("runLeftUp", this::swimUpLeft);
        entity.getEvents().addListener("runUp", this::swimUp);
        entity.getEvents().addListener("runRightUp", this::swimUpRight);
        entity.getEvents().addListener("runRight", this::swimRight);
        entity.getEvents().addListener("runRightDown", this::swimDownRight);
        entity.getEvents().addListener("runDown", this::swimDown);
        entity.getEvents().addListener("runLeftDown", this::swimDownLeft);
    }

    private void swimLeft() {
        animator.setFlipX(true);
        animator.startAnimation("swim_right");
    }

    private void swimUpLeft() {
        animator.setFlipX(true);
        animator.startAnimation("swim_up_right");
    }

    private void swimUp() {
        animator.setFlipX(false);
        animator.startAnimation("swim_up");
    }

    private void swimUpRight() {
        animator.setFlipX(false);
        animator.startAnimation("swim_up_right");
    }

    private void swimRight() {
        animator.setFlipX(false);
        animator.startAnimation("swim_right");
    }

    private void swimDownRight() {
        animator.setFlipX(false);
        animator.startAnimation("swim_down_right");
    }

    private void swimDown() {
        animator.setFlipX(false);
        animator.startAnimation("swim_down");
    }

    private void swimDownLeft() {
        animator.setFlipX(true);
        animator.startAnimation("swim_down_right");
    }
}
