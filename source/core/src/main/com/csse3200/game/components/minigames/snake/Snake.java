package com.csse3200.game.components.minigame.snake;

import com.csse3200.game.components.minigame.Direction;
import com.csse3200.game.components.minigame.Grid;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents the snake in the Snake mini-game, managing its position, movement, and growth.
 */
public class Snake {
    private int x;
    private int y;
    private final Grid grid;
    private final Deque<Segment> snakeBody;
    private int length;
    private Direction direction;
    private final Deque<Direction> nextDirection;
    private final float movePeriod;
    private float moveTimer;

    /**
     * Initialises a new Snake with specified parameters.
     *
     * @param grid The grid on which the snake moves.
     * @param startX The starting x-coordinate of the snake's head.
     * @param startY The starting y-coordinate of the snake's head.
     * @param startDirection The initial direction the snake is facing.
     * @param startLength The initial length of the snake.
     * @param movePeriod The time interval between moves.
     */
    public Snake(Grid grid, int startX, int startY, Direction startDirection, int startLength,
                 float movePeriod) {
        this.grid = grid;
        this.movePeriod = movePeriod;
        this.moveTimer = movePeriod;
        this.direction = startDirection;
        this.nextDirection = new ArrayDeque<>();
        this.length = startLength;
        x = startX;
        y = startY;
        snakeBody = new ArrayDeque<>();
    }

    /**
     * Sets the direction the snake is moving
     * @param direction the direction to set to
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Returns the current direction of the snake's head.
     *
     * @return The direction of the snake's head.
     */
    public Direction getDirection() {return this.direction;}

    /**
     * Update snake direction based on an input direction.
     * If the input direction is reversed 180 degrees from the current
     * direction of the snake, it will not change.
     * @param direction the direction to update to
     */
    public void updateDirectionOnInput(Direction direction) {
        Direction latestDirection;
        if (nextDirection.size() > 1) {
            return;
        } else if (nextDirection.isEmpty()) {
            latestDirection = this.direction;
        } else {
            latestDirection = nextDirection.peekLast();
        }
        if (direction == Direction.UP && latestDirection != Direction.DOWN) {
            this.nextDirection.add(direction);
        }
        if (direction == Direction.DOWN && latestDirection != Direction.UP) {
            this.nextDirection.add(direction);
        }
        if (direction == Direction.LEFT && latestDirection != Direction.RIGHT) {
            this.nextDirection.add(direction);
        }
        if (direction == Direction.RIGHT && latestDirection != Direction.LEFT) {
            this.nextDirection.add(direction);
        }
    }

    /**
     * Moves the snake in a direction
     * @param direction the direction to move in
     */
    public void move(Direction direction) {
        snakeBody.add(new Segment(x, y, this.direction));
        switch (direction) {
            case RIGHT -> this.x += 1;
            case LEFT -> this.x -= 1;
            case UP -> this.y += 1;
            case DOWN -> this.y -= 1;
            default -> {}
        }
        if (snakeBody.size() >= length) {
            Segment removed = snakeBody.removeFirst();
            grid.setOccupied(removed.x(), removed.y(), false);
        }
        grid.setOccupied(x, y, true);
    }

    /**
     * Grows the snake (increase its length by 1)
     */
    void grow() {
        length += 1;
    }

    /**
     * Returns current snake length. Used only for testing.
     */
    public int getLength() {
        return length;
    }

    /**
     * Update the position of the snake after a given amount of time.
     * @param dt time since the last update
     */
    public void update(float dt) {
        moveTimer -= dt;
        if (moveTimer <= 0) {
            if (!nextDirection.isEmpty()) {
                direction = nextDirection.removeFirst();
            }
            move(direction);
            moveTimer += movePeriod;
        }
    }

    /**
     * @return the x co-ordinate of the head
     */
    public int getX() {
        return x;
    }

    /**
     * @return the y coordinate of the head
     */
    public int getY() {
        return y;
    }

    /**
     * @return a list of the snake's body segments
     */
    public List<Segment> getBodySegments() {
        return new ArrayList<>(snakeBody);
    }

    /**
     * @return the last snake body segment or null if none exist
     */
    public Segment getLastSegment() {
        if (snakeBody.isEmpty()) {
            return null; // Return null if no segments exist
        }
        return snakeBody.getFirst();
    }

    /**
     * Record that represents a segment of the snake's body
     */
    public record Segment(int x, int y, Direction direction) {

            /**
             * @return the x-coordinate of the snake body segment
             */
            @Override
            public int x() {
                return x;
            }

            /**
             * @return the y-coordinate of the snake body segment
             */
            @Override
            public int y() {
                return y;
            }

            /**
             * @return the direction of the snake body segment
             */
            @Override
            public Direction direction() {
                return this.direction;
            }

        /**
         * A function to check if segments are equal
         * @param o   the reference object with which to compare.
         * @return true if equal, otherwise false.
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true; // Check if the same object
            if (o == null || getClass() != o.getClass()) return false; // Check for null and class type

            Segment segment = (Segment) o;

            // Compare x, y, and direction fields
            if (x != segment.x) return false;
            if (y != segment.y) return false;
            return direction == segment.direction;
        }
    }
}
