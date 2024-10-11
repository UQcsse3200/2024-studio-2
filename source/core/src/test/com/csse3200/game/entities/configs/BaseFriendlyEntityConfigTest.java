//package com.csse3200.game.entities.configs;
//
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.csse3200.game.entities.Entity;
//import com.csse3200.game.entities.factories.*;
//import com.csse3200.game.files.FileLoader;
//import com.csse3200.game.input.InputService;
//import com.csse3200.game.lighting.LightingEngine;
//import com.csse3200.game.lighting.LightingService;
//import com.csse3200.game.physics.PhysicsService;
//import com.csse3200.game.rendering.RenderService;
//import com.csse3200.game.services.DialogueBoxService;
//import com.csse3200.game.services.GameTime;
//import com.csse3200.game.services.ResourceService;
//import com.csse3200.game.services.ServiceLocator;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyFloat;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//class BaseFriendlyEntityConfigTest {
//    private static Entity player;
//    private static Entity cow;
//    private DialogueBoxService dialogueBoxService;
//    private ResourceService resourceService;
//
//    private static final NPCConfigs configs =
//            FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");
//
//    private static String[] textures = {
//            "images/chicken.png",
//            "images/frog.png",
//            "images/monkey.png",
//            "images/friendly_npcs/friendly-npcs.png",
//            "images/final_boss_kangaroo.png",
//            "images/bear2.png"
//    };
//
//    private static String[] atlas = {
//            "images/friendly_npcs/Cow.atlas",
//            "images/friendly_npcs/lion.atlas",
//            "images/friendly_npcs/snake.atlas",
//            "images/friendly_npcs/eagle.atlas",
//            "images/friendly_npcs/turtle.atlas",
//            "images/friendly_npcs/magpie.atlas",
//            "images/friendly_npcs/Fish.atlas",
//            "images/final_boss_kangaroo.atlas",
//            "images/chicken.atlas",
//            "images/monkey.atlas",
//            "images/frog.atlas",
//            "images/bear.atlas"
//    };
//
//    @BeforeAll
//    static void setup() {
//        // Mock GameTime and register it
//        GameTime gameTime = mock(GameTime.class);
//        when(gameTime.getDeltaTime()).thenReturn(0.02f);
//        ServiceLocator.registerTimeSource(gameTime);
//
//        // Register services
//        ServiceLocator.registerPhysicsService(new PhysicsService());
//
//        ResourceService resourceService = new ResourceService();
//        ServiceLocator.registerResourceService(resourceService);
//        RenderService renderService = mock(RenderService.class);
//        when(renderService.getStage()).thenReturn(mock(Stage.class));
//        ServiceLocator.registerInputService(new InputService());
//
//        // lighting service
//        LightingEngine mockLightingEngine = mock(LightingEngine.class);
//        LightingService mockLightingService = mock(LightingService.class);
//        when(mockLightingService.getLighting()).thenReturn(mockLightingEngine);
//        when(mockLightingEngine.createPointLight(anyFloat(), anyFloat(), anyFloat(), any(Color.class))).thenReturn(null);
//        ServiceLocator.registerLightingService(mockLightingService);
//
//        // Load resources
//        resourceService.loadTextures(textures);
//        resourceService.loadTextureAtlases(atlas);
//        resourceService.loadAll();
//
//        ServiceLocator.registerRenderService(renderService);
//
//        // Create and register the Stage
//        Stage stage = ServiceLocator.getRenderService().getStage();
//        when(renderService.getStage()).thenReturn(stage);
//        DialogueBoxService entityChatService = new DialogueBoxService(stage);
//        ServiceLocator.registerDialogueBoxService(entityChatService);
//
//        // Create NPCs
//        Entity player = new Entity();
//        List<Entity> enemies = new ArrayList<>();
//        cow = NPCFactory.createCow(player, enemies);
//    }
//
//    @BeforeEach
//    void setUpServices() {
//        dialogueBoxService = mock(DialogueBoxService.class);
//        resourceService = mock(ResourceService.class);
//
//        ServiceLocator.registerDialogueBoxService(dialogueBoxService);
//        ServiceLocator.registerResourceService(resourceService);
//    }
//
//    @Test
//    void getSpritePath() {
//        String sound = configs.cow.getSpritePath();
//        System.out.println(sound);
//        assertEquals("sounds/mooing-cow.wav", sound);
//    }
//
//    @Test
//    void setSpritePath() {
//    }
//
//    @Test
//    void getAnimationSpeed() {
//    }
//
//    @Test
//    void getSoundPath() {
//    }
//
//    @Test
//    void setSoundPath() {
//    }
//
//    @Test
//    void getHealth() {
//    }
//
//    @Test
//    void getAnimalName() {
//    }
//
//    @Test
//    void getBaseAttack() {
//    }
//
//    @Test
//    void getBaseHint() {
//    }
//
//    @Test
//    void restartCurrentHint() {
//    }
//
//    @Test
//    void setHealth() {
//    }
//
//    @Test
//    void getHunger() {
//    }
//
//    @Test
//    void setHunger() {
//    }
//
//    @Test
//    void setBaseAttack() {
//    }
//
//    @Test
//    void getStrength() {
//    }
//
//    @Test
//    void setStrength() {
//    }
//
//    @Test
//    void getDefense() {
//    }
//
//    @Test
//    void setDefense() {
//    }
//
//    @Test
//    void getSpeed() {
//    }
//
//    @Test
//    void setSpeed() {
//    }
//
//    @Test
//    void getExperience() {
//    }
//
//    @Test
//    void setExperience() {
//    }
//
//    @Test
//    void getItemProbability() {
//    }
//
//    @Test
//    void isBoss() {
//    }
//
//    @Test
//    void getStamina() {
//    }
//
//    @Test
//    void setStamina() {
//    }
//
//    @Test
//    void getLevel() {
//    }
//
//    @Test
//    void setLevel() {
//    }
//}