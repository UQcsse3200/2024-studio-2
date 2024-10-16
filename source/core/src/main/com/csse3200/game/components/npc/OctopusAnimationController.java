package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to an octopus entity's state and plays the animation when one
 * of the events is triggered.
 */
public class OctopusAnimationController extends Component {
  AnimationRenderComponent animator;
  private boolean flipped;

  @Override
  public void create() {
    super.create();
    flipped = false;
    animator = this.entity.getComponent(AnimationRenderComponent.class);
    entity.getEvents().addListener("wanderLeft", this::animateLeft);
    entity.getEvents().addListener("wanderRight", this::animateRight);
    entity.getEvents().addListener("chaseLeft", this::animateChaseLeft);
    entity.getEvents().addListener("chaseRight", this::animateChaseRight);
    entity.getEvents().addListener("pullRight", this::animatePullRight);
    entity.getEvents().addListener("pullLeft", this::animatePullLeft);
  }
  
  //The following methods will be updated with new animations in future sprints,
  //so are currently placeholders

  void animateLeft() {
    flipped = true;
    animator.setFlipX(true);
    animator.startAnimation("float");
  }
  
  void animateRight() {
    flipped = false;
    animator.setFlipX(false);
    animator.startAnimation("float");
  }

  void animateChaseRight() {
    flipped = false;
    animator.setFlipX(false);
    animator.startAnimation("chase");
  }
  void animateChaseLeft() {
    flipped = false;
    animator.setFlipX(false);
    animator.startAnimation("chase");
  }

  private void animatePullRight() {
    animator.setFlipX(false);
    animator.startAnimation("pull");
  }

  private void animatePullLeft() {
    animator.setFlipX(true);
    animator.startAnimation("pull");
  }


}
