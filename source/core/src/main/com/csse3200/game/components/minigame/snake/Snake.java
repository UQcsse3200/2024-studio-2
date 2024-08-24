package com.csse3200.game.components.minigame.snake;

import com.csse3200.game.components.minigame.Direction;
import com.csse3200.game.components.minigame.Grid;
import com.csse3200.game.components.minigame.GridCell;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.List;
import java.util.ArrayList;

public class Snake {
    private int x;
    private int y;
    private final Grid grid;
    private final Deque<Segment> snakeBody;
    private int length = 1;
    private Direction direction;
    private final float movePeriod;
    private float moveTimer;

    public Snake(Grid grid, int startX, int startY, Direction startDirection, int startLength, float movePeriod) {
        this.grid = grid;
        this.movePeriod = movePeriod;
        this.moveTimer = movePeriod;
        this.direction = startDirection;
        this.length = startLength;
        x = startX;
        y = startY;
        snakeBody = new ArrayDeque<>();
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {return this.direction;}

    public void updateDirectionOnInput(Direction direction) {
        if (direction == Direction.UP && this.direction != Direction.DOWN) {
            this.direction = direction;
        }
        if (direction == Direction.DOWN && this.direction != Direction.UP) {
            this.direction = direction;
        }
        if (direction == Direction.LEFT && this.direction != Direction.RIGHT) {
            this.direction = direction;
        }
        if (direction == Direction.RIGHT && this.direction != Direction.LEFT) {
            this.direction = direction;
        }
    }

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
        }
        if (snakeBody.size() >= length) {
            snakeBody.removeFirst();
        }
    }

    void grow() {
        length += 1;
    }
    
    public void update(float dt) {
        moveTimer -= dt;
        if (moveTimer <= 0) {
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

    public List<Segment> getBodySegments() {
        return new ArrayList<Segment>(snakeBody);
    }

    public class Segment {
        private int x;
        private int y;

        public Segment(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }
    
        public int getY() {
            return y;
        }

    }
}
