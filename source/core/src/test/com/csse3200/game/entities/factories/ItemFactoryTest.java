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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class ItemFactoryTest {
    private Entity apple;
    private Entity healthPotion;

    String[] itemTextures = {
            "images/foodtextures/apple.png",
            "images/Healthpotion.png"
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
        resourceService.loadTextures(itemTextures);
        resourceService.loadAll();

        Entity player = new Entity();

        // Ensure that the quantity is non-negative and less than the limit
        apple = ItemFactory.createApple(player);
        healthPotion = ItemFactory.createHealthPotion(player);
    }

    @Test
    void TestAppleCreation() {
        assertNotNull(apple, "apple should not be null");
    }

    @Test
    void TestAppleEntity() {
        assert(apple.getClass() == Entity.class);
    }

    @Test
    void TestHealthPotionCreation(){
        assertNotNull(healthPotion, "healthPotion should not be null");
    }

    @Test
    void TestHealthPotionEntity(){
        assert(healthPotion.getClass() == Entity.class);
    }
}
