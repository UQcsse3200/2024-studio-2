package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.npc.BearAnimationController;
import com.csse3200.game.components.npc.ChickenAnimationController;
import com.csse3200.game.components.npc.FrogAnimationController;
import com.csse3200.game.components.npc.MonkeyAnimationController;
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
            "images/pigeon2.png",
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
            "images/frog.atlas",
            "images/enemy-frog.atlas",
            "images/bear.atlas",
            "images/pigeon.atlas",
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
        assertTrue((monkey.getComponent(CombatStatsComponent.class).getHealth() > 8)
                        && (monkey.getComponent(CombatStatsComponent.class).getHealth() < 12),
                "Monkey should have between 9 and 11 HP inclusive.");
        assertTrue((monkey.getComponent(CombatStatsComponent.class).getStrength() > 22)
                        && (monkey.getComponent(CombatStatsComponent.class).getStrength() < 29),
                "Monkey should have between 23 and 28 attack inclusive.");
        assertTrue((monkey.getComponent(CombatStatsComponent.class).getDefense() > 6)
                        && (monkey.getComponent(CombatStatsComponent.class).getDefense() < 10),
                "Monkey should have defense between 7 and 9 inclusive.");
        assertEquals(150,
                monkey.getComponent(CombatStatsComponent.class).getSpeed(),
                "monkey should have 150 speed.");
        assertEquals(25,
                monkey.getComponent(CombatStatsComponent.class).getExperience(),
                "monkey should have 25 experience.");
    }

    /**
     * Tests that the monkey has correct animations.
     */
    @Test
    void TestMonkeyAnimation() {
        //test that the animation controller is there
        assertNotNull(monkey.getComponent(MonkeyAnimationController.class), "Missing animation controller");
        
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
     * Tests that the chicken has the correct stats.
     */
    @Test
    void TestChickenStats() {
        assertTrue((chicken.getComponent(CombatStatsComponent.class).getHealth() > 3)
                        && (chicken.getComponent(CombatStatsComponent.class).getHealth() < 7),
                "chicken should have between 4 and 6 HP.");
        assertTrue((chicken.getComponent(CombatStatsComponent.class).getStrength() >= 0)
                        && (chicken.getComponent(CombatStatsComponent.class).getStrength() < 5),
                "chicken should have between 0 and 4 attack.");
        assertTrue((chicken.getComponent(CombatStatsComponent.class).getDefense() > 0)
                        && (chicken.getComponent(CombatStatsComponent.class).getDefense() < 4),
                "chicken should have between 1 and 3 defense.");
        assertEquals(200,
                chicken.getComponent(CombatStatsComponent.class).getSpeed(),
                "chicken should have 200 speed.");
        assertEquals(10,
                chicken.getComponent(CombatStatsComponent.class).getExperience(),
                "chicken should have 10 experience.");
    }

    /**
     * Tests that the chicken has correct animations.
     */
    @Test
    void TestChickenAnimation() {
        //test that the animation controller is there
        assertNotNull(chicken.getComponent(ChickenAnimationController.class));
        
        assertTrue(chicken.getComponent(AnimationRenderComponent.class).hasAnimation("walk") ,
                "Chicken should have walk animation.");
        assertTrue(chicken.getComponent(AnimationRenderComponent.class).hasAnimation("spawn") ,
                "Chicken should have spawn animation.");
    }

    /**
     * Tests that the frog has the correct stats.
     */
    @Test
    void TestFrogStats() {
        assertTrue((frog.getComponent(CombatStatsComponent.class).getHealth() > 2)
                        && (frog.getComponent(CombatStatsComponent.class).getHealth() < 6),
                "frog should have between 3 and 5 HP.");
        assertTrue((frog.getComponent(CombatStatsComponent.class).getStrength() > 22)
                        && (frog.getComponent(CombatStatsComponent.class).getStrength() < 28),
                "frog should have between 23 and 27 attack.");
        assertTrue((frog.getComponent(CombatStatsComponent.class).getDefense() > 0)
                        && (frog.getComponent(CombatStatsComponent.class).getDefense() < 3),
                "frog should have between 1 and 2 defense.");
        assertEquals(50,
                (frog.getComponent(CombatStatsComponent.class).getSpeed()),
                "frog should have 50 speed.");
        assertEquals(20,
                (frog.getComponent(CombatStatsComponent.class).getExperience()),
                "frog should have 20 experience.");
    }

    /**
     * Tests that the Frog has correct animations.
     */
    @Test
    void TestFrogAnimation() {
        //test that the animation controller is there
        assertNotNull(frog.getComponent(FrogAnimationController.class));
        
        assertTrue(frog.getComponent(AnimationRenderComponent.class).hasAnimation("jump") ,
                "Frog should have jump animation.");
        assertTrue(frog.getComponent(AnimationRenderComponent.class).hasAnimation("still") ,
                "Frog should have still animation.");
    }

    /**
     * Tests that the bear has the correct stats.
     */
    @Test
    void TestBearStats() {
        assertTrue((bear.getComponent(CombatStatsComponent.class).getHealth() > 12)
                        && (bear.getComponent(CombatStatsComponent.class).getHealth() < 18),
                "bear should have between 13 and 17 HP.");
        assertTrue((bear.getComponent(CombatStatsComponent.class).getStrength() > 7)
                        && (bear.getComponent(CombatStatsComponent.class).getStrength() < 14),
                "bear should have between 8 and 13 Attack.");
        assertTrue((bear.getComponent(CombatStatsComponent.class).getDefense() > 12)
                        && (bear.getComponent(CombatStatsComponent.class).getDefense() < 18),
                "bear should have between 13 and 17 defense.");
        assertEquals(75,
                bear.getComponent(CombatStatsComponent.class).getSpeed(),
                "bear should have 75 speed.");
        assertEquals(85,
                bear.getComponent(CombatStatsComponent.class).getExperience(),
                "bear should have 85 experience.");
    }

    /**
     * Tests that the bear has correct animations.
     */
    @Test
    void TestBearAnimation() {
        //test that the animation controller is there
        assertNotNull(bear.getComponent(BearAnimationController.class));
        
        assertTrue(bear.getComponent(AnimationRenderComponent.class).hasAnimation("chase") ,
                "bear should have chase animation.");
        assertTrue(bear.getComponent(AnimationRenderComponent.class).hasAnimation("float") ,
                "bear should have float animation.");
        assertTrue(bear.getComponent(AnimationRenderComponent.class).hasAnimation("spawn") ,
                "bear should have spawn animation.");
    }
}