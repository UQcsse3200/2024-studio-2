package com.csse3200.game.components.minigames.birdieDash.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class CoinTest {

    private Coin coin;
    private final float initialX = 1000;
    private final float speed = 300; // Example speed

    @BeforeEach
    public void setUp() {
        coin = new Coin(initialX, speed);
    }

    @Test
    public void testInitialPosition() {
        assertEquals(initialX, coin.getPosition().x, "Initial X position should be as provided");
        assertTrue(coin.getPosition().y >= 100 && coin.getPosition().y <= 1100, "Initial Y position should be within the specified range");
    }

    @Test
    public void testChangePosition() {
        float dt = 0.5f; // Example delta time
        coin.changePosition(dt);

        // Position should decrease by speed * dt
        assertEquals(initialX - speed * dt, coin.getPosition().x, "X position should decrease correctly based on speed and delta time");
    }

    @Test
    public void testRespawnCoin() {
        coin.changePosition(1000); // Move the coin off screen
        coin.respawnCoin();

        // Coin should respawn at a new X position off the right edge of the screen
        assertEquals(1920 + 960 - 80 / 2, coin.getPosition().x, "X position after respawn should be as specified");
        assertTrue(coin.getPosition().y >= 100 && coin.getPosition().y <= 1100, "Y position after respawn should be within the specified range");
    }

    @Test
    public void testCoinOffScreen() throws Exception {
    }


    @Test
    public void testBoundarySetting() throws Exception {
    }
}
