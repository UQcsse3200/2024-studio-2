package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a friendlyNPCs entity's state and plays the animation when one
 * of the events is triggered.
 */
public class FriendlyNPCAnimationController extends Component {
    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("wanderStart", this::animateWander);
    }

    void animateWander() {
        animator.startAnimation("float");
    }

}
