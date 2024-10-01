package com.csse3200.game.minigames.maze.entities.factories;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.lighting.LightingEngine;
import com.csse3200.game.lighting.LightingService;
import com.csse3200.game.minigames.maze.components.MazeCombatStatsComponent;
import com.csse3200.game.minigames.maze.components.npc.MazeEntityAnimationController;
import com.csse3200.game.minigames.maze.entities.mazenpc.AnglerFish;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.AnimationRenderWithAudioComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.ResourceService;
import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.lighting.components.LightingComponent;
import box2dLight.PointLight;
import org.mockito.MockedConstruction;

@ExtendWith(GameExtension.class)
public class AnglerFishTest {

    private AnglerFish anglerFish;
    private static final String[] TEXTURE_ATLASES = {"images/minigames/angler.atlas"};
    private static final String[] SOUNDS = {"sounds/minigames/angler-chomp.mp3"};
    private Entity mockTarget;

    @BeforeEach
    void setup() {
        // Mock services
        LightingEngine mockLightingEngine = mock(LightingEngine.class);
        LightingService mockLightingService = mock(LightingService.class);
        when(mockLightingService.getLighting()).thenReturn(mockLightingEngine);
        ServiceLocator.registerLightingService(mockLightingService);

        // Use mockConstruction to mock PointLight constructor
        try (MockedConstruction<PointLight> mockPointLight = mockConstruction(PointLight.class)) {
            PhysicsService physicsService = new PhysicsService();
            ServiceLocator.registerPhysicsService(physicsService);
            RenderService renderService = new RenderService();
            ServiceLocator.registerRenderService(renderService);
            ResourceService resourceService = new ResourceService();
            ServiceLocator.registerResourceService(resourceService);
            resourceService.loadTextureAtlases(TEXTURE_ATLASES);
            resourceService.loadSounds(SOUNDS);
            resourceService.loadAll();
            mockTarget = mock(Entity.class);
            anglerFish = MazeNPCFactory.createAngler(mockTarget);
        }
    }
    @AfterEach
    void tearDown() {
        ServiceLocator.clear();
    }

    @Test
    void testAnglerFishCreation() {
        assertNotNull(anglerFish, "AnglerFish should not be null.");
    }

    @Test
    void testAnglerFishComponents() {
        assertNotNull(anglerFish.getComponent(PhysicsMovementComponent.class),
                "AnglerFish should have a PhysicsMovementComponent");
        assertNotNull(anglerFish.getComponent(MazeCombatStatsComponent.class),
                "AnglerFish should have a MazeCombatStatsComponent");
        assertNotNull(anglerFish.getComponent(AnimationRenderWithAudioComponent.class),
                "AnglerFish should have an AnimationRenderWithAudioComponent");
        assertNotNull(anglerFish.getComponent(MazeEntityAnimationController.class),
                "AnglerFish should have a MazeEntityAnimationController");
        assertNotNull(anglerFish.getComponent(LightingComponent.class),
                "AnglerFish should have a LightingComponent");
        assertNotNull(anglerFish.getComponent(AITaskComponent.class),
                "AnglerFish should have an AITaskComponent");
    }

    @Test
    void testAnimationSetup() {
        AnimationRenderWithAudioComponent animator = anglerFish.getComponent(AnimationRenderWithAudioComponent.class);
        assertNotNull(animator, "Animator should not be null");
        assertTrue(animator.hasAnimation("Walk"), "Animator should have 'Walk' animation");
        assertTrue(animator.hasAnimation("Attack"), "Animator should have 'Attack' animation");
        assertTrue(animator.hasAnimation("Idle"), "Animator should have 'Idle' animation");
    }

    @Test
    void testMazeCombatStatsComponent() {
        MazeCombatStatsComponent combatStats = anglerFish.getComponent(MazeCombatStatsComponent.class);
        assertNotNull(combatStats, "AnglerFish should have a MazeCombatStatsComponent");
        assertEquals(100, combatStats.getHealth(), "Health should be set from config");
        assertEquals(20, combatStats.getBaseAttack(), "Base attack should be set from config");
        assertEquals(new Vector2(0.8f, 0.8f), combatStats.getBaseSpeed(), "Base speed should be " +
                "set" +
                " " +
            "from " +
                "config");
    }

    @Test
    void testEntityScale() {
        Vector2 scale = anglerFish.getScale();
        assertEquals(0.4f, scale.x, 0.001, "Scale x should be 0.4f");
        assertEquals(0.4f, scale.y, 0.001, "Scale y should be 0.4f");
    }

}

