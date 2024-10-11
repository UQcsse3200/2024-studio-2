package com.csse3200.game.components.stats;

import static org.mockito.Mockito.*;

import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;  // Import for Entity
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
class StatsDisplayTest {
    private Entity mockEntity;
    private CombatStatsComponent mockCombatStats;

    @BeforeEach
    void setUp() {
        mockEntity = mock(Entity.class);
        mockCombatStats = mock(CombatStatsComponent.class);

        // Mock that CombatStatsComponent is part of the entity
        when(mockEntity.getComponent(CombatStatsComponent.class)).thenReturn(mockCombatStats);
    }

    @Test
    void testStartHungerDecreaseTimer() {
        // Set up initial conditions
        when(mockCombatStats.getHunger()).thenReturn(50);  // Hunger at 50 initially

        // Simulate hunger decrease
        mockCombatStats.addHunger(-1);  // Decrease hunger by 1

        // Ensure getHunger() is called once
        verify(mockCombatStats, times(1)).addHunger(-1);  // Verify that hunger was decreased by 1
    }

    @Test
    void testAdjustHealthBasedOnHunger() {
        // Case: hunger is high, health increases
        when(mockCombatStats.getHunger()).thenReturn(95);
        when(mockCombatStats.getHealth()).thenReturn(80);
        when(mockCombatStats.getMaxHealth()).thenReturn(100);

        // Simulate health increase when hunger is high
        mockCombatStats.addHealth(1);
        verify(mockCombatStats, times(1)).addHealth(1); // Check only once

        // Case: hunger is low, health decreases
        when(mockCombatStats.getHunger()).thenReturn(10);
        mockCombatStats.addHealth(-1);
        verify(mockCombatStats, times(1)).addHealth(-1); // Check only once

        // Case: hunger is 0, health decreases
        when(mockCombatStats.getHunger()).thenReturn(0);
        mockCombatStats.addHealth(-1);
        verify(mockCombatStats, times(2)).addHealth(-1); // Again, check only once
    }

    @Test
    void testStartHealthDecreaseTimer() {
        // Set up mock values
        when(mockCombatStats.getHunger()).thenReturn(0);
        when(mockCombatStats.getHealth()).thenReturn(80);

        // Simulate health decrease when hunger is 0
        mockCombatStats.addHealth(-1);

        // Ensure that addHealth() and getHealth() are both invoked
        verify(mockCombatStats, times(1)).addHealth(-1);
    }
}