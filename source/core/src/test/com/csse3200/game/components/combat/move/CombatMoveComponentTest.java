package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.combat.CombatManager;
import com.csse3200.game.entities.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the CombatMoveComponent class.
 * These tests ensure that the correct move is executed based on the provided action,
 * and that interactions with CombatStatsComponent are handled properly.
 */
class CombatMoveComponentTest {

    private CombatMoveComponent combatMoveComponent;
    private Entity mockEntity;
    private CombatStatsComponent mockAttackerStats;
    private CombatStatsComponent mockTargetStats;
    private AttackMove mockAttackMove;
    private GuardMove mockGuardMove;
    private SleepMove mockSleepMove;
    private SpecialMove mockSpecialMove;

    /**
     * Sets up the test environment before each test.
     * This includes mocking the necessary components, such as moves and combat stats.
     */
    @BeforeEach
    void setUp() {
        // Mock CombatStatsComponent for the attacker and target
        mockAttackerStats = mock(CombatStatsComponent.class);
        mockTargetStats = mock(CombatStatsComponent.class);

        // Mock different move types
        mockAttackMove = mock(AttackMove.class);
        mockGuardMove = mock(GuardMove.class);
        mockSleepMove = mock(SleepMove.class);
        mockSpecialMove = mock(SpecialMove.class);

        // Mock entity and attach the attacker's stats to it
        mockEntity = mock(Entity.class);
        when(mockEntity.getComponent(CombatStatsComponent.class)).thenReturn(mockAttackerStats);

        // Create a CombatMoveComponent with a move set containing all moves
        List<CombatMove> moveSet = Arrays.asList(mockAttackMove, mockGuardMove, mockSleepMove, mockSpecialMove);
        combatMoveComponent = new CombatMoveComponent(moveSet);

        // Assign the mocked entity to the combat move component
        combatMoveComponent.setEntity(mockEntity);
    }

    /**
     * Test that the executeMove method successfully executes an attack move.
     * Verifies that the attacker's CombatStatsComponent is used and that the attack move is called.
     */
    @Test
    void testExecuteAttackMove() {
        // Call executeMove with ATTACK action
        combatMoveComponent.executeMove(CombatManager.Action.ATTACK);

        // Verify that the attack move is executed with the correct CombatStatsComponent
        verify(mockAttackMove).execute(mockAttackerStats);
    }

    /**
     * Test that the executeMove method successfully executes a guard move.
     * Verifies that the guard move is called correctly with the attacker's stats.
     */
    @Test
    void testExecuteGuardMove() {
        // Call executeMove with GUARD action
        combatMoveComponent.executeMove(CombatManager.Action.GUARD);

        // Verify that the guard move is executed with the correct CombatStatsComponent
        verify(mockGuardMove).execute(mockAttackerStats);
    }

    /**
     * Test that the executeMove method successfully executes a sleep move.
     * Verifies that the sleep move is called correctly with the attacker's stats.
     */
    @Test
    void testExecuteSleepMove() {
        // Call executeMove with SLEEP action
        combatMoveComponent.executeMove(CombatManager.Action.SLEEP);

        // Verify that the sleep move is executed with the correct CombatStatsComponent
        verify(mockSleepMove).execute(mockAttackerStats);
    }

    /**
     * Test that the executeMove method successfully executes a special move.
     * Verifies that the special move is called correctly with the attacker's stats.
     */
    @Test
    void testExecuteSpecialMove() {
        // Call executeMove with SPECIAL action
        combatMoveComponent.executeMove(CombatManager.Action.SPECIAL);

        // Verify that the special move is executed with the correct CombatStatsComponent
        verify(mockSpecialMove).execute(mockAttackerStats);
    }

    /**
     * Test that executeMove with both an attacker and target executes the correct move.
     * Ensures that the attack move is called with the correct stats for both attacker and target.
     */
    @Test
    void testExecuteMoveWithTarget() {
        // Call executeMove with ATTACK action and a target
        combatMoveComponent.executeMove(CombatManager.Action.ATTACK, mockTargetStats);

        // Verify that the attack move is executed with the correct attacker and target stats
        verify(mockAttackMove).execute(mockAttackerStats, mockTargetStats);
    }

    /**
     * Test that executeMove with guard status and target executes the correct move.
     * Ensures that the guard multiplier is considered when executing the move.
     */
    @Test
    void testExecuteMoveWithGuardAndTarget() {
        // Call executeMove with ATTACK action, a target, and guard status
        combatMoveComponent.executeMove(CombatManager.Action.ATTACK, mockTargetStats, true);

        // Verify that the attack move is executed with the correct attacker, target, and guard status
        verify(mockAttackMove).execute(mockAttackerStats, mockTargetStats, true);
    }

    /**
     * Test that executeMove with guard status, target, and multiple hits executes the correct move.
     * Ensures that the attack move is called multiple times based on the number of hits.
     */
    @Test
    void testExecuteMoveWithMultipleHits() {
        // Call executeMove with ATTACK action, target, guard status, and multiple hits
        combatMoveComponent.executeMove(CombatManager.Action.ATTACK, mockTargetStats, false, 3);

        // Verify that the attack move is executed with the correct attacker, target, guard status, and hit count
        verify(mockAttackMove).execute(mockAttackerStats, mockTargetStats, false, 3);
    }

    /**
     * Test that if the action does not match any move, no move is executed.
     * Verifies that no interactions occur when an invalid action is passed.
     */
    @Test
    void testExecuteMoveWithInvalidAction() {
        // Call executeMove with an invalid action (e.g. ITEM)
        combatMoveComponent.executeMove(CombatManager.Action.ITEM);

        // Verify that none of the moves were executed
        verify(mockAttackMove, never()).execute(any());
        verify(mockGuardMove, never()).execute(any());
        verify(mockSleepMove, never()).execute(any());
        verify(mockSpecialMove, never()).execute(any());
    }

    /**
     * Test that hasSpecialMove returns true when a special move is present in the move set.
     */
    @Test
    void testHasSpecialMoveTrue() {
        List<CombatMove> moveSet = Arrays.asList(mockAttackMove, mockGuardMove, mockSleepMove, mockSpecialMove);
        combatMoveComponent = new CombatMoveComponent(moveSet);

        assertTrue(combatMoveComponent.hasSpecialMove(), "The move set contains a special move, but hasSpecialMove returned false.");
    }

    /**
     * Test that hasSpecialMove returns false when no special move is present in the move set.
     */
    @Test
    void testHasSpecialMoveFalse() {
        List<CombatMove> moveSet = Arrays.asList(mockAttackMove, mockGuardMove, mockSleepMove);
        combatMoveComponent = new CombatMoveComponent(moveSet);

        assertFalse(combatMoveComponent.hasSpecialMove(), "The move set does not contain a special move, but hasSpecialMove returned true.");
    }

    /**
     * Test that hasSpecialMove returns false when the move set is empty.
     */
    @Test
    void testHasSpecialMoveEmptyMoveSet() {
        List<CombatMove> emptyMoveSet = Collections.emptyList();
        combatMoveComponent = new CombatMoveComponent(emptyMoveSet);

        assertFalse(combatMoveComponent.hasSpecialMove(), "The move set is empty, but hasSpecialMove returned true.");
    }

    /**
     * Test that the getMoveSet method returns the correct move set that was passed in.
     * Ensures that the move set returned matches the one used to instantiate the component.
     */
    @Test
    void testGetMoveSet() {
        // Get the move set and verify its contents
        List<CombatMove> moveSet = combatMoveComponent.getMoveSet();

        assertTrue(moveSet.contains(mockAttackMove));
        assertTrue(moveSet.contains(mockGuardMove));
        assertTrue(moveSet.contains(mockSleepMove));
        assertTrue(moveSet.contains(mockSpecialMove));
    }
}
