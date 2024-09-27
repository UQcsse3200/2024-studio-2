package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a Water Boss entity's state and plays the animation when one
 * of the events is triggered.
 */
public class WaterBossAnimationController extends Component {
    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("bossWanderStart", this::animateWander);
        entity.getEvents().addListener("bossChaseStart", this::animateChase);
    }

    void animateWander() {
        animator.startAnimation("wander");
    }

    void animateChase() {
        animator.startAnimation("chase");
    }
}
