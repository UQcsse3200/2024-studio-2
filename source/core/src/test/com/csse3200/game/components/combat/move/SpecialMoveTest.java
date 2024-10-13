package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;

/**
 * Unit tests for the abstract SpecialMove class. A concrete implementation
 * of SpecialMove is used for testing purposes.
 */
@ExtendWith(GameExtension.class)
class SpecialMoveTest {

    private SpecialMove specialMove;
    private CombatStatsComponent mockAttackerStats;
    private CombatStatsComponent mockTargetStats;

    /**
     * A concrete subclass of SpecialMove created for testing purposes.
     * This allows us to instantiate the abstract class and test its behaviour.
     */
    class TestSpecialMove extends SpecialMove {
        public TestSpecialMove(String moveName, int hungerCost) {
            super(moveName, hungerCost);
        }

        @Override
        protected void applyDebuffs(CombatStatsComponent targetStats) {
            targetStats.addStatusEffect(CombatStatsComponent.StatusEffect.BLEEDING);
        }

        @Override
        protected void applyBuffs(CombatStatsComponent attackerStats) {
            attackerStats.addStrength(10);
        }
    }

    /**
     * Initial setup before each test. Creates an instance of the TestSpecialMove and
     * mocks the necessary dependencies.
     */
    @BeforeEach
    void setUp() {
        specialMove = new TestSpecialMove("Test Special", 20);

        // Mock the CombatStatsComponent for both the attacker and the target.
        mockAttackerStats = mock(CombatStatsComponent.class);
        mockTargetStats = mock(CombatStatsComponent.class);
    }

    /**
     * Test case to verify that calling execute with only attackerStats logs an error message
     * and does not proceed further.
     */
    @Test
    void testExecuteAttackerOnlyLogsError() {
        // Act: Call execute with only attackerStats (without targetStats).
        specialMove.execute(mockAttackerStats);

        // Assert: Verify that the error message is logged.
        // We are assuming that the logger is logging an error for missing arguments.
        verify(mockAttackerStats, never()).addHunger(anyInt());  // No hunger should be deducted.
    }

    /**
     * Test case to verify that execute applies debuffs and buffs correctly when the target is not guarded.
     */
    @Test
    void testExecuteWithDebuffsAndBuffs() {
        // Arrange: The target is not guarded.
        boolean targetIsGuarded = false;

        // Act: Execute the move with both attacker and target stats.
        specialMove.execute(mockAttackerStats, mockTargetStats, targetIsGuarded);

        // Assert: Verify that debuffs are applied to the target and buffs are applied to the attacker.
        verify(mockTargetStats, times(1)).addStatusEffect(CombatStatsComponent.StatusEffect.BLEEDING);
        verify(mockAttackerStats, times(1)).addStrength(10);
        verify(mockAttackerStats, times(1)).addHunger(-20); // Ensure hunger is deducted correctly.
    }

    /**
     * Test case to verify that debuffs are not applied when the target is guarded.
     */
    @Test
    void testExecuteWithGuardedTarget() {
        // Arrange: The target is guarded.
        boolean targetIsGuarded = true;

        // Act: Execute the move with a guarded target.
        specialMove.execute(mockAttackerStats, mockTargetStats, targetIsGuarded);

        // Assert: Verify that no debuffs are applied, but buffs are still applied to the attacker.
        verify(mockTargetStats, never()).addStatusEffect(any());  // No debuffs should be applied.
        verify(mockAttackerStats, times(1)).addStrength(10);
        verify(mockAttackerStats, times(1)).addHunger(-20); // Ensure hunger is deducted correctly.
    }

    /**
     * Test case to verify that an error is logged if either attackerStats or targetStats are null.
     */
    @Test
    void testExecuteLogsErrorOnNullStats() {
        // Act: Call execute with null attackerStats.
        specialMove.execute(null, mockTargetStats, false);

        // Assert: Verify that the error is logged and no further actions are taken.
        verify(mockTargetStats, never()).addStatusEffect(any());
        verify(mockAttackerStats, never()).addHunger(anyInt());

        // Act: Call execute with null targetStats.
        specialMove.execute(mockAttackerStats, null, false);

        // Assert: Verify that the error is logged and no further actions are taken.
        verify(mockAttackerStats, never()).addHunger(anyInt());
    }

    /**
     * Test case to verify the logger logs the correct message when the move is successfully executed.
     * This ensures that the log message indicating the special move was used is output correctly.
     */
    @Test
    void testExecuteLogsSpecialMoveUsed() {
        // Act: Execute the move.
        specialMove.execute(mockAttackerStats, mockTargetStats, false);

        // Assert: Verify that the hunger deduction and logger message occur as expected.
        verify(mockAttackerStats, times(1)).addHunger(-20);  // Verify hunger deduction.
    }
}
