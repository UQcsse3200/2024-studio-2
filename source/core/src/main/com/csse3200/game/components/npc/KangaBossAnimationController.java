package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a Kanga Boss entity's state and plays the animation when one
 * of the events is triggered.
 */
public class KangaBossAnimationController extends Component {
    AnimationRenderComponent animator;

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("kangaWanderStart", this::animateWander);
        entity.getEvents().addListener("kangaChaseStart", this::animateChase);
    }

    void animateWander() {
        animator.startAnimation("float");
    }

    void animateChase() {
        animator.startAnimation("angry_float");
    }
}
