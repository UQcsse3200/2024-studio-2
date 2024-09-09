package com.csse3200.game.entities.factories;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.ConfigComponent;
import com.csse3200.game.components.npc.FriendlyNPCAnimationController;
import com.csse3200.game.entities.DialogueBoxService;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.*;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;
import org.mockito.ArgumentCaptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(GameExtension.class)
class NPCFactoryTest {
    private static Entity lion;
    private static Entity cow;
    private static Entity eagle;
    private static Entity turtle;
    private static Entity snake;
    private static Entity fish;
    private static Entity magpie;
    private Entity kanga;

    private static final NPCConfigs configs =
            FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");

    private static String[] textures = {
            "images/Cow.png",
            "images/Lion-Spritesheet.png",
            "images/snake.png",
            "images/eagle.png",
            "images/turtle.png",
            "images/magpie.png",
            "images/Fish.png",
            "images/final_boss_kangaroo.png"
    };

    private static String[] atlas = {
            "images/Cow.atlas",
            "images/lion.atlas",
            "images/snake.atlas",
            "images/eagle.atlas",
            "images/turtle.atlas",
            "images/magpie.atlas",
            "images/Fish.atlas",
            "images/final_boss_kangaroo.atlas"
    };

    @BeforeAll
    static void setup() {
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
        List<Entity> enemies = new ArrayList<>();
        cow = NPCFactory.createCow(player, enemies);
        lion = NPCFactory.createLion(player, enemies);
        eagle = NPCFactory.createEagle(player, enemies);
        turtle = NPCFactory.createTurtle(player, enemies);
        snake = NPCFactory.createSnake(player, enemies);
        magpie = NPCFactory.createMagpie(player, enemies);
        fish = NPCFactory.createFish(player, enemies);
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
        Assertions.assertArrayEquals(new String[][]{{"Welcome to Animal Kingdom!", "I am Finny the Fish."}}, baseHint);
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
        Assertions.assertArrayEquals(baseHint, new String[][]{
                {"Welcome to Animal Kingdom!", "I am Charlie the Cow.", "/cWhich tip do you wanna hear about?/s01What do potions do???/s02How to beat the final boss/s03Nothing. Bye"},
                {"Potions heals you by (n) HP!", "I hope this helped."},
                {"Final boss?? That Kangaroo??", "idk"},
                {"Good luck!"}
        });
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
        Assertions.assertArrayEquals(baseHint, new String[][]{{"HHIISSSSSSS, I am the mighty Snake of the Jungle!",
                "You look very tasty and I am very hungry",
                "Go play a game and collect me some apples...",
                "/msOr I will eat you whole!"}});
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
        Assertions.assertArrayEquals(new String[][]{{"Welcome to Animal Kingdom!", "I am Maggie the Magpie."}}, baseHint);
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
    * Test the initiate and end dialogue boxes
    */
    /** @Test
    void testInitiateDialogueWithSound() {
        // Initialise mocks
        DialogueBoxService chatOverlayService = mock(DialogueBoxService.class);
        ResourceService resourceService = mock(ResourceService.class);

        // Set up ServiceLocator to return mocks
        ServiceLocator.registerDialogueBoxService(chatOverlayService);
        ServiceLocator.registerResourceService(resourceService);

        // Given
        String[] animalSoundPaths = {"sound1.wav", "sound2.wav"};
        String[][] hintText = {{"Hint 1", "Hint 2"}};

        Sound sound1 = mock(Sound.class);
        Sound sound2 = mock(Sound.class);

        when(resourceService.getAsset("sound1.wav", Sound.class)).thenReturn(sound1);
        when(resourceService.getAsset("sound2.wav", Sound.class)).thenReturn(sound2);

        // When
        NPCFactory.initiateDialogue(animalSoundPaths, hintText);

        // Then
        verify(chatOverlayService).updateText(hintText);

        ArgumentCaptor<Long> soundIdCaptor = ArgumentCaptor.forClass(Long.class);

        // Verify interactions with sound1
        verify(sound1, times(1)).play();
        verify(sound1).setVolume(soundIdCaptor.capture(), eq(0.3f));
        verify(sound1).setLooping(soundIdCaptor.capture(), eq(false));

        // Verify interactions with sound2
        verify(sound2, times(1)).play();
        verify(sound2).setVolume(soundIdCaptor.capture(), eq(0.3f));
        verify(sound2).setLooping(soundIdCaptor.capture(), eq(false));
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