package com.csse3200.game.minigames.maze.entities.factories;

import static org.junit.jupiter.api.Assertions.*;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.lighting.LightingEngine;
import com.csse3200.game.lighting.LightingService;
import com.csse3200.game.lighting.components.LightingComponent;
import com.csse3200.game.minigames.maze.entities.mazenpc.FishEgg;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
public class FishEggTest {

    private Entity fishEgg;
    private static final String[] TEXTURE_MAZE = { "images/minigames/fishegg.png" };
    private static final String[] PARTICLE_EFFECTS = {"images/minigames/starlight.p"};

    private static final String PARTICLE_EFFECT_IMAGES_DIR = "images/minigames";

    @BeforeEach
    public void setUp() {
        LightingEngine mockLightingEngine = mock(LightingEngine.class);
        LightingService mockLightingService = mock(LightingService.class);
        when(mockLightingService.getLighting()).thenReturn(mockLightingEngine);
        ServiceLocator.registerLightingService(mockLightingService);

        PhysicsService physicsService = new PhysicsService();
        ServiceLocator.registerPhysicsService(physicsService);
        RenderService renderService = new RenderService();
        ServiceLocator.registerRenderService(renderService);
        ResourceService resourceService = new ResourceService();
        ServiceLocator.registerResourceService(resourceService);
        resourceService.loadTextures(TEXTURE_MAZE);
        resourceService.loadParticleEffects(PARTICLE_EFFECTS, PARTICLE_EFFECT_IMAGES_DIR);
        resourceService.loadAll();
        fishEgg = MazeNPCFactory.createFishEgg();
    }

    @Test
    public void testFishEggInstantiation() {
        assertNotNull(fishEgg);

        assertNotNull(fishEgg.getComponent(LightingComponent.class),
          "FishEgg should have a LightingComponent");
        assertNotNull(fishEgg.getComponent(HitboxComponent.class),
                "FishEgg should have a HitboxComponent");
        assertNotNull(fishEgg.getComponent(ColliderComponent.class),
                "FishEgg should have a ColliderComponent");
        assertNotNull(fishEgg.getComponent(PhysicsComponent.class),
                "FishEgg should have a PhysicsComponent");
        assertNotNull(fishEgg.getComponent(TextureRenderComponent.class),
                "FishEgg should have a TextureRenderComponent");

    }

    @Test
    public void testFishEggScale() {
        Vector2 scale = fishEgg.getScale();
        assertEquals(new Vector2(0.1f, 0.1f), scale, "Scale should be 0.1f");

    }

    @Test
    public void testFishEggHitboxLayer() {
        // Check if the hitbox layer is set correctly
        HitboxComponent hitboxComponent = fishEgg.getComponent(HitboxComponent.class);
        assertNotNull(hitboxComponent);
        assertEquals(PhysicsLayer.PLAYER | PhysicsLayer.NPC, hitboxComponent.getLayer(),
                "FishEgg hitbox should be on the PLAYER layer.");
    }
}


