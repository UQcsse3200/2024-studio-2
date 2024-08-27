package com.csse3200.game.components.minigame.snake.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csse3200.game.components.minigame.Direction;
import com.csse3200.game.components.minigame.snake.Snake;
import com.csse3200.game.components.minigame.snake.SnakeGrid;

public class SnakeRenderer {

    private static final int CELL_SIZE = 55;
    private final Snake snake;
    private final SnakeGrid grid;
    private final Texture snakeTexture;
    private final Texture snakeBodyHorizontalTexture;
    private final Texture snakeBodyVerticalTexture;
    private final Texture snakeBodyBentTexture;
    private final SpriteBatch spriteBatch;

    public SnakeRenderer(Snake snake, SnakeGrid grid, Texture snakeTexture,
                         Texture snakeBodyHorizontalTexture, Texture snakeBodyVerticalTexture,
                         Texture snakeBodyBentTexture, SpriteBatch spriteBatch) {
        this.snake = snake;
        this.grid = grid;
        this.snakeTexture = snakeTexture;
        this.snakeBodyHorizontalTexture = snakeBodyHorizontalTexture;
        this.snakeBodyVerticalTexture = snakeBodyVerticalTexture;
        this.snakeBodyBentTexture = snakeBodyBentTexture;
        this.spriteBatch = spriteBatch;
    }

    public void renderSnake() {
        int gridWidthInPixels = grid.getWidth() * CELL_SIZE;
        int gridHeightInPixels = grid.getHeight() * CELL_SIZE;

        float offsetX = (Gdx.graphics.getWidth() - gridWidthInPixels) / 2f;
        float offsetY = (Gdx.graphics.getHeight() - gridHeightInPixels) / 2f;

        // Get the current direction of the snake
        Direction direction = snake.getDirection();
        float rotation = 0f;

        switch (direction) {
            case UP -> rotation = 180f;
            case DOWN -> rotation = 0f;
            case LEFT -> rotation = 270f;
            case RIGHT -> rotation = 90f;
        }

        // Render snake head with rotation
        spriteBatch.draw(
                snakeTexture,
                offsetX + snake.getX() * CELL_SIZE,
                offsetY + snake.getY() * CELL_SIZE,
                CELL_SIZE / 2f,
                CELL_SIZE / 2f,
                CELL_SIZE,
                CELL_SIZE,
                1f,
                1f,
                rotation,
                0,
                0,
                snakeTexture.getWidth(),
                snakeTexture.getHeight(),
                false,
                false
        );

        // Render snake body
        Direction prevDirection = direction;
        float segmentX, segmentY;
        Snake.Segment lastSegment = snake.getLastSegment();

        for (Snake.Segment segment : snake.getBodySegments()) {

            Direction currentDirection = segment.direction();
            Texture bodyTexture;
            rotation = 0f;

            segmentX = offsetX + segment.x() * CELL_SIZE;
            segmentY = offsetY + segment.y() * CELL_SIZE;

            if ((prevDirection != currentDirection && !segment.equals(lastSegment))) {
                bodyTexture = snakeBodyBentTexture;

                // Simplified rotation logic
                if (prevDirection == Direction.UP) {
                    rotation = (currentDirection == Direction.RIGHT) ? 0f : 270f;
                } else if (prevDirection == Direction.RIGHT) {
                    rotation = (currentDirection == Direction.DOWN) ? 270f : 180f;
                } else if (prevDirection == Direction.DOWN) {
                    rotation = (currentDirection == Direction.LEFT) ? 180f : 90f;
                } else if (prevDirection == Direction.LEFT) {
                    rotation = (currentDirection == Direction.UP) ? 90f : 0f;
                }
            } else {
                // Handle straight segments
                if (currentDirection == Direction.LEFT || currentDirection == Direction.RIGHT) {
                    bodyTexture = snakeBodyHorizontalTexture;
                } else {
                    bodyTexture = snakeBodyVerticalTexture;
                }
            }

            spriteBatch.draw(
                    bodyTexture,
                    segmentX,
                    segmentY,
                    CELL_SIZE / 2f,
                    CELL_SIZE / 2f,
                    CELL_SIZE,
                    CELL_SIZE,
                    1f,
                    1f,
                    rotation,
                    0,
                    0,
                    bodyTexture.getWidth(),
                    bodyTexture.getHeight(),
                    false,
                    false
            );
            prevDirection = currentDirection;
        }
    }
}
