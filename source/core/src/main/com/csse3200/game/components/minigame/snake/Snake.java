package com.csse3200.game.components.minigame.snake;

import com.csse3200.game.components.minigame.Direction;
import com.csse3200.game.components.minigame.Grid;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.List;
import java.util.ArrayList;

/*
 * Class for the snake in the snake game
 */
public class Snake {
    private int x;
    private int y;
    private final Grid grid;
    private final Deque<Segment> snakeBody;
    private int length = 1;
    private Direction direction;
    private Direction nextDirection;
    private final float movePeriod;
    private float moveTimer;

    public Snake(Grid grid, int startX, int startY, Direction startDirection, int startLength, float movePeriod) {
        this.grid = grid;
        this.movePeriod = movePeriod;
        this.moveTimer = movePeriod;
        this.direction = startDirection;
        this.nextDirection = startDirection;
        this.length = startLength;
        x = startX;
        y = startY;
        snakeBody = new ArrayDeque<>();
    }

    /*
     * Sets the direction the snake is moving
     * @param direction: the direction to set to 
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /*
     * Get the current direction of the snake
     */
    public Direction getDirection() {return this.direction;}

    /*
     * Update snake direction with checks
     * @param direction: the direction to update to
     */
    public void updateDirectionOnInput(Direction direction) {
        if (direction == Direction.UP && this.direction != Direction.DOWN) {
            this.nextDirection = direction;
        }
        if (direction == Direction.DOWN && this.direction != Direction.UP) {
            this.nextDirection = direction;
        }
        if (direction == Direction.LEFT && this.direction != Direction.RIGHT) {
            this.nextDirection = direction;
        }
        if (direction == Direction.RIGHT && this.direction != Direction.LEFT) {
            this.nextDirection = direction;
        }
    }

    /*
     * moves the snake in a direction
     */
    public void move(Direction direction) {
        snakeBody.add(new Segment(x, y));
        switch (direction) {
            case RIGHT: {
                this.x += 1;
                break;
            }
            case LEFT: {
                this.x -= 1;
                break;
            }
            case UP: {
                this.y += 1;
                break;
            }
            case DOWN: {
                this.y -= 1;
                break;
            }
            default:
                break;
        }
        if (snakeBody.size() >= length) {
            Segment removed = snakeBody.removeFirst();
            grid.setOccupied(removed.getX(), removed.getY(), false);
        }
        grid.setOccupied(x, y, true);
    }

    /*
     * grows the snake (increase it's length)
     */
    void grow() {
        length += 1;
    }

    /*
     * Returns current snake length. Used only for testing.
     */
    public int getLength() {
        return length;
    }
    
    public void update(float dt) {
        moveTimer -= dt;
        if (moveTimer <= 0) {
            direction = nextDirection;
            move(direction);
            moveTimer += movePeriod;
        }
    }

    /**
     *
     * @return the x co-ordinate of the head
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @return the y coordinate of the head
     */
    public int getY() {
        return y;
    }

    /*
     * Returns the full snake in segments
     */
    public List<Segment> getBodySegments() {
        return new ArrayList<Segment>(snakeBody);
    }

    /*
     * Stores each segment of the snake
     */
    public class Segment {
        private int x;
        private int y;

        public Segment(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /*
         * get Y co-ordinate of the snake segment
         */
        public int getX() {
            return x;
        }
    
        /*
         * get X xo-ordinate of the snake segment
         */
        public int getY() {
            return y;
        }

    }
}
