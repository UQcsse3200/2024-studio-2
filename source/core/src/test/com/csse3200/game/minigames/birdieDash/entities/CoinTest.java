package com.csse3200.game.minigames.birdieDash.entities;

import static org.junit.jupiter.api.Assertions.*;

import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@ExtendWith(GameExtension.class)
class CoinTest {

    private Coin coin;
    // Method Set Up to initialise the Coin instance
    @BeforeEach
    void setUp() {
        coin = new Coin(500, 100); // This is the initial coin's position
    }

    // Used helper method to get the value of a private field from the Coin class using reflection
    private float getPrivateField(String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = Coin.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (float) field.get(coin);
    }

    // Used helper method to set the value of a private field in the Coin class using reflection
    private void setPrivateField(String fieldName, float value) throws NoSuchFieldException, IllegalAccessException {
        Field field = Coin.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(coin, value);
    }

    // Used helper method to invoke a private method from the Coin class using reflection
    private boolean invokePrivateMethod(String methodName) throws Exception {
        Method method = Coin.class.getDeclaredMethod(methodName);
        method.setAccessible(true);
        return (boolean) method.invoke(coin);
    }

    // Used helper method to get the GAME_WIDTH value from the Coin class using reflection
    private float getGameWidth() throws NoSuchFieldException, IllegalAccessException {
        Field field = Coin.class.getDeclaredField("GAME_WIDTH");
        field.setAccessible(true);
        return (float) field.get(coin);
    }

    // Testing to verify the initial position of the coin is within the expected bounds
    @Test
    void testInitialPosition() throws NoSuchFieldException, IllegalAccessException {
        float minY = getPrivateField("MIN_Y");
        float maxY = getPrivateField("MAX_Y");

        assertTrue(coin.getPosition().x >= 500);
        assertTrue(coin.getPosition().y >= minY && coin.getPosition().y <= maxY);
    }

    // Testing to verify the initial boundary of the coin matches its position and dimensions
    @Test
    void testInitialBoundary() {
        Rectangle expectedBoundary = new Rectangle(coin.getPosition().x, coin.getPosition().y, coin.getWidth(), coin.getHeight());
        assertEquals(expectedBoundary, coin.getBoundary());
    }

    // Testing to verify the coin's position updates correctly based on a simulated delta time
    @Test
    void testChangePosition() {
        float deltaTime = 0.016f; // Simulate 16ms
        coin.changePosition(deltaTime);
        Vector2 newPosition = coin.getPosition();
        assertEquals(500 - 100 * deltaTime, newPosition.x, 0.01); // Added tolerance for floating-point comparison
    }

    // Testing to verify the coin respawns correctly when it moves off-screen
    @Test
    void testRespawnCoin() throws NoSuchFieldException, IllegalAccessException {
        // Simulates a coin that has moved off the screen
        coin.changePosition(1000); // Move it off screen

        // Captures the current x position before respawn
        float initialX = coin.getPosition().x;

        // Calls respawn method
        coin.respawnCoin();

        // Retrieves the GAME_WIDTH value via reflection
        float gameWidth = getGameWidth();

        // Checks if the new position is correctly updated
        assertTrue(coin.getPosition().x > initialX);
        assertTrue(coin.getPosition().x <= initialX + gameWidth + 960);

        // Ensure the y position is within the valid range
        float minY = getPrivateField("MIN_Y");
        float maxY = getPrivateField("MAX_Y");
        assertTrue(coin.getPosition().y >= minY && coin.getPosition().y <= maxY);
    }

    // Tests to verify the coin's off-screen status is detected correctly
    @Test
    void testCoinOffScreen() throws Exception {
        coin.changePosition(1000);
        assertTrue(invokePrivateMethod("coinOffScreen"));
    }

    // Tests to check if the boundary updates correctly on position change
    // NEEDS TO BE DONE
    @Test
    void testBoundaryUpdateOnChangePosition() {
        coin.changePosition(1);
        Rectangle expectedBoundary = new Rectangle(400 - coin.getWidth() / 2,
                coin.getPosition().y - coin.getHeight() / 2,
                coin.getWidth(),
                coin.getHeight());
        assertEquals(expectedBoundary, coin.getBoundary());
    }
}


