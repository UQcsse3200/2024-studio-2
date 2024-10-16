package com.csse3200.game.components.tasks;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CameraZoomComponent;
import com.csse3200.game.components.ConfigComponent;
import com.csse3200.game.components.quests.QuestManager;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.gamestate.SaveHandler;
import com.csse3200.game.lighting.DayNightCycle;
import com.csse3200.game.lighting.LightingEngine;
import com.csse3200.game.lighting.LightingService;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.services.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.input.InputService;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.ui.dialoguebox.DialogueBox;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.ArrayList;
import static org.mockito.ArgumentMatchers.anyFloat;  // For matching any float value
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class PauseTaskTest {
    ResourceService resourceService;
    private static final NPCConfigs configs =
            FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");
    private static String[] textures = {
            "images/Cow.png",
    };

    private static String[] atlas = {
            "images/friendly_npcs/Cow.atlas",
    };

    private static String[] sounds = {
            "sounds/QuestComplete.wav", "sounds/achievement-sound.mp3", "sounds/mooing-cow.wav"
    };

    @BeforeEach
    void beforeEach() {
        EntityService entityService = new EntityService();
        resourceService = new ResourceService();

        // Mock RenderService and set DebugRenderer mock
        RenderService renderService = mock(RenderService.class);
        when(renderService.getStage()).thenReturn(mock(Stage.class));
        when(renderService.getDebug()).thenReturn(mock(DebugRenderer.class)); // Add DebugRenderer

        // Mock GameTime to control time in the test
        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getDeltaTime()).thenReturn(20f / 1000);
        InGameTime inGameTime = new InGameTime();
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerInGameTime(inGameTime);

        // Register InputService and PhysicsService
        InputService inputService = new InputService();
        ServiceLocator.registerInputService(inputService);
        ServiceLocator.registerEntityService(entityService);
        ServiceLocator.registerResourceService(resourceService);
        ServiceLocator.registerRenderService(renderService);
        ServiceLocator.registerPhysicsService(new PhysicsService()); // Add PhysicsService

        // Register DayNightCycle and LightingService for FadeLightsDayTimeComponent
        DayNightCycle dayNightCycle = new DayNightCycle(mock(RayHandler.class));
        ServiceLocator.registerDayNightCycle(dayNightCycle); // Register DayNightCycle

        // lighting service
        LightingEngine mockLightingEngine = mock(LightingEngine.class);
        LightingService mockLightingService = mock(LightingService.class);
        PointLight mockPointLight = mock(PointLight.class);
        when(mockLightingService.getLighting()).thenReturn(mockLightingEngine);
        when(mockLightingEngine.createPointLight(anyFloat(), anyFloat(), anyFloat(), any(Color.class))).thenReturn(mockPointLight);
        when(mockPointLight.getDistance()).thenReturn(1f);
        ServiceLocator.registerLightingService(mockLightingService);

        // Retrieve and set stage for DialogueBoxService
        Stage stage = ServiceLocator.getRenderService().getStage();
        DialogueBoxService dialogueBoxService = new DialogueBoxService(stage);
        ServiceLocator.registerDialogueBoxService(dialogueBoxService);

        resourceService.loadTextures(textures);
        resourceService.loadTextureAtlases(atlas);
        resourceService.loadSounds(sounds);
        resourceService.loadAll();

        while (!resourceService.loadForMillis(10)) {
            continue;
        }
    }

    @AfterEach
    void afterEach() {
        resourceService.unloadAssets(new String[]{"sounds/QuestComplete.wav"});
    }

    @Test
    void shouldMoveTowardsTarget() {
        Entity target = new Entity();
        target.setPosition(2f, 2f);

        AITaskComponent ai = new AITaskComponent()
                .addTask(new PauseTask(target, 10, 5, 2, false))
                .addTask(new WanderTask(new Vector2(2f, 2f), 2f, false));

        Entity entity = new Entity()
                .addComponent(ai)
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new ConfigComponent<>(configs.lion));

        entity.create();
        entity.setPosition(0f, 0f);

        float initialDistance = entity.getPosition().dst(target.getPosition());

        // Run the game for a few cycles
        for (int i = 0; i < 3; i++) {
            entity.earlyUpdate();
            entity.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
        }

        float newDistance = entity.getPosition().dst(target.getPosition());
        assertTrue(newDistance <= initialDistance);
    }

    @Test
    void shouldChaseOnlyWhenInDistance() {
        Entity target = new Entity();
        target.setPosition(2f, 2f);

        NPCConfigs newConfigs = FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");
        PauseTask pauseTask = new PauseTask(target, 10, 10, 5, false);

        AITaskComponent ai = new AITaskComponent()
                .addTask(pauseTask)
                .addTask(new WanderTask(new Vector2(2f, 2f), 2f, false));

        Entity entity = new Entity()
                .addComponent(ai)
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new ConfigComponent<>(newConfigs.lion));

        entity.create();

        // Test: When out of view distance, pauseTask should have negative priority
        target.setPosition(0f, 12f);
        entity.update(); // Ensure update methods are called
        assertTrue(pauseTask.getPriority() < 0, "Priority should be negative when out of view distance");

        // Test: When within view distance but outside max pause distance, pauseTask should have high priority
        target.setPosition(0f, 4f);
        entity.update();
        assertEquals(10, pauseTask.getPriority(), "Priority should be high when within view distance but outside pause distance");

        // Test: When within max pause distance, pauseTask should have high priority and should be active
        target.setPosition(0f, 2f);
        pauseTask.start(); // Start the PauseTask
        entity.update();
        assertEquals(10, pauseTask.getPriority(), "Priority should be high when within pause distance");

        // Test: After moving out of view distance, pauseTask should have negative priority
        target.setPosition(0f, 12f);
        entity.update();
        assertTrue(pauseTask.getPriority() < 0, "Priority should be negative after moving out of view distance");
    }

    @Test
    void shouldDisplayCorrectDialogue() {
        DialogueBox dialogueBox = ServiceLocator.getDialogueBoxService().getCurrentOverlay();
        Entity player =
                new Entity()
                        .addComponent(new CameraZoomComponent())
                        .addComponent(new PhysicsComponent(true))
                        .addComponent(new ColliderComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER));

        SaveHandler.getInstance().load(GameState.class, "defaultsaves", FileLoader.Location.INTERNAL);

        QuestManager questManager = new QuestManager(player);
        player.addComponent(questManager);

        questManager.loadQuests();
        player.setPosition(2f, 2f);
        player.create();

        Entity cow = NPCFactory.createCow(player, new ArrayList<>());
        cow.setPosition(1.5f, 1.5f);
        cow.create();

        cow.update();

        String cowInitialDialogue = "Moo there adventurer, welcome to the Animal Kingdom! ";

        String hintDialogue = dialogueBox.getLabel().getText().toString();
        assertEquals(cowInitialDialogue, hintDialogue);
        dialogueBox.handleForwardButtonClick();
    }

}
