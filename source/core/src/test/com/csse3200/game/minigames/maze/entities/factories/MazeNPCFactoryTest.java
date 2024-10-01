package com.csse3200.game.minigames.maze.entities.factories;

import box2dLight.PointLight;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.lighting.LightingEngine;
import com.csse3200.game.lighting.LightingService;
import com.csse3200.game.minigames.maze.entities.mazenpc.AnglerFish;
import com.csse3200.game.minigames.maze.entities.mazenpc.ElectricEel;
import com.csse3200.game.minigames.maze.entities.mazenpc.Jellyfish;
import com.csse3200.game.minigames.maze.entities.mazenpc.FishEgg;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
public class MazeNPCFactoryTest {

    private static final String[] TEXTURE_ATLASES = {"images/minigames/angler.atlas", "images/minigames/eels.atlas", "images/minigames/Jellyfish.atlas"};
    private static final String[] SOUNDS = {"sounds/minigames/angler-chomp.mp3"};
    private static final String[] TEXTURE_MAZE = { "images/minigames/fishegg.png",  };

    @BeforeEach
    public void setUp() {
        LightingEngine mockLightingEngine = mock(LightingEngine.class);
        LightingService mockLightingService = mock(LightingService.class);
        when(mockLightingService.getLighting()).thenReturn(mockLightingEngine);
        ServiceLocator.registerLightingService(mockLightingService);

        try (MockedConstruction<PointLight> mockPointLight = mockConstruction(PointLight.class)) {
            PhysicsService physicsService = new PhysicsService();
            ServiceLocator.registerPhysicsService(physicsService);
            RenderService renderService = new RenderService();
            ServiceLocator.registerRenderService(renderService);
            ResourceService resourceService = new ResourceService();
            ServiceLocator.registerResourceService(resourceService);
            resourceService.loadTextures(TEXTURE_MAZE);
            resourceService.loadTextureAtlases(TEXTURE_ATLASES);
            resourceService.loadSounds(SOUNDS);
            resourceService.loadAll();
        }
    }

    @Test
    void testCreateAngler() {
//        Entity target = new Entity();
//        AnglerFish anglerFish = MazeNPCFactory.createAngler(target);
//        assertNotNull(anglerFish, "AnglerFish should not be null");
    }

    @Test
    void testCreateEel() {
        Entity target = new Entity();
        ElectricEel electricEel = MazeNPCFactory.createEel(target);
        assertNotNull(electricEel, "ElectricEel should not be null");
    }

    @Test
    void testCreateJellyfish() {
        Jellyfish jellyfish = MazeNPCFactory.createJellyfish();
        assertNotNull(jellyfish, "Jellyfish should not be null");
    }

    @Test
    void testCreateFishEgg() {
        FishEgg fishEgg = MazeNPCFactory.createFishEgg();
        assertNotNull(fishEgg, "FishEgg should not be null");
    }
}

