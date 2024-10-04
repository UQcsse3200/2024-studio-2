package com.csse3200.game.components;


import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.components.tasks.HiveTask;
import com.csse3200.game.components.tasks.ProjectileMovementTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.components.HitboxComponent;
import static com.csse3200.game.entities.Entity.EnemyType.ELECTRICORB;
import static com.csse3200.game.entities.Entity.EnemyType.HIVE;


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
    Entity target;
    
    /**
     * Constructs a ProjectileAttackComponent for handling attacks on entities upon collision.
     * The component is configured to target entities in a specific physics layer.
     *
     * @param targetLayer The physics layer of the target entities' collider.
     * @param damage the amount of damage this projectile does
     */
    public ProjectileAttackComponent(short targetLayer, int damage, Entity target) {
        super(targetLayer);
        this.damage = damage;
        this.target = target;
    }
    
    @Override
    protected void onCollisionStart(Fixture me, Fixture other) {
        if (checkHitboxAndLayer(me, other)) return;
        
        // does damage if player
        if (((BodyUserData) other.getBody().getUserData()).entity.isPlayer()) { //dont do damage if not player
            if (getEntity().getEnemyType() == ELECTRICORB) {
                target.getComponent(KeyboardPlayerInputComponent.class).paralyze();
            } else {
                target.getComponent(CombatStatsComponent.class).addHealth(-damage);
            }
        }
        
        // disposes of projectile
        Entity owner = getEntity();
        if (owner.getEnemyType() == HIVE) {
            HiveTask task = (HiveTask) owner.getComponent(AITaskComponent.class).getCurrentTask();
            if (task != null) {
                task.stop();
            }
        } else {
            ProjectileMovementTask task = (ProjectileMovementTask) owner.getComponent(AITaskComponent.class).getCurrentTask();
            if (task != null) task.getMovementTask().stop();
        }
    }
}
