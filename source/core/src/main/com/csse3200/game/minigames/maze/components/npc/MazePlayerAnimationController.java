package com.csse3200.game.minigames.maze.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderWithAudioComponent;

/**
 * This class listens to events relevant to the player's state and plays the appropriate animation
 * when an event is triggered.
 */
public class MazePlayerAnimationController extends Component {
    private AnimationRenderWithAudioComponent animator;

    /**
     * Creates events for animations
     */
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

    /**
     * Animation for the walking state
     */
    private void animateWalk() {
        animator.startAnimation("Walk");
    }

    /**
     * Animation for the attack state
     */
    private void animateAttack() {
        animator.startAnimation("Attack");
    }

    /**
     * Animation for the idle state
     */
    private void animateIdle() {
        animator.startAnimation("Idle");
    }

    /**
     * Animation for the facing left state
     */
    private void faceLeft() {
        animator.setFlipX(true);
    }

    /**
     * Animation for the facing right state
     */
    private void faceRight() {
        animator.setFlipX(false);
    }
}
