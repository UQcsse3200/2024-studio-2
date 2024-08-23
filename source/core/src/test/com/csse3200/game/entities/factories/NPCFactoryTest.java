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

    @Test
    void TestCowCreation() {
        assertNotNull(cow, "Cow should not be null.");
    }

    @Test
    void TestCowName() {
        String name = configs.cow.getAnimalName();
        assertEquals("Cow", name);
    }

    @Test
    void TestCowIsEntity() {
        assert(cow.getClass() == Entity.class);
    }

    @Test
    void TestCowHasPhysicsComponent() {
        assertNotNull(cow.getComponent(PhysicsComponent.class));
    }

    @Test
    void TestCowHasPhysicsMovementComponent() {
        assertNotNull(cow.getComponent(PhysicsMovementComponent.class));
    }

    @Test
    void TestCowHasColliderComponent() {
        assertNotNull(cow.getComponent(ColliderComponent.class));
    }
    @Test
    void TestCowHasConfigComponent() {
        assertNotNull(cow.getComponent(ConfigComponent.class));
    }

    @Test
    void TestCowHasCorrectHP() {
        assertEquals(30, cow.getComponent(CombatStatsComponent.class).getHealth(),
                "Cow should have 30 HP.");
    }

    @Test
    void TestCowHasCorrectBaseAttack() {
        assertEquals(0, cow.getComponent(CombatStatsComponent.class).getBaseAttack(),
                "Cow should have 0 Base Attack.");
    }

    @Test
    void TestCowHasAnimation() {
        assertTrue(cow.getComponent(AnimationRenderComponent.class).hasAnimation("float") ,
                "Cow should have idle animation.");
    }

    @Test
    void TestCowIsFriendly() {
        assertNotNull(cow.getComponent(FriendlyNPCAnimationController.class),
                "Cow should have a friendly AI controller.");
    }

    @Test
    void TestCowSetPosition() {
        Vector2 pos = new Vector2(0f, 0f);
        cow.setPosition(pos);

        assertEquals(pos, cow.getPosition());
    }

    @Test
    void TestCowGetInitialHintLevel() {
        CowConfig cowConfig = configs.cow;
        int hintLevel = cowConfig.getHintLevel();
        assertEquals(0, hintLevel);
    }

    @Test
    void TestUpdateCowHint() {
        CowConfig cowConfig = configs.cow;
        cowConfig.incrementHintLevel();
        assertEquals(1, cowConfig.getHintLevel());
        cowConfig.restartCurrentHint();

    }

    @Test
    void TestCowHintIsNotNull() {
        assertNotNull(configs.cow.getCurrentHint());
    }





    @Test
    void TestLionCreation() {
        assertNotNull(lion, "Lion should not be null.");
    }

    @Test
    void TestLionName() {
        String name = configs.lion.getAnimalName();
        assertEquals("Lion", name);
    }

    @Test
    void TestLionIsEntity() {
        assert(lion.getClass() == Entity.class);
    }

    @Test
    void TestLionHasPhysicsComponent() {
        assertNotNull(lion.getComponent(PhysicsComponent.class));
    }

    @Test
    void TestLionHasPhysicsMovementComponent() {
        assertNotNull(lion.getComponent(PhysicsMovementComponent.class));
    }

    @Test
    void TestLionHasColliderComponent() {
        assertNotNull(lion.getComponent(ColliderComponent.class));
    }

    @Test
    void TestLionHasConfigComponent() {
        assertNotNull(lion.getComponent(ConfigComponent.class));
    }

    @Test
    void TestLionHasCorrectHP() {
        assertEquals(40, lion.getComponent(CombatStatsComponent.class).getHealth(),
                "Lion should have 40 HP.");
    }

    @Test
    void TestLionHasCorrectBaseAttack() {
        assertEquals(0, lion.getComponent(CombatStatsComponent.class).getBaseAttack(),
                "Lion should have 0 Base Attack.");
    }

    @Test
    void TestLionHasIdleAnimation() {
        assertTrue(lion.getComponent(AnimationRenderComponent.class).hasAnimation("float"),
                "Lion should have idle animation.");
    }

    @Test
    void TestLionIsFriendly() {
        assertNotNull(lion.getComponent(FriendlyNPCAnimationController.class),
                "Lion should have a friendly AI controller.");
    }

    @Test
    void TestLionSetPosition() {
        Vector2 pos = new Vector2(0f, 0f);
        lion.setPosition(pos);

        assertEquals(pos, lion.getPosition());
    }

    @Test
    void TestLionGetInitialHintLevel() {
        LionConfig lionConfig = configs.lion;
        int hintLevel = lionConfig.getHintLevel();
        assertEquals(0, hintLevel);
    }

    @Test
    void TestUpdateLionHint() {
        configs.lion.incrementHintLevel();
        assertEquals(1, configs.lion.getHintLevel());
        configs.lion.restartCurrentHint();

    }

    @Test
    void TestLionHintIsNotNull() {
        assertNotNull(configs.lion.getCurrentHint());
    }





    @Test
    void TestEagleCreation() {
        assertNotNull(eagle, "Eagle should not be null.");
    }

