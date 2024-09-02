package com.csse3200.game.components.minigame.snake;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csse3200.game.components.minigame.MinigameRenderer;
import com.csse3200.game.components.minigame.snake.SnakeGrid;

import com.csse3200.game.components.minigame.snake.rendering.GridRenderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class GridRendererTest {

    @Mock
    private SnakeGrid grid;

    @Mock
    private Texture grassTexture;

    @Mock
    private MinigameRenderer renderer;

    @Mock
    private SpriteBatch spriteBatch;

    @Spy
    private OrthographicCamera camera = new OrthographicCamera();

    @InjectMocks
    private GridRenderer gridRenderer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(renderer.getSb()).thenReturn(spriteBatch);
        when(renderer.getCam()).thenReturn(camera);

        // Set up the camera's position
        camera.position.set(0, 0, 0);
    }

    @Test
    public void testRenderGrid() {
        // Arrange the grid Width and height also the cellSize
        int gridWidth = 10;
        int gridHeight = 15;
        float cellSize = 20.0f;

        when(grid.getWidth()).thenReturn(gridWidth);
        when(grid.getHeight()).thenReturn(gridHeight);

        // Sets the camera position
        camera.position.set(gridWidth * cellSize / 2.0f, gridHeight * cellSize / 2.0f, 0);

        // Acts
        gridRenderer.render();

        // Asserts
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                verify(spriteBatch).draw(eq(grassTexture),
                        eq(x * cellSize + (camera.position.x - gridWidth * cellSize / 2.0f)),
                        eq(y * cellSize + (camera.position.y - gridHeight * cellSize / 2.0f)),
                        eq(cellSize),
                        eq(cellSize));
            }
        }
    }
}
