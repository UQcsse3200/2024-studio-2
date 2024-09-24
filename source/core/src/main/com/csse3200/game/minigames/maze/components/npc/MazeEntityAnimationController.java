package com.csse3200.game.minigames.maze.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.AnimationRenderWithAudioComponent;

/**
 * This class listens to events relevant to a maze entity's state and plays the animation when one
 * of the events is triggered.
 */
public class MazeEntityAnimationController extends Component {
  AnimationRenderWithAudioComponent animator;

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

  void animateIdle() {
    animator.startAnimation("Idle");
  }

  void animateWander() {
    animator.startAnimation("Walk");
  }

  void animateChase() {
    animator.startAnimation("Attack");
  }

  void faceLeft() {
      animator.setFlipX(true);
  }

  void faceRight() {
      animator.setFlipX(false);
  }
}
