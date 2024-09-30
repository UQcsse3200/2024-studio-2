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
public class ProjectileAttackComponent extends TouchAttackComponent {
    
    private int damage;
    
    /**
     * Constructs a ProjectileAttackComponent for handling attacks on entities upon collision.
     * The component is configured to target entities in a specific physics layer.
     *
     * @param targetLayer The physics layer of the target entities' collider.
     * @param damage the amount of damage this projectile does
     */
    public ProjectileAttackComponent(short targetLayer, int damage) {
        super(targetLayer);
        this.damage = damage;
    }
    
    @Override
    protected void onCollisionStart(Fixture me, Fixture other) {
        if (checkHitboxAndLayer(me, other)) return;
        
        // does damage if player
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        CombatStatsComponent combatStatsComponent = target.getComponent(CombatStatsComponent.class);
        if (combatStatsComponent != null) {
            combatStatsComponent.addHealth(-this.damage);
        }
        
        // disposes of projectile
        Entity owner = getEntity();
        ProjectileMovementTask task = (ProjectileMovementTask) owner.getComponent(AITaskComponent.class).getCurrentTask();
        task.getMovementTask().stop();
    }
}
