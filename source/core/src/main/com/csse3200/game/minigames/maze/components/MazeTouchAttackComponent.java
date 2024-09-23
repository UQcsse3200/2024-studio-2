package com.csse3200.game.minigames.maze.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.minigames.maze.entities.MazePlayer;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;

/**
 * When this entity touches a valid enemy's hitbox, deal damage to them and apply a knockback.
 *
 * <p>Requires CombatStatsComponent, HitboxComponent on this entity.
 *
 * <p>Damage is only applied if target entity has a CombatStatsComponent. Knockback is only applied
 * if target entity has a PhysicsComponent.
 */
public class MazeTouchAttackComponent extends Component {
  private short targetLayer;
  private float knockbackForce = 0f;
  private CombatStatsComponent combatStats;
  private HitboxComponent hitboxComponent;

  /**
   * Create a component which attacks entities on collision, without knockback.
   * @param targetLayer The physics layer of the target's collider.
   */
  public MazeTouchAttackComponent(short targetLayer) {
    this.targetLayer = targetLayer;
  }

  /**
   * Create a component which attacks entities on collision, with knockback.
   * @param targetLayer The physics layer of the target's collider.
   * @param knockback The magnitude of the knockback applied to the entity.
   */
  public MazeTouchAttackComponent(short targetLayer, float knockback) {
    this.targetLayer = targetLayer;
    this.knockbackForce = knockback;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("collisionStart", this::onCollisionStart);
    combatStats = entity.getComponent(CombatStatsComponent.class);
    hitboxComponent = entity.getComponent(HitboxComponent.class);
  }

  private void onCollisionStart(Fixture me, Fixture other) {
    if (hitboxComponent.getFixture() != me) {
      // Not triggered by hitbox, ignore
      return;
    }
    if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
      // Doesn't match our target layer, ignore
      return;
    }

    Entity targetEntity = ((BodyUserData) other.getBody().getUserData()).entity;
    Entity meEntity = ((BodyUserData) me.getBody().getUserData()).entity;

    //if (meEntity instanceof MazePlayer) {
      // Means it is the player. player should not attack or knockback npcs
      //return;
    //}

    // Change to maze combat stats
    MazeCombatStatsComponent targetStats = targetEntity.getComponent(MazeCombatStatsComponent.class);
    MazeCombatStatsComponent myStats = meEntity.getComponent(MazeCombatStatsComponent.class);

    if (targetStats != null && myStats != null) {

      System.out.println(targetStats.getHealth());
      targetStats.hit(myStats);
      System.out.println(targetStats.getHealth());

    }

    // Apply knockback (We can adjust knockback now based off what type of entity it is)
    PhysicsComponent physicsComponent = targetEntity.getComponent(PhysicsComponent.class);
    if (physicsComponent != null && knockbackForce > 0f) {
      Body targetBody = physicsComponent.getBody();
      Vector2 direction = targetEntity.getCenterPosition().sub(entity.getCenterPosition());
      // Knockback can be changed. tried doing velocity instead of impulse for more consistency
      // I imagine we also give the player invincibility for a few seconds after getting hit
      Vector2 knockbackVelocity = direction.scl(knockbackForce);
      targetBody.setLinearVelocity(knockbackVelocity);
    }
  }
}
