package com.csse3200.game.minigames.maze.components;

import static org.junit.jupiter.api.Assertions.*;

import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
class MazeCombatStatsComponentTest {
    private MazeCombatStatsComponent player;
    private MazeCombatStatsComponent enemy;

    @BeforeEach
    void setUp() {
        player = new MazeCombatStatsComponent(100, 15, 1); // Player with 100 health and 15 base attack
        enemy = new MazeCombatStatsComponent(50, 10, 1);   // Enemy with 50 health and 10 base attack
    }

    @Test
    void testInitialHealthAndAttack() {
        assertEquals(100, player.getHealth(), "Initial health should be 100");
        assertEquals(15, player.getBaseAttack(), "Initial base attack should be 15");
    }

    @Test
    void testSetHealth() {
        player.setHealth(80);
        assertEquals(80, player.getHealth(), "Health should be set to 80");

        player.setHealth(0); // Setting health to 0 should trigger isDead()
        assertEquals(0, player.getHealth(), "Health should be 0");
    }

    @Test
    void testAddHealth() {
        enemy.addHealth(10); // Assuming this method increases health

        // Assert the expected health after adding
        assertEquals(60, enemy.getHealth(), "Enemy health should be increased correctly");
    }

    @Test
    void testHit() {
        // Assuming the hit method reduces enemy's health based on player's attack value
        enemy.hit(player); // Pass the player to the enemy's hit method

        // Assert that the enemy's health is now reduced based on the player's attack
        assertEquals(35, enemy.getHealth(), "Enemy health should be reduced by player's attack");
    }

    @Test
    void testSetBaseAttack() {
        player.setBaseAttack(20);
        assertEquals(20, player.getBaseAttack(), "Base attack should be set to 20");

        player.setBaseAttack(-5); // Should not allow negative attack values
        assertEquals(20, player.getBaseAttack(), "Base attack should remain 20 after trying to set a negative value");
    }
}
