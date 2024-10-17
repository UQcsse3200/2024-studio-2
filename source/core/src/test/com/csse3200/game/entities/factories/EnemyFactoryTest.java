package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.npc.BearAnimationController;
import com.csse3200.game.components.npc.ChickenAnimationController;
import com.csse3200.game.components.npc.FrogAnimationController;
import com.csse3200.game.components.npc.MonkeyAnimationController;
import com.csse3200.game.components.npc.EelAnimationController;
import com.csse3200.game.components.npc.BeeAnimationController;
import com.csse3200.game.components.npc.OctopusAnimationController;
//import com.csse3200.game.components.npc.PigeonAnimationController;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class EnemyFactoryTest {

    private static Entity chicken;
    private static Entity frog;
    private static Entity monkey;
    private static Entity bear;
    private static Entity pigeon;
    private static Entity eel;
    private static Entity octopus;
    private static Entity bee;
    //private static Entity bigsawfish;
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
		    "images/bear.png",
            "images/eel.png",
            "images/octopus.png",
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
            //"images/pigeon.atlas",
            "images/enemy-bear.atlas",
            "images/final_boss_kangaroo.atlas",
            "images/eel.atlas",
            "images/octopus.atlas",
            "images/bee.atlas"
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
        chicken = EnemyFactory.createChicken(player);
        frog = EnemyFactory.createFrog(player);
        monkey = EnemyFactory.createMonkey(player);
        bear = EnemyFactory.createBear(player);
        eel = EnemyFactory.createEel(player);
        octopus = EnemyFactory.createOctopus(player);
        bee = EnemyFactory.createBee(player);
        //pigeon = EnemyFactory.createPigeon(player);
        //bigsawfish = EnemyFactory.createBigsawfish(player);
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
        assertTrue(frog.getComponent(AnimationRenderComponent.class).hasAnimation("jump") ,
                "Frog should have jump animation.");
        assertTrue(frog.getComponent(AnimationRenderComponent.class).hasAnimation("still") ,
                "Frog should have still animation.");
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
        assertTrue(bear.getComponent(AnimationRenderComponent.class).hasAnimation("chase") ,
                "bear should have chase animation.");
        assertTrue(bear.getComponent(AnimationRenderComponent.class).hasAnimation("float") ,
                "bear should have float animation.");
        assertTrue(bear.getComponent(AnimationRenderComponent.class).hasAnimation("spawn") ,
                "bear should have spawn animation.");
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

    /**
     * Tests Creation of a octopus.
     */
    @Test
    void TestOctopusCreation() {
        assertNotNull(octopus, "octopus should not be null.");
    }

    /**
     * Tests that the octopus is an Entity.
     */
    @Test
    void TestOctopusIsEntity() {
        assertEquals(octopus.getClass(), Entity.class);
    }

    /**
     * Tests that the octopus has the correct components.
     */
    @Test
    void TestOctopusHasComponents() {
        assertNotNull(octopus.getComponent(PhysicsComponent.class));
        assertNotNull(octopus.getComponent(PhysicsMovementComponent.class));
        assertNotNull(octopus.getComponent(OctopusAnimationController.class));
        assertNotNull(octopus.getComponent(CombatStatsComponent.class));
        assertNotNull(octopus.getComponent(HitboxComponent.class));
        assertNotNull(octopus.getComponent(ColliderComponent.class));
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
        assertTrue(octopus.getComponent(AnimationRenderComponent.class).hasAnimation("chase") ,
                "octopus should have chase animation.");
        assertTrue(octopus.getComponent(AnimationRenderComponent.class).hasAnimation("float") ,
                "octopus should have float animation.");
    }

    /**
     * Tests that the octopus is in the correct spot when placed.
     */
    @Test
    void TestOctopusSetPosition() {
        Vector2 pos = new Vector2(0f, 0f);
        octopus.setPosition(pos);
        assertEquals(pos, octopus.getPosition());
    }


    /**
     * Tests Creation of a eel.
     */
    @Test
    void TestEelCreation() {
        assertNotNull(eel, "eel should not be null.");
    }

    /**
     * Tests that the eel is an Entity.
     */
    @Test
    void TestEelIsEntity() {
        assertEquals(eel.getClass(), Entity.class);
    }

    /**
     * Tests that the eel has the correct components.
     */
    @Test
    void TestEelHasComponents() {
        assertNotNull(eel.getComponent(PhysicsComponent.class));
        assertNotNull(eel.getComponent(PhysicsMovementComponent.class));
        assertNotNull(eel.getComponent(EelAnimationController.class));
        assertNotNull(eel.getComponent(CombatStatsComponent.class));
        assertNotNull(eel.getComponent(HitboxComponent.class));
        assertNotNull(eel.getComponent(ColliderComponent.class));
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
     * Tests that the eel has correct animations.
     */
    @Test
    void TestEelAnimation() {
        assertTrue(eel.getComponent(AnimationRenderComponent.class).hasAnimation("swim_down") ,
                "eel should have swim down animation.");
        assertTrue(eel.getComponent(AnimationRenderComponent.class).hasAnimation("swim_down_right") ,
                "eel should have swim down right animation.");
        assertTrue(eel.getComponent(AnimationRenderComponent.class).hasAnimation("swim_right") ,
                "eel should have swim right animation.");
        assertTrue(eel.getComponent(AnimationRenderComponent.class).hasAnimation("swim_up_right") ,
                "eel should have swim up right animation.");
        assertTrue(eel.getComponent(AnimationRenderComponent.class).hasAnimation("swim_up") ,
                "eel should have swim up animation.");
    }

    /**
     * Tests that the eel is in the correct spot when placed.
     */
    @Test
    void TestEelSetPosition() {
        Vector2 pos = new Vector2(0f, 0f);
        eel.setPosition(pos);
        assertEquals(pos, eel.getPosition());
    }

    /**
     * Tests Creation of a bee.
     */
    @Test
    void TestBeeCreation() {
        assertNotNull(bee, "bee should not be null.");
    }

    /**
     * Tests that the bee is an Entity.
     */
    @Test
    void TestBeeIsEntity() {
        assertEquals(bee.getClass(), Entity.class);
    }

    /**
     * Tests that the bee has the correct components.
     */
    @Test
    void TestBeeHasComponents() {
        assertNotNull(bee.getComponent(PhysicsComponent.class));
        assertNotNull(bee.getComponent(PhysicsMovementComponent.class));
        assertNotNull(bee.getComponent(BeeAnimationController.class));
        assertNotNull(bee.getComponent(CombatStatsComponent.class));
        assertNotNull(bee.getComponent(HitboxComponent.class));
        assertNotNull(bee.getComponent(ColliderComponent.class));
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
        assertTrue(bee.getComponent(AnimationRenderComponent.class).hasAnimation("alert") ,
                "bee should have alert animation.");
        assertTrue(bee.getComponent(AnimationRenderComponent.class).hasAnimation("float") ,
                "bee should have float animation.");
    }

    /**
     * Tests that the bee is in the correct spot when placed.
     */
    @Test
    void TestBeeSetPosition() {
        Vector2 pos = new Vector2(0f, 0f);
        bee.setPosition(pos);
        assertEquals(pos, bee.getPosition());
    }

}