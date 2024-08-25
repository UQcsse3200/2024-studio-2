package com.csse3200.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.GdxGame;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * When this entity touches a valid enemy's hitbox, deal damage to them and apply a knockback.
 *
 * <p>Requires CombatStatsComponent, HitboxComponent on this entity.
 *
 * <p>Damage is only applied if target entity has a CombatStatsComponent. Knockback is only applied
 * if target entity has a PhysicsComponent.
 */
public class EnterCombatComponent extends Component {
    private final short targetLayer;
    private HitboxComponent hitboxComponent;
    private final GdxGame game;

    /**
     * Create a component which attacks entities on collision, without knockback.
     * @param targetLayer The physics layer of the target's collider.
     */
    public EnterCombatComponent(short targetLayer, GdxGame game) {
        this.targetLayer = targetLayer;
        this.game = game;
    }


    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        hitboxComponent = entity.getComponent(HitboxComponent.class);
    }

    private void onCollisionStart(Fixture me, Fixture other) {
        Logger logger = LoggerFactory.getLogger(EnterCombatComponent.class);
        logger.info("collision Hit");
        if (hitboxComponent.getFixture() != me) {
            // Not triggered by hitbox, ignore
            return;
        }

        if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore
            return;
        }
        game.addMainGameDup();
    }
}

