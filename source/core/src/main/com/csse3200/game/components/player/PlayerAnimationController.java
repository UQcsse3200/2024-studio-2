package com.csse3200.game.components.player;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a monkey entity's state and plays the animation when one
 * of the events is triggered.
 */
public class PlayerAnimationController extends Component {
  AnimationRenderComponent animator;

  @Override
  public void create() {
    super.create();
    animator = this.entity.getComponent(AnimationRenderComponent.class);
    entity.getEvents().addListener("die", this::animateDie);
    entity.getEvents().addListener("attack", this::animateAttack);
    entity.getEvents().addListener("damage", this::animateDamage);
    entity.getEvents().addListener("idle", this::animateIdle);
    entity.getEvents().addListener("move", this::animateMove);
    
  }
  
  private void animateDie(Boolean dir) {
    animator.setFlipX(dir);
    animator.startAnimation("die");
  }
  
  private void animateAttack(Boolean dir) {
    animator.setFlipX(dir);
    animator.startAnimation("attack");
  }
  private void animateDamage(Boolean dir) {
    animator.setFlipX(dir);
    animator.startAnimation("damage");
  }
  private void animateIdle(Boolean dir) {
    animator.setFlipX(dir);
    animator.startAnimation("idle");
  }
  private void animateMove(Boolean dir) {
    animator.setFlipX(dir);
    animator.startAnimation("move");
  }
}
