package com.csse3200.game.components.minigames.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.csse3200.game.components.minigames.Direction;
import com.csse3200.game.components.minigames.snake.controller.Events;
import com.csse3200.game.components.minigames.snake.controller.SnakeController;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SnakeControllerTest {
    // Before each test it initialises the SnakeController instance
    private SnakeController snakeController;

    @Before
    public void setUp() {
        snakeController = new SnakeController();
        // Mock the Gdx.input methods
        Gdx.input = mock(Input.class);

        // Mock the Gdx.gl methods
        Gdx.gl = mock(GL20.class);
    }

    @Test
    public void testGetInputDirectionRight() {
        // The right key is pressed and D key is not pressed
        when(Gdx.input.isKeyPressed(Input.Keys.RIGHT)).thenReturn(true);
        when(Gdx.input.isKeyPressed(Input.Keys.D)).thenReturn(false);

        // calls the method under test scenarios
        Direction direction = snakeController.getInputDirection();

        // verifies the expected result
        assertEquals(Direction.RIGHT, direction);
    }

    @Test
    public void testGetInputDirectionLeft() {
        // The left key is pressed and A key is not pressed
        when(Gdx.input.isKeyPressed(Input.Keys.LEFT)).thenReturn(true);
        when(Gdx.input.isKeyPressed(Input.Keys.A)).thenReturn(false);

        // calls the method under test scenarios
        Direction direction = snakeController.getInputDirection();

        // verifies the expected result
        assertEquals(Direction.LEFT, direction);
    }

    @Test
    public void testGetInputDirectionUp() {
        // The up key is pressed and W key is not pressed
        when(Gdx.input.isKeyPressed(Input.Keys.UP)).thenReturn(true);
        when(Gdx.input.isKeyPressed(Input.Keys.W)).thenReturn(false);

        // calls the method under test scenarios
        Direction direction = snakeController.getInputDirection();

        // verifies the expected result
        assertEquals(Direction.UP, direction);
    }

    @Test
    public void testGetInputDirectionDown() {
        // The down key is pressed and S key is not pressed
        when(Gdx.input.isKeyPressed(Input.Keys.DOWN)).thenReturn(true);
        when(Gdx.input.isKeyPressed(Input.Keys.S)).thenReturn(false);

        // calls the method under test scenarios
        Direction direction = snakeController.getInputDirection();

        // verifies the expected result
        assertEquals(Direction.DOWN, direction);
    }

    @Test
    public void testGetInputDirectionNone() {
        // no keys are pressed
        when(Gdx.input.isKeyPressed(anyInt())).thenReturn(false);

        // calls the method under test scenarios
        Direction direction = snakeController.getInputDirection();

        // verifies the expected result
        assertEquals(Direction.ZERO, direction);
    }

    @Test
    public void testHandleInputRestart() {
        // R key is pressed
        when(Gdx.input.isKeyJustPressed(Input.Keys.R)).thenReturn(true);

        // calls the method under test scenarios
        Events event = snakeController.handleInput();

        // verifies the expected result
        assertEquals(Events.RESTART, event);
    }

    @Test
    public void testHandleInputExitToMenu() {
        // Esc key is pressed
        when(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)).thenReturn(true);

        // calls the method under test scenarios
        Events event = snakeController.handleInput();

        // verifies the expected result
        assertEquals(Events.EXIT_TO_MENU, event);
    }

    @Test
    public void testHandleInputNone() {
        // no keys are pressed
        when(Gdx.input.isKeyJustPressed(anyInt())).thenReturn(false);

        // calls the method under test scenarios
        Events event = snakeController.handleInput();

        // verifies the expected result
        assertEquals(Events.NONE, event);
    }
}



