package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a monkey entity's state and plays the animation when one
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
    entity.getEvents().addListener("wait", this::animateWait);
  }

  //The following methods will be updated with new animations in future sprints,
  //so are currently placeholders

  private void animateRunDown() {
    animator.startAnimation("run_down");
  }

  private void animateRunUp() {
    animator.startAnimation("run_up");
  }

  private void animateRunLeft() {
    animator.startAnimation("run_left");
  }

  private void animateRunRight() {
    animator.startAnimation("run_right");
  }

  private void animateRunLeftDown() {
    animator.startAnimation("run_left_down");
  }

  private void animateRunRightDown() {
    animator.startAnimation("run_right_down");
  }

  private void animateRunLeftUp() {
    animator.startAnimation("run_left_up");
  }

  private void animateRunRightUp() {
    animator.startAnimation("run_right_up");
  }

  private void animateWait() {
    animator.startAnimation("wait");
  }

}
