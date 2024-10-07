package com.csse3200.game.minigames.maze.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderWithAudioComponent;

/**
 * This class listens to events relevant to a maze entity's state and plays the animation when one
 * of the events is triggered.
 */
public class MazeOctopusAnimationController extends Component {
    AnimationRenderWithAudioComponent animator;

    /**
     * Creates events for animations
     */
    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderWithAudioComponent.class);
        entity.getEvents().addListener("wanderStart", this::animateWander);
        entity.getEvents().addListener("chaseStart", this::animateChase);
        entity.getEvents().addListener("idleStart", this::animateIdle);
        entity.getEvents().addListener("spawnStart", this::animateIdle);
        entity.getEvents().addListener("faceLeft", this::faceLeft);
        entity.getEvents().addListener("faceRight", this::faceRight);
    }

    /**
     * Animation for the idle state
     */
    void animateIdle() {
        animator.startAnimation("Idle");
    }

    /**
     * Animation for the walking state
     */
    void animateWander() {
        animator.startAnimation("Idle");
    }

    /**
     * Animation for the attack state
     */
    void animateChase() {
        animator.startAnimation("Walk");
    }

    /**
     * Animation for the facing left state
     */
    void faceLeft() {
        animator.setFlipX(true);
    }

    /**
     * Animation for the facing right state
     */
    void faceRight() {
        animator.setFlipX(false);
    }
}
