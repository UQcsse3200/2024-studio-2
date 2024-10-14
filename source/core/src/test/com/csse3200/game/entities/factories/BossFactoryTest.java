package com.csse3200.game.entities.factories;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.combat.CombatAnimationController;
import com.csse3200.game.components.npc.BossAnimationController;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class BossFactoryTest {

    private static Entity kanga;
    private static Entity waterBoss;
    private static Entity airBoss;
    private static Entity genericCombatBoss;

    private static final String[] atlas = {
            "images/final_boss_kangaroo.atlas",
            "images/water_boss.atlas",
            "images/air_boss.atlas"
    };

    @BeforeAll
    static void setup() {
        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getDeltaTime()).thenReturn(0.02f);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
        RenderService render = new RenderService();
        render.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(render);
        ResourceService resourceService = new ResourceService();
        ServiceLocator.registerResourceService(resourceService);

        resourceService.loadTextureAtlases(atlas);
        resourceService.loadAll();

        Entity player = new Entity();
        kanga = BossFactory.createKangaBossEntity(player);
        waterBoss = BossFactory.createWaterBossEntity(player);
        airBoss = BossFactory.createAirBossEntity(player);
        genericCombatBoss = CombatAnimalFactory.createKangaBossCombatEntity();
    }

    /**
     * Tests Creation of a Kanga boss.
     */
    @Test
    void TestKangaCreation() {
        assertNotNull(kanga, "Kanga should not be null.");
    }

    /**
     * Tests that Kanga is an Entity.
     */
    @Test
    void TestKangaIsEntity() {
        assertEquals(kanga.getClass(), Entity.class);
    }

    /**
     * Tests that Kanga has correct components.
     */
    @Test
    void TestKangaHasComponents() {
        assertNotNull(kanga.getComponent(PhysicsComponent.class));
        assertNotNull(kanga.getComponent(PhysicsMovementComponent.class));
        assertNotNull(kanga.getComponent(ColliderComponent.class));
        assertNotNull(kanga.getComponent(BossAnimationController.class));
        assertNotNull(kanga.getComponent(CombatStatsComponent.class));
    }

    /**
     * Tests that Kanga has correct stats.
     */
    @Test
    void TestKangaStats() {
        assertEquals(150, kanga.getComponent(CombatStatsComponent.class).getHealth(),
                "Kanga should have 150 HP.");
    }

    /**
     * Tests Kanga animation.
     */
    @Test
    void TestKangaAnimation() {
        assertTrue(kanga.getComponent(AnimationRenderComponent.class).hasAnimation("wander"),
                "Kanga should have wander animation.");
    }

    /**
     * Tests Kanga's position.
     */
    @Test
    void TestKangaSetPosition() {
        Vector2 pos = new Vector2(0f, 0f);
        kanga.setPosition(pos);
        assertEquals(pos, kanga.getPosition());
    }

    /**
     * Tests Creation of Water Boss.
     */
    @Test
    void TestWaterBossCreation() {
        assertNotNull(waterBoss, "Water Boss should not be null.");
    }

    /**
     * Tests that Water Boss is an Entity.
     */
    @Test
    void TestWaterBossIsEntity() {
        assertEquals(waterBoss.getClass(), Entity.class);
    }

    /**
     * Tests Water Boss has correct components.
     */
    @Test
    void TestWaterBossHasComponents() {
        assertNotNull(waterBoss.getComponent(PhysicsComponent.class));
        assertNotNull(waterBoss.getComponent(PhysicsMovementComponent.class));
        assertNotNull(waterBoss.getComponent(ColliderComponent.class));
        assertNotNull(waterBoss.getComponent(BossAnimationController.class));
        assertNotNull(waterBoss.getComponent(CombatStatsComponent.class));
    }

    /**
     * Tests Water Boss has correct HP.
     */
    @Test
    void TestWaterBossStats() {
        assertEquals(150, waterBoss.getComponent(CombatStatsComponent.class).getHealth(),
                "Water Boss should have 150 HP.");
    }

    /**
     * Tests Water Boss animation.
     */
    @Test
    void TestWaterBossAnimation() {
        assertTrue(waterBoss.getComponent(AnimationRenderComponent.class).hasAnimation("wander"),
                "Water Boss should have wander animation.");
    }

    /**
     * Tests Creation of Air Boss.
     */
    @Test
    void TestAirBossCreation() {
        assertNotNull(airBoss, "Air Boss should not be null.");
    }

    /**
     * Tests that Air Boss is an Entity.
     */
    @Test
    void TestAirBossIsEntity() {
        assertEquals(airBoss.getClass(), Entity.class);
    }

    /**
     * Tests Air Boss has correct components.
     */
    @Test
    void TestAirBossHasComponents() {
        assertNotNull(airBoss.getComponent(PhysicsComponent.class));
        assertNotNull(airBoss.getComponent(PhysicsMovementComponent.class));
        assertNotNull(airBoss.getComponent(ColliderComponent.class));
        assertNotNull(airBoss.getComponent(BossAnimationController.class));
        assertNotNull(airBoss.getComponent(CombatStatsComponent.class));
    }

    /**
     * Tests Air Boss has correct HP.
     */
    @Test
    void TestAirBossStats() {
        assertEquals(150, airBoss.getComponent(CombatStatsComponent.class).getHealth(),
                "Air Boss should have 150 HP.");
    }

    /**
     * Tests Air Boss animation.
     */
    @Test
    void TestAirBossAnimation() {
        assertTrue(airBoss.getComponent(AnimationRenderComponent.class).hasAnimation("wander"),
                "Air Boss should have wander animation.");
    }
    /**
     * Tests Creation of Kanga combat boss.
     */
    @Test
    void TestKangaCombatBossCreation() {
        assertNotNull(kanga, "Kanga combat boss should not be null.");
    }

    /**
     * Tests that Kanga combat boss is an Entity.
     */
    @Test
    void TestKangaCombatBossIsEntity() {
        assertEquals(kanga.getClass(), Entity.class);
    }

    /**
     * Tests that Kanga combat boss has correct HP.
     */
    @Test
    void TestKangaCombatBossStats() {
        assertEquals(150, kanga.getComponent(CombatStatsComponent.class).getHealth(),
                "Kanga combat boss should have 150 HP.");
    }

    /**
     * Tests Creation of a generic combat boss.
     */
    @Test
    void TestGenericCombatBossCreation() {
        assertNotNull(genericCombatBoss, "Generic combat boss should not be null.");
    }

    /**
     * Tests that generic combat boss is an Entity.
     */
    @Test
    void TestGenericCombatBossIsEntity() {
        assertEquals(genericCombatBoss.getClass(), Entity.class);
    }

    /**
     * Tests that generic combat boss has correct components.
     */
    @Test
    void TestGenericCombatBossHasComponents() {
        assertNotNull(genericCombatBoss.getComponent(PhysicsComponent.class));
        assertNotNull(genericCombatBoss.getComponent(ColliderComponent.class));
        assertNotNull(genericCombatBoss.getComponent(AnimationRenderComponent.class));
        assertNotNull(genericCombatBoss.getComponent(CombatAnimationController.class));
    }
}