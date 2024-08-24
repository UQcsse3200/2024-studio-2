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
    private static final NPCConfigs configs =
            FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");

    private String[] textures = {
            "images/ghost.png",
            "images/Cow.png",
            "images/Lion-Spritesheet.png",
            "images/snake.png",
            "images/eagle.png",
            "images/turtle.png"
    };

    private String[] atlas = {
            "images/ghost.atlas",
            "images/Cow.atlas",
            "images/lion.atlas",
            "images/snake.atlas",
            "images/eagle.atlas",
            "images/turtle.atlas"
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
        assert(cow.getClass() == Entity.class);
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
     * Tests that the cow has the correct HP stat.
     */
    @Test
    void TestCowHasCorrectHP() {
        assertEquals(30, cow.getComponent(CombatStatsComponent.class).getHealth(),
                "Cow should have 30 HP.");
    }

    /**
     * Tests that the cow has the correct base attack stat.
     */
    @Test
    void TestCowHasCorrectBaseAttack() {
        assertEquals(0, cow.getComponent(CombatStatsComponent.class).getBaseAttack(),
                "Cow should have 0 Base Attack.");
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

//    /**
//     * Tests that the cow's hint system works by getting current hint level to display hints.
//     */
//    @Test
//    void TestCowGetInitialHintLevel() {
//        CowConfig cowConfig = configs.cow;
//        int hintLevel = cowConfig.getHintLevel();
//        assertEquals(0, hintLevel);
//    }
//
//    /**
//     * Tests that the cow updates its hint when the hint level is incremented.
//     */
//    @Test
//    void TestUpdateCowHint() {
//        CowConfig cowConfig = configs.cow;
//        cowConfig.incrementHintLevel();
//        assertEquals(1, cowConfig.getHintLevel());
//        cowConfig.restartCurrentHint();
//
//    }

//    /**
//     * Tests that the cow hint exists.
//     */
//    @Test
//    void TestCowHintIsNotNull() {
//        assertNotNull(configs.cow.getCurrentHint());
//    }




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
        assert(lion.getClass() == Entity.class);
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
     * Tests that the lion has the correct HP stat.
     */
    @Test
    void TestLionHasCorrectHP() {
        assertEquals(40, lion.getComponent(CombatStatsComponent.class).getHealth(),
                "Lion should have 40 HP.");
    }

    /**
     * Tests that the lion has the correct base attack stat.
     */
    @Test
    void TestLionHasCorrectBaseAttack() {
        assertEquals(0, lion.getComponent(CombatStatsComponent.class).getBaseAttack(),
                "Lion should have 0 Base Attack.");
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

//    /**
//     * Tests that the lion's hint system works by getting current hint level to display hints.
//     */
//    @Test
//    void TestLionGetInitialHintLevel() {
//        LionConfig lionConfig = configs.lion;
//        int hintLevel = lionConfig.getHintLevel();
//        assertEquals(0, hintLevel);
//    }
//
//    /**
//     * Tests that the lion updates its hint when the hint level is incremented.
//     */
//    @Test
//    void TestUpdateLionHint() {
//        configs.lion.incrementHintLevel();
//        assertEquals(1, configs.lion.getHintLevel());
//        configs.lion.restartCurrentHint();
//
//    }
//
//    /**
//     * Tests that the lion hint exists.
//     */
//    @Test
//    void TestLionHintIsNotNull() {
//        assertNotNull(configs.lion.getCurrentHint());
//    }
//



    /**
     * Tests Creation of an eagle.
     */
    @Test
    void TestEagleCreation() {
        assertNotNull(eagle, "Eagle should not be null.");
    }

    /**
     * Tests that the eagle has the correct name.
     */
//    @Test
//    void TestEagleName() {
//        String name = configs.eagle.getAnimalName();
//        assertEquals("Eagle", name);
//    }
//
    /**
     * Tests that the eagle is an Entity.
     */
    @Test
    void TestEagleIsEntity() {
        assert(eagle.getClass() == Entity.class);
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
     * Tests that the eagle has the correct HP stat.
     */
    @Test
    void TestEagleHasCorrectHP() {
        assertEquals(25, eagle.getComponent(CombatStatsComponent.class).getHealth(),
                "Eagle should have 25 HP.");
    }

    /**
     * Tests that the eagle has the correct base attack stat.
     */
    @Test
    void TestEagleHasCorrectBaseAttack() {
        assertEquals(0, eagle.getComponent(CombatStatsComponent.class).getBaseAttack(),
                "Eagle should have 0 Base Attack.");
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
     * Tests that the eagle's hint system works by getting current hint level to display hints.
     */
//    @Test
//    void TestEagleGetInitialHintLevel() {
//        EagleConfig eagleConfig = configs.eagle;
//        int hintLevel = eagleConfig.getHintLevel();
//        assertEquals(0, hintLevel);
//    }
//
    /**
     * Tests that the eagle updates its hint when the hint level is incremented.
     */
//    @Test
//    void TestUpdateEagleHint() {
//        configs.eagle.incrementHintLevel();
//        assertEquals(1, configs.eagle.getHintLevel());
//        configs.eagle.restartCurrentHint();
//
//    }
//
    /**
     * Tests that the eagle hint exists.
     */
//    @Test
//    void TestEagleHintIsNotNull() {
//        assertNotNull(configs.eagle.getCurrentHint());
//    }




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
        assert(turtle.getClass() == Entity.class);
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
     * Tests that the turtle has the correct HP stat.
     */
    @Test
    void TestTurtleHasCorrectHP() {
        assertEquals(20, turtle.getComponent(CombatStatsComponent.class).getHealth(),
                "Turtle should have 20 HP.");
    }

    /**
     * Tests that the turtle has the correct base attack stat.
     */
    @Test
    void TestTurtleHasCorrectBaseAttack() {
        assertEquals(0, turtle.getComponent(CombatStatsComponent.class).getBaseAttack(),
                "Turtle should have 0 Base Attack.");
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

//    /**
//     * Tests that the turtle's hint system works by getting current hint level to display hints.
//     */
//    @Test
//    void TestTurtleGetInitialHintLevel() {
//        TurtleConfig turtleConfig = configs.turtle;
//        int hintLevel = turtleConfig.getHintLevel();
//        assertEquals(0, hintLevel);
//    }
//
//    /**
//     * Tests that the turtle updates its hint when the hint level is incremented.
//     */
//    @Test
//    void TestUpdateTurtleHint() {
//        configs.turtle.incrementHintLevel();
//        assertEquals(1, configs.turtle.getHintLevel());
//        configs.turtle.restartCurrentHint();
//
//    }

//    /**
//     * Tests that the turtle hint exists.
//     */
//    @Test
//    void TestTurtleHintIsNotNull() {
//        assertNotNull(configs.turtle.getCurrentHint());
//    }
//


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
//    @Test
//    void TestSnakeName() {
//        String name = configs.snake.getAnimalName();
//        assertEquals("Snake", name);
//    }

    /**
     * Tests that the snake is an Entity.
     */
    @Test
    void TestSnakeIsEntity() {
        assert(snake.getClass() == Entity.class);
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
     * Tests that the snake has the correct HP stat.
     */
    @Test
    void TestSnakeHasCorrectHP() {
        assertEquals(30, snake.getComponent(CombatStatsComponent.class).getHealth(),
                "Snake should have 30 HP.");
    }

    /**
     * Tests that the snake has the correct base attack stat.
     */
    @Test
    void TestSnakeHasCorrectBaseAttack() {
        assertEquals(0, snake.getComponent(CombatStatsComponent.class).getBaseAttack(),
                "Snake should have 0 Base Attack.");
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
     * Tests that the snake's hint system works by getting current hint level to display hints.
     */
//    @Test
//    void TestSnakeGetInitialHintLevel() {
//        SnakeConfig snakeConfig = configs.snake;
//        int hintLevel = snakeConfig.getHintLevel();
//        assertEquals(0, hintLevel);
//    }
//
    /**
     * Tests that the snake updates its hint when the hint level is incremented.
     */
//    @Test
//    void TestUpdateSnakeHint() {
//        configs.snake.incrementHintLevel();
//        assertEquals(1, configs.snake.getHintLevel());
//        configs.snake.restartCurrentHint();
//
//    }
//
    /**
     * Tests that the snake hint exists.
     */
//    @Test
//    void TestSnakeHintIsNotNull() {
//        assertNotNull(configs.snake.getCurrentHint());
//    }

    static class TestComponent1 extends Component {}

}