//    @Test
//    void TestEagleName() {
//        String name = configs.eagle.getAnimalName();
//        assertEquals("Eagle", name);
//    }
//
    @Test
    void TestEagleIsEntity() {
        assert(eagle.getClass() == Entity.class);
    }

    @Test
    void TestEagleHasPhysicsComponent() {
        assertNotNull(eagle.getComponent(PhysicsComponent.class));
    }

    @Test
    void TestEagleHasPhysicsMovementComponent() {
        assertNotNull(eagle.getComponent(PhysicsMovementComponent.class));
    }

    @Test
    void TestEagleHasColliderComponent() {
        assertNotNull(eagle.getComponent(ColliderComponent.class));
    }


    @Test
    void TestEagleHasConfigComponent() {
        assertNotNull(eagle.getComponent(ConfigComponent.class));
    }

    @Test
    void TestEagleHasCorrectHP() {
        assertEquals(25, eagle.getComponent(CombatStatsComponent.class).getHealth(),
                "Eagle should have 25 HP.");
    }

    @Test
    void TestEagleHasCorrectBaseAttack() {
        assertEquals(0, eagle.getComponent(CombatStatsComponent.class).getBaseAttack(),
                "Eagle should have 0 Base Attack.");
    }

    @Test
    void TestEagleHasIdleAnimation() {
        assertTrue(eagle.getComponent(AnimationRenderComponent.class).hasAnimation("float"),
                "Eagle should have idle animation.");
    }

    @Test
    void TestEagleIsFriendly() {
        assertNotNull(eagle.getComponent(FriendlyNPCAnimationController.class),
                "Eagle should have a friendly AI controller.");
    }

    @Test
    void TestEagleSetPosition() {
        Vector2 pos = new Vector2(0f, 0f);
        eagle.setPosition(pos);

        assertEquals(pos, eagle.getPosition());
    }


