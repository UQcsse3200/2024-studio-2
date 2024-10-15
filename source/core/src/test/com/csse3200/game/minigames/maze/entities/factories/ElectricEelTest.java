package com.csse3200.game.minigames.maze.entities.factories;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import box2dLight.PointLight;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.lighting.LightingEngine;
import com.csse3200.game.lighting.LightingService;
import com.csse3200.game.lighting.components.LightingComponent;
import com.csse3200.game.minigames.maze.entities.configs.MazeEntityConfig;
import com.csse3200.game.minigames.maze.entities.mazenpc.ElectricEel;
import com.csse3200.game.particles.ParticleService;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.rendering.AnimationRenderWithAudioComponent;
import com.csse3200.game.minigames.maze.components.MazeCombatStatsComponent;
import com.csse3200.game.minigames.maze.components.npc.MazeEntityAnimationController;
import com.badlogic.gdx.graphics.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
class ElectricEelTest {

    private ElectricEel electricEel;
    private static final String[] TEXTURE_ATLASES = {"images/minigames/eels.atlas"};

    private static final String[] PARTICLE_EFFECTS = {"images/minigames/electricparticles.p"};

    private static final String PARTICLE_EFFECT_IMAGES_DIR = "images/minigames";
    private MazeEntityConfig config;
    private Entity target;

    @BeforeEach
    void setup() {
        LightingEngine mockLightingEngine = mock(LightingEngine.class);
        LightingService mockLightingService = mock(LightingService.class);
        PointLight mockPointLight = mock(PointLight.class);
        when(mockLightingService.getLighting()).thenReturn(mockLightingEngine);
        when(mockLightingEngine.createPointLight(anyFloat(), anyFloat(), anyFloat(), any(Color.class))).thenReturn(mockPointLight);
        ServiceLocator.registerLightingService(mockLightingService);

        PhysicsService physicsService = new PhysicsService();
        ServiceLocator.registerPhysicsService(physicsService);

        RenderService renderService = new RenderService();
        ServiceLocator.registerRenderService(renderService);

        ResourceService resourceService = new ResourceService();
        ServiceLocator.registerResourceService(resourceService);
        resourceService.loadTextureAtlases(TEXTURE_ATLASES);
        resourceService.loadParticleEffects(PARTICLE_EFFECTS, PARTICLE_EFFECT_IMAGES_DIR);
        resourceService.loadAll();

        ServiceLocator.registerParticleService(mock(ParticleService.class));

        target = mock(Entity.class);
        config = new MazeEntityConfig();
        config.health = 150;
        config.baseAttack = 20;
        config.speed = 5f;

        electricEel = new ElectricEel(target, config);
    }

    @AfterEach
    void tearDown() {
        ServiceLocator.clear();
    }

    @Test
    void testElectricEelCreation() {
        assertNotNull(electricEel, "ElectricEel should not be null.");
    }

    @Test
    void testDefaultMazeEntityComponents() {
        assertNotNull(electricEel.getComponent(PhysicsComponent.class),
                "ElectricEel should have a PhysicsComponent from MazeEntity");
        assertNotNull(electricEel.getComponent(PhysicsMovementComponent.class),
                "ElectricEel should have a PhysicsMovementComponent from MazeEntity");
        assertNotNull(electricEel.getComponent(ColliderComponent.class),
                "ElectricEel should have a ColliderComponent from MazeEntity");
        assertNotNull(electricEel.getComponent(HitboxComponent.class),
                "ElectricEel should have a HitboxComponent from MazeEntity");
    }

    @Test
    void testElectricEelHasComponents() {
        assertNotNull(electricEel.getComponent(MazeEntityAnimationController.class),
                "ElectricEel should have a MazeEntityAnimationController");
        assertNotNull(electricEel.getComponent(LightingComponent.class),
                "ElectricEel should have a LightingComponent");
        assertNotNull(electricEel.getComponent(AITaskComponent.class));
        AnimationRenderWithAudioComponent animator =
                electricEel.getComponent(AnimationRenderWithAudioComponent.class);
        assertNotNull(animator, "ElectricEel should have an AnimationRenderWithAudioComponent");
        assertTrue(animator.hasAnimation("Walk"), "Animator should have 'Walk' animation");
        assertTrue(animator.hasAnimation("Attack"), "Animator should have 'Attack' animation");
        assertTrue(animator.hasAnimation("Idle"), "Animator should have 'Idle' animation");
    }

    @Test
    void testMazeCombatStatsComponent() {
        MazeCombatStatsComponent combatStats = electricEel.getComponent(MazeCombatStatsComponent.class);
        assertNotNull(combatStats, "ElectricEel should have a MazeCombatStatsComponent");
        assertEquals(150, combatStats.getHealth(), "Health should be set from config");
        assertEquals(20, combatStats.getBaseAttack(), "Base attack should be set from config");
    }

    @Test
    void testEntityScale() {
        Vector2 scale = electricEel.getScale();
        assertEquals(0.3f, scale.x, 0.001, "Scale x should be 0.3f");
        assertEquals(0.3f, scale.y, 0.001, "Scale y should be 0.3f");
    }
}