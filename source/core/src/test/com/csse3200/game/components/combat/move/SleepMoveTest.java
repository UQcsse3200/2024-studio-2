package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

/**
 * Unit tests for the SleepMove class.
 * This test ensures that SleepMove restores health and stamina to the user when executed.
 */
class SleepMoveTest {

    private SleepMove sleepMove;
    private CombatStatsComponent attackerStats;

    /**
     * Set up the test environment before each test.
     * Instantiates the SleepMove object and mocks the necessary components, such as CombatStatsComponent.
     */
    @BeforeEach
    void setUp() {
        // Create a SleepMove object with a name and stamina cost
        sleepMove = new SleepMove("Basic Sleep", 5);

        // Mock CombatStatsComponent for the attacker
        attackerStats = Mockito.mock(CombatStatsComponent.class);
    }

    /**
     * Tests that the execute() method restores stamina and health for the attacker.
     * Ensures that the correct percentage of health and stamina are restored.
     */
    @Test
    void testExecuteRestoresStaminaAndHealth() {
        // Mock the max stamina and health values for the attacker
        when(attackerStats.getMaxStamina()).thenReturn(100);
        when(attackerStats.getMaxHealth()).thenReturn(200);

        // Execute the SleepMove
        sleepMove.execute(attackerStats);

        // Verify that 25% of stamina and 10% of health are restored
        verify(attackerStats).addStamina(25);
        verify(attackerStats).addHealth(20);
    }

    /**
     * Tests that the execute() method handles null attacker stats gracefully.
     * Ensures that no interaction occurs when the stats are null.
     */
    @Test
    void testExecuteWithNullAttacker() {
        // Call execute() with null attacker stats
        sleepMove.execute(null);

        // Nothing should happen, so no need to verify any behavior here
    }

    /**
     * Tests the overloaded execute() method with both attacker and target stats.
     * Since the SleepMove doesn't interact with the target, this test ensures that the target's stats are not affected.
     */
    @Test
    void testExecuteWithAttackerAndTarget() {
        // Mock target stats
        CombatStatsComponent targetStats = mock(CombatStatsComponent.class);

        // Mock the max stamina and health values for the attacker
        when(attackerStats.getMaxStamina()).thenReturn(100);
        when(attackerStats.getMaxHealth()).thenReturn(200);

        // Execute the SleepMove with both attacker and target stats
        sleepMove.execute(attackerStats, targetStats);

        // Verify that only the attacker's stamina and health are restored
        verify(attackerStats).addStamina(25);
        verify(attackerStats).addHealth(20);
        verify(targetStats, never()).setHealth(anyInt());
    }

    /**
     * Tests the overloaded execute() method with a guarded target.
     * Since SleepMove doesn't interact with guarding, this ensures that the method functions the same as normal execution.
     */
    @Test
    void testExecuteWithGuardedTarget() {
        // Mock target stats
        CombatStatsComponent targetStats = mock(CombatStatsComponent.class);

        // Mock the max stamina and health values for the attacker
        when(attackerStats.getMaxStamina()).thenReturn(100);
        when(attackerStats.getMaxHealth()).thenReturn(200);

        // Execute the SleepMove with attacker, target, and guarding status
        sleepMove.execute(attackerStats, targetStats, true);

        // Verify that only the attacker's stamina and health are restored
        verify(attackerStats).addStamina(25);
        verify(attackerStats).addHealth(20);
        verify(targetStats, never()).setHealth(anyInt());
    }

    /**
     * Tests the overloaded execute() method with multiple hits.
     * Since SleepMove doesn't handle multi-hits, this ensures the method is treated the same as a standard execution.
     */
    @Test
    void testExecuteWithMultiHits() {
        // Mock target stats
        CombatStatsComponent targetStats = mock(CombatStatsComponent.class);

        // Mock the max stamina and health values for the attacker
        when(attackerStats.getMaxStamina()).thenReturn(100);
        when(attackerStats.getMaxHealth()).thenReturn(200);

        // Execute the SleepMove with multiple hits (this parameter is ignored for SleepMove)
        sleepMove.execute(attackerStats, targetStats, false, 3);

        // Verify that only the attacker's stamina and health are restored
        verify(attackerStats).addStamina(25);
        verify(attackerStats).addHealth(20);
        verify(targetStats, never()).setHealth(anyInt());
    }
}
