package com.csse3200.game.minigames.snake.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.minigames.Direction;
import com.csse3200.game.minigames.MinigameRenderable;
import com.csse3200.game.minigames.MinigameRenderer;
import com.csse3200.game.minigames.snake.Snake;
import com.csse3200.game.minigames.snake.SnakeGrid;

/**
 * Renders the snake and its segments on the grid in the Snake mini-game.
 */
public class SnakeRenderer implements MinigameRenderable {

    private static final int CELL_SIZE = 20;
    private final Snake snake;
    private final SnakeGrid grid;
    private final Texture snakeTexture;
    private final Texture snakeBodyVerticalTexture;
    private final Texture snakeBodyBentTexture;
    private final Texture snakeTailTexture;
    private final MinigameRenderer renderer;

    /**
     * Creates a new SnakeRenderer.
     *
     * @param snake The snake to render.
     * @param grid The grid the snake is on.
     * @param snakeTexture The texture for the snake's head.
     * @param snakeBodyVerticalTexture The texture for the snake's vertical body segments.
     * @param snakeBodyBentTexture The texture for the snake's bent body segments.
     * @param renderer The renderer used for drawing.
     */
    public SnakeRenderer(Snake snake, SnakeGrid grid, Texture snakeTexture,
                         Texture snakeBodyVerticalTexture,
                         Texture snakeBodyBentTexture,
                         Texture snakeTailTexture, MinigameRenderer renderer) {
        this.snake = snake;
        this.grid = grid;
        this.snakeTexture = snakeTexture;
        this.snakeBodyVerticalTexture = snakeBodyVerticalTexture;
        this.snakeBodyBentTexture = snakeBodyBentTexture;
        this.snakeTailTexture = snakeTailTexture;
        this.renderer = renderer;
    }

    /**
     * Renders the snake, including its head and body segments, with appropriate rotation.
     */
    public void render() {
        // Calculate the top-left corner of the grid, centered in the camera's view
        float gridWidthInWorldUnits = (float) grid.getWidth() * CELL_SIZE;
        float gridHeightInWorldUnits = (float) grid.getHeight() * CELL_SIZE;

        float startX = renderer.getCam().position.x - gridWidthInWorldUnits / 2f;
        float startY = renderer.getCam().position.y - gridHeightInWorldUnits / 2f;

        // Render the snake's head
        renderSnakeHead(startX, startY);

        // Render the snake's body
        renderSnakeBody(startX, startY);
    }

    /**
     * Renders the snake's head with the appropriate rotation.
     */
    private void renderSnakeHead(float startX, float startY) {
        Direction direction = snake.getDirection();
        float rotation = getRotationForDirection(direction);

        renderer.getSb().draw(
                snakeTexture,
                startX + snake.getX() * CELL_SIZE,
                startY + snake.getY() * CELL_SIZE,
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
    private void renderSnakeBody(float startX, float startY) {
        Direction prevDirection = snake.getDirection();
        float segmentX;
        float segmentY;
        Snake.Segment tail = snake.getLastSegment();


        for (Snake.Segment segment : snake.getBodySegments()) {
            Direction currentDirection = segment.direction();
            Texture bodyTexture;
            float rotation;

            segmentX = startX + segment.x() * CELL_SIZE;
            segmentY = startY + segment.y() * CELL_SIZE;

            if (prevDirection != currentDirection && !segment.equals(tail)) {
                bodyTexture = snakeBodyBentTexture;
                rotation = getBentRotation(prevDirection, currentDirection);
            } else if (segment.equals(tail)){
                bodyTexture = snakeTailTexture;
                switch(currentDirection) {
                    case UP -> rotation = 0;
                    case DOWN -> rotation = 180;
                    case LEFT -> rotation = 90;
                    case RIGHT -> rotation = 270;
                    default -> throw new IllegalArgumentException("Unknown tail direction");
                }
            }else {
                bodyTexture = snakeBodyVerticalTexture;
                switch(currentDirection) {
                    case UP -> rotation = 0;
                    case DOWN -> rotation = 180;
                    case LEFT -> rotation = 90;
                    case RIGHT -> rotation = 270;
                    default -> throw new IllegalArgumentException("Unknown body direction");
                }

            }

            renderer.getSb().draw(
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
        return switch (prevDirection) {
            case UP -> (currentDirection == Direction.RIGHT) ? 0f : 270f;
            case RIGHT -> (currentDirection == Direction.DOWN) ? 270f : 180f;
            case DOWN -> (currentDirection == Direction.LEFT) ? 180f : 90f;
            default -> (currentDirection == Direction.UP) ? 90f : 0f;
        };
    }
}
