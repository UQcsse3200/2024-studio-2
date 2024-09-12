package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a frog entity's state and plays the animation when one
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
    entity.getEvents().addListener("chaseLeft", this::animateChaseLeft);
    entity.getEvents().addListener("chaseRight", this::animateChaseRight);
    entity.getEvents().addListener("spawnStart", this::animateSpawn);
  }

  private void animateSpawn() {
    animator.startAnimation("spawn");
  }

  //The following methods will be updated with new animations in future sprints,
  //so are currently placeholders

  void animateWanderLeft() {
    animator.setFlipX(true);
    animator.startAnimation("float");
  }
  void animateWanderRight() {
    animator.setFlipX(false);
    animator.startAnimation("float");
  }

  void animateChaseLeft() {
    animator.setFlipX(true);
    animator.startAnimation("angry_float");
  }

  void animateChaseRight() {
    animator.setFlipX(false);
    animator.startAnimation("angry_float");
  }

}
