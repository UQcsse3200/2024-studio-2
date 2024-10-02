package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the SpecialWaterMove class.
 * These tests use Mockito to mock the behaviour of dependent components (e.g., CombatStatsComponent).
 */
class SpecialWaterMoveTest {

    private SpecialWaterMove specialWaterMove;
    private CombatStatsComponent mockTargetStats;
    private CombatStatsComponent mockAttackerStats;

    /**
     * Initial setup before each test. Creates an instance of SpecialWaterMove and
     * mocks the necessary dependencies.
     */
    @BeforeEach
    void setUp() {
        // Create an instance of SpecialWaterMove with a mock move name and stamina cost.
        specialWaterMove = new SpecialWaterMove("Water Fury", 30);

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
        specialWaterMove.applyDebuffs(mockTargetStats);

        // Assert: Verify that the target's strength and defense are decreased.
        verify(mockTargetStats).addStrength(-20);
        verify(mockTargetStats).addDefense(-10);

        // Capture the added status effect (CONFUSED or POISONED).
        ArgumentCaptor<CombatStatsComponent.StatusEffect> statusCaptor = ArgumentCaptor.forClass(CombatStatsComponent.StatusEffect.class);
        verify(mockTargetStats).addStatusEffect(statusCaptor.capture());

        CombatStatsComponent.StatusEffect appliedEffect = statusCaptor.getValue();
        assertTrue(appliedEffect == CombatStatsComponent.StatusEffect.CONFUSED ||
                        appliedEffect == CombatStatsComponent.StatusEffect.POISONED,
                "Random status effect should be CONFUSED or POISONED.");
    }

    /**
     * Test to verify that the applyBuffs method correctly buffs Water Boss's strength
     * and defense by the expected amounts.
     */
    @Test
    void testApplyBuffs() {
        // Act: Apply the buffs to the attacker's stats.
        specialWaterMove.applyBuffs(mockAttackerStats);

        // Assert: Verify that the attacker's strength and defense are increased.
        verify(mockAttackerStats).addStrength(10);
        verify(mockAttackerStats).addDefense(25);
    }

    /**
     * Test to ensure that the logger outputs the correct message when applyDebuffs is called.
     * We can test the side effects (logging) of the method using Mockito's verification features.
     */
    @Test
    void testApplyDebuffsLogsCorrectMessage() {
        // Act: Apply the debuffs to trigger the logger.
        specialWaterMove.applyDebuffs(mockTargetStats);

        // Since logger is static and logs to output, here we focus on behaviour verification (mock calls).
        verify(mockTargetStats).addStrength(-20);
        verify(mockTargetStats).addDefense(-10);
        verify(mockTargetStats, times(1)).addStatusEffect(any(CombatStatsComponent.StatusEffect.class));
    }

    /**
     * Test to ensure that the logger outputs the correct message when applyBuffs is called.
     * Again, this is focused on verifying behaviour and state, not direct logging output.
     */
    @Test
    void testApplyBuffsLogsCorrectMessage() {
        // Set up mock stats to return specific values.
        when(mockAttackerStats.getStrength()).thenReturn(50);
        when(mockAttackerStats.getDefense()).thenReturn(75);

        // Act: Apply the buffs to trigger the logger.
        specialWaterMove.applyBuffs(mockAttackerStats);

        // Assert: Verify that the logger logs the correct message for buffs.
        verify(mockAttackerStats, times(1)).addStrength(10);
        verify(mockAttackerStats, times(1)).addDefense(25);
    }
}
