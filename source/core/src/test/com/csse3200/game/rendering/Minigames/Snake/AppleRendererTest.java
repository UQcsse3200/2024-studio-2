package com.csse3200.game.rendering.Minigames.Snake;

import com.csse3200.game.components.minigame.snake.Apple;
import com.csse3200.game.components.minigame.snake.SnakeGrid;
import com.csse3200.game.components.minigame.snake.rendering.AppleRenderer;
import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.minigame.MinigameRenderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class AppleRendererTest {

    private AppleRenderer renderer;
    private static final int CELL_SIZE = 20;  // the default value for CELL_SIZE

    @BeforeEach
    public void setUp() {
        // Mock dependencies for the AppleRenderer
        Apple apple = mock(Apple.class);
        SnakeGrid grid = mock(SnakeGrid.class);
        Texture appleTexture = mock(Texture.class);
        MinigameRenderer minigameRenderer = mock(MinigameRenderer.class);

        // Initialising the AppleRenderer with mocked dependencies
        renderer = new AppleRenderer(
                apple, grid, appleTexture, minigameRenderer
        );
    }

    @Test
    public void testCellSize() throws Exception {
        Field cellSizeField = AppleRenderer.class.getDeclaredField("CELL_SIZE");
        cellSizeField.setAccessible(true);
        int cellSize = (int) cellSizeField.get(null);  // Since CELL_SIZE is static

        // Assert that the expected value matches the actual CELL_SIZE
        assertEquals(CELL_SIZE, cellSize);
    }
}
