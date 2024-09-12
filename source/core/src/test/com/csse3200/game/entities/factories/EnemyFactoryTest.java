package com.csse3200.game.entities.factories;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.ConfigComponent;
import com.csse3200.game.components.npc.BearAnimationController;
import com.csse3200.game.components.npc.ChickenAnimationController;
import com.csse3200.game.components.npc.FrogAnimationController;
import com.csse3200.game.components.npc.MonkeyAnimationController;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.BaseEntityConfig;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class EnemyFactoryTest {

    private static Entity chicken;
    private static Entity frog;
    private static Entity monkey;
    private static Entity kanga;
    private static Entity bear;
    private static final NPCConfigs configs =
            FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");

    private static String[] textures = {
            "images/chicken.png",
            "images/monkey.png",
            "images/frog.png",
            "images/bear.png"
    };

    private static String[] atlas = {
            "images/chicken.atlas",
            "images/enemy-chicken.atlas",
            "images/monkey.atlas",
            "images/enemy-monkey.atlas",
            "images/frog.atlas",
            "images/enemy-frog.atlas",
            "images/frog.atlas",
            "images/bear.atlas",
            "images/final_boss_kangaroo.atlas"
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
        resourceService.loadTextures(textures);
        resourceService.loadTextureAtlases(atlas);
        resourceService.loadAll();

        Entity player = new Entity();
        chicken = EnemyFactory.createChicken(player);
        frog = EnemyFactory.createFrog(player);
        monkey = EnemyFactory.createMonkey(player);
        bear = EnemyFactory.createBear(player);
        kanga = EnemyFactory.createKangaBossEntity(player);
    }

    /**
     * Tests Creation of a monkey.
     */
    @Test
    void TestMonkeyCreation() {
        assertNotNull(monkey, "monkey should not be null.");
    }

    /**
     * Tests that the monkey is an Entity.
     */
    @Test
    void TestMonkeyIsEntity() {
        assertEquals(monkey.getClass(), Entity.class);
    }

    /**
     * Tests that the monkey has the correct components physics component.
     */
    @Test
    void TestMonkeyHasComponents() {
        assertNotNull(monkey.getComponent(PhysicsComponent.class));
        assertNotNull(monkey.getComponent(PhysicsMovementComponent.class));
        assertNotNull(monkey.getComponent(MonkeyAnimationController.class));
        assertNotNull(monkey.getComponent(CombatStatsComponent.class));
        assertNotNull(monkey.getComponent(HitboxComponent.class));
        assertNotNull(monkey.getComponent(ColliderComponent.class));
    }

    /**
     * Tests that the monkey has the correct stats.
     */
    @Test
    void TestMonkeyStats() {
        assertTrue((monkey.getComponent(CombatStatsComponent.class).getHealth() > 8) && (monkey.getComponent(CombatStatsComponent.class).getHealth() < 12),
                "Monkey should have between 9 and 11 HP inclusive.");
        assertEquals(1,
                monkey.getComponent(CombatStatsComponent.class).getSpeed(),
                "monkey should have 1 speed.");
    }

    /**
     * Tests that the monkey has correct animations.
     */
    @Test
    void TestMonkeyAnimation() {
        assertTrue(monkey.getComponent(AnimationRenderComponent.class).hasAnimation("run_down") ,
                "Monkey should have run down animation.");
        assertTrue(monkey.getComponent(AnimationRenderComponent.class).hasAnimation("run_up") ,
                "Monkey should have run up animation.");
        assertTrue(monkey.getComponent(AnimationRenderComponent.class).hasAnimation("run_right") ,
                "Monkey should have run right animation.");
        assertTrue(monkey.getComponent(AnimationRenderComponent.class).hasAnimation("run_down") ,
                "Cow should have idle animation.");
        assertTrue(monkey.getComponent(AnimationRenderComponent.class).hasAnimation("run_right_down") ,
                "Monkey should have run right down animation.");
        assertTrue(monkey.getComponent(AnimationRenderComponent.class).hasAnimation("run_left_down") ,
                "Monkey should have run left down animation.");
        assertTrue(monkey.getComponent(AnimationRenderComponent.class).hasAnimation("run_right_up") ,
                "Monkey should have run right up animation.");
        assertTrue(monkey.getComponent(AnimationRenderComponent.class).hasAnimation("run_left_up") ,
                "Monkey should have run left up animation.");
    }

    /**
     * Tests that the monkey is in the correct spot when placed.
     */
    @Test
    void TestMonkeySetPosition() {
        Vector2 pos = new Vector2(0f, 0f);
        monkey.setPosition(pos);

        assertEquals(pos, monkey.getPosition());
    }

    /**
     * Tests Creation of a chicken.
     */
    @Test
    void TestChickenCreation() {
        assertNotNull(chicken, "Chicken should not be null.");
    }

    /**
     * Tests that the chicken is an Entity.
     */
    @Test
    void TestChickenIsEntity() {
        assertEquals(chicken.getClass(), Entity.class);
    }

    /**
     * Tests that the chicken has the correct components.
     */
    @Test
    void TestChickenHasComponents() {
        assertNotNull(chicken.getComponent(PhysicsComponent.class));
        assertNotNull(chicken.getComponent(PhysicsMovementComponent.class));
        assertNotNull(chicken.getComponent(ChickenAnimationController.class));
        assertNotNull(chicken.getComponent(CombatStatsComponent.class));
        assertNotNull(chicken.getComponent(HitboxComponent.class));
        assertNotNull(chicken.getComponent(ColliderComponent.class));
    }

    /**
     * Tests that the chicken has the correct stats.
     */
    @Test
    void TestChickenStats() {
        assertTrue((chicken.getComponent(CombatStatsComponent.class).getHealth() > 3) && (chicken.getComponent(CombatStatsComponent.class).getHealth() < 7),
                "chicken should have between 4 and 6 HP.");
        assertEquals(2,
                chicken.getComponent(CombatStatsComponent.class).getSpeed(),
                "chicken should have 3 speed.");
    }

    /**
     * Tests that the chicken has correct animations.
     */
    @Test
    void TestChickenAnimation() {
        assertTrue(chicken.getComponent(AnimationRenderComponent.class).hasAnimation("walk") ,
                "Chicken should have walk animation.");
        assertTrue(chicken.getComponent(AnimationRenderComponent.class).hasAnimation("spawn") ,
                "Chicken should have spawn animation.");
    }

    /**
     * Tests that the chicken is in the correct spot when placed.
     */
    @Test
    void TestChickenSetPosition() {
        Vector2 pos = new Vector2(0f, 0f);
        chicken.setPosition(pos);
        assertEquals(pos, chicken.getPosition());
    }

    /**
     * Tests Creation of a frog.
     */
    @Test
    void TestFrogCreation() {
        assertNotNull(frog, "Frog should not be null.");
    }

    /**
     * Tests that the frog is an Entity.
     */
    @Test
    void TestFrogIsEntity() {
        assertEquals(frog.getClass(), Entity.class);
    }

    /**
     * Tests that the frog has the correct components.
     */
    @Test
    void TestFrogHasComponents() {
        assertNotNull(frog.getComponent(PhysicsComponent.class));
        assertNotNull(frog.getComponent(PhysicsMovementComponent.class));
        assertNotNull(frog.getComponent(FrogAnimationController.class));
        assertNotNull(frog.getComponent(CombatStatsComponent.class));
        assertNotNull(frog.getComponent(HitboxComponent.class));
        assertNotNull(frog.getComponent(ColliderComponent.class));
    }

    /**
     * Tests that the frog has the correct stats.
     */
    @Test
    void TestFrogStats() {
        assertTrue((frog.getComponent(CombatStatsComponent.class).getHealth() > 2) && (frog.getComponent(CombatStatsComponent.class).getHealth() < 6),
                "frog should have between 3 and 5 HP.");
        assertEquals(1,
                (frog.getComponent(CombatStatsComponent.class).getSpeed()),
                "frog should have 1 speed.");
    }

    /**
     * Tests that the Frog has correct animations.
     */
    @Test
    void TestFrogAnimation() {
        assertTrue(frog.getComponent(AnimationRenderComponent.class).hasAnimation("float") ,
                "Frog should have wander float animation.");
        assertTrue(frog.getComponent(AnimationRenderComponent.class).hasAnimation("angry_float") ,
                "Frog should have wander angry_float animation.");
    }

    /**
     * Tests that the frog is in the correct spot when placed.
     */
    @Test
    void TestFrogSetPosition() {
        Vector2 pos = new Vector2(0f, 0f);
        frog.setPosition(pos);
        assertEquals(pos, frog.getPosition());
    }

    /**
     * Tests Creation of a bear.
     */
    @Test
    void TestBearCreation() {
        assertNotNull(bear, "Bear should not be null.");
    }

    /**
     * Tests that the bear is an Entity.
     */
    @Test
    void TestBearIsEntity() {
        assertEquals(bear.getClass(), Entity.class);
    }

    /**
     * Tests that the bear has the correct components.
     */
    @Test
    void TestBearHasComponents() {
        assertNotNull(bear.getComponent(PhysicsComponent.class));
        assertNotNull(bear.getComponent(PhysicsMovementComponent.class));
        assertNotNull(bear.getComponent(BearAnimationController.class));
        assertNotNull(bear.getComponent(CombatStatsComponent.class));
        assertNotNull(bear.getComponent(HitboxComponent.class));
        assertNotNull(bear.getComponent(ColliderComponent.class));
    }

    /**
     * Tests that the bear has the correct stats.
     */
    @Test
    void TestBearStats() {
        assertTrue((bear.getComponent(CombatStatsComponent.class).getHealth() > 12) && (bear.getComponent(CombatStatsComponent.class).getHealth() < 18),
                "bear should have between 13 and 17 HP.");
        assertEquals(0,
                bear.getComponent(CombatStatsComponent.class).getSpeed(),
                "bear should have 0 speed.");
    }

    /**
     * Tests that the bear has correct animations.
     */
    @Test
    void TestBearAnimation() {
        assertTrue(bear.getComponent(AnimationRenderComponent.class).hasAnimation("chase") ,
                "bear should have chase animation.");
        assertTrue(bear.getComponent(AnimationRenderComponent.class).hasAnimation("float") ,
                "bear should have float animation.");
    }

    /**
     * Tests that the bear is in the correct spot when placed.
     */
    @Test
    void TestBearSetPosition() {
        Vector2 pos = new Vector2(0f, 0f);
        bear.setPosition(pos);
        assertEquals(pos, bear.getPosition());
    }


    static class TestComponent1 extends Component {}

    /**
     * Tests Creation of Kanga.
     */
    @Test
    void TestKangaCreation() {
        assertNotNull(kanga, "Kanga should not be null.");
    }

    /**
     * Tests that the Kanga is an Entity.
     */
    @Test
    void TestKangaIsEntity() {
        assertEquals(kanga.getClass(), Entity.class);
    }

    /**
     * Tests that the Kanga has a physics component.
     */
    @Test
    void TestKangaHasPhysicsComponent() {
        assertNotNull(kanga.getComponent(PhysicsComponent.class));
    }

    /**
     * Tests that the Kanga has a physics movement component.
     */
    @Test
    void TestKangaHasPhysicsMovementComponent() {
        assertNotNull(kanga.getComponent(PhysicsMovementComponent.class));
    }

    /**
     * Tests the Kanga has a collider component.
     */
    @Test
    void TestKangaHasColliderComponent() {
        assertNotNull(kanga.getComponent(ColliderComponent.class));
    }

    /**
     * Tests that the Kanga has the correct HP stat.
     */
    @Test
    void TestKangaHasCorrectHP() {
        assertEquals(150, kanga.getComponent(CombatStatsComponent.class).getHealth(),
                "Kanga should have 150 HP.");
    }

    /**
     * Tests that the Kanga has an idle animation.
     */
    @Test
    void TestKangaHasAnimation() {
        assertTrue(kanga.getComponent(AnimationRenderComponent.class).hasAnimation("float") ,
                "Kanga should have idle animation.");
    }

    /**
     * Tests that the Kanga is in the correct spot when placed.
     */
    @Test
    void TestKangaSetPosition() {
        Vector2 pos = new Vector2(0f, 0f);
        kanga.setPosition(pos);

        assertEquals(pos, kanga.getPosition());
    }
}