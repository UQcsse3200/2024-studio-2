package com.csse3200.game.minigames.maze.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderWithAudioComponent;

/**
 * This class listens to events relevant to the player's state and plays the appropriate animation
 * when an event is triggered.
 */
public class MazePlayerAnimationController extends Component {
    private AnimationRenderWithAudioComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderWithAudioComponent.class);

        entity.getEvents().addListener("walkStart", this::animateWalk);
        entity.getEvents().addListener("attackStart", this::animateAttack);
        entity.getEvents().addListener("idleStart", this::animateIdle);
        entity.getEvents().addListener("faceLeft", this::faceLeft);
        entity.getEvents().addListener("faceRight", this::faceRight);
    }

    private void animateWalk() {
        animator.startAnimation("Walk");
    }

    private void animateAttack() {
        animator.startAnimation("Attack");
    }

    private void animateIdle() {
        animator.startAnimation("Idle");
    }

    private void faceLeft() {
        animator.setFlipX(true);
    }

    private void faceRight() {
        animator.setFlipX(false);
    }
}
