package com.csse3200.game.components.npc;

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
  }

  void animateWander() {
    animator.startAnimation("Idle");
  }

  void animateChase() {
    animator.startAnimation("Attack");
  }
}
