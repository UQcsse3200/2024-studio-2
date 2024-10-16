package com.csse3200.game.rendering.Minigames.Snake;

import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.minigames.Direction;
import com.csse3200.game.minigames.snake.Snake;
import com.csse3200.game.minigames.snake.SnakeGrid;
import com.csse3200.game.minigames.snake.rendering.SnakeRenderer;
import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.minigames.MinigameRenderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@ExtendWith(GameExtension.class)
class SnakeRendererTest {

    private SnakeRenderer renderer;
    private static final int CELL_SIZE = 20;  // the default value for CELL_SIZE

    @BeforeEach
    void setUp() {
        // Mock dependencies for the SnakeRenderer
        Snake snake = mock(Snake.class);
        SnakeGrid grid = mock(SnakeGrid.class);
        Texture snakeTexture = mock(Texture.class);
        Texture snakeBodyHorizontalTexture = mock(Texture.class);
        Texture snakeBodyVerticalTexture = mock(Texture.class);
        Texture snakeBodyBentTexture = mock(Texture.class);
        Texture snakeTailTexture = mock(Texture.class);
        MinigameRenderer minigameRenderer = mock(MinigameRenderer.class);

        // Initialising the SnakeRenderer with mocked dependencies
        renderer = new SnakeRenderer(
                snake, grid, snakeTexture,
                snakeBodyVerticalTexture,
                snakeBodyBentTexture,
                snakeTailTexture,
                minigameRenderer
        );
    }

    @Test
    void testCellSize() throws Exception {
        Field cellSizeField = SnakeRenderer.class.getDeclaredField("CELL_SIZE");
        cellSizeField.setAccessible(true);
        int cellSize = (int) cellSizeField.get(null);  // Since CELL_SIZE is static

        // Assert that the expected value matches the actual CELL_SIZE
        assertEquals(CELL_SIZE, cellSize);
    }

    @Test
    void testGetRotationForDirection() throws Exception {
        Method method = SnakeRenderer.class.getDeclaredMethod("getRotationForDirection", Direction.class);
        method.setAccessible(true);

        // Invoking the methods to get the rotational values (up, down, left and right)
        float rotationUp = (float) method.invoke(renderer, Direction.UP);
        float rotationDown = (float) method.invoke(renderer, Direction.DOWN);
        float rotationLeft = (float) method.invoke(renderer, Direction.LEFT);
        float rotationRight = (float) method.invoke(renderer, Direction.RIGHT);

        // value for the Direction.UP
        float expectedRotationUp = 180.0f;
        // value for the Direction.DOWN
        float expectedRotationDown = 0.0f;
        // value for the Direction.LEFT
        float expectedRotationLeft = 270.0f;
        // value for the Direction.RIGHT
        float expectedRotationRight = 90.0f;

        // Assert that the expected value matches the rotations
        assertEquals(expectedRotationUp, rotationUp, 0.01);
        assertEquals(expectedRotationDown, rotationDown, 0.01);
        assertEquals(expectedRotationLeft, rotationLeft, 0.01);
        assertEquals(expectedRotationRight, rotationRight, 0.01);
    }

    @Test
    void testGetBentRotation() throws Exception {
        Method method = SnakeRenderer.class.getDeclaredMethod("getBentRotation", Direction.class, Direction.class);
        method.setAccessible(true);

        // Invoking the methods to get the bent rotational values
        float rotation1 = (float) method.invoke(renderer, Direction.UP, Direction.RIGHT);
        float rotation2 = (float) method.invoke(renderer, Direction.RIGHT, Direction.DOWN);
        float rotation3 = (float) method.invoke(renderer, Direction.DOWN, Direction.LEFT);
        float rotation4 = (float) method.invoke(renderer, Direction.LEFT, Direction.UP);

        // Log actual rotation values for the debugging purposes
        System.out.println("Rotation 1: " + rotation1);
        System.out.println("Rotation 2: " + rotation2);
        System.out.println("Rotation 3: " + rotation3);
        System.out.println("Rotation 4: " + rotation4);

        // Defining the expected values based on the behavior of getBentRotation
        float expectedRotation1 = 0.0f;   // From UP to RIGHT
        float expectedRotation2 = 270.0f;  // From RIGHT to DOWN
        float expectedRotation3 = 180.0f;  // From DOWN to LEFT
        float expectedRotation4 = 90.0f;  // From LEFT to UP

        // Assert that the expected value matches the bent rotations
        assertEquals(expectedRotation1, rotation1, 0.01);
        assertEquals(expectedRotation2, rotation2, 0.01);
        assertEquals(expectedRotation3, rotation3, 0.01);
        assertEquals(expectedRotation4, rotation4, 0.01);
    }
}

