package com.csse3200.game.entities.factories;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.ProjectileAttackComponent;
import com.csse3200.game.components.npc.*;
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
    };

    private String[] atlas = {
            "images/banana.atlas",
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

    /**
     * Tests Creation of a banana.
     */
    @Test
    void TestMonkeyCreation() {
        assertNotNull(banana, "monkey should not be null.");
    }

    /**
     * Tests that the banana is an Entity.
     */
    @Test
    void TestMonkeyIsEntity() {
        assertEquals(banana.getClass(), Entity.class);
    }

    /**
     * Tests that the banana has the correct components.
     */
    @Test
    void TestMonkeyHasComponents() {
        assertNotNull(banana.getComponent(PhysicsComponent.class));
        assertNotNull(banana.getComponent(PhysicsMovementComponent.class));
        assertNotNull(banana.getComponent(BananaAnimationController.class));
        assertNotNull(banana.getComponent(ProjectileAttackComponent.class));
        assertNotNull(banana.getComponent(HitboxComponent.class));
        assertNotNull(banana.getComponent(ColliderComponent.class));
    }

    /**
     * Tests Creation of a chicken.
     */
    @Test
    void TestBananaCreation() {
        assertNotNull(banana, "Banana should not be null.");
    }

    /**
     * Tests that the chicken is an Entity.
     */
    @Test
    void TestBananaIsEntity() {
        assertEquals(banana.getClass(), Entity.class);
    }
}



