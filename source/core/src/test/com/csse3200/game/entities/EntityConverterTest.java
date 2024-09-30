package com.csse3200.game.entities;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.entities.configs.BaseEntityConfig;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.entities.factories.EnemyFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.lighting.LightingEngine;
import com.csse3200.game.lighting.LightingService;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

import static com.csse3200.game.entities.EntityConverter.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class EntityConverterTest {
    private Entity chicken;
    private Entity frog;
    private Entity monkey;
    private Entity cow;
    private Entity player;
    private List<Entity> entities;
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
    private final Vector2 test_speed = new Vector2(2f, 2f);
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
        LightingEngine mockLightingEngine = mock(LightingEngine.class);
        LightingService mockLightingService = mock(LightingService.class);
        when(mockLightingService.getLighting()).thenReturn(mockLightingEngine);
        when(mockLightingEngine.createPointLight(anyFloat(), anyFloat(), anyFloat(), any(Color.class))).thenReturn(null);
        ServiceLocator.registerLightingService(mockLightingService);

        resourceService.loadTextures(textures);
        resourceService.loadTextureAtlases(atlas);
        resourceService.loadAll();

        player = new Entity();
        entities = new ArrayList<>();
        chicken = EnemyFactory.createChicken(player);
        frog = EnemyFactory.createFrog(player);
        monkey = EnemyFactory.createMonkey(player);
    }

    @Test
    void TestChickenRemovalOfTouchAttack() {
        assertNotNull(chicken.getComponent(TouchAttackComponent.class));
        convertToFriendly(chicken, player, entities);
        assertNull(chicken.getComponent(TouchAttackComponent.class),
                "Chicken should not have a touch attack component.");
    }

    @Test
    void TestChickenMovementSpeedChange() {
        convertToFriendly(chicken, player, entities);
        assertEquals(test_speed, chicken.getComponent(PhysicsMovementComponent.class).getMaxSpeed());
    }

    @Test
    void TestChickenRemovalCombatStats() {
        assertNotNull(chicken.getComponent(CombatStatsComponent.class));
        convertToFriendly(chicken, player, entities);
        assertNull(chicken.getComponent(CombatStatsComponent.class));
    }

    @Test
    void TestChickenHasFriendlyAI() {
        BaseEntityConfig config = EntityConverter.handleConfig(chicken.getComponent(AnimationRenderComponent.class));
//        System.out.println(chicken.getComponent(AnimationRenderComponent.class));
//        String currentAnimation = chicken.getComponent(AnimationRenderComponent.class).getCurrentAnimation();
//        System.out.println(currentAnimation);
//        assertEquals("chicken", currentAnimation);
        assertFalse(config.isFriendly());
        convertToFriendly(chicken, player, entities);
        assertTrue(config.isFriendly());
    }


    @Test
    void TestChickenGetHintText() {
        BaseEntityConfig config = EntityConverter.handleConfig(chicken.getComponent(AnimationRenderComponent.class));
        String[][] testText = {{"A mysterious creature appears!"}};
        Assertions.assertArrayEquals(testText, retrieveHintText(config));
    }


//    @Test
//    void TestChickenHasSoundPath() {
//        String[] sound = {"Sound Path"};
//        assertNotNull(sound);
//        assertEquals(sound, new String[]{"Sound Path"});
//    }

    @Test
    void TestFrogRemovalOfTouchAttack() {
        assertNotNull(frog.getComponent(TouchAttackComponent.class));
        convertToFriendly(frog, player, entities);
        assertNull(frog.getComponent(TouchAttackComponent.class),
                "Frog should not have a touch attack component.");
    }

    @Test
    void TestFrogMovementSpeedChange() {
        convertToFriendly(frog, player, entities);
        assertEquals(test_speed, frog.getComponent(PhysicsMovementComponent.class).getMaxSpeed());
    }

    @Test
    void TestFrogRemovalCombatStats() {
        assertNotNull(frog.getComponent(CombatStatsComponent.class));
        convertToFriendly(frog, player, entities);
        assertNull(frog.getComponent(CombatStatsComponent.class));
    }

    @Test
    void TestFrogHasFriendlyAI() {
        BaseEntityConfig config = EntityConverter.handleConfig(frog.getComponent(AnimationRenderComponent.class));
//        String currentAnimation = frog.getComponent(AnimationRenderComponent.class).getCurrentAnimation();
//        System.out.println(currentAnimation);
//        assertEquals("frog", currentAnimation);
        assertFalse(config.isFriendly());
        convertToFriendly(frog, player, entities);
        assertTrue(config.isFriendly());
    }

    @Test
    void TestFrogGetHintText() {
        BaseEntityConfig config = EntityConverter.handleConfig(frog.getComponent(AnimationRenderComponent.class));
        String[][] testText = {{"A mysterious creature appears!"}};
        Assertions.assertArrayEquals(testText, retrieveHintText(config));
    }

    //    @Test
//    void TestFrogHasSoundPath() {
//        String[] sound = {"Sound Path"};
//        assertNotNull(sound);
//        assertEquals(sound, new String[]{"Sound Path"});
//    }

    @Test
    void TestMonkeyRemovalOfTouchAttack() {
        assertNotNull(monkey.getComponent(TouchAttackComponent.class));
        convertToFriendly(monkey, player, entities);
        assertNull(monkey.getComponent(TouchAttackComponent.class),
                "Monkey should not have a touch attack component.");
    }

    @Test
    void TestMonkeyMovementSpeedChange() {
        convertToFriendly(monkey, player, entities);
        assertEquals(test_speed, monkey.getComponent(PhysicsMovementComponent.class).getMaxSpeed());
    }

    @Test
    void TestMonkeyRemovalCombatStats() {
        assertNotNull(monkey.getComponent(CombatStatsComponent.class));
        convertToFriendly(monkey, player, entities);
        assertNull(monkey.getComponent(CombatStatsComponent.class));
    }

    @Test
    void TestMonkeyHasFriendlyAI() {
        BaseEntityConfig config = EntityConverter.handleConfig(monkey.getComponent(AnimationRenderComponent.class));
        assertFalse(config.isFriendly());
        convertToFriendly(monkey, player, entities);
        assertTrue(config.isFriendly());
    }

    @Test
    void TestMonkeyGetHintText() {
        BaseEntityConfig config = EntityConverter.handleConfig(monkey.getComponent(AnimationRenderComponent.class));
        String[][] testText = {{"A mysterious creature appears!"}};
        Assertions.assertArrayEquals(testText, retrieveHintText(config));
    }
//    @Test
//    void TestMonkeyHasSoundPath() {
//        String[] sound = {"Sound Path"};
//        assertNotNull(sound);
//        assertEquals(sound, new String[]{"Sound Path"});
//    }

}