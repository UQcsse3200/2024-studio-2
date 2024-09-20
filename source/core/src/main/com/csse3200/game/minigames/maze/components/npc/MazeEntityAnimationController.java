package com.csse3200.game.minigames.maze.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a maze entity's state and plays the animation when one
 * of the events is triggered.
 */
public class MazeEntityAnimationController extends Component {
  AnimationRenderComponent animator;

  @Override
  public void create() {
    super.create();
    animator = this.entity.getComponent(AnimationRenderComponent.class);
    entity.getEvents().addListener("wanderStart", this::animateWander);
    entity.getEvents().addListener("chaseStart", this::animateChase);
    entity.getEvents().addListener("faceLeft", this::faceLeft);
    entity.getEvents().addListener("faceRight", this::faceRight);
    entity.getEvents().addListener("chaseLeft", this::faceLeft);
    entity.getEvents().addListener("chaseRight", this::faceRight);
  }

  void animateWander() {
    animator.startAnimation("Idle");
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
