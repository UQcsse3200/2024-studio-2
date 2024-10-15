package com.csse3200.game.entities.factories;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.ProjectileAttackComponent;
import com.csse3200.game.components.npc.*;
import com.csse3200.game.components.tasks.HiveTask;
import com.csse3200.game.components.tasks.ProjectileMovementTask;
import com.csse3200.game.entities.Entity;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class ProjectileFactoryTest {

    private Entity banana;
    private static final NPCConfigs configs =
            FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");

    private String[] textures = {
            "images/banana.png",
            "images/effects.png",
            "images/orb.png",
            "images/worm.png",
            "images/griffinEffects.png",
            "images/hive.png"
    };

    private String[] atlas = {
            "images/banana.atlas",
            "images/effects.atlas",
            "images/orb.atlas",
            "images/worm.atlas",
            "images/griffinEffects.atlas",
            "images/hive.atlas"
    };


    @BeforeEach
    void setup() {
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
        banana = ProjectileFactory.createBanana(player);
    }

    // Banana Tests

    @Test
    void TestBananaHasComponents() {
        assertNotNull(banana.getComponent(PhysicsComponent.class));
        assertNotNull(banana.getComponent(PhysicsMovementComponent.class));
        assertNotNull(banana.getComponent(BananaAnimationController.class));
        assertNotNull(banana.getComponent(ProjectileAttackComponent.class));
        assertNotNull(banana.getComponent(HitboxComponent.class));
        assertNotNull(banana.getComponent(ColliderComponent.class));
    }

    @Test
    void TestBananaCreation() {
        assertNotNull(banana, "Banana should not be null.");
    }

    @Test
    void TestBananaIsEntity() {
        assertEquals(banana.getClass(), Entity.class);
    }

    @Test
    void TestBananaAnimationLoaded() {
        AnimationRenderComponent animationComponent = banana.getComponent(AnimationRenderComponent.class);
        assertNotNull(animationComponent);
        assertTrue(animationComponent.hasAnimation("fire"));
    }

    @Test
    void TestBananaAITask() {
        AITaskComponent aiTaskComponent = banana.getComponent(AITaskComponent.class);
        assertNotNull(aiTaskComponent, "AITaskComponent should not be null.");

        boolean containsProjectileMovementTask = false;
        for (PriorityTask task : aiTaskComponent.getTasks()) {
            if (task instanceof ProjectileMovementTask) {
                containsProjectileMovementTask = true;
                break;
            }
        }

        assertTrue(containsProjectileMovementTask, "The AI task list should contain a ProjectileMovementTask.");
        assertNull(aiTaskComponent.getCurrentTask(), "No task should be active initially.");
    }

    // WaterSpiral Tests

    @Test
    void TestWaterSpiralHasComponents() {
        Entity waterSpiral = ProjectileFactory.createWaterSpiral(new Entity());

        assertNotNull(waterSpiral.getComponent(PhysicsComponent.class));
        assertNotNull(waterSpiral.getComponent(PhysicsMovementComponent.class));
        assertNotNull(waterSpiral.getComponent(WaterSpiralAnimationController.class));
        assertNotNull(waterSpiral.getComponent(ProjectileAttackComponent.class));
        assertNotNull(waterSpiral.getComponent(HitboxComponent.class));
        assertNotNull(waterSpiral.getComponent(ColliderComponent.class));
        assertNotNull(waterSpiral.getComponent(AITaskComponent.class));
    }

    @Test
    void TestWaterSpiralAnimationLoaded() {
        Entity waterSpiral = ProjectileFactory.createWaterSpiral(new Entity());
        AnimationRenderComponent animationComponent = waterSpiral.getComponent(AnimationRenderComponent.class);

        assertNotNull(animationComponent);
        assertTrue(animationComponent.hasAnimation("waterSpiral"), "WaterSpiral should have waterSpiral animation.");
    }

    @Test
    void TestWaterSpiralSpeed() {
        Entity waterSpiral = ProjectileFactory.createWaterSpiral(new Entity());
        PhysicsMovementComponent movementComponent = waterSpiral.getComponent(PhysicsMovementComponent.class);

        Vector2 expectedSpeed = new Vector2(6.0f, 6.0f);
        assertEquals(expectedSpeed, movementComponent.getMaxSpeed());
    }

    @Test
    void TestWaterSpiralAITask() {
        Entity waterSpiral = ProjectileFactory.createWaterSpiral(new Entity());
        AITaskComponent aiTaskComponent = waterSpiral.getComponent(AITaskComponent.class);

        assertNotNull(aiTaskComponent, "AITaskComponent should not be null for WaterSpiral.");

        boolean containsProjectileMovementTask = false;
        for (PriorityTask task : aiTaskComponent.getTasks()) {
            if (task instanceof ProjectileMovementTask) {
                containsProjectileMovementTask = true;
                break;
            }
        }

        assertTrue(containsProjectileMovementTask, "WaterSpiral AI task list should contain a ProjectileMovementTask.");
        assertNull(aiTaskComponent.getCurrentTask(), "No task should be active initially for WaterSpiral.");
    }

    // WindGust Tests

    @Test
    void TestWindGustHasComponents() {
        Entity windGust = ProjectileFactory.createWindGust(new Entity());

        assertNotNull(windGust.getComponent(PhysicsComponent.class));
        assertNotNull(windGust.getComponent(PhysicsMovementComponent.class));
        assertNotNull(windGust.getComponent(WindGustAnimationController.class));
        assertNotNull(windGust.getComponent(ProjectileAttackComponent.class));
        assertNotNull(windGust.getComponent(HitboxComponent.class));
        assertNotNull(windGust.getComponent(ColliderComponent.class));
        assertNotNull(windGust.getComponent(AITaskComponent.class));
    }

    @Test
    void TestWindGustAnimationLoaded() {
        Entity windGust = ProjectileFactory.createWindGust(new Entity());
        AnimationRenderComponent animationComponent = windGust.getComponent(AnimationRenderComponent.class);

        assertNotNull(animationComponent);
        assertTrue(animationComponent.hasAnimation("windGust"), "WindGust should have windGust animation.");
    }

    @Test
    void TestWindGustAITask() {
        Entity windGust = ProjectileFactory.createWindGust(new Entity());

        AITaskComponent aiTaskComponent = windGust.getComponent(AITaskComponent.class);

        assertNotNull(aiTaskComponent, "AITaskComponent should not be null for WindGust.");

        boolean containsProjectileMovementTask = false;
        for (PriorityTask task : aiTaskComponent.getTasks()) {
            if (task instanceof ProjectileMovementTask) {
                containsProjectileMovementTask = true;
                break;
            }
        }

        assertTrue(containsProjectileMovementTask, "The AI task list for WindGust should contain a ProjectileMovementTask.");

        assertNull(aiTaskComponent.getCurrentTask(), "No task should be active initially for WindGust.");
    }

    // worm tests

    @Test
    void TestWormHasComponents() {
        Entity worm = ProjectileFactory.createWorm(new Entity());

        assertNotNull(worm.getComponent(PhysicsComponent.class));
        assertNotNull(worm.getComponent(PhysicsMovementComponent.class));
        assertNotNull(worm.getComponent(BananaAnimationController.class));  // It uses BananaAnimationController
        assertNotNull(worm.getComponent(ProjectileAttackComponent.class));
        assertNotNull(worm.getComponent(HitboxComponent.class));
        assertNotNull(worm.getComponent(ColliderComponent.class));
        assertNotNull(worm.getComponent(AITaskComponent.class));
    }

    @Test
    void TestWormAnimationLoaded() {
        Entity worm = ProjectileFactory.createWorm(new Entity());

        AnimationRenderComponent animationComponent = worm.getComponent(AnimationRenderComponent.class);

        assertNotNull(animationComponent, "AnimationRenderComponent should not be null for Worm.");
        assertTrue(animationComponent.hasAnimation("fire"), "Worm should have 'fire' animation.");
    }

    @Test
    void TestWormAITask() {
        Entity worm = ProjectileFactory.createWorm(new Entity());

        AITaskComponent aiTaskComponent = worm.getComponent(AITaskComponent.class);

        assertNotNull(aiTaskComponent, "AITaskComponent should not be null for Worm.");

        boolean containsProjectileMovementTask = false;
        for (PriorityTask task : aiTaskComponent.getTasks()) {
            if (task instanceof ProjectileMovementTask) {
                containsProjectileMovementTask = true;
                break;
            }
        }

        assertTrue(containsProjectileMovementTask, "Worm AI task list should contain a ProjectileMovementTask.");

        assertNull(aiTaskComponent.getCurrentTask(), "No task should be active initially for Worm.");
    }

    // hive tests

    @Test
    void TestHiveHasComponents() {
        // Create the Hive entity
        Entity hive = ProjectileFactory.createHive(new Entity());

        // Verify that all required components are present
        assertNotNull(hive.getComponent(PhysicsComponent.class));
        assertNotNull(hive.getComponent(PhysicsMovementComponent.class));
        assertNotNull(hive.getComponent(HiveAnimationController.class));  // Hive-specific animation controller
        assertNotNull(hive.getComponent(ProjectileAttackComponent.class));
        assertNotNull(hive.getComponent(HitboxComponent.class));
        assertNotNull(hive.getComponent(ColliderComponent.class));
        assertNotNull(hive.getComponent(AITaskComponent.class));  // Ensure AI component is present
    }

    @Test
    void TestHiveAnimationLoaded() {
        // Create the Hive entity
        Entity hive = ProjectileFactory.createHive(new Entity());

        // Get the AnimationRenderComponent
        AnimationRenderComponent animationComponent = hive.getComponent(AnimationRenderComponent.class);

        // Verify that the animation component is not null and has the correct animation
        assertNotNull(animationComponent, "AnimationRenderComponent should not be null for Hive.");
        assertTrue(animationComponent.hasAnimation("float"), "Hive should have 'float' animation.");
    }

    @Test
    void TestHiveAITask() {
        Entity hive = ProjectileFactory.createHive(new Entity());

        AITaskComponent aiTaskComponent = hive.getComponent(AITaskComponent.class);

        assertNotNull(aiTaskComponent, "AITaskComponent should not be null for Hive.");

        boolean containsProjectileMovementTask = false;
        boolean containsHiveTask = false;

        for (PriorityTask task : aiTaskComponent.getTasks()) {
            if (task instanceof ProjectileMovementTask) {
                containsProjectileMovementTask = true;
            } else if (task instanceof HiveTask) {
                containsHiveTask = true;
            }
        }

        assertTrue(containsProjectileMovementTask, "Hive AI task list should contain a ProjectileMovementTask.");
        assertTrue(containsHiveTask, "Hive AI task list should contain a HiveTask.");

        assertNull(aiTaskComponent.getCurrentTask(), "No task should be active initially for Hive.");
    }
}