//    @Test
//    void TestEagleGetInitialHintLevel() {
//        EagleConfig eagleConfig = configs.eagle;
//        int hintLevel = eagleConfig.getHintLevel();
//        assertEquals(0, hintLevel);
//    }
//
//    @Test
//    void TestUpdateEagleHint() {
//        configs.eagle.incrementHintLevel();
//        assertEquals(1, configs.eagle.getHintLevel());
//        configs.eagle.restartCurrentHint();
//
//    }
//
//    @Test
//    void TestEagleHintIsNotNull() {
//        assertNotNull(configs.eagle.getCurrentHint());
//    }



    @Test
    void TestTurtleCreation() {
        assertNotNull(turtle, "Turtle should not be null.");
    }

    @Test
    void TestTurtleName() {
        String name = configs.turtle.getAnimalName();
        assertEquals("Turtle", name);
    }

    @Test
    void TestTurtleIsEntity() {
        assert(turtle.getClass() == Entity.class);
    }

    @Test
    void TestTurtleHasPhysicsComponent() {
        assertNotNull(turtle.getComponent(PhysicsComponent.class));
    }

    @Test
    void TestTurtleHasPhysicsMovementComponent() {
        assertNotNull(turtle.getComponent(PhysicsMovementComponent.class));
    }

    @Test
    void TestTurtleHasColliderComponent() {
        assertNotNull(turtle.getComponent(ColliderComponent.class));
    }

    @Test
    void TestTurtleHasConfigComponent() {
        assertNotNull(turtle.getComponent(ConfigComponent.class));
    }

    @Test
    void TestTurtleHasCorrectHP() {
        assertEquals(20, turtle.getComponent(CombatStatsComponent.class).getHealth(),
                "Turtle should have 20 HP.");
    }

    @Test
    void TestTurtleHasCorrectBaseAttack() {
        assertEquals(0, turtle.getComponent(CombatStatsComponent.class).getBaseAttack(),
                "Turtle should have 0 Base Attack.");
    }

    @Test
    void TestTurtleHasIdleAnimation() {
        assertTrue(turtle.getComponent(AnimationRenderComponent.class).hasAnimation("float"),
                "Turtle should have idle animation.");
    }

    @Test
    void TestTurtleIsFriendly() {
        assertNotNull(turtle.getComponent(FriendlyNPCAnimationController.class),
                "Turtle should have a friendly AI controller.");
    }

    @Test
    void TestTurtleSetPosition() {
        Vector2 pos = new Vector2(0f, 0f);
        turtle.setPosition(pos);

        assertEquals(pos, turtle.getPosition());
    }


    @Test
    void TestTurtleGetInitialHintLevel() {
        TurtleConfig turtleConfig = configs.turtle;
        int hintLevel = turtleConfig.getHintLevel();
        assertEquals(0, hintLevel);
    }

    @Test
    void TestUpdateTurtleHint() {
        configs.turtle.incrementHintLevel();
        assertEquals(1, configs.turtle.getHintLevel());
        configs.turtle.restartCurrentHint();

    }

    @Test
    void TestTurtleHintIsNotNull() {
        assertNotNull(configs.turtle.getCurrentHint());
    }




    @Test
    void TestSnakeCreation() {
        assertNotNull(snake, "Snake should not be null.");
    }

//    @Test
//    void TestSnakeName() {
//        String name = configs.snake.getAnimalName();
//        assertEquals("Snake", name);
//    }

    @Test
    void TestSnakeIsEntity() {
        assert(snake.getClass() == Entity.class);
    }

    @Test
    void TestSnakeHasPhysicsComponent() {
        assertNotNull(snake.getComponent(PhysicsComponent.class));
    }

    @Test
    void TestSnakeHasPhysicsMovementComponent() {
        assertNotNull(snake.getComponent(PhysicsMovementComponent.class));
    }

    @Test
    void TestSnakeHasColliderComponent() {
        assertNotNull(snake.getComponent(ColliderComponent.class));
    }

    @Test
    void TestSnakeHasConfigComponent() {
        assertNotNull(snake.getComponent(ConfigComponent.class));
    }

    @Test
    void TestSnakeHasCorrectHP() {
        assertEquals(30, snake.getComponent(CombatStatsComponent.class).getHealth(),
                "Snake should have 30 HP.");
    }

    @Test
    void TestSnakeHasCorrectBaseAttack() {
        assertEquals(0, snake.getComponent(CombatStatsComponent.class).getBaseAttack(),
                "Snake should have 0 Base Attack.");
    }

    @Test
    void TestSnakeHasIdleAnimation() {
        assertTrue(snake.getComponent(AnimationRenderComponent.class).hasAnimation("float"),
                "Snake should have idle animation.");
    }

    @Test
    void TestSnakeIsFriendly() {
        assertNotNull(snake.getComponent(FriendlyNPCAnimationController.class),
                "Snake should have a friendly AI controller.");
    }

    @Test
    void TestSnakeSetPosition() {
        Vector2 pos = new Vector2(0f, 0f);
        snake.setPosition(pos);

        assertEquals(pos, snake.getPosition());
    }

//    @Test
//    void TestSnakeGetInitialHintLevel() {
//        SnakeConfig snakeConfig = configs.snake;
//        int hintLevel = snakeConfig.getHintLevel();
//        assertEquals(0, hintLevel);
//    }
//
//    @Test
//    void TestUpdateSnakeHint() {
//        configs.snake.incrementHintLevel();
//        assertEquals(1, configs.snake.getHintLevel());
//        configs.snake.restartCurrentHint();
//
//    }
//
//    @Test
//    void TestSnakeHintIsNotNull() {
//        assertNotNull(configs.snake.getCurrentHint());
//    }

    static class TestComponent1 extends Component {}

}