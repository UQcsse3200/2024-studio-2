package com.csse3200.game.entities;

import com.csse3200.game.components.npc.ChickenAnimationController;
import com.csse3200.game.components.npc.FriendlyNPCAnimationController;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.entities.factories.EnemyFactory;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

import static com.csse3200.game.entities.EntityConverter.convertToFriendly;
import static com.csse3200.game.entities.factories.EnemyFactory.isFRIENDLY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class EntityConverterTest {

    private Entity chicken;
    private Entity frog;
    private Entity monkey;
    private static final NPCConfigs configs =
            FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");

    private String[] textures = {
            "images/chicken.png",
            "images/monkey.png",
            "images/frog.png",
    };

    private String[] atlas = {
            "images/chicken.atlas",
            "images/enemy-chicken.atlas",
            "images/monkey.atlas",
            "images/enemy-monkey.atlas",
            "images/frog.atlas",
            "images/enemy-frog.atlas"
    };
    @BeforeEach
    void setUp() {
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
    }

    @Test
    void TestConvertToFriendly() {
        assertNotNull(chicken.getComponent(ChickenAnimationController.class));
        convertToFriendly(chicken);
        System.out.println(chicken);
        System.out.println(chicken.getComponent(ChickenAnimationController.class));
        assertNull(chicken.getComponent(ChickenAnimationController.class),
                "Chicken should not have an enemy AI controller.");
    }
}