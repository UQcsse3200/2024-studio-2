package com.csse3200.game.components.minigame.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.csse3200.game.components.minigame.Direction;
import com.csse3200.game.components.minigame.snake.Snake;
import com.csse3200.game.components.minigame.snake.Apple;

public class SnakeGame {
    private final Snake snake;
    private final Apple apple;

    public SnakeGame(Snake snake, Apple apple) {
        this.snake = snake;
        this.apple = apple;
    }
    
    public void attemptEatFruit() {
        if (apple.isTouchingSnakeHead(snake)) {
            apple.spawn();
            snake.grow();
        }
    }
}
