package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a ghost entity's state and plays the animation when one
 * of the events is triggered.
 */
public class FrogAnimationController extends Component {
  AnimationRenderComponent animator;

  @Override
  public void create() {
    super.create();
    animator = this.entity.getComponent(AnimationRenderComponent.class);
    entity.getEvents().addListener("wanderLeft", this::animateWanderLeft);
    entity.getEvents().addListener("wanderRight", this::animateWanderRight);
    entity.getEvents().addListener("chaseStart", this::animateChase);
  }

  void animateWanderLeft() {
    System.out.println("WanderLeft");
    animator.setFlipX(true);
    animator.startAnimation("float");
  }
  void animateWanderRight() {
    System.out.println("WanderRight");
    animator.setFlipX(false);
    animator.startAnimation("float");
  }

  void animateChase() {
    animator.startAnimation("angry_float");
  }
}
