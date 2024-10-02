package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a frog entity's state and plays the animation when one
 * of the events is triggered.
 */
public class OrbAnimationController extends Component {
    AnimationRenderComponent animator;
    
    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("up", this::animateUp);
        entity.getEvents().addListener("rightUp", this::animateRightUp);
        entity.getEvents().addListener("right", this::animateRight);
        entity.getEvents().addListener("rightDown", this::animateRightDown);
        entity.getEvents().addListener("down", this::animateDown);
        entity.getEvents().addListener("leftDown", this::animateLeftDown);
        entity.getEvents().addListener("left", this::animateLeft);
        entity.getEvents().addListener("leftUp", this::animateLeftUp);
    }
    
    private void animateUp() {
        animator.startAnimation("down");
        animator.setFlipY(true);
    }
    
    private void animateRightUp() {
        animator.startAnimation("diagonal");
        animator.setFlipY(true);
        animator.setFlipX(true);
    }
    
    private void animateRight() {
        animator.startAnimation("left");
        animator.setFlipX(true);
    }
    
    private void animateRightDown() {
        animator.startAnimation("diagonal");
        animator.setFlipX(true);
    }
    
    private void animateDown() {
        animator.startAnimation("down");
    }
    
    private void animateLeftDown() {
        animator.startAnimation("diagonal");
    }
    
    private void animateLeft() {
        animator.startAnimation("left");
    }
    
    private void animateLeftUp() {
        animator.startAnimation("diagonal");
        animator.setFlipY(true);
    }
}
