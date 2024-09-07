package com.csse3200.game.entities;

import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.npc.ChickenAnimationController;
import com.csse3200.game.components.npc.FriendlyNPCAnimationController;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.entities.factories.EnemyFactory;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class EntityConverterTest {

    private Entity chicken;
    private Entity frog;
    private Entity monkey;
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
    void TestChickenUpdatedCombatStatsHunger() {
        assertNotNull(chicken.getComponent(CombatStatsComponent.class));
        convertToFriendly(chicken, player, entities);
        assertEquals(0, chicken.getComponent(CombatStatsComponent.class).getHunger(),
                "Chicken should have 0 hunger in combat stats component.");
    }
    @Test
    void TestChickenUpdatedCombatStatsStrength() {
        assertNotNull(chicken.getComponent(CombatStatsComponent.class));
        convertToFriendly(chicken, player, entities);
        assertEquals(0, chicken.getComponent(CombatStatsComponent.class).getStrength(),
                "Chicken should have 0 strength in combat stats component.");
    }
    @Test
    void TestChickenUpdatedCombatStatsDef() {
        assertNotNull(chicken.getComponent(CombatStatsComponent.class));
        convertToFriendly(chicken, player, entities);
        assertEquals(0, chicken.getComponent(CombatStatsComponent.class).getDefense(),
                "Chicken should have 0 defense in combat stats component.");
    }

    @Test
    void TestChickenUpdatedCombatStatsSpeed() {
        assertNotNull(chicken.getComponent(CombatStatsComponent.class));
        convertToFriendly(chicken, player, entities);
        assertEquals(0, chicken.getComponent(CombatStatsComponent.class).getSpeed(),
                "Chicken should have 0 speed in combat stats component.");
    }

    @Test
    void TestChickenUpdatedCombatStatsXP() {
        assertNotNull(chicken.getComponent(CombatStatsComponent.class));
        convertToFriendly(chicken, player, entities);
        assertEquals(0, chicken.getComponent(CombatStatsComponent.class).getExperience(),
                "Chicken should have 0 experience in combat stats component.");
    }

    @Test
    void TestChickenHasFriendlyAI() {
        assertNull(chicken.getComponent(FriendlyNPCAnimationController.class));
        convertToFriendly(chicken, player, entities);
        assertNotNull(chicken.getComponent(FriendlyNPCAnimationController.class));
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
    void TestFrogUpdatedCombatStatsHunger() {
        assertNotNull(frog.getComponent(CombatStatsComponent.class));
        convertToFriendly(frog, player, entities);
        assertEquals(0, frog.getComponent(CombatStatsComponent.class).getHunger(),
                "Frog should have 0 hunger in combat stats component.");
    }
    @Test
    void TestFrogUpdatedCombatStatsStrength() {
        assertNotNull(frog.getComponent(CombatStatsComponent.class));
        convertToFriendly(frog, player, entities);
        assertEquals(0, frog.getComponent(CombatStatsComponent.class).getStrength(),
                "Frog should have 0 strength in combat stats component.");
    }
    @Test
    void TestFrogUpdatedCombatStatsDef() {
        assertNotNull(frog.getComponent(CombatStatsComponent.class));
        convertToFriendly(frog, player, entities);
        assertEquals(0, frog.getComponent(CombatStatsComponent.class).getDefense(),
                "Frog should have 0 defense in combat stats component.");
    }

    @Test
    void TestFrogUpdatedCombatStatsSpeed() {
        assertNotNull(frog.getComponent(CombatStatsComponent.class));
        convertToFriendly(frog, player, entities);
        assertEquals(0, frog.getComponent(CombatStatsComponent.class).getSpeed(),
                "Frog should have 0 speed in combat stats component.");
    }

    @Test
    void TestFrogUpdatedCombatStatsXP() {
        assertNotNull(frog.getComponent(CombatStatsComponent.class));
        convertToFriendly(frog, player, entities);
        assertEquals(0, frog.getComponent(CombatStatsComponent.class).getExperience(),
                "Frog should have 0 experience in combat stats component.");
    }

    @Test
    void TestFrogHasFriendlyAI() {
        assertNull(frog.getComponent(FriendlyNPCAnimationController.class));
        convertToFriendly(frog, player, entities);
        assertNotNull(frog.getComponent(FriendlyNPCAnimationController.class));
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
    void TestMonkeyUpdatedCombatStatsHunger() {
        assertNotNull(monkey.getComponent(CombatStatsComponent.class));
        convertToFriendly(monkey, player, entities);
        assertEquals(0, monkey.getComponent(CombatStatsComponent.class).getHunger(),
                "Monkey should have 0 hunger in combat stats component.");
    }
    @Test
    void TestMonkeyUpdatedCombatStatsStrength() {
        assertNotNull(monkey.getComponent(CombatStatsComponent.class));
        convertToFriendly(monkey, player, entities);
        assertEquals(0, monkey.getComponent(CombatStatsComponent.class).getStrength(),
                "Monkey should have 0 strength in combat stats component.");
    }
    @Test
    void TestMonkeyUpdatedCombatStatsDef() {
        assertNotNull(monkey.getComponent(CombatStatsComponent.class));
        convertToFriendly(monkey, player, entities);
        assertEquals(0, monkey.getComponent(CombatStatsComponent.class).getDefense(),
                "Monkey should have 0 defense in combat stats component.");
    }

    @Test
    void TestMonkeyUpdatedCombatStatsSpeed() {
        assertNotNull(monkey.getComponent(CombatStatsComponent.class));
        convertToFriendly(monkey, player, entities);
        assertEquals(0, monkey.getComponent(CombatStatsComponent.class).getSpeed(),
                "Monkey should have 0 speed in combat stats component.");
    }

    @Test
    void TestMonkeyUpdatedCombatStatsXP() {
        assertNotNull(monkey.getComponent(CombatStatsComponent.class));
        convertToFriendly(monkey, player, entities);
        assertEquals(0, monkey.getComponent(CombatStatsComponent.class).getExperience(),
                "Monkey should have 0 experience in combat stats component.");
    }

    @Test
    void TestMonkeyHasFriendlyAI() {
        assertNull(monkey.getComponent(FriendlyNPCAnimationController.class));
        convertToFriendly(monkey, player, entities);
        assertNotNull(monkey.getComponent(FriendlyNPCAnimationController.class));
    }

//    @Test
//    void TestMonkeyHasSoundPath() {
//        String[] sound = {"Sound Path"};
//        assertNotNull(sound);
//        assertEquals(sound, new String[]{"Sound Path"});
//    }

}