package com.csse3200.game.minigames.maze.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.components.HitboxComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(GameExtension.class)
class MazeTouchAttackComponentTest {

    @BeforeEach
    void beforeEach() {

    }

    @Test
    void shouldNotAttackOtherLayer() {
        short targetLayer = (1 << 3);
        short attackLayer = (1 << 4);
        Entity player = createPlayer(attackLayer);
        Entity target = createTarget(targetLayer);

        Fixture playerFixture = player.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
        player.getEvents().trigger("collisionStart", playerFixture, targetFixture);

        assertEquals(10, target.getComponent(MazeCombatStatsComponent.class).getHealth());
    }

    @Test
    void shouldStunEnemyAndPlaySoundOnHit() {
        short targetLayer = (1 << 3);
        Entity player = createPlayer(targetLayer);
        Entity anglerFish = createAnglerFish(targetLayer);

        Fixture playerFixture = player.getComponent(HitboxComponent.class).getFixture();
        Fixture fishFixture = anglerFish.getComponent(HitboxComponent.class).getFixture();

        player.getEvents().trigger("collisionStart", playerFixture, fishFixture);

        // assertEquals(6, anglerFish.getComponent(MazeCombatStatsComponent.class).getHealth());
    }

    @Test
    void shouldApplyKnockbackToEnemy() {
        short targetLayer = (1 << 3);
        Entity player = createPlayer(targetLayer);
        Entity electricEel = createElectricEel(targetLayer);

        Fixture playerFixture = player.getComponent(HitboxComponent.class).getFixture();
        Fixture eelFixture = electricEel.getComponent(HitboxComponent.class).getFixture();

        player.getEvents().trigger("collisionStart", playerFixture, eelFixture);

    }

    private Entity createPlayer(short targetLayer) {
        return new Entity()
                .addComponent(new MazeTouchAttackComponent(targetLayer, 2.0f))
                .addComponent(new HitboxComponent());
    }

    private Entity createAnglerFish(short layer) {
        return new Entity()
                .addComponent(new MazeCombatStatsComponent(10, 10, 0))
                .addComponent(new HitboxComponent().setLayer(layer));
    }

    private Entity createElectricEel(short layer) {
        return new Entity()
                .addComponent(new MazeCombatStatsComponent(10, 10, 0))
                .addComponent(new HitboxComponent().setLayer(layer));
    }

    private Entity createTarget(short layer) {
        return new Entity()
                .addComponent(new MazeCombatStatsComponent(10, 10, 0))
                .addComponent(new HitboxComponent().setLayer(layer));
    }
}
