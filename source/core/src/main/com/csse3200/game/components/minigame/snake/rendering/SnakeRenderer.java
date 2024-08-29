package com.csse3200.game.components.minigame.snake.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csse3200.game.components.minigame.Direction;
import com.csse3200.game.components.minigame.snake.Snake;
import com.csse3200.game.components.minigame.snake.SnakeGrid;

/**
 * Renders the snake and its segments on the grid in the Snake mini-game.
 */
public class SnakeRenderer {

    private static final int CELL_SIZE = 55;
    private final Snake snake;
    private final SnakeGrid grid;
    private final Texture snakeTexture;
    private final Texture snakeBodyHorizontalTexture;
    private final Texture snakeBodyVerticalTexture;
    private final Texture snakeBodyBentTexture;
    private final SpriteBatch spriteBatch;

    /**
     * Creates a new SnakeRenderer.
     *
     * @param snake The snake to render.
     * @param grid The grid the snake is on.
     * @param snakeTexture The texture for the snake's head.
     * @param snakeBodyHorizontalTexture The texture for the snake's horizontal body segments.
     * @param snakeBodyVerticalTexture The texture for the snake's vertical body segments.
     * @param snakeBodyBentTexture The texture for the snake's bent body segments.
     * @param spriteBatch The SpriteBatch used for drawing.
     */
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

    /**
     * Renders the snake, including its head and body segments, with appropriate rotation.
     */
    public void renderSnake() {
        int gridWidthInPixels = grid.getWidth() * CELL_SIZE;
        int gridHeightInPixels = grid.getHeight() * CELL_SIZE;

        float offsetX = (Gdx.graphics.getWidth() - gridWidthInPixels) / 2f;
        float offsetY = (Gdx.graphics.getHeight() - gridHeightInPixels) / 2f;

        // Render the snake's head
        renderSnakeHead(offsetX, offsetY);

        // Render the snake's body
        renderSnakeBody(offsetX, offsetY);
    }

    /**
     * Renders the snake's head with the appropriate rotation.
     */
    private void renderSnakeHead(float offsetX, float offsetY) {
        Direction direction = snake.getDirection();
        float rotation = getRotationForDirection(direction);

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
    }

    /**
     * Renders the snake's body segments with the appropriate textures and rotations.
     */
    private void renderSnakeBody(float offsetX, float offsetY) {
        Direction prevDirection = snake.getDirection();
        float segmentX, segmentY;
        Snake.Segment lastSegment = snake.getLastSegment();

        for (Snake.Segment segment : snake.getBodySegments()) {
            Direction currentDirection = segment.direction();
            Texture bodyTexture;
            float rotation = 0f;

            segmentX = offsetX + segment.x() * CELL_SIZE;
            segmentY = offsetY + segment.y() * CELL_SIZE;

            if (prevDirection != currentDirection && !segment.equals(lastSegment)) {
                bodyTexture = snakeBodyBentTexture;
                rotation = getBentRotation(prevDirection, currentDirection);
            } else {
                bodyTexture = (currentDirection == Direction.LEFT || currentDirection == Direction.RIGHT)
                        ? snakeBodyHorizontalTexture
                        : snakeBodyVerticalTexture;
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

    /**
     * Returns the rotation angle for the snake's head based on its direction.
     *
     * @param direction The direction the snake is facing.
     * @return The rotation angle in degrees.
     */
    private float getRotationForDirection(Direction direction) {
        return switch (direction) {
            case UP -> 180f;
            case DOWN -> 0f;
            case LEFT -> 270f;
            case RIGHT -> 90f;
            default -> 0f;
        };
    }

    /**
     * Returns the rotation angle for bent segments based on the previous and current directions.
     *
     * @param prevDirection The previous direction of the snake.
     * @param currentDirection The current direction of the snake.
     * @return The rotation angle in degrees.
     */
    private float getBentRotation(Direction prevDirection, Direction currentDirection) {
        if (prevDirection == Direction.UP) {
            return (currentDirection == Direction.RIGHT) ? 0f : 270f;
        } else if (prevDirection == Direction.RIGHT) {
            return (currentDirection == Direction.DOWN) ? 270f : 180f;
        } else if (prevDirection == Direction.DOWN) {
            return (currentDirection == Direction.LEFT) ? 180f : 90f;
        } else {
            return (currentDirection == Direction.UP) ? 90f : 0f;
        }
    }
}
