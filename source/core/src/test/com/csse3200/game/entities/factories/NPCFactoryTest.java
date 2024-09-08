package com.csse3200.game.entities.factories;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.ConfigComponent;
import com.csse3200.game.components.npc.FriendlyNPCAnimationController;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class NPCFactoryTest {

    private Entity lion;
    private Entity cow;
    private Entity eagle;
    private Entity turtle;
    private Entity snake;
    private Entity fish;
    private Entity kanga;

    private static final NPCConfigs configs =
            FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");

    private String[] textures = {
            "images/Cow.png",
            "images/Lion-Spritesheet.png",
            "images/snake.png",
            "images/eagle.png",
            "images/turtle.png",
            "images/Fish.png",
            "images/final_boss_kangaroo.png"
    };

    private String[] atlas = {
            "images/Cow.atlas",
            "images/lion.atlas",
            "images/snake.atlas",
            "images/eagle.atlas",
            "images/turtle.atlas",
            "images/Fish.atlas",
            "images/final_boss_kangaroo.atlas"
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
        List<Entity> enemies = new ArrayList<>();
        cow = NPCFactory.createCow(player, enemies);
        lion = NPCFactory.createLion(player, enemies);
        eagle = NPCFactory.createEagle(player, enemies);
        turtle = NPCFactory.createTurtle(player, enemies);
        snake = NPCFactory.createSnake(player, enemies);
        fish = NPCFactory.createSnake(player, enemies);
    }

    /**
     * Tests Creation of a fish.
     */
    @Test
    void TestFishCreation() {
        assertNotNull(fish, "Fish should not be null.");
    }

    /**
     * Tests that the Fish has the correct name.
     */
    @Test
    void TestFishName() {
        String name = configs.fish.getAnimalName();
        assertEquals("Fish", name);
    }

    /**
     * Tests that the fish is an Entity.
     */
    @Test
    void TestFishIsEntity() {
        assertEquals(fish.getClass(), Entity.class);
    }

    /**
     * Tests that the fish has a physics component.
     */
    @Test
    void TestFishHasPhysicsComponent() {
        assertNotNull(fish.getComponent(PhysicsComponent.class));
    }

    /**
     * Tests that the fish has a physics movement component.
     */
    @Test
    void TestFishHasPhysicsMovementComponent() {
        assertNotNull(fish.getComponent(PhysicsMovementComponent.class));
    }

    /**
     * Tests the fish has a collider component.
     */
    @Test
    void TestFishHasColliderComponent() {
        assertNotNull(fish.getComponent(ColliderComponent.class));
    }

    /**
     * Tests that the fish has stat config component.
     */
    @Test
    void TestFishHasConfigComponent() {
        assertNotNull(fish.getComponent(ConfigComponent.class));
    }

    /**
     * Tests that the fish has the correct sound path.
     */
    @Test
    void TestFishHasCorrectSoundPath() {
        String[] sound = configs.fish.getSoundPath();
        assertNotNull(sound);
        assert(Arrays.equals(sound, new String[]{"sounds/FishBubble.wav"}));
    }

    /**
     * Tests that the fish has the correct base hint.
     */
    @Test
    void TestFishHasCorrectBaseHint() {
        String[] baseHint = configs.fish.getBaseHint();
        assertNotNull(baseHint);
        assert(Arrays.equals(baseHint, new String[]{"Welcome to Animal Kingdom!", "I am Finny the Fish."}));
    }

    /**
     * Tests that the fish has an idle animation.
     */
    @Test
    void TestFishHasAnimation() {
        assertTrue(fish.getComponent(AnimationRenderComponent.class).hasAnimation("float") ,
                "Fish should have idle animation.");
    }

    /**
     * Tests that the fish is a friendly NPC meaning it won't attack players.
     */
    @Test
    void TestFishIsFriendly() {
        assertNotNull(fish.getComponent(FriendlyNPCAnimationController.class),
                "Fish should have a friendly AI controller.");
    }

    /**
     * Tests that the fish is in the correct spot when placed.
     */
    @Test
    void TestFishSetPosition() {
        Vector2 pos = new Vector2(0f, 0f);
        fish.setPosition(pos);

        assertEquals(pos, fish.getPosition());
    }

    /**
     * Tests Creation of a cow.
     */
    @Test
    void TestCowCreation() {
        assertNotNull(cow, "Cow should not be null.");
    }

    /**
     * Tests that the Cow has the correct name.
     */
    @Test
    void TestCowName() {
        String name = configs.cow.getAnimalName();
        assertEquals("Cow", name);
    }

    /**
     * Tests that the cow is an Entity.
     */
    @Test
    void TestCowIsEntity() {
        assertEquals(cow.getClass(), Entity.class);
    }

    /**
     * Tests that the cow has a physics component.
     */
    @Test
    void TestCowHasPhysicsComponent() {
        assertNotNull(cow.getComponent(PhysicsComponent.class));
    }

    /**
     * Tests that the cow has a physics movement component.
     */
    @Test
    void TestCowHasPhysicsMovementComponent() {
        assertNotNull(cow.getComponent(PhysicsMovementComponent.class));
    }

    /**
     * Tests the cow has a collider component.
     */
    @Test
    void TestCowHasColliderComponent() {
        assertNotNull(cow.getComponent(ColliderComponent.class));
    }

    /**
     * Tests that the cow has stat config component.
     */
    @Test
    void TestCowHasConfigComponent() {
        assertNotNull(cow.getComponent(ConfigComponent.class));
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
        String[] baseHint = configs.cow.getBaseHint();
        assertNotNull(baseHint);
        assert(Arrays.equals(baseHint, new String[]{"Welcome to Animal Kingdom!", "I am Charlie the Cow."}));
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
     * Tests Creation of a lion.
     */
    @Test
    void TestLionCreation() {
        assertNotNull(lion, "Lion should not be null.");
    }

    /**
     * Tests that the lion has the correct name.
     */
    @Test
    void TestLionName() {
        String name = configs.lion.getAnimalName();
        assertEquals("Lion", name);
    }

    /**
     * Tests that the lion is an Entity.
     */
    @Test
    void TestLionIsEntity() {
        assertEquals(lion.getClass(), Entity.class);
    }

    /**
     * Tests that the lion has a physics component.
     */
    @Test
    void TestLionHasPhysicsComponent() {
        assertNotNull(lion.getComponent(PhysicsComponent.class));
    }

    /**
     * Tests that the lion has a physics movement component.
     */
    @Test
    void TestLionHasPhysicsMovementComponent() {
        assertNotNull(lion.getComponent(PhysicsMovementComponent.class));
    }

    /**
     * Tests the lion has a collider component.
     */
    @Test
    void TestLionHasColliderComponent() {
        assertNotNull(lion.getComponent(ColliderComponent.class));
    }

    /**
     * Tests that the lion has stat config component.
     */

    @Test
    void TestLionHasConfigComponent() {
        assertNotNull(lion.getComponent(ConfigComponent.class));
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
        String[] baseHint = configs.lion.getBaseHint();
        assertNotNull(baseHint);
        assert(Arrays.equals(baseHint, new String[]{"Welcome to Animal Kingdom!", "I am Lenny the Lion."}));
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
     * Tests Creation of an eagle.
     */
    @Test
    void TestEagleCreation() {
        assertNotNull(eagle, "Eagle should not be null.");
    }

    /**
     * Tests that the eagle's name is correct.
     */
    @Test
    void TestEagleName() {
        String name = configs.eagle.getAnimalName();
        assertEquals("Eagle", name);
    }

    /**
     * Tests that the eagle is an Entity.
     */
    @Test
    void TestEagleIsEntity() {
        assertEquals(eagle.getClass(), Entity.class);
    }

    /**
     * Tests that the eagle has a physics component.
     */
    @Test
    void TestEagleHasPhysicsComponent() {
        assertNotNull(eagle.getComponent(PhysicsComponent.class));
    }

    /**
     * Tests that the eagle has a physics movement component.
     */
    @Test
    void TestEagleHasPhysicsMovementComponent() {
        assertNotNull(eagle.getComponent(PhysicsMovementComponent.class));
    }

    /**
     * Tests the eagle has a collider component.
     */
    @Test
    void TestEagleHasColliderComponent() {
        assertNotNull(eagle.getComponent(ColliderComponent.class));
    }

    /**
     * Tests that the eagle has stat config component.
     */

    @Test
    void TestEagleHasConfigComponent() {
        assertNotNull(eagle.getComponent(ConfigComponent.class));
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
        String[] baseHint = configs.eagle.getBaseHint();
        assertNotNull(baseHint);
        assert(Arrays.equals(baseHint, new String[]{"Welcome to Animal Kingdom!", "I am Ethan the Eagle."}));
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
     * Tests Creation of a turtle.
     */
    @Test
    void TestTurtleCreation() {
        assertNotNull(turtle, "Turtle should not be null.");
    }

    /**
     * Tests that the turtle has the correct name.
     */
    @Test
    void TestTurtleName() {
        String name = configs.turtle.getAnimalName();
        assertEquals("Turtle", name);
    }

    /**
     * Tests that the turtle is an Entity.
     */
    @Test
    void TestTurtleIsEntity() {
        assertEquals(turtle.getClass(), Entity.class);
    }

    /**
     * Tests that the turtle has a physics component.
     */
    @Test
    void TestTurtleHasPhysicsComponent() {
        assertNotNull(turtle.getComponent(PhysicsComponent.class));
    }

    /**
     * Tests that the turtle has a physics movement component.
     */
    @Test
    void TestTurtleHasPhysicsMovementComponent() {
        assertNotNull(turtle.getComponent(PhysicsMovementComponent.class));
    }

    /**
     * Tests the turtle has a collider component.
     */
    @Test
    void TestTurtleHasColliderComponent() {
        assertNotNull(turtle.getComponent(ColliderComponent.class));
    }

    /**
     * Tests that the turtle has stat config component.
     */
    @Test
    void TestTurtleHasConfigComponent() {
        assertNotNull(turtle.getComponent(ConfigComponent.class));
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
     * Tests Creation of a snake.
     */
    @Test
    void TestSnakeCreation() {
        assertNotNull(snake, "Snake should not be null.");
    }

    /**
     * Tests that the snake has the correct name.
     */
    @Test
    void TestSnakeName() {
        String name = configs.snake.getAnimalName();
        assertEquals("Snake", name);
    }

    /**
     * Tests that the snake is an Entity.
     */
    @Test
    void TestSnakeIsEntity() {
        assertEquals(snake.getClass(), Entity.class);
    }

    /**
     * Tests that the snake has a physics component.
     */
    @Test
    void TestSnakeHasPhysicsComponent() {
        assertNotNull(snake.getComponent(PhysicsComponent.class));
    }

    /**
     * Tests that the snake has a physics movement component.
     */
    @Test
    void TestSnakeHasPhysicsMovementComponent() {
        assertNotNull(snake.getComponent(PhysicsMovementComponent.class));
    }

    /**
     * Tests the snake has a collider component.
     */
    @Test
    void TestSnakeHasColliderComponent() {
        assertNotNull(snake.getComponent(ColliderComponent.class));
    }

    /**
     * Tests that the snake has stat config component.
     */
    @Test
    void TestSnakeHasConfigComponent() {
        assertNotNull(snake.getComponent(ConfigComponent.class));
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
        String[] baseHint = configs.snake.getBaseHint();
        assertNotNull(baseHint);
        assert(Arrays.equals(baseHint, new String[]{"Welcome to Animal Kingdom!", "I am Sam the Snake."}));
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
}