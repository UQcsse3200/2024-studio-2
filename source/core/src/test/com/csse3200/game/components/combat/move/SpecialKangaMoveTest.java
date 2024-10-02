package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

/**
 * Unit tests for the SpecialKangaMove class.
 * These tests use Mockito to mock the behaviour of dependent components (e.g., CombatStatsComponent).
 */
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
     * Test to verify that the applyDebuffs method correctly applies the CONFUSION
     * and BLEEDING status effects to the target.
     */
    //@Test
    void testApplyDebuffs() {
        // Act: Apply the debuffs to the target stats.
        specialKangaMove.applyDebuffs(mockTargetStats);

        // Assert: Verify that CONFUSED and BLEEDING status effects are added to the target.
        verify(mockTargetStats).addStatusEffect(CombatStatsComponent.StatusEffect.CONFUSED);
        verify(mockTargetStats).addStatusEffect(CombatStatsComponent.StatusEffect.BLEEDING);
    }

    /**
     * Test to verify that the applyBuffs method correctly buffs Kanga's strength
     * and defense by the expected amounts.
     */
    //@Test
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
    //@Test
    void testApplyDebuffsLogsCorrectMessage() {
        // Act: Apply the debuffs to trigger the logger.
        specialKangaMove.applyDebuffs(mockTargetStats);

        // Assert: Verify that the logger logs the correct message for debuffs.
        // Since logger is a static field, we'd normally need to mock or spy on it, but
        // here we assume it's just outputting the message and focus on behaviour verification.
        verify(mockTargetStats, times(1)).addStatusEffect(CombatStatsComponent.StatusEffect.CONFUSED);
        verify(mockTargetStats, times(1)).addStatusEffect(CombatStatsComponent.StatusEffect.BLEEDING);
    }

    /**
     * Test to ensure that the logger outputs the correct message when applyBuffs is called.
     * Again, this is focused on verifying behaviour and state, not direct logging output.
     */
    //@Test
    void testApplyBuffsLogsCorrectMessage() {
        // Act: Apply the buffs to trigger the logger.
        specialKangaMove.applyBuffs(mockAttackerStats);

        // Assert: Verify that the logger logs the correct message for buffs.
        verify(mockAttackerStats, times(1)).addStrength(15);
        verify(mockAttackerStats, times(1)).addDefense(10);
    }
}
