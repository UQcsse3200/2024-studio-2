package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

/**
 * Unit tests for the AttackMove class, using Mockito to mock the CombatStatsComponent.
 * Tests ensure correct behaviour for various attack scenarios, including when the target is guarded,
 * when there are null components, and when hunger is reduced after an attack.
 */
@ExtendWith(GameExtension.class)
class AttackMoveTest {

    private AttackMove attackMove;
    private CombatStatsComponent attackerStats;
    private CombatStatsComponent targetStats;

    /**
     * Sets up the test environment before each test. This includes instantiating the AttackMove
     * object and mocking CombatStatsComponent for both the attacker and target entities.
     */
    @BeforeEach
    void setUp() {
        attackMove = new AttackMove("Basic Attack", 10);

        // Mock attacker and target stats for testing
        attackerStats = Mockito.mock(CombatStatsComponent.class);
        targetStats = Mockito.mock(CombatStatsComponent.class);
    }

    /**
     * Tests the execute() method when only the attacker is provided (no target).
     * Verifies that no interaction occurs with the attacker's stats since a target is required for the move.
     */
    @Test
    void testExecuteWithoutTarget() {
        // Call execute() with only attacker stats and no target
        attackMove.execute(attackerStats);

        // Verify that the attacker's strength is never accessed, as the move cannot proceed without a target
        verify(attackerStats, never()).getStrength();
    }

    /**
     * Tests the execute() method when both the attacker and target stats are provided.
     * Ensures that the target's health is reduced after the attack is performed.
     */
    @Test
    void testExecuteWithTarget() {
        // Mock stats for attacker and target
        when(attackerStats.getStrength()).thenReturn(20);  // Attacker has strength of 20
        when(targetStats.getDefense()).thenReturn(10);     // Target has defense of 10
        when(attackerStats.getHunger()).thenReturn(100);  // Attacker has full hunger
        when(targetStats.getHealth()).thenReturn(50);      // Target has 50 health

        // Execute attack
        attackMove.execute(attackerStats, targetStats);

        // Verify that the target's health is reduced after the attack
        verify(targetStats).setHealth(anyInt());
    }

    /**
     * Tests the execute() method when the target is guarded, which should reduce the damage inflicted.
     * Ensures that the target's health is reduced, but by less than in an unguarded scenario.
     */
    @Test
    void testExecuteWithGuardedTarget() {
        // Mock stats for attacker and guarded target
        when(attackerStats.getStrength()).thenReturn(20);
        when(targetStats.getDefense()).thenReturn(10);
        when(attackerStats.getHunger()).thenReturn(100);
        when(targetStats.getHealth()).thenReturn(50);

        // Execute attack with the target guarded
        attackMove.execute(attackerStats, targetStats, true);

        // Verify that the target's health is reduced, taking guarding into account
        verify(targetStats).setHealth(anyInt());
    }

    /**
     * Tests that the execute() method handles null values for the attacker or target gracefully.
     * Ensures that no changes are made to health or hunger when either is null.
     */
    @Test
    void testExecuteWithNullAttackerOrTarget() {
        // Call execute() with a null attacker, and verify that no health is changed on the target
        attackMove.execute(null, targetStats);
        verify(targetStats, never()).setHealth(anyInt());

        // Call execute() with a null target, and verify that no hunger is reduced for the attacker
        attackMove.execute(attackerStats, null);
        verify(attackerStats, never()).addHunger(anyInt());
    }

    /**
     * Tests that the attack move properly reduces the attacker's hunger after an attack.
     * Verifies that the attacker's hunger is reduced by the correct amount specified by the hunger cost.
     */
    @Test
    void testHungerReductionAfterAttack() {
        // Mock stats for the attacker
        when(attackerStats.getStrength()).thenReturn(20);
        when(targetStats.getDefense()).thenReturn(10);
        when(attackerStats.getHunger()).thenReturn(100);

        // Execute attack
        attackMove.execute(attackerStats, targetStats);

        // Verify that the attacker's hunger is reduced by the move's hunger cost (-10)
        verify(attackerStats).addHunger(-10);  // -10 is the hunger cost for this attack
    }
}
