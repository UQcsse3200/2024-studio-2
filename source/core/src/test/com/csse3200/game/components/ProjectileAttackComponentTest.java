package com.csse3200.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(GameExtension.class)
class ProjectileAttackComponentTest {
  @BeforeEach
  void beforeEach() {
    ServiceLocator.registerPhysicsService(new PhysicsService());
  }

  @Test
  void shouldNotAttackOtherLayer() {
    short targetLayer = (1 << 3);
    short attackLayer = (1 << 4);
    Entity projectile = createProjectile(attackLayer);
    Entity target = createTarget(targetLayer);

    Fixture projectileFixture = projectile.getComponent(HitboxComponent.class).getFixture();
    Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
    projectile.getEvents().trigger("collisionStart", projectileFixture, targetFixture);

    // Assert that the target's health has not changed
    assertEquals(10, target.getComponent(CombatStatsComponent.class).getHealth());
  }

  Entity createProjectile(short targetLayer) {
    Entity projectile = new Entity()
            .addComponent(new ProjectileAttackComponent(targetLayer))
            .addComponent(new CombatStatsComponent(0, 100, 2, 0, 0, 0, 0, false, false))  // Attack damage is 2
            .addComponent(new PhysicsComponent())
            .addComponent(new HitboxComponent());
    projectile.create();
    return projectile;
  }

  Entity createTarget(short layer) {
    Entity target = new Entity()
            .addComponent(new CombatStatsComponent(10, 100, 0, 0, 0, 0, 0, false, false))  // Initial health is 10
            .addComponent(new PhysicsComponent())
            .addComponent(new HitboxComponent().setLayer(layer));
    target.create();
    return target;
  }
}