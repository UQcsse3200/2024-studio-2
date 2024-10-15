package com.csse3200.game.entities.factories;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.components.ConfigComponent;
import com.csse3200.game.components.npc.FriendlyNPCAnimationController;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.input.InputService;
import com.csse3200.game.lighting.LightingEngine;
import com.csse3200.game.lighting.LightingService;
import com.csse3200.game.services.DialogueBoxService;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.*;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.AudioManager;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.dialoguebox.KeyboardDialogueBoxInputComponent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(GameExtension.class)
class NPCFactoryTest {
    private static Entity player;
    private static Entity lion;
    private static Entity cow;
    private static Entity eagle;
    private static Entity turtle;
    private static Entity snake;
    private static Entity fish;
    private static Entity magpie;

    private static Entity friendlyChicken;
    private static Entity friendlyFrog;
    private static Entity friendlyMonkey;
    private static Entity friendlyBear;

    private DialogueBoxService dialogueBoxService;
    private ResourceService resourceService;

    private static final NPCConfigs configs =
            FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");

    private static String[] textures = {
            "images/chicken.png",
            "images/frog.png",
            "images/monkey.png",
            "images/friendly_npcs/friendly-npcs.png",
            "images/final_boss_kangaroo.png",
            "images/bear2.png",
            "images/foodtextures/Milk.png"
    };

    private static String[] atlas = {
            "images/friendly_npcs/Cow.atlas",
            "images/friendly_npcs/lion.atlas",
            "images/friendly_npcs/snake.atlas",
            "images/friendly_npcs/eagle.atlas",
            "images/friendly_npcs/turtle.atlas",
            "images/friendly_npcs/magpie.atlas",
            "images/friendly_npcs/Fish.atlas",
            "images/final_boss_kangaroo.atlas",
            "images/chicken.atlas",
            "images/monkey.atlas",
            "images/frog.atlas",
            "images/bear.atlas"
    };

    /**
     * Sets up the necessary services needed for the tests.
     * Also creates the NPCs being tested both friendly and converted.
     */
    @BeforeAll
    static void setup() {
        // Mock GameTime and register it
        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getDeltaTime()).thenReturn(0.02f);
        ServiceLocator.registerTimeSource(gameTime);

        // Register services
        PhysicsService physicsService = new PhysicsService();
        ServiceLocator.registerPhysicsService(physicsService);
        ResourceService resourceService = new ResourceService();
        ServiceLocator.registerResourceService(resourceService);
        RenderService renderService = mock(RenderService.class);
        when(renderService.getStage()).thenReturn(mock(Stage.class));
        ServiceLocator.registerInputService(new InputService());

        // lighting service
        LightingEngine mockLightingEngine = mock(LightingEngine.class);
        LightingService mockLightingService = mock(LightingService.class);
        when(mockLightingService.getLighting()).thenReturn(mockLightingEngine);
        when(mockLightingEngine.createPointLight(anyFloat(), anyFloat(), anyFloat(), any(Color.class))).thenReturn(null);
        ServiceLocator.registerLightingService(mockLightingService);

        // Load resources
        resourceService.loadTextures(textures);
        resourceService.loadTextureAtlases(atlas);
        resourceService.loadAll();

        ServiceLocator.registerRenderService(renderService);

        // Create and register the Stage
        Stage stage = ServiceLocator.getRenderService().getStage();
        when(renderService.getStage()).thenReturn(stage);
        DialogueBoxService entityChatService = new DialogueBoxService(stage);
        ServiceLocator.registerDialogueBoxService(entityChatService);

        // Create NPCs
        player = new Entity();
        List<Entity> enemies = new ArrayList<>();
        cow = NPCFactory.createCow(player, enemies);
        lion = NPCFactory.createLion(player, enemies);
        eagle = NPCFactory.createEagle(player, enemies);
        turtle = NPCFactory.createTurtle(player, enemies);
        snake = NPCFactory.createSnake(player, enemies);
        magpie = NPCFactory.createMagpie(player, enemies);
        fish = NPCFactory.createFish(player, enemies);

        friendlyChicken = NPCFactory.createChicken(player, enemies);
        friendlyFrog = NPCFactory.createFrog(player, enemies);
        friendlyMonkey = NPCFactory.createMonkey(player, enemies);
        friendlyBear = NPCFactory.createBear(player, enemies);
    }

    /**
     * Sets up a mock dialogue box and resource service to be used when testing dialogue interactions.
     */
    @BeforeEach
    void setUpServices() {
        dialogueBoxService = mock(DialogueBoxService.class);
        resourceService = mock(ResourceService.class);

        ServiceLocator.registerDialogueBoxService(dialogueBoxService);
        ServiceLocator.registerResourceService(resourceService);
    }

    /**
     * Tests that the dialogue initialisation works as intended.
     */
    @Test
    void testInitiateDialogue() {
        String[] testSoundPath = new String[]{"sounds/tiger-roar.wav"};
        String[][] testHintText = new String[][]{
                {"Welcome to Animal Kingdom!", "I am Lenny the Lion.", "/cWhich tip do you wanna hear about?/s01What do potions do???/s02How to beat the final boss/s03Nothing. Bye"},
                {"Potions heals you by (n) HP!", "I hope this helped."},
                {"Final boss?? That Kangaroo??", "idk"},
                {"Good luck!"}
        };
        NPCFactory.initiateDialogue(testSoundPath, testHintText);
        verify(dialogueBoxService).updateText(testHintText, -2);
        for (String soundPath : testSoundPath) {
            AudioManager.playSound(soundPath);
            verify(resourceService, atLeastOnce()).getAsset(soundPath, Sound.class);
        }
    }

    /**
     * Tests the initialization of a friendly bear by checking its creation, name, type,
     * and the presence of necessary components.
     */
    @Test
    void testBearInitialisation() {
        // Test creation of the friendlyBear
        Assertions.assertNotNull(friendlyBear, "Bear should not be null.");

        // Test that the friendlyBear has the correct name
        String name = configs.friendlyBear.getAnimalName();
        Assertions.assertEquals("Bear", name);

        // Test that the friendlyBear is an Entity
        Assertions.assertEquals(Entity.class, friendlyBear.getClass());

        // Test that the friendlyBear has a PhysicsComponent
        Assertions.assertNotNull(friendlyBear.getComponent(PhysicsComponent.class));

        // Test that the friendlyBear has a PhysicsMovementComponent
        Assertions.assertNotNull(friendlyBear.getComponent(PhysicsMovementComponent.class));

        // Test that the friendlyBear has a ColliderComponent
        Assertions.assertNotNull(friendlyBear.getComponent(ColliderComponent.class));

        // Test that the friendlyBear has a ConfigComponent
        Assertions.assertNotNull(friendlyBear.getComponent(ConfigComponent.class));
    }

    /**
     * Tests that the friendly bear has the correct sound path.
     */
    @Test
    void TestBearHasCorrectSoundPath() {
        String[] sound = configs.friendlyBear.getSoundPath();
        Assertions.assertNotNull(sound);
        Assertions.assertArrayEquals(new String[]{"sounds/bear.wav"}, sound);
    }

    /**
     * Tests that the friendly bear has the correct base hint.
     */
    @Test
    void TestBearHasCorrectBaseHint() {
        String[][] baseHint = configs.friendlyBear.getBaseHint();
        assertNotNull(baseHint);
        Assertions.assertArrayEquals(new String[][]{{"Welcome to Animal Kingdom!","I am Benny the Bear."}},
                baseHint);
    }

    /**
     * Tests that the friendly bear has a wait animation.
     */
    @Test
    void TestBearHasAnimation() {
        Assertions.assertTrue(friendlyBear.getComponent(AnimationRenderComponent.class).hasAnimation("float"),
                "Bear should have idle animation.");
    }

    /**
     * Tests that the friendly bear is a friendly NPC meaning it won't attack players.
     */
    @Test
    void TestBearIsFriendly() {
        Assertions.assertNotNull(friendlyBear.getComponent(FriendlyNPCAnimationController.class),
                "Bear should have a friendly AI controller.");
    }

    /**
     * Tests that the friendly bear is in the correct spot when placed.
     */
    @Test
    void TestBearSetPosition() {
        Vector2 pos = new Vector2(0f, 0f);
        friendlyBear.setPosition(pos);

        Assertions.assertEquals(pos, friendlyBear.getPosition());
    }

    /**
     * Tests the initialization of a friendly chicken by checking its creation, name, type,
     * and the presence of necessary components.
     */
    @Test
    void testChickenInitialisation() {
        // Test creation of the friendlyChicken
        Assertions.assertNotNull(friendlyChicken, "Chicken should not be null.");

        // Test that the friendlyChicken has the correct name
        String name = configs.friendlyChicken.getAnimalName();
        Assertions.assertEquals("Chicken", name);

        // Test that the friendlyChicken is an Entity
        Assertions.assertEquals(Entity.class, friendlyChicken.getClass());

        // Test that the friendlyChicken has a PhysicsComponent
        Assertions.assertNotNull(friendlyChicken.getComponent(PhysicsComponent.class));

        // Test that the friendlyChicken has a PhysicsMovementComponent
        Assertions.assertNotNull(friendlyChicken.getComponent(PhysicsMovementComponent.class));

        // Test that the friendlyChicken has a ColliderComponent
        Assertions.assertNotNull(friendlyChicken.getComponent(ColliderComponent.class));

        // Test that the friendlyChicken has a ConfigComponent
        Assertions.assertNotNull(friendlyChicken.getComponent(ConfigComponent.class));
    }

    /**
     * Tests that the friendly chicken has the correct sound path.
     */
    @Test
    void TestChickenHasCorrectSoundPath() {
        String[] sound = configs.friendlyChicken.getSoundPath();
        Assertions.assertNotNull(sound);
        Assertions.assertArrayEquals(new String[]{"sounds/chicken.wav"}, sound);
    }

    /**
     * Tests that the friendly chicken has the correct base hint.
     */
    @Test
    void TestChickenHasCorrectBaseHint() {
        String[][] baseHint = configs.friendlyChicken.getBaseHint();
        assertNotNull(baseHint);
        Assertions.assertArrayEquals(new String[][]{{"Welcome to Animal Kingdom!","I am Charles the Chicken."}},
                baseHint);
    }

    /**
     * Tests that the friendly chicken is a friendly NPC meaning it won't attack players.
     */
    @Test
    void TestChickenIsFriendly() {
        Assertions.assertNotNull(friendlyChicken.getComponent(FriendlyNPCAnimationController.class),
                "Chicken should have a friendly AI controller.");
    }

    /**
     * Tests that the friendly chicken is in the correct spot when placed.
     */
    @Test
    void TestChickenSetPosition() {
        Vector2 pos = new Vector2(0f, 0f);
        friendlyChicken.setPosition(pos);

        Assertions.assertEquals(pos, friendlyChicken.getPosition());
    }

    /**
     * Tests the initialization of a friendly frog by checking its creation, name, type,
     * and the presence of necessary components.
     */
    @Test
    void testFrogInitialisation() {
        // Test creation of the friendlyFrog
        Assertions.assertNotNull(friendlyFrog, "Frog should not be null.");

        // Test that the friendlyFrog has the correct name
        String name = configs.friendlyFrog.getAnimalName();
        Assertions.assertEquals("Frog", name);

        // Test that the friendlyFrog is an Entity
        Assertions.assertEquals(Entity.class, friendlyFrog.getClass());

        // Test that the friendlyFrog has a PhysicsComponent
        Assertions.assertNotNull(friendlyFrog.getComponent(PhysicsComponent.class));

        // Test that the friendlyFrog has a PhysicsMovementComponent
        Assertions.assertNotNull(friendlyFrog.getComponent(PhysicsMovementComponent.class));

        // Test that the friendlyFrog has a ColliderComponent
        Assertions.assertNotNull(friendlyFrog.getComponent(ColliderComponent.class));

        // Test that the friendlyFrog has a ConfigComponent
        Assertions.assertNotNull(friendlyFrog.getComponent(ConfigComponent.class));
    }

    /**
     * Tests that the friendly frog has the correct sound path.
     */
    @Test
    void TestFrogHasCorrectSoundPath() {
        String[] sound = configs.friendlyFrog.getSoundPath();
        Assertions.assertNotNull(sound);
        Assertions.assertArrayEquals(new String[]{"sounds/frog.wav"}, sound);
    }

    /**
     * Tests that the friendly frog has the correct base hint.
     */
    @Test
    void TestFrogHasCorrectBaseHint() {
        String[][] baseHint = configs.friendlyFrog.getBaseHint();
        assertNotNull(baseHint);
        Assertions.assertArrayEquals(new String[][]{{"Welcome to Animal Kingdom!","I am Fred the Frog."}},
                baseHint);
    }

    /**
     * Tests that the friendly frog has a wait animation.
     */
    @Test
    void TestFrogHasAnimation() {
        Assertions.assertTrue(friendlyFrog.getComponent(AnimationRenderComponent.class).hasAnimation("float"),
                "Frog should have idle animation.");
    }

    /**
     * Tests that the friendly frog is a friendly NPC meaning it won't attack players.
     */
    @Test
    void TestFrogIsFriendly() {
        Assertions.assertNotNull(friendlyFrog.getComponent(FriendlyNPCAnimationController.class),
                "Frog should have a friendly AI controller.");
    }

    /**
     * Tests that the friendly frog is in the correct spot when placed.
     */
    @Test
    void TestFrogSetPosition() {
        Vector2 pos = new Vector2(0f, 0f);
        friendlyFrog.setPosition(pos);

        Assertions.assertEquals(pos, friendlyFrog.getPosition());
    }

    /**
     * Tests the initialization of a friendly monkey by checking its creation, name, type,
     * and the presence of necessary components.
     */
    @Test
    void testMonkeyInitialisation() {
        // Test creation of the friendlyMonkey
        Assertions.assertNotNull(friendlyMonkey, "Monkey should not be null.");

        // Test that the friendlyMonkey has the correct name
        String name = configs.friendlyMonkey.getAnimalName();
        Assertions.assertEquals("Monkey", name);

        // Test that the friendlyMonkey is an Entity
        Assertions.assertEquals(Entity.class, friendlyMonkey.getClass());

        // Test that the friendlyMonkey has a PhysicsComponent
        Assertions.assertNotNull(friendlyMonkey.getComponent(PhysicsComponent.class));

        // Test that the friendlyMonkey has a PhysicsMovementComponent
        Assertions.assertNotNull(friendlyMonkey.getComponent(PhysicsMovementComponent.class));

        // Test that the friendlyMonkey has a ColliderComponent
        Assertions.assertNotNull(friendlyMonkey.getComponent(ColliderComponent.class));

        // Test that the friendlyMonkey has a ConfigComponent
        Assertions.assertNotNull(friendlyMonkey.getComponent(ConfigComponent.class));
    }

    /**
     * Tests that the friendly monkey has the correct sound path.
     */
    @Test
    void TestMonkeyHasCorrectSoundPath() {
        String[] sound = configs.friendlyMonkey.getSoundPath();
        Assertions.assertNotNull(sound);
        Assertions.assertArrayEquals(new String[]{"sounds/monkey.wav"}, sound);
    }

    /**
     * Tests that the friendly monkey has the correct base hint.
     */
    @Test
    void TestMonkeyHasCorrectBaseHint() {
        String[][] baseHint = configs.friendlyMonkey.getBaseHint();
        assertNotNull(baseHint);
        Assertions.assertArrayEquals(new String[][]{{"Welcome to Animal Kingdom!","I am Max the Monkey."}},
                baseHint);
    }

    /**
     * Tests that the friendly monkey has a wait animation.
     */
    @Test
    void TestMonkeyHasAnimation() {
        Assertions.assertTrue(friendlyMonkey.getComponent(AnimationRenderComponent.class).hasAnimation("float"),
                "Monkey should have idle animation.");
    }

    /**
     * Tests that the friendly monkey is a friendly NPC meaning it won't attack players.
     */
    @Test
    void TestMonkeyIsFriendly() {
        Assertions.assertNotNull(friendlyMonkey.getComponent(FriendlyNPCAnimationController.class),
                "Monkey should have a friendly AI controller.");
    }

    /**
     * Tests that the friendly monkey is in the correct spot when placed.
     */
    @Test
    void TestMonkeySetPosition() {
        Vector2 pos = new Vector2(0f, 0f);
        friendlyMonkey.setPosition(pos);

        Assertions.assertEquals(pos, fish.getPosition());
    }

    /**
     * Test keys control an animals dialogue appropriately
     */
    @Test
    @Order(1)
    void testDialogueInputTriggersForwardAndBackward() {
        RenderService renderService = mock(RenderService.class);
        when(renderService.getStage()).thenReturn(mock(Stage.class));
        ServiceLocator.registerRenderService(renderService);
        Stage stage = ServiceLocator.getRenderService().getStage();
        DialogueBoxService entityChatService = new DialogueBoxService(stage);
        ServiceLocator.registerDialogueBoxService(entityChatService);

        InputComponent inputComponent = cow.getComponent(KeyboardDialogueBoxInputComponent.class);
        Assertions.assertNotNull(inputComponent, "InputComponent should be added to the NPC");

        cow.getEvents().trigger("CowPauseStart");
        ServiceLocator.getDialogueBoxService().updateText(new String[][] {{"1", "2"}}, DialogueBoxService.DialoguePriority.NONEDEFAULT);
        String firstHint = ServiceLocator.getDialogueBoxService().getCurrentOverlay().getLabel().toString();
        inputComponent.keyDown(Input.Keys.RIGHT);
        String secondHint = ServiceLocator.getDialogueBoxService().getCurrentOverlay().getLabel().toString();
        Assertions.assertNotEquals(firstHint, secondHint);
        inputComponent.keyDown(Input.Keys.LEFT);
        Assertions.assertEquals(firstHint, ServiceLocator.getDialogueBoxService().getCurrentOverlay().getLabel().toString());
    }

    /**
     * Tests the initialization of a fish by checking its creation, name, type,
     * and the presence of necessary components.
     */
    @Test
    void testFishInitialisation() {
        // Test creation of the fish
        Assertions.assertNotNull(fish, "Fish should not be null.");

        // Test that the fish has the correct name
        String name = configs.fish.getAnimalName();
        Assertions.assertEquals("Fish", name);

        // Test that the fish is an Entity
        Assertions.assertEquals(Entity.class, fish.getClass());

        // Test that the fish has a PhysicsComponent
        Assertions.assertNotNull(fish.getComponent(PhysicsComponent.class));

        // Test that the fish has a PhysicsMovementComponent
        Assertions.assertNotNull(fish.getComponent(PhysicsMovementComponent.class));

        // Test that the fish has a ColliderComponent
        Assertions.assertNotNull(fish.getComponent(ColliderComponent.class));

        // Test that the fish has a ConfigComponent
        Assertions.assertNotNull(fish.getComponent(ConfigComponent.class));
    }

    /**
     * Tests that the fish has the correct sound path.
     */
    @Test
    void TestFishHasCorrectSoundPath() {
        String[] sound = configs.fish.getSoundPath();
        Assertions.assertNotNull(sound);
        Assertions.assertArrayEquals(new String[]{"sounds/FishBubble.wav"}, sound);
    }

    /**
     * Tests that the fish has the correct base hint.
     */
    @Test
    void TestFishHasCorrectBaseHint() {
        String[][] baseHint = configs.fish.getBaseHint();
        assertNotNull(baseHint);
        Assertions.assertArrayEquals(new String[][]{{"Help me please!",
                "A strong current just scattered all my eggs and now I am lost",
                "It is getting dark now, they are in great danger!",
                "I must get them back and find my way home!",
                "/muHelp me collect all the fish eggs an get back home before it is too late"}}, baseHint);
    }

    /**
     * Tests that the fish has an idle animation.
     */
    @Test
    void TestFishHasAnimation() {
        Assertions.assertTrue(fish.getComponent(AnimationRenderComponent.class).hasAnimation("float") ,
                "Fish should have idle animation.");
    }

    /**
     * Tests that the fish is a friendly NPC meaning it won't attack players.
     */
    @Test
    void TestFishIsFriendly() {
        Assertions.assertNotNull(fish.getComponent(FriendlyNPCAnimationController.class),
                "Fish should have a friendly AI controller.");
    }

    /**
     * Tests that the fish is in the correct spot when placed.
     */
    @Test
    void TestFishSetPosition() {
        Vector2 pos = new Vector2(0f, 0f);
        fish.setPosition(pos);

        Assertions.assertEquals(pos, fish.getPosition());
    }

    /**
     * Tests the initialization of a cow by checking its creation, name, type,
     * and the presence of necessary components.
     */
    @Test
    void testCowInitialisation() {
        // Test creation of the cow
        Assertions.assertNotNull(cow, "Cow should not be null.");

        // Test that the cow has the correct name
        String name = configs.cow.getAnimalName();
        Assertions.assertEquals("Cow", name);

        // Test that the cow is an Entity
        Assertions.assertEquals(Entity.class, cow.getClass());

        // Test that the cow has a PhysicsComponent
        Assertions.assertNotNull(cow.getComponent(PhysicsComponent.class));

        // Test that the cow has a PhysicsMovementComponent
        Assertions.assertNotNull(cow.getComponent(PhysicsMovementComponent.class));

        // Test that the cow has a ColliderComponent
        Assertions.assertNotNull(cow.getComponent(ColliderComponent.class));

        // Test that the cow has a ConfigComponent
        Assertions.assertNotNull(cow.getComponent(ConfigComponent.class));
    }

    /**
     * Tests that the cow has the correct sound path.
     */
    @Test
    void TestCowHasCorrectSoundPath() {
        String[] sound = configs.cow.getSoundPath();
        assertNotNull(sound);
        assert(Arrays.equals(sound, new String[]{"sounds/mooing-cow.wav"}));
    }

    /**
     * Tests that the cow has the correct base hint.
     */
    @Test
    void TestCowHasCorrectBaseHint() {
        String[][] baseHint = configs.cow.getBaseHint();
        assertNotNull(baseHint);
        Assertions.assertArrayEquals(baseHint, new String[][]{{
                "Go complete your quest adventurer!"

        }});
    }
    /**
     * Tests that the cow has an idle animation.
     */
    @Test
    void TestCowHasAnimation() {
        assertTrue(cow.getComponent(AnimationRenderComponent.class).hasAnimation("float") ,
                "Cow should have idle animation.");
    }

    /**
     * Tests that the cow is a friendly NPC meaning it won't attack players.
     */
    @Test
    void TestCowIsFriendly() {
        assertNotNull(cow.getComponent(FriendlyNPCAnimationController.class),
                "Cow should have a friendly AI controller.");
    }

    /**
     * Tests that the cow is in the correct spot when placed.
     */
    @Test
    void TestCowSetPosition() {
        Vector2 pos = new Vector2(0f, 0f);
        cow.setPosition(pos);

        assertEquals(pos, cow.getPosition());
    }

    /**
     * Tests the initialization of a lion by checking its creation, name, type,
     * and the presence of necessary components.
     */
    @Test
    void testLionInitialisation() {
        // Test creation of the lion
        Assertions.assertNotNull(lion, "Lion should not be null.");

        // Test that the lion has the correct name
        String name = configs.lion.getAnimalName();
        Assertions.assertEquals("Lion", name);

        // Test that the lion is an Entity
        Assertions.assertEquals(Entity.class, lion.getClass());

        // Test that the lion has a PhysicsComponent
        Assertions.assertNotNull(lion.getComponent(PhysicsComponent.class));

        // Test that the lion has a PhysicsMovementComponent
        Assertions.assertNotNull(lion.getComponent(PhysicsMovementComponent.class));

        // Test that the lion has a ColliderComponent
        Assertions.assertNotNull(lion.getComponent(ColliderComponent.class));

        // Test that the lion has a ConfigComponent
        Assertions.assertNotNull(lion.getComponent(ConfigComponent.class));
    }

    /**
     * Tests that the lion has the correct sound path.
     */
    @Test
    void TestLionHasCorrectSoundPath() {
        String[] sound = configs.lion.getSoundPath();
        assertNotNull(sound);
        assert(Arrays.equals(sound, new String[]{"sounds/tiger-roar.wav"}));
    }

    /**
     * Tests that the lion has the correct base hint.
     */
    @Test
    void TestLionHasCorrectBaseHint() {
        String[][] baseHint = configs.lion.getBaseHint();
        assertNotNull(baseHint);
        Assertions.assertArrayEquals(baseHint, new String[][]{
                {"Welcome to Animal Kingdom!", "I am Lenny the Lion.", "/cWhich tip do you wanna hear about?/s01What do potions do???/s02How to beat the final boss/s03Nothing. Bye"},
                {"Potions heals you by (n) HP!", "I hope this helped."},
                {"Final boss?? That Kangaroo??", "idk"},
                {"Good luck!"}
        });
    }

    /**
     * Tests that the lion has an idle animation.
     */
    @Test
    void TestLionHasIdleAnimation() {
        assertTrue(lion.getComponent(AnimationRenderComponent.class).hasAnimation("float"),
                "Lion should have idle animation.");
    }

    /**
     * Tests that the lion is a friendly NPC meaning it won't attack players.
     */
    @Test
    void TestLionIsFriendly() {
        assertNotNull(lion.getComponent(FriendlyNPCAnimationController.class),
                "Lion should have a friendly AI controller.");
    }

    /**
     * Tests that the lion is in the correct spot when placed.
     */
    @Test
    void TestLionSetPosition() {
        Vector2 pos = new Vector2(0f, 0f);
        lion.setPosition(pos);

        assertEquals(pos, lion.getPosition());
    }

    /**
     * Tests the initialization of an eagle by checking its creation, name, type,
     * and the presence of necessary components.
     */
    @Test
    void testEagleInitialisation() {
        // Test creation of the eagle
        Assertions.assertNotNull(eagle, "Eagle should not be null.");

        // Test that the eagle has the correct name
        String name = configs.eagle.getAnimalName();
        Assertions.assertEquals("Eagle", name);

        // Test that the eagle is an Entity
        Assertions.assertEquals(Entity.class, eagle.getClass());

        // Test that the eagle has a PhysicsComponent
        Assertions.assertNotNull(eagle.getComponent(PhysicsComponent.class));

        // Test that the eagle has a PhysicsMovementComponent
        Assertions.assertNotNull(eagle.getComponent(PhysicsMovementComponent.class));

        // Test that the eagle has a ColliderComponent
        Assertions.assertNotNull(eagle.getComponent(ColliderComponent.class));

        // Test that the eagle has a ConfigComponent
        Assertions.assertNotNull(eagle.getComponent(ConfigComponent.class));
    }

    /**
     * Tests that the eagle has the correct sound path.
     */
    @Test
    void TestEagleHasCorrectSoundPath() {
        String[] sound = configs.eagle.getSoundPath();
        assertNotNull(sound);
        assert(Arrays.equals(sound, new String[]{"sounds/eagle-scream.wav"}));
    }

    /**
     * Tests that the eagle has the correct base hint.
     */
    @Test
    void TestEagleHasCorrectBaseHint() {
        String[][] baseHint = configs.eagle.getBaseHint();
        assertNotNull(baseHint);
        Assertions.assertArrayEquals(baseHint, new String[][]{
                {"Welcome to Animal Kingdom!", "I am Ethan the Eagle.", "/cWhich tip do you wanna hear about?/s01What do potions do???/s02How to beat the final boss/s03Nothing. Bye"},
                {"Potions heals you by (n) HP!", "I hope this helped."},
                {"Final boss?? That Kangaroo??", "idk"},
                {"Good luck!"}
        });
    }

    /**
     * Tests that the eagle has an idle animation.
     */
    @Test
    void TestEagleHasIdleAnimation() {
        assertTrue(eagle.getComponent(AnimationRenderComponent.class).hasAnimation("float"),
                "Eagle should have idle animation.");
    }

    /**
     * Tests that the eagle is a friendly NPC meaning it won't attack players.
     */
    @Test
    void TestEagleIsFriendly() {
        assertNotNull(eagle.getComponent(FriendlyNPCAnimationController.class),
                "Eagle should have a friendly AI controller.");
    }

    /**
     * Tests that the eagle is in the correct spot when placed.
     */
    @Test
    void TestEagleSetPosition() {
        Vector2 pos = new Vector2(0f, 0f);
        eagle.setPosition(pos);

        assertEquals(pos, eagle.getPosition());
    }

    /**
     * Tests the initialization of a cow by checking its creation, name, type,
     * and the presence of necessary components.
     */
    @Test
    void testTurtleInitialisation() {
        // Test creation of the turtle
        Assertions.assertNotNull(turtle, "Turtle should not be null.");

        // Test that the turtle has the correct name
        String name = configs.turtle.getAnimalName();
        Assertions.assertEquals("Turtle", name);

        // Test that the turtle is an Entity
        Assertions.assertEquals(Entity.class, turtle.getClass());

        // Test that the turtle has a PhysicsComponent
        Assertions.assertNotNull(turtle.getComponent(PhysicsComponent.class));

        // Test that the turtle has a PhysicsMovementComponent
        Assertions.assertNotNull(turtle.getComponent(PhysicsMovementComponent.class));

        // Test that the turtle has a ColliderComponent
        Assertions.assertNotNull(turtle.getComponent(ColliderComponent.class));

        // Test that the turtle has a ConfigComponent
        Assertions.assertNotNull(turtle.getComponent(ConfigComponent.class));
    }

    /**
     * Tests that the turtle has the correct sound path.
     */
    @Test
    void TestTurtleHasCorrectSoundPath() {
        String[] sound = configs.turtle.getSoundPath();
        assertNotNull(sound);
        assert(Arrays.equals(sound, new String[]{"sounds/turtle-hiss.wav"}));
    }

    /**
     * Tests that the turtle has an idle animation.
     */
    @Test
    void TestTurtleHasIdleAnimation() {
        assertTrue(turtle.getComponent(AnimationRenderComponent.class).hasAnimation("float"),
                "Turtle should have idle animation.");
    }

    /**
     * Tests that the turtle is a friendly NPC meaning it won't attack players.
     */
    @Test
    void TestTurtleIsFriendly() {
        assertNotNull(turtle.getComponent(FriendlyNPCAnimationController.class),
                "Turtle should have a friendly AI controller.");
    }

    /**
     * Tests that the turtle is in the correct spot when placed.
     */
    @Test
    void TestTurtleSetPosition() {
        Vector2 pos = new Vector2(0f, 0f);
        turtle.setPosition(pos);

        assertEquals(pos, turtle.getPosition());
    }

    /**
     * Tests the initialization of a snake by checking its creation, name, type,
     * and the presence of necessary components.
     */
    @Test
    void testSnakeInitialisation() {
        // Test creation of the snake
        Assertions.assertNotNull(snake, "Snake should not be null.");

        // Test that the snake has the correct name
        String name = configs.snake.getAnimalName();
        Assertions.assertEquals("Snake", name);

        // Test that the snake is an Entity
        Assertions.assertEquals(Entity.class, snake.getClass());

        // Test that the snake has a PhysicsComponent
        Assertions.assertNotNull(snake.getComponent(PhysicsComponent.class));

        // Test that the snake has a PhysicsMovementComponent
        Assertions.assertNotNull(snake.getComponent(PhysicsMovementComponent.class));

        // Test that the snake has a ColliderComponent
        Assertions.assertNotNull(snake.getComponent(ColliderComponent.class));

        // Test that the snake has a ConfigComponent
        Assertions.assertNotNull(snake.getComponent(ConfigComponent.class));
    }

    /**
     * Tests that the snake has the correct sound path.
     */
    @Test
    void TestSnakeHasCorrectSoundPath() {
        String[] sound = configs.snake.getSoundPath();
        assertNotNull(sound);
        assert(Arrays.equals(sound, new String[]{"sounds/snake-hiss.wav"}));
    }

    /**
     * Tests that the snake has the correct base hint.
     */
    @Test
    void TestSnakeHasCorrectBaseHint() {
        String[][] baseHint = configs.snake.getBaseHint();
        assertNotNull(baseHint);
        Assertions.assertArrayEquals(baseHint, new String[][]{{"HHIISSSSSSS", "I am the Sally the Snake", "The might snake of the Jungle!"}});
    }

    /**
     * Tests that the snake has an idle animation.
     */
    @Test
    void TestSnakeHasIdleAnimation() {
        assertTrue(snake.getComponent(AnimationRenderComponent.class).hasAnimation("float"),
                "Snake should have idle animation.");
    }

    /**
     * Tests that the snake is a friendly NPC meaning it won't attack players.
     */
    @Test
    void TestSnakeIsFriendly() {
        assertNotNull(snake.getComponent(FriendlyNPCAnimationController.class),
                "Snake should have a friendly AI controller.");
    }

    /**
     * Tests that the snake is in the correct spot when placed.
     */
    @Test
    void TestSnakeSetPosition() {
        Vector2 pos = new Vector2(0f, 0f);
        snake.setPosition(pos);

        assertEquals(pos, snake.getPosition());
    }

    /**
     * Tests the initialization of a magpie by checking its creation, name, type,
     * and the presence of necessary components.
     */
    @Test
    void testMagpieInitialisation() {
        // Test creation of the magpie
        Assertions.assertNotNull(magpie, "Magpie should not be null.");

        // Test that the magpie has the correct name
        String name = configs.magpie.getAnimalName();
        Assertions.assertEquals("Magpie", name);

        // Test that the magpie is an Entity
        Assertions.assertEquals(Entity.class, magpie.getClass());

        // Test that the magpie has a PhysicsComponent
        Assertions.assertNotNull(magpie.getComponent(PhysicsComponent.class));

        // Test that the magpie has a PhysicsMovementComponent
        Assertions.assertNotNull(magpie.getComponent(PhysicsMovementComponent.class));

        // Test that the magpie has a ColliderComponent
        Assertions.assertNotNull(magpie.getComponent(ColliderComponent.class));

        // Test that the magpie has a ConfigComponent
        Assertions.assertNotNull(magpie.getComponent(ConfigComponent.class));
    }

    /**
     * Tests that the magpie has the correct sound path.
     */
    @Test
    void TestMagpieHasCorrectSoundPath() {
        String[] sound = configs.magpie.getSoundPath();
        Assertions.assertNotNull(sound);
        Assertions.assertArrayEquals(new String[]{"sounds/aus-magpie.wav"}, sound);
    }

    /**
     * Tests that the magpie has the correct base hint.
     */
    @Test
    void TestMagpieHasCorrectBaseHint() {
        String[][] baseHint = configs.magpie.getBaseHint();
        assertNotNull(baseHint);
        Assertions.assertArrayEquals(new String[][]{{"I LOVE GOLD!!"}}, baseHint);
    }

    /**
     * Tests that the magpie has an idle animation.
     */
    @Test
    void TestMagpieHasAnimation() {
        Assertions.assertTrue(magpie.getComponent(AnimationRenderComponent.class).hasAnimation("float") ,
                "magpie should have idle animation.");
    }

    /**
     * Tests that the magpie is a friendly NPC meaning it won't attack players.
     */
    @Test
    void TestMagpieIsFriendly() {
        Assertions.assertNotNull(magpie.getComponent(FriendlyNPCAnimationController.class),
                "magpie should have a friendly AI controller.");
    }

    /**
     * Tests that the magpie is in the correct spot when placed.
     */
    @Test
    void TestMagpieSetPosition() {
        Vector2 pos = new Vector2(0f, 0f);
        magpie.setPosition(pos);

        Assertions.assertEquals(pos, magpie.getPosition());
    }

    /**
     * Test end Dialogue works appropriately
     */
    @Test
    void testEndDialogue() {
        // Initialise mocks
        DialogueBoxService chatOverlayService = mock(DialogueBoxService.class);
        ResourceService resourceService = mock(ResourceService.class);

        // Set up ServiceLocator to return mocks
        ServiceLocator.registerDialogueBoxService(chatOverlayService);
        ServiceLocator.registerResourceService(resourceService);

        // When
        NPCFactory.endDialogue();

        // Then
        verify(chatOverlayService).hideCurrentOverlay();
    }
}