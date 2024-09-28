package com.csse3200.game.minigames.maze.entities.factories;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.reflect.Constructor;

@ExtendWith(GameExtension.class)
public class MazeObstacleFactoryTest {

    private Entity wall;
    private static final String[] TEXTURE_MAZE = { "images/minigames/wall.png" };

    @BeforeEach
    void setUp() {
        PhysicsService physicsService = new PhysicsService();
        ServiceLocator.registerPhysicsService(physicsService);
        RenderService renderService = new RenderService();
        ServiceLocator.registerRenderService(renderService);
        ResourceService resourceService = new ResourceService();
        ServiceLocator.registerResourceService(resourceService);
        resourceService.loadTextures(TEXTURE_MAZE);
        resourceService.loadAll();
        wall = MazeObstacleFactory.createMazeWall(2.0f, 3.0f);
    }

    @Test
    public void testWallComponents() {

        // Assert that the wall entity is not null
        assertNotNull(wall, "Wall entity should not be null");

        // Assert that the wall has the necessary components
        assertNotNull(wall.getComponent(PhysicsComponent.class), "Wall should have a PhysicsComponent");
        assertNotNull(wall.getComponent(ColliderComponent.class), "Wall should have a ColliderComponent");
        assertNotNull(wall.getComponent(TextureRenderComponent.class), "Wall should have a TextureRenderComponent");
    }

    @Test
    public void testWallScale() {
        Vector2 scale = wall.getScale();
        assertEquals(new Vector2(2.0f, 3.0f), scale, "Scale should be 2 and 3");
    }

    @Test
    public void testWallCollider() {
        ColliderComponent collider = wall.getComponent(ColliderComponent.class);
        assertEquals(PhysicsLayer.OBSTACLE, collider.getLayer(), "Wall collider layer should be OBSTACLE");
    }
}
