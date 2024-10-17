package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.npc.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.lighting.LightingEngine;
import com.csse3200.game.lighting.LightingService;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class EnemyFactoryTest {

    //pigeon and hive are special enemies that need their own testing classes
    private static Entity chicken;
    private static Entity frog;
    private static Entity monkey;
    private static Entity bear;
    private static Entity bee;
    private static Entity eel;
    private static Entity octopus;
    private static Entity bigSawFish;
    private static Entity macaw;
    private static Entity joey;
    private static List<Entity> enemies = new ArrayList<>();
    private static final NPCConfigs configs =
            FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");

    private static String[] textures = {
            "images/chicken2.png",
            "images/monkey.png",
            "images/frog2.png",
            "images/bear2.png",
		    "images/frog.png",
		    "images/chicken.png",
            "images/enemy-chicken.png",
            "images/bear.png",
            "images/eel1.png",
            "images/octopus.png",
            "images/bigsawfish.png",
            "images/macaw.png",
            "images/joey.png",
            "images/bee.png"
    };

    private static String[] atlas = {
            "images/chicken.atlas",
            "images/enemy-chicken.atlas",
            "images/monkey.atlas",
            "images/enemy-monkey.atlas",
            "images/enemy-frog.atlas",
            "images/bear.atlas",
            "images/enemy-bear.atlas",
            "images/eel.atlas",
            "images/octopus.atlas",
            "images/bigsawfish.atlas",
            "images/macaw.atlas",
            "images/joey.atlas",
            "images/bee.atlas",
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

        LightingEngine mockLightingEngine = mock(LightingEngine.class);
        LightingService mockLightingService = mock(LightingService.class);
        when(mockLightingService.getLighting()).thenReturn(mockLightingEngine);
        when(mockLightingEngine.createPointLight(anyFloat(), anyFloat(), anyFloat(), any(Color.class))).thenReturn(null);
        ServiceLocator.registerLightingService(mockLightingService);

        Entity player = new Entity();

        enemies.add(chicken = EnemyFactory.createChicken(player));
        enemies.add(frog = EnemyFactory.createFrog(player));
        enemies.add(monkey = EnemyFactory.createMonkey(player));
        enemies.add(bear = EnemyFactory.createBear(player));
        enemies.add(bee = EnemyFactory.createBee(player));
        enemies.add(eel = EnemyFactory.createEel(player));
        enemies.add(octopus = EnemyFactory.createOctopus(player));
        enemies.add(bigSawFish = EnemyFactory.createBigsawfish(player));
        enemies.add(macaw = EnemyFactory.createMacaw(player));
        enemies.add(joey = EnemyFactory.createJoey(player));
    }

    /**
     * Tests that entities are not null (have been initialised by their creator method)
     */
    @Test
    void TestEnemyCreation() {
        for (Entity enemy : enemies) {
            assertNotNull(enemy, String.format("%s should not be null.", enemy.getEnemyType()));
        }
    }

    /**
     * Tests that the monkey is an Entity.
     */
    @Test
    void TestEnemyIsEntity() {
        for (Entity enemy : enemies) {
            assertEquals(enemy.getClass(), Entity.class);
        }
    }

    /**
     * Tests that enemies are in the correct spot when placed.
     */
    @Test
    void TestEnemySetPosition() {
        for (Entity enemy : enemies) {
            //check initial set pos
            Vector2 pos = new Vector2(0f, 0f);
            enemy.setPosition(pos);

            assertEquals(pos, enemy.getPosition());

            //check can change pos
            pos.set((float) (Math.random() * 10), (float) (Math.random() * 10));
            enemy.setPosition(pos);

            assertEquals(pos, enemy.getPosition());
        }
    }

    /**
     * Tests that enemies have the correct components.
     */
    @Test
    void TestEnemiesHasComponents() {
        for (Entity enemy : enemies) {
            assertNotNull(enemy.getComponent(PhysicsComponent.class));
            assertNotNull(enemy.getComponent(PhysicsMovementComponent.class));
            assertNotNull(enemy.getComponent(EnemyAnimationController.class));
            assertNotNull(enemy.getComponent(CombatStatsComponent.class));
            assertNotNull(enemy.getComponent(HitboxComponent.class));
            assertNotNull(enemy.getComponent(ColliderComponent.class));
        }
    }

    /**
     * Tests that the monkey has the correct stats.
     */
    @Test
    void TestMonkeyStats() {
        assertTrue((monkey.getComponent(CombatStatsComponent.class).getHealth() > 12)
                        && (monkey.getComponent(CombatStatsComponent.class).getHealth() < 18),
                "Monkey should have between 12 and 18 HP inclusive.");
        assertTrue((monkey.getComponent(CombatStatsComponent.class).getStrength() > 27)
                        && (monkey.getComponent(CombatStatsComponent.class).getStrength() < 33),
                "Monkey should have between 28 and 32 attack inclusive.");
        assertTrue((monkey.getComponent(CombatStatsComponent.class).getDefense() > 17)
                        && (monkey.getComponent(CombatStatsComponent.class).getDefense() < 23),
                "Monkey should have defense between 7 and 9 inclusive.");
        assertEquals(75,
                monkey.getComponent(CombatStatsComponent.class).getSpeed(),
                "monkey should have 75 speed.");
        assertEquals(80,
                monkey.getComponent(CombatStatsComponent.class).getExperience(),
                "monkey should have 80 experience.");
    }

    /**
     * Tests that the monkey has correct animations.
     */
    @Test
    void TestMonkeyAnimation() {
        assertTrue(monkey.getComponent(AnimationRenderComponent.class).hasAnimation("runDown") ,
                "Monkey should have run down animation.");
        assertTrue(monkey.getComponent(AnimationRenderComponent.class).hasAnimation("runUp") ,
                "Monkey should have run up animation.");
        assertTrue(monkey.getComponent(AnimationRenderComponent.class).hasAnimation("runRight") ,
                "Monkey should have run right animation.");
        assertTrue(monkey.getComponent(AnimationRenderComponent.class).hasAnimation("runDown") ,
                "Cow should have idle animation.");
        assertTrue(monkey.getComponent(AnimationRenderComponent.class).hasAnimation("runRightDown") ,
                "Monkey should have run right down animation.");
        assertTrue(monkey.getComponent(AnimationRenderComponent.class).hasAnimation("runLeftDown") ,
                "Monkey should have run left down animation.");
        assertTrue(monkey.getComponent(AnimationRenderComponent.class).hasAnimation("runRightUp") ,
                "Monkey should have run right up animation.");
        assertTrue(monkey.getComponent(AnimationRenderComponent.class).hasAnimation("runLeftUp") ,
                "Monkey should have run left up animation.");
    }

    /**
     * Tests that the chicken has the correct stats.
     */
    @Test
    void TestChickenStats() {
        assertTrue((chicken.getComponent(CombatStatsComponent.class).getHealth() > 2)
                        && (chicken.getComponent(CombatStatsComponent.class).getHealth() < 8),
                "chicken should have between 3 and 7 HP.");
        assertTrue((chicken.getComponent(CombatStatsComponent.class).getStrength() > 7)
                        && (chicken.getComponent(CombatStatsComponent.class).getStrength() < 13),
                "chicken should have between 8 and 12 attack.");
        assertTrue((chicken.getComponent(CombatStatsComponent.class).getDefense() > 7)
                        && (chicken.getComponent(CombatStatsComponent.class).getDefense() < 13),
                "chicken should have between 8 and 12 defense.");
        assertEquals(100,
                chicken.getComponent(CombatStatsComponent.class).getSpeed(),
                "chicken should have 100 speed.");
        assertEquals(40,
                chicken.getComponent(CombatStatsComponent.class).getExperience(),
                "chicken should have 40 experience.");
    }

    /**
     * Tests that the chicken has correct animations.
     */
    @Test
    void TestChickenAnimation() {
        assertTrue(chicken.getComponent(AnimationRenderComponent.class).hasAnimation("wait") ,
                "Chicken should have wait animation.");
        assertTrue(chicken.getComponent(AnimationRenderComponent.class).hasAnimation("runRight") ,
                "Chicken should have runRight animation.");
    }

    /**
     * Tests that the frog has the correct stats.
     */
    @Test
    void TestFrogStats() {
        assertTrue((frog.getComponent(CombatStatsComponent.class).getHealth() > 7)
                        && (frog.getComponent(CombatStatsComponent.class).getHealth() < 13),
                "frog should have between 7 and 13 HP.");
        assertTrue((frog.getComponent(CombatStatsComponent.class).getStrength() > 57)
                        && (frog.getComponent(CombatStatsComponent.class).getStrength() < 63),
                "frog should have between 58 and 62 attack.");
        assertTrue((frog.getComponent(CombatStatsComponent.class).getDefense() > 7)
                        && (frog.getComponent(CombatStatsComponent.class).getDefense() < 13),
                "frog should have between 8 and 12 defense.");
        assertEquals(25,
                (frog.getComponent(CombatStatsComponent.class).getSpeed()),
                "frog should have 25 speed.");
        assertEquals(60,
                (frog.getComponent(CombatStatsComponent.class).getExperience()),
                "frog should have 60 experience.");
    }

    /**
     * Tests that the Frog has correct animations.
     */
    @Test
    void TestFrogAnimation() {
        assertTrue(frog.getComponent(AnimationRenderComponent.class).hasAnimation("wait") ,
                "Frog should have wait animation.");
        assertTrue(frog.getComponent(AnimationRenderComponent.class).hasAnimation("runRight") ,
                "Frog should have runRight animation.");
    }

    /**
     * Tests that the bear has the correct stats.
     */
    @Test
    void TestBearStats() {
        assertTrue((bear.getComponent(CombatStatsComponent.class).getHealth() > 22)
                        && (bear.getComponent(CombatStatsComponent.class).getHealth() < 28),
                "bear should have between 23 and 27 HP.");
        assertTrue((bear.getComponent(CombatStatsComponent.class).getStrength() > 52)
                        && (bear.getComponent(CombatStatsComponent.class).getStrength() < 58),
                "bear should have between 53 and 57 Attack.");
        assertTrue((bear.getComponent(CombatStatsComponent.class).getDefense() > 27)
                        && (bear.getComponent(CombatStatsComponent.class).getDefense() < 33),
                "bear should have between 28 and 32 defense.");
        assertEquals(37,
                bear.getComponent(CombatStatsComponent.class).getSpeed(),
                "bear should have 37 speed.");
        assertEquals(200,
                bear.getComponent(CombatStatsComponent.class).getExperience(),
                "bear should have 200 experience.");
    }

    /**
     * Tests that the bear has correct animations.
     */
    @Test
    void TestBearAnimation() {
        assertTrue(bear.getComponent(AnimationRenderComponent.class).hasAnimation("runRight") ,
                "bear should have runRight animation.");
        assertTrue(bear.getComponent(AnimationRenderComponent.class).hasAnimation("wait") ,
                "bear should have wait animation.");
    }

    /**
     * Tests that the eel has the correct stats.
     */
    @Test
    void TestEelStats() {
        assertTrue((eel.getComponent(CombatStatsComponent.class).getHealth() > 17)
                        && (eel.getComponent(CombatStatsComponent.class).getHealth() < 23),
                "eel should have between 18 and 22 HP.");
        assertTrue((eel.getComponent(CombatStatsComponent.class).getStrength() > 17)
                        && (eel.getComponent(CombatStatsComponent.class).getStrength() < 23),
                "eel should have between 18 and 22 Attack.");
        assertTrue((eel.getComponent(CombatStatsComponent.class).getDefense() > 12)
                        && (eel.getComponent(CombatStatsComponent.class).getDefense() < 18),
                "eel should have between 13 and 17 defense.");
        assertEquals(100,
                eel.getComponent(CombatStatsComponent.class).getSpeed(),
                "eel should have 100 speed.");
        assertEquals(100,
                eel.getComponent(CombatStatsComponent.class).getExperience(),
                "eel should have 100 experience.");
    }

    /**
     * Tests that the Eel has correct animations.
     */
    @Test
    void TestEelAnimation() {
        assertTrue(eel.getComponent(AnimationRenderComponent.class).hasAnimation("runDown") ,
                "eel should have runDown animation.");
        assertTrue(eel.getComponent(AnimationRenderComponent.class).hasAnimation("runRightDown") ,
                "eel should have runRightDown animation.");
        assertTrue(eel.getComponent(AnimationRenderComponent.class).hasAnimation("runRight") ,
                "eel should have runRight animation.");
        assertTrue(eel.getComponent(AnimationRenderComponent.class).hasAnimation("runRightUp") ,
                "eel should have runRightUp animation.");
        assertTrue(eel.getComponent(AnimationRenderComponent.class).hasAnimation("runUp") ,
                "eel should have runUp animation.");
    }

    /**
     * Tests that the joey has correct animations.
     */
    @Test
    void TestJoeyAnimation() {
        assertTrue(joey.getComponent(AnimationRenderComponent.class).hasAnimation("wait"),
                "joey should have wait animation.");
        assertTrue(joey.getComponent(AnimationRenderComponent.class).hasAnimation("runRight"),
                "joey should have runRight animation.");
    }

    /**
     * Tests that the bee has the correct stats.
     */
    @Test
    void TestBeeStats() {
        assertTrue((bee.getComponent(CombatStatsComponent.class).getHealth() > 2)
                        && (bee.getComponent(CombatStatsComponent.class).getHealth() < 8),
                "bee should have between 3 and 7 HP.");
        assertTrue((bee.getComponent(CombatStatsComponent.class).getStrength() > 22)
                        && (bee.getComponent(CombatStatsComponent.class).getStrength() < 28),
                "bee should have between 23 and 27 Attack.");
        assertTrue((bee.getComponent(CombatStatsComponent.class).getDefense() > 2)
                        && (bee.getComponent(CombatStatsComponent.class).getDefense() < 8),
                "bee should have between 3 and 7 defense.");
        assertEquals(135,
                bee.getComponent(CombatStatsComponent.class).getSpeed(),
                "bee should have 135 speed.");
        assertEquals(60,
                bee.getComponent(CombatStatsComponent.class).getExperience(),
                "bee should have 60 experience.");
    }

    /**
     * Tests that the bee has correct animations.
     */
    @Test
    void TestBeeAnimation() {

        assertTrue(bee.getComponent(AnimationRenderComponent.class).hasAnimation("runLeft"),
                "bee should have runLeft animation.");
        assertTrue(bee.getComponent(AnimationRenderComponent.class).hasAnimation("wait"),
                "bee should have wait animation.");
    }

    /**
     * Tests that the octopus has the correct stats.
     */
    @Test
    void TestOctopusStats() {
        assertTrue((octopus.getComponent(CombatStatsComponent.class).getHealth() > 22)
                        && (octopus.getComponent(CombatStatsComponent.class).getHealth() < 28),
                "octopus should have between 23 and 27 HP.");
        assertTrue((octopus.getComponent(CombatStatsComponent.class).getStrength() > 0)
                        && (octopus.getComponent(CombatStatsComponent.class).getStrength() < 6),
                "octopus should have between 1 and 3 Attack.");
        assertTrue((octopus.getComponent(CombatStatsComponent.class).getDefense() > 27)
                        && (octopus.getComponent(CombatStatsComponent.class).getDefense() < 33),
                "octopus should have between 27 and 33 defense.");
        assertEquals(85,
                octopus.getComponent(CombatStatsComponent.class).getSpeed(),
                "octopus should have 85 speed.");
        assertEquals(120,
                octopus.getComponent(CombatStatsComponent.class).getExperience(),
                "octopus should have 120 experience.");
    }

    /**
     * Tests that the octopus has correct animations.
     */
    @Test
    void TestOctopusAnimation() {

        assertTrue(octopus.getComponent(AnimationRenderComponent.class).hasAnimation("runRight"),
                "octopus should have runRight animation.");
        assertTrue(octopus.getComponent(AnimationRenderComponent.class).hasAnimation("wait"),
                "octopus should have wait animation.");
        assertTrue(octopus.getComponent(AnimationRenderComponent.class).hasAnimation("pull"),
                "octopus should have pull animation.");
    }

    /**
     * Tests that the macaw has the correct stats.
     */
    @Test
    void TestMacawStats() {
        assertTrue((macaw.getComponent(CombatStatsComponent.class).getHealth() > 27)
                        && (macaw.getComponent(CombatStatsComponent.class).getHealth() < 33),
                "macaw should have between 28 and 32 HP.");
        assertTrue((macaw.getComponent(CombatStatsComponent.class).getStrength() > 37)
                        && (macaw.getComponent(CombatStatsComponent.class).getStrength() < 43),
                "macaw should have between 38 and 42 Attack.");
        assertTrue((macaw.getComponent(CombatStatsComponent.class).getDefense() > 26)
                        && (macaw.getComponent(CombatStatsComponent.class).getDefense() < 32),
                "macaw should have between 27 and 31 defense.");
        assertEquals(50,
                macaw.getComponent(CombatStatsComponent.class).getSpeed(),
                "macaw should have 50 speed.");
        assertEquals(160,
                macaw.getComponent(CombatStatsComponent.class).getExperience(),
                "macaw should have 160 experience.");
    }

    /**
     * Tests that the macaw has correct animations.
     */
    @Test
    void TestMacawAnimation() {

        assertTrue(macaw.getComponent(AnimationRenderComponent.class).hasAnimation("wait"),
                "macaw should have wait animation.");
        assertTrue(macaw.getComponent(AnimationRenderComponent.class).hasAnimation("runRight"),
                "macaw should have runRight animation.");
    }

    /**
     * Tests that the bigSawFish has the correct stats.
     */
    @Test
    void TestBigSawFishStats() {
        assertTrue((bigSawFish.getComponent(CombatStatsComponent.class).getHealth() > 27)
                        && (bigSawFish.getComponent(CombatStatsComponent.class).getHealth() < 33),
                "bigSawFish should have between 28 and 32 HP.");
        assertTrue((bigSawFish.getComponent(CombatStatsComponent.class).getStrength() > 42)
                        && (bigSawFish.getComponent(CombatStatsComponent.class).getStrength() < 48),
                "bigSawFish should have between 43 and 47 Attack.");
        assertTrue((bigSawFish.getComponent(CombatStatsComponent.class).getDefense() > 27)
                        && (bigSawFish.getComponent(CombatStatsComponent.class).getDefense() < 33),
                "bigSawFish should have between 28 and 32 defense.");
        assertEquals(37,
                bigSawFish.getComponent(CombatStatsComponent.class).getSpeed(),
                "bigSawFish should have 37 speed.");
        assertEquals(140,
                bigSawFish.getComponent(CombatStatsComponent.class).getExperience(),
                "bigSawFish should have 140 experience.");
    }

    /**
     * Tests that the bigSawFish has correct animations.
     */
    @Test
    void TestBigSawFishAnimation() {

        assertTrue(macaw.getComponent(AnimationRenderComponent.class).hasAnimation("wait"),
                "macaw should have wait animation.");
        assertTrue(macaw.getComponent(AnimationRenderComponent.class).hasAnimation("runRight"),
                "macaw should have runRight animation.");
    }
}