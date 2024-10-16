package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Test class for the CombatMove abstract class.
 * This class verifies the behaviour of the abstract methods and properties defined in CombatMove.
 * We will create a concrete subclass for testing purposes.
 */
class CombatMoveTest {

    private TestCombatMove testCombatMove;
    private CombatStatsComponent attackerStats;
    private CombatStatsComponent targetStats;

    /**
     * A concrete implementation of the CombatMove class.
     * This subclass will be used to test the abstract methods and logic in CombatMove.
     */
    static class TestCombatMove extends CombatMove {

        public TestCombatMove(String moveName, int hungerCost) {
            super(moveName, hungerCost);
        }

        @Override
        public void execute(CombatStatsComponent attacker) {
            // Simulated behaviour for testing
        }

        @Override
        public void execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats) {
            // Simulated behaviour for testing
        }

        @Override
        public void execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats, boolean targetIsGuarded) {
            // Simulated behaviour for testing
        }

        @Override
        public void execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats, boolean targetIsGuarded, int numHitsLanded) {
            // Simulated behaviour for testing
        }
    }

    /**
     * Set up the test environment before each test.
     * We will instantiate a TestCombatMove object and mock the CombatStatsComponent for both attacker and target.
     */
    @BeforeEach
    void setUp() {
        // Create a concrete TestCombatMove object for testing
        testCombatMove = new TestCombatMove("Test Move", 15);

        // Mock the CombatStatsComponent for both attacker and target
        attackerStats = Mockito.mock(CombatStatsComponent.class);
        targetStats = Mockito.mock(CombatStatsComponent.class);
    }

    /**
     * Test the getMoveName method to ensure the move name is returned correctly.
     */
    @Test
    void testGetMoveName() {
        assertEquals("Test Move", testCombatMove.getMoveName(), "Move name should be 'Test Move'.");
    }

    /**
     * Test the getHungerCost method to ensure the hunger cost is returned correctly.
     */
    @Test
    void testGetHungerCost() {
        assertEquals(15, testCombatMove.getHungerCost(), "Hunger cost should be 15.");
    }

    /**
     * Test the execute method that only takes the attacker as a parameter.
     * In this case, we're only verifying that the method is called and no changes are made to health or hunger.
     */
    @Test
    void testExecuteWithAttackerOnly() {
        testCombatMove.execute(attackerStats);
        // Ensure that no changes were made to health since this method does not target anyone
        verify(attackerStats, never()).setHealth(anyInt());
    }

    /**
     * Test the execute method that takes both attacker and target stats as parameters.
     * Ensure the method works as intended without modifying health or other stats since this is a basic test.
     */
    @Test
    void testExecuteWithAttackerAndTarget() {
        testCombatMove.execute(attackerStats, targetStats);
        // Ensure that health changes are not made as this is just an initial test
        verify(attackerStats, never()).setHealth(anyInt());
        verify(targetStats, never()).setHealth(anyInt());
    }

    /**
     * Test the execute method that includes the target's guard status.
     * This test ensures that guarded moves are processed correctly, without changing health since it's a simple test.
     */
    @Test
    void testExecuteWithGuard() {
        testCombatMove.execute(attackerStats, targetStats, true);
        // No health change expected since this is a simple guard test
        verify(attackerStats, never()).setHealth(anyInt());
        verify(targetStats, never()).setHealth(anyInt());
    }

    /**
     * Test the execute method that includes the number of hits landed along with the attacker, target, and guard status.
     * Ensure the method can handle multiple hits without altering health in this basic test.
     */
    @Test
    void testExecuteWithMultiHits() {
        testCombatMove.execute(attackerStats, targetStats, false, 3);
        // No health change expected, just verifying method execution
        verify(attackerStats, never()).setHealth(anyInt());
        verify(targetStats, never()).setHealth(anyInt());
    }

    /**
     * Test that passing a null attacker to the execute methods does not cause any issues.
     * Ensure that no changes are made and that the code handles nulls gracefully.
     */
    @Test
    void testNullAttackerInAllExecuteMethods() {
        testCombatMove.execute(null);
        testCombatMove.execute(null, targetStats);
        testCombatMove.execute(null, targetStats, false);
        testCombatMove.execute(null, targetStats, false, 1);

        // Ensure no interaction occurred when attacker was null
        verify(targetStats, never()).setHealth(anyInt());
    }

    /**
     * Test that passing a null target to the execute methods does not cause any issues.
     * Ensure that no changes are made when the target is null.
     */
    @Test
    void testNullTargetInExecuteMethods() {
        testCombatMove.execute(attackerStats, null);
        testCombatMove.execute(attackerStats, null, false);
        testCombatMove.execute(attackerStats, null, false, 2);

        // Ensure no interaction occurred when target was null
        verify(attackerStats, never()).setHealth(anyInt());
    }
}
