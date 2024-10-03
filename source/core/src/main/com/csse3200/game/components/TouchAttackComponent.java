package com.csse3200.game.components;


import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;


/**
 * When this entity touches a valid enemy's hitbox, deal damage to them and apply a knockback.
 *
 * <p>Requires CombatStatsComponent, HitboxComponent on this entity.
 *
 * <p>Damage is only applied if target entity has a CombatStatsComponent. Knockback is only applied
 * if target entity has a PhysicsComponent.
 */
public class TouchAttackComponent extends Component {
    protected short targetLayer;
    protected CombatStatsComponent combatStats;
    protected HitboxComponent hitboxComponent;
    
    /**
     * Create a component which attacks entities on collision, without knockback.
     * @param targetLayer The physics layer of the target's collider.
     */
    public TouchAttackComponent(short targetLayer) {
        this.targetLayer = targetLayer;
    }
    
    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        combatStats = entity.getComponent(CombatStatsComponent.class);
        hitboxComponent = entity.getComponent(HitboxComponent.class);
    }
    
    protected boolean checkHitboxAndLayer(Fixture me, Fixture other) {
        // Not triggered by hitbox, ignore
        return hitboxComponent.getFixture() != me ||
                !PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits);
    }
    
    protected void onCollisionStart(Fixture me, Fixture other) {
        if (checkHitboxAndLayer(me, other)) return;
        
        // Try to attack target.
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);
        if (targetStats != null) {
            // Trigger event to start combat screen
            target.getEvents().trigger("startCombat", this.entity);
        }
    }
}
