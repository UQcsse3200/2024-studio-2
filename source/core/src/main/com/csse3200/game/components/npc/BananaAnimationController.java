package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a chicken entity's state and plays the animation when one
 * of the events is triggered.
 */
public class BananaAnimationController extends Component {
  AnimationRenderComponent animator;

  @Override
  public void create() {
    super.create();
    // Get the AnimationRenderComponent associated with the entity and store it in the animator field
    animator = this.entity.getComponent(AnimationRenderComponent.class);
    entity.getEvents().addListener("ProjectileMove", this::animateFire);
  }

  //The following methods will be updated with new animations in future sprints,
  //so are currently placeholders

  private void animateFire() {
    System.out.println("BananaAnimationController animateFire");
    animator.startAnimation("fire");
  }
}
