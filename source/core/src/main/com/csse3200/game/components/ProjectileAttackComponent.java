package com.csse3200.game.components;


import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.tasks.ProjectileMovementTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;


/**
 * A component that allows an entity to attack other entities upon collision, dealing damage and applying knockback.
 *
 * <p>This component should be added to a projectile entity. It requires the entity to have a
 * {@link CombatStatsComponent} to define its damage capabilities and a {@link HitboxComponent} to detect collisions.
 * When a collision is detected with an entity on the specified target layer, this component will deal damage to the
 * target if it has a {@link CombatStatsComponent}.
 * The projectile is then disposed of after the collision.
 */
public class ProjectileAttackComponent extends Component {
  private short targetLayer;
  private CombatStatsComponent combatStats;
  private HitboxComponent hitboxComponent;

  /**
   * Constructs a ProjectileAttackComponent for handling attacks on entities upon collision.
   * The component is configured to target entities in a specific physics layer.
   *
   * @param targetLayer The physics layer of the target entities' collider.
   */
  public ProjectileAttackComponent(short targetLayer) {
    this.targetLayer = targetLayer;
  }

  /**
   * Initializes the component by registering collision listeners and fetching the necessary components
   * for handling attacks and detecting collisions. This method is called when the component is created.
   */
  @Override
  public void create() {
    entity.getEvents().addListener("collisionStart", this::onCollisionStart);
    combatStats = entity.getComponent(CombatStatsComponent.class);
    hitboxComponent = entity.getComponent(HitboxComponent.class);
  }

  /**
   * Handles collision start events for the projectile. If the collision is with a valid target on the
   * specified layer, the target takes damage and the projectile is disposed of.
   *
   * @param me    The fixture of this entity that was involved in the collision.
   * @param other The fixture of the other entity involved in the collision.
   */
  private void onCollisionStart(Fixture me, Fixture other) {
    if (hitboxComponent.getFixture() != me) {
      // Not triggered by hitbox, ignore
      return;
    }

    if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
      // Doesn't match our target layer, ignore
      return;
    }

    // does damage if player
    Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
    target.getComponent(CombatStatsComponent.class).addHealth(-2); //placeholder value
    
    // disposes of projectile
    Entity owner = getEntity();
    ProjectileMovementTask task = (ProjectileMovementTask) owner.getComponent(AITaskComponent.class).getCurrentTask();
    task.movementTask.stop();
  }
}
