package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a ghost entity's state and plays the animation when one
 * of the events is triggered.
 */
public class MonkeyAnimationController extends Component {
  AnimationRenderComponent animator;

  @Override
  public void create() {
    super.create();
    animator = this.entity.getComponent(AnimationRenderComponent.class);
    entity.getEvents().addListener("runDown", this::animateRunDown);
    entity.getEvents().addListener("runUp", this::animateRunUp);
    entity.getEvents().addListener("runLeft", this::animateRunLeft);
    entity.getEvents().addListener("runRight", this::animateRunRight);
    entity.getEvents().addListener("runLeftDown", this::animateRunLeftDown);
    entity.getEvents().addListener("runRightDown", this::animateRunRightDown);
    entity.getEvents().addListener("runLeftUp", this::animateRunLeftUp);
    entity.getEvents().addListener("runRightUp", this::animateRunRightUp);
  }

  void animateRunDown() {
    animator.startAnimation("run_down");
  }

  void animateRunUp() {
    animator.startAnimation("run_up");
  }

  void animateRunLeft() {
    animator.startAnimation("run_left");
  }

  void animateRunRight() {
    animator.startAnimation("run_right");
  }

  void animateRunLeftDown() {
    animator.startAnimation("run_left_down");
  }

  void animateRunRightDown() {
    animator.startAnimation("run_right_down");
  }

  void animateRunLeftUp() {
    animator.startAnimation("run_left_up");
  }

  void animateRunRightUp() {
    animator.startAnimation("run_right_up");
  }

}
