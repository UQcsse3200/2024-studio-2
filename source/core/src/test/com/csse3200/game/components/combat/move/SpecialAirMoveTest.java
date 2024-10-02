package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the SpecialAirMove class.
 * These tests use Mockito to mock the behaviour of dependent components (e.g., CombatStatsComponent).
 */
class SpecialAirMoveTest {

    private SpecialAirMove specialAirMove;
    private CombatStatsComponent mockTargetStats;
    private CombatStatsComponent mockAttackerStats;

    /**
     * Initial setup before each test. Creates an instance of SpecialAirMove and
     * mocks the necessary dependencies.
     */
    @BeforeEach
    void setUp() {
        // Create an instance of SpecialAirMove with a mock move name and stamina cost.
        specialAirMove = new SpecialAirMove("Air Strike", 40);

        // Mock the target and attacker stats (CombatStatsComponent).
        mockTargetStats = mock(CombatStatsComponent.class);
        mockAttackerStats = mock(CombatStatsComponent.class);
    }

    /**
     * Test to verify that the applyDebuffs method correctly applies the debuff to the target
     * by reducing strength and defense, and applies a random status effect.
     */
    @Test
    void testApplyDebuffs() {
        // Act: Apply the debuffs to the target stats.
        specialAirMove.applyDebuffs(mockTargetStats);

        // Assert: Verify that the target's strength and defense are decreased.
        verify(mockTargetStats).addStrength(-30);
        verify(mockTargetStats).addDefense(-25);

        // Capture the added status effect (CONFUSION or SHOCKED).
        ArgumentCaptor<CombatStatsComponent.StatusEffect> statusCaptor = ArgumentCaptor.forClass(CombatStatsComponent.StatusEffect.class);
        verify(mockTargetStats).addStatusEffect(statusCaptor.capture());

        CombatStatsComponent.StatusEffect appliedEffect = statusCaptor.getValue();
        assertTrue(appliedEffect == CombatStatsComponent.StatusEffect.CONFUSION ||
                        appliedEffect == CombatStatsComponent.StatusEffect.SHOCKED,
                "Random status effect should be CONFUSION or SHOCKED.");
    }

    /**
     * Test to verify that the applyBuffs method correctly buffs Air Boss's strength
     * and defense by the expected amounts.
     */
    @Test
    void testApplyBuffs() {
        // Act: Apply the buffs to the attacker's stats.
        specialAirMove.applyBuffs(mockAttackerStats);

        // Assert: Verify that the attacker's strength and defense are increased.
        verify(mockAttackerStats).addStrength(25);
        verify(mockAttackerStats).addDefense(25);
    }

    /**
     * Test to ensure that the logger outputs the correct message when applyDebuffs is called.
     * We can test the side effects (logging) of the method using Mockito's verification features.
     */
    @Test
    void testApplyDebuffsLogsCorrectMessage() {
        // Act: Apply the debuffs to trigger the logger.
        specialAirMove.applyDebuffs(mockTargetStats);

        // Since logger is static and logs to output, here we focus on behaviour verification (mock calls).
        verify(mockTargetStats).addStrength(-30);
        verify(mockTargetStats).addDefense(-25);
        verify(mockTargetStats, times(1)).addStatusEffect(any(CombatStatsComponent.StatusEffect.class));
    }

    /**
     * Test to ensure that the logger outputs the correct message when applyBuffs is called.
     * Again, this is focused on verifying behaviour and state, not direct logging output.
     */
    @Test
    void testApplyBuffsLogsCorrectMessage() {
        // Set up mock stats to return specific values.
        when(mockAttackerStats.getStrength()).thenReturn(75);
        when(mockAttackerStats.getDefense()).thenReturn(100);

        // Act: Apply the buffs to trigger the logger.
        specialAirMove.applyBuffs(mockAttackerStats);

        // Assert: Verify that the logger logs the correct message for buffs.
        verify(mockAttackerStats, times(1)).addStrength(25);
        verify(mockAttackerStats, times(1)).addDefense(25);
    }
}
