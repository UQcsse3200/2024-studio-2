package com.csse3200.game.minigames.maze.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.minigames.maze.entities.MazePlayer;
import com.csse3200.game.minigames.maze.entities.mazenpc.AnglerFish;
import com.csse3200.game.minigames.maze.entities.mazenpc.ElectricEel;
import com.csse3200.game.minigames.maze.entities.mazenpc.FishEgg;
import com.csse3200.game.particles.ParticleService;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.AudioManager;
import com.csse3200.game.services.ServiceLocator;

/**
 * When this entity touches a valid enemy's hitbox, deal damage to them and apply a knockback.
 *
 * <p>Requires CombatStatsComponent, HitboxComponent on this entity.
 *
 * <p>Damage is only applied if target entity has a CombatStatsComponent. Knockback is only applied
 * if target entity has a PhysicsComponent.
 */
public class MazeTouchAttackComponent extends Component {
    private final short targetLayer;
    private final float knockbackForce;
    private HitboxComponent hitboxComponent;

    /**
     * Create a component which attacks entities on collision, with knockback.
     * @param targetLayer The physics layer of the target's collider.
     * @param knockback The magnitude of the knockback applied to the entity.
     */
    public MazeTouchAttackComponent(short targetLayer, float knockback) {
        this.targetLayer = targetLayer;
        this.knockbackForce = knockback;
    }

    /**
     * Creates event for combat when there is a collision
     */
    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        hitboxComponent = entity.getComponent(HitboxComponent.class);
    }

    /**
     * Callback when a fixture of this entity collides with another entity.
     * In particular, this callback will handle collisions between the hitbox of this component
     * with other entities and "attack" the other entity.
     * "attacking" the other entity includes decreasing hit-points, stunning, and applying a
     * knock-back depending on the types of entities involved.
     *
     * @param me the fixture of this entity
     * @param other the other fixture that collided
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

        Entity targetEntity = ((BodyUserData) other.getBody().getUserData()).getEntity();
        Entity meEntity = entity;

        if (meEntity instanceof MazePlayer) {
            // Means it is the player who is attacking. Should stun enemies for a short duration
            // Increment score if collision with fish eggs
            if (targetEntity instanceof FishEgg) {
                meEntity.getComponent(MazeGameManagerComponent.class).setLastFishEgg(targetEntity);
                AudioManager.playSound("sounds/minigames/collect-fishegg.mp3");
                return;
            }
            float stunDuration = 0.8f;
            if (targetEntity instanceof AnglerFish) {
                stunDuration = 1;
            } else if (targetEntity instanceof ElectricEel) {
                stunDuration = 2.8f;
            } else {
                ServiceLocator.getParticleService().playEffect(ParticleService.ParticleType.DAMAGE10, meEntity.getCenterPosition());
            }
            targetEntity.getComponent(StatusEffectComponent.class).setMinStatusExpiry("stun", stunDuration);
            AudioManager.playSound("sounds/minigames/maze-hit.mp3");
        }

        if (meEntity instanceof ElectricEel && targetEntity instanceof MazePlayer) {
            targetEntity.getComponent(StatusEffectComponent.class).setExpiryIfInactive("stun", 2f);
            AudioManager.playSound("sounds/minigames/eel-zap.mp3");
        }

        // Change to maze combat stats
        MazeCombatStatsComponent targetStats = targetEntity.getComponent(MazeCombatStatsComponent.class);
        MazeCombatStatsComponent myStats = meEntity.getComponent(MazeCombatStatsComponent.class);

        if (targetStats != null && myStats != null) {
            targetStats.hit(myStats);
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
