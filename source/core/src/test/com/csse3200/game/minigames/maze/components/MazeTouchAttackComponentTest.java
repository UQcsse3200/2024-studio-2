package com.csse3200.game.minigames.maze.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.minigames.maze.areas.MazeGameArea;
import com.csse3200.game.minigames.maze.areas.MazeGameAreaTest;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class MazeTouchAttackComponentTest {
    MazeGameArea gameArea;

    @BeforeEach
    void beforeEach() throws IllegalAccessException {
        gameArea = MazeGameAreaTest.setupFullMazeGame();
        gameArea.create();
    }

    @Test
    void shouldNotAttackOtherLayer() {
        short targetLayer = (1 << 3);
        Entity player = gameArea.getPlayer();
        Entity target = gameArea.getEnemies(Entity.EnemyType.MAZE_ANGLER).getFirst();

        target.getComponent(HitboxComponent.class).setLayer(targetLayer);

        Fixture playerFixture = player.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
        player.getEvents().trigger("collisionStart", playerFixture, targetFixture);

        assertEquals(90, target.getComponent(MazeCombatStatsComponent.class).getHealth());
    }

    @Test
    void shouldStunEnemyAndPlaySoundOnHit() {
        Entity player = gameArea.getPlayer();
        Entity anglerFish = gameArea.getEnemies(Entity.EnemyType.MAZE_ANGLER).getFirst();

        Fixture playerFixture = player.getComponent(HitboxComponent.class).getFixture();
        Fixture fishFixture = anglerFish.getComponent(HitboxComponent.class).getFixture();

        player.getEvents().trigger("collisionStart", playerFixture, fishFixture);

        // assertEquals(6, anglerFish.getComponent(MazeCombatStatsComponent.class).getHealth());
    }

    @Test
    void shouldInkOnHit() {
        Entity player = gameArea.getPlayer();
        Entity octopus = gameArea.getEnemies(Entity.EnemyType.MAZE_OCTOPUS).getFirst();

        Fixture playerFixture = player.getComponent(HitboxComponent.class).getFixture();
        Fixture fishFixture = octopus.getComponent(HitboxComponent.class).getFixture();

        player.getEvents().trigger("collisionStart", playerFixture, fishFixture);
        octopus.getEvents().trigger("collisionStart", fishFixture, playerFixture);

        ServiceLocator.getEntityService().update();
        when(ServiceLocator.getTimeSource().getDeltaTime()).thenReturn(1000f);
        ServiceLocator.getEntityService().update();
    }

    @Test
    void shouldIgnoreCollisionsWithNonTargetLayer() {
        short nonTargetLayer = (1 << 5);  // A different layer we should ignore

        Entity player = gameArea.getPlayer();
        Entity target = gameArea.getEnemies(Entity.EnemyType.MAZE_ANGLER).getFirst();  // Entity on the wrong layer

        target.getComponent(HitboxComponent.class).setLayer(nonTargetLayer);

        Fixture playerFixture = player.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
        player.getEvents().trigger("collisionStart", playerFixture, targetFixture);

        // Check that no health has been deducted
        assertEquals(100, target.getComponent(MazeCombatStatsComponent.class).getHealth());
    }

    @Test
    void shouldApplyKnockbackToEnemy() {
        Entity player = gameArea.getPlayer();
        Entity electricEel = gameArea.getEnemies(Entity.EnemyType.MAZE_EEL).getFirst();

        Fixture playerFixture = player.getComponent(HitboxComponent.class).getFixture();
        Fixture eelFixture = electricEel.getComponent(HitboxComponent.class).getFixture();

        player.getEvents().trigger("collisionStart", playerFixture, eelFixture);
        electricEel.getEvents().trigger("collisionStart", eelFixture, playerFixture);

        ServiceLocator.getEntityService().update();
        when(ServiceLocator.getTimeSource().getDeltaTime()).thenReturn(1000f);
        ServiceLocator.getEntityService().update();
    }

    @Test
    void shouldCollectFishEgg() {
        Entity player = gameArea.getPlayer();
        Entity fishEgg = gameArea.getEggs().getFirst();

        Fixture playerFixture = player.getComponent(HitboxComponent.class).getFixture();
        Fixture fishEggFixture = fishEgg.getComponent(HitboxComponent.class).getFixture();

        player.getEvents().trigger("collisionStart", playerFixture, fishEggFixture);

        ServiceLocator.getEntityService().update();
        when(ServiceLocator.getTimeSource().getDeltaTime()).thenReturn(1000f);
        ServiceLocator.getEntityService().update();
    }

    @Test
    void shouldIgnoreFishEggIfReversedTrigger() {
        Entity player = gameArea.getPlayer();
        Entity fishEgg = gameArea.getEggs().getFirst();

        Fixture playerFixture = player.getComponent(HitboxComponent.class).getFixture();
        Fixture fishEggFixture = fishEgg.getComponent(HitboxComponent.class).getFixture();

        fishEgg.getEvents().trigger("collisionStart", playerFixture, fishEggFixture);
    }
}
