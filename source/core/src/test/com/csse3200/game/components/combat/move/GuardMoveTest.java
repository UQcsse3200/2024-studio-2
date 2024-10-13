package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

/**
 * Unit tests for the GuardMove class using JUnit.
 * These tests ensure that the GuardMove behaves correctly, including reducing hunger when executed,
 * and properly handling cases with null attacker stats.
 */
class GuardMoveTest {

    private GuardMove guardMove;
    private CombatStatsComponent attackerStats;

    /**
     * Set up the test environment before each test.
     * Instantiates the GuardMove object and mocks the necessary components, such as CombatStatsComponent.
     */
    @BeforeEach
    void setUp() {
        // Create a GuardMove object with a name and hunger cost
        guardMove = new GuardMove("Basic Guard", 5);

        // Mock CombatStatsComponent for the attacker
        attackerStats = mock(CombatStatsComponent.class);
    }

    /**
     * Tests that the execute() method correctly reduces the attacker's hunger.
     * Ensures that the GuardMove reduces hunger by the expected amount.
     */
    @Test
    void testExecuteReducesHunger() {
        // Execute the GuardMove with the mock attacker stats
        guardMove.execute(attackerStats);

        // Verify that hunger is reduced by the expected amount (5 in this case)
        verify(attackerStats).addHunger(-5);
    }

    /**
     * Tests that the execute() method handles null attacker stats gracefully.
     * Ensures that no exception is thrown and the method doesn't proceed with a null attacker.
     */
    @Test
    void testExecuteWithNullAttackerDoesNothing() {
        // Call execute() with null attacker stats
        guardMove.execute(null);

        // No exception should be thrown, and no interactions should occur
        verify(attackerStats, never()).addHunger(anyInt());
    }

    /**
     * Tests the overloaded execute() method with both attacker and target stats.
     * Since the GuardMove doesn't interact with the target, this test ensures that the target's stats are not affected.
     */
    @Test
    void testExecuteWithAttackerAndTarget() {
        // Mock target stats
        CombatStatsComponent targetStats = mock(CombatStatsComponent.class);

        // Execute the GuardMove with both attacker and target stats
        guardMove.execute(attackerStats, targetStats);

        // Verify that only the attacker's hunger is reduced, and the target's stats are unaffected
        verify(attackerStats).addHunger(-5);
        verify(targetStats, never()).setHealth(anyInt());
    }

    /**
     * Tests the overloaded execute() method with a guarded target.
     * Since GuardMove doesn't interact with guarding, this ensures that the method functions the same as normal execution.
     */
    @Test
    void testExecuteWithGuardedTarget() {
        // Mock target stats
        CombatStatsComponent targetStats = mock(CombatStatsComponent.class);

        // Execute the GuardMove with attacker, target, and guarding status
        guardMove.execute(attackerStats, targetStats, true);

        // Verify that only the attacker's hunger is reduced, and the target's stats are unaffected
        verify(attackerStats).addHunger(-5);
        verify(targetStats, never()).setHealth(anyInt());
    }

    /**
     * Tests the overloaded execute() method with multiple hits.
     * Since GuardMove doesn't handle multi-hits, this ensures the method is treated the same as a standard execution.
     */
    @Test
    void testExecuteWithMultiHits() {
        // Mock target stats
        CombatStatsComponent targetStats = mock(CombatStatsComponent.class);

        // Execute the GuardMove with multiple hits (this parameter is ignored for GuardMove)
        guardMove.execute(attackerStats, targetStats, false, 3);

        // Verify that only the attacker's hunger is reduced, and the target's stats are unaffected
        verify(attackerStats).addHunger(-5);
        verify(targetStats, never()).setHealth(anyInt());
    }
}
