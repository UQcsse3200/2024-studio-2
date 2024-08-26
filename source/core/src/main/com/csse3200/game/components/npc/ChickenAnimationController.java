package com.csse3200.game.components.npc;

import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a ghost entity's state and plays the animation when one
 * of the events is triggered.
 */
public class ChickenAnimationController extends Component {
  AnimationRenderComponent animator;

  @Override
  public void create() {
    super.create();
    animator = this.entity.getComponent(AnimationRenderComponent.class);
    entity.getEvents().addListener("spawnChicken", this::animateSpawn);
    entity.getEvents().addListener("wanderStart", this::animateWander);
    entity.getEvents().addListener("chaseStart", this::animateChase);
  }

  private void animateSpawn() {
    animator.startAnimation("spawn");
  }

  private void animateWander() {
    animator.startAnimation("walk");
  }

  private void animateChase() {
    animator.startAnimation("walk");
  }
}
