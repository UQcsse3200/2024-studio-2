package com.csse3200.game.components.minigame.snake;

import com.csse3200.game.components.minigame.Direction;
import com.csse3200.game.components.minigame.Grid;
import com.csse3200.game.components.minigame.GridCell;
import java.util.Deque;
import java.util.ArrayDeque;

public class Snake {
    private int x;
    private int y;
    private final Grid grid;
    private final Deque<GridCell> snakeBody;
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
        snakeBody.add(grid.getCell(startX, startY));
    }

    public void move(Direction direction) {
        switch (direction) {
            case RIGHT: {
                this.x += 1;
                return;
            }
            case LEFT: {
                this.x -= 1;
                return;
            }
            case UP: {
                this.y -= 1;
                return;
            }
            case DOWN: {
                this.y += 1;
                return;
            }
        }
    }
    
    public void update(float dt) {
        moveTimer -= dt;
        if (moveTimer <= 0) {
            move(direction);
            moveTimer = movePeriod;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
