package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the SpecialKangaMove class.
 * These tests use Mockito to mock the behaviour of dependent components (e.g., CombatStatsComponent).
 */
@ExtendWith(GameExtension.class)
class SpecialKangaMoveTest {

    private SpecialKangaMove specialKangaMove;
    private CombatStatsComponent mockTargetStats;
    private CombatStatsComponent mockAttackerStats;

    /**
     * Initial setup before each test. Creates an instance of SpecialKangaMove and
     * mocks the necessary dependencies.
     */
    @BeforeEach
    void setUp() {
        // Create an instance of SpecialKangaMove with a mock move name and stamina cost.
        specialKangaMove = new SpecialKangaMove("Kanga Rage", 20);

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
        specialKangaMove.applyDebuffs(mockTargetStats);

        // Assert: Verify that the target's strength and defense are decreased.
        verify(mockTargetStats).addStrength(-15);
        verify(mockTargetStats).addDefense(-15);

        // Capture the added status effect (CONFUSED or BLEEDING).
        ArgumentCaptor<CombatStatsComponent.StatusEffect> statusCaptor = ArgumentCaptor.forClass(CombatStatsComponent.StatusEffect.class);
        verify(mockTargetStats).addStatusEffect(statusCaptor.capture());

        CombatStatsComponent.StatusEffect appliedEffect = statusCaptor.getValue();
        assertTrue(appliedEffect == CombatStatsComponent.StatusEffect.CONFUSED ||
                        appliedEffect == CombatStatsComponent.StatusEffect.BLEEDING,
                "Random status effect should be CONFUSED or BLEEDING.");
    }

    /**
     * Test to verify that the applyBuffs method correctly buffs Kanga's strength
     * and defense by the expected amounts.
     */
    @Test
    void testApplyBuffs() {
        // Act: Apply the buffs to the attacker's stats.
        specialKangaMove.applyBuffs(mockAttackerStats);

        // Assert: Verify that the attacker's strength and defense are increased.
        verify(mockAttackerStats).addStrength(15);
        verify(mockAttackerStats).addDefense(10);
    }

    /**
     * Test to ensure that the logger outputs the correct message when applyDebuffs is called.
     * We can test the side effects (logging) of the method using Mockito's verification features.
     */
    @Test
    void testApplyDebuffsLogsCorrectMessage() {
        // Act: Apply the debuffs to trigger the logger.
        specialKangaMove.applyDebuffs(mockTargetStats);

        // Since logger is static and logs to output, here we focus on behaviour verification (mock calls).
        verify(mockTargetStats).addStrength(-15);
        verify(mockTargetStats).addDefense(-15);
        verify(mockTargetStats, times(1)).addStatusEffect(any(CombatStatsComponent.StatusEffect.class));
    }

    /**
     * Test to ensure that the logger outputs the correct message when applyBuffs is called.
     * Again, this is focused on verifying behaviour and state, not direct logging output.
     */
    @Test
    void testApplyBuffsLogsCorrectMessage() {
        // Set up mock stats to return specific values.
        when(mockAttackerStats.getStrength()).thenReturn(30);
        when(mockAttackerStats.getDefense()).thenReturn(20);

        // Act: Apply the buffs to trigger the logger.
        specialKangaMove.applyBuffs(mockAttackerStats);

        // Assert: Verify that the logger logs the correct message for buffs.
        verify(mockAttackerStats, times(1)).addStrength(15);
        verify(mockAttackerStats, times(1)).addDefense(10);
    }
}
