package com.csse3200.game.entities.factories;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.npc.FriendlyNPCAnimationController;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
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
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class NPCFactoryTest {

    private Entity lion;
    private Entity cow;
    private Entity eagle;
    private Entity turtle;
    private Entity snake;

    private String[] textures = {
            "images/ghost.png"
    };

    private String[] atlas = {
            "images/ghost.atlas"
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
        cow = NPCFactory.createCow(player);
        lion = NPCFactory.createLion(player);
//        eagle = NPCFactory.createEagle(player);
//        turtle = NPCFactory.createTurtle(player);
//        snake = NPCFactory.createSnake(player);
    }

    @Test
    void TestCowCreation() {
        assertNotNull(cow, "Cow should not be null.");
    }

    @Test
    void TestCowIsEntity() {
        assert(cow.getClass() == Entity.class);
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
        assertNotNull(cow.getComponent(AnimationRenderComponent.class),
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
    void TestLionCreation() {
        assertNotNull(lion, "Lion should not be null.");
    }

    @Test
    void TestLionIsEntity() {
        assert(lion.getClass() == Entity.class);
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
    void TestLionHasAnimation() {
        assertNotNull(lion.getComponent(AnimationRenderComponent.class),
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
    void TestEagleCreation() {
        assertNotNull(eagle, "Eagle should not be null.");
    }

    @Test
    void TestEagleIsEntity() {
        assert(eagle.getClass() == Entity.class);
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
    void TestEagleHasAnimation() {
        assertNotNull(eagle.getComponent(AnimationRenderComponent.class),
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

    @Test
    void TestTurtleCreation() {
        assertNotNull(turtle, "Turtle should not be null.");
    }

    @Test
    void TestTurtleIsEntity() {
        assert(turtle.getClass() == Entity.class);
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
    void TestTurtleHasAnimation() {
        assertNotNull(turtle.getComponent(AnimationRenderComponent.class),
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
    void TestSnakeCreation() {
        assertNotNull(snake, "Snake should not be null.");
    }

    @Test
    void TestSnakeIsEntity() {
        assert(snake.getClass() == Entity.class);
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
    void TestSnakeHasAnimation() {
        assertNotNull(snake.getComponent(AnimationRenderComponent.class),
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

    static class TestComponent1 extends Component {}

}