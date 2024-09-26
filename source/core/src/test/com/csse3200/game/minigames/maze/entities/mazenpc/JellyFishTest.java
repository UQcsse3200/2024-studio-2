package com.csse3200.game.minigames.maze.entities.mazenpc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.lighting.LightingEngine;
import com.csse3200.game.lighting.LightingService;
import com.csse3200.game.minigames.maze.components.MazeTouchAttackComponent;
import com.csse3200.game.minigames.maze.components.StatusEffectComponent;
import com.csse3200.game.minigames.maze.entities.configs.MazeNPCConfigs;
import com.csse3200.game.minigames.maze.entities.factories.MazeNPCFactory;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.FaceMoveDirectionXComponent;
import com.csse3200.game.rendering.RenderService;
import org.junit.jupiter.api.*;
import com.csse3200.game.minigames.maze.components.MazeCombatStatsComponent;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.lighting.components.LightingComponent;
import com.csse3200.game.rendering.AnimationRenderWithAudioComponent;
import com.csse3200.game.minigames.maze.components.npc.MazeEntityAnimationController;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.ResourceService;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
public class JellyFishTest {

    private Jellyfish jellyfish;
    private static final String[] TEXTURE_ATLASES = {"images/minigames/Jellyfish.atlas"};

    @BeforeEach
    void setup() {
        LightingEngine mockLightingEngine = mock(LightingEngine.class);
        LightingService mockLightingService = mock(LightingService.class);
        when(mockLightingService.getLighting()).thenReturn(mockLightingEngine);
        when(mockLightingEngine.createPointLight(anyFloat(), anyFloat(), anyFloat(), any(Color.class))).thenReturn(null);
        ServiceLocator.registerLightingService(mockLightingService);
        PhysicsService physicsService = new PhysicsService();
        ServiceLocator.registerPhysicsService(physicsService);
        RenderService renderService = new RenderService();
        ServiceLocator.registerRenderService(renderService);
        ResourceService resourceService = new ResourceService();
        ServiceLocator.registerResourceService(resourceService);
        resourceService.loadTextureAtlases(TEXTURE_ATLASES);
        resourceService.loadAll();
        jellyfish = MazeNPCFactory.createJellyfish();
    }

    @AfterEach
    void tearDown() {
        ServiceLocator.clear();
    }

    @Test
    void testJellyfishCreation() {
        assertNotNull(jellyfish, "Jellyfish should not be null.");
    }

    @Test
    public void testDefaultMazeEntityComponents() {
        assertNotNull(jellyfish.getComponent(PhysicsComponent.class),
                "Jellyfish should have a PhysicsComponent from MazeEntity");
        assertNotNull(jellyfish.getComponent(PhysicsMovementComponent.class),
                "Jellyfish should have a PhysicsMovementComponent from MazeEntity");
        assertNotNull(jellyfish.getComponent(ColliderComponent.class),
                "Jellyfish should have a ColliderComponent from MazeEntity");
        assertNotNull(jellyfish.getComponent(HitboxComponent.class),
                "Jellyfish should have a Hit-box Component from MazeEntity");
        assertNotNull(jellyfish.getComponent(MazeTouchAttackComponent.class), "Jellyfish should " +
                "have a MazeTouchAttackComponent from MazeEntity");
        assertNotNull(jellyfish.getComponent(FaceMoveDirectionXComponent.class),
                "Jellyfish should have a FaceMoveDirectionXComponent from MazeEntity");
        assertNotNull(jellyfish.getComponent(StatusEffectComponent.class),
                "Jellyfish should have a StatusEffectComponent from MazeEntity");
    }

    @Test
    public void testJellyFishHasComponents() {
        assertNotNull(jellyfish.getComponent(MazeEntityAnimationController.class),
                "Jellyfish should have a MazeEntityAnimationController");
        assertNotNull(jellyfish.getComponent(LightingComponent.class),
                "Jellyfish should have a LightingComponent");
        assertNotNull(jellyfish.getComponent(AITaskComponent.class));
        AnimationRenderWithAudioComponent animator =
                jellyfish.getComponent(AnimationRenderWithAudioComponent.class);
        assertNotNull(animator, "Jellyfish should have an AnimationRenderWithAudioComponent");
        assertTrue(animator.hasAnimation("Walk"), "Animator should have 'Walk' animation");
        assertTrue(animator.hasAnimation("Attack"), "Animator should have 'Attack' animation");
        assertTrue(animator.hasAnimation("Idle"), "Animator should have 'Idle' animation");
    }

    @Test
    public void testMazeCombatStatsComponent() {
        MazeCombatStatsComponent combatStats = jellyfish.getComponent(MazeCombatStatsComponent.class);
        assertNotNull(combatStats, "Jellyfish should have a MazeCombatStatsComponent");
        assertEquals(100, combatStats.getHealth(), "Health should be set from config");
        assertEquals(10, combatStats.getBaseAttack(), "Base attack should be set from config");
    }

    @Test
    public void testEntityScale() {
        Vector2 scale = jellyfish.getScale();
        assertEquals(0.3f, scale.x, 0.001, "Scale x should be 0.3f");
        assertEquals(0.3f, scale.y, 0.001, "Scale y should be 0.3f");
    }
}