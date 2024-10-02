package com.csse3200.game.entities.factories;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
public class ObstacleFactoryTest {
    private Entity tree;

    String[] obstacleTextures = {
            "images/tree.png"
    };

    @BeforeEach
    void setup() {
        // Mocking GameTime
        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getDeltaTime()).thenReturn(0.02f);
        ServiceLocator.registerTimeSource(gameTime);

        // Registering physics service
        ServiceLocator.registerPhysicsService(new PhysicsService());

        // Setting up the render service and registering it
        RenderService renderService = new RenderService();
        renderService.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(renderService);

        // Loading textures for the obstacle
        ResourceService resourceService = new ResourceService();
        ServiceLocator.registerResourceService(resourceService);
        resourceService.loadTextures(obstacleTextures);
        resourceService.loadAll();

        // Creating a tree entity
        tree = ObstacleFactory.createTree();
    }

    @Test
    void testTreeCreation() {
        assertNotNull(tree, "Tree should not be null");
    }

    @Test
    void testTreeEntityType() {
        assert(tree.getClass() == Entity.class);
    }
}
