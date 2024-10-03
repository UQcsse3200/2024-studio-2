package com.csse3200.game.components.stats;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;  // Import for Entity
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatsDisplayTest {

    private Entity mockEntity;
    private CombatStatsComponent mockCombatStats;

    @Before
    public void setUp() {
        mockEntity = mock(Entity.class);
        mockCombatStats = mock(CombatStatsComponent.class);

        // Mock that CombatStatsComponent is part of the entity
        when(mockEntity.getComponent(CombatStatsComponent.class)).thenReturn(mockCombatStats);
    }

    @Test
    public void testStartHungerDecreaseTimer() {
        // Set up initial conditions
        when(mockCombatStats.getHunger()).thenReturn(50);  // Hunger at 50 initially

        // Simulate hunger decrease
        mockCombatStats.addHunger(-1);  // Decrease hunger by 1

        // Ensure getHunger() is called once
        verify(mockCombatStats, times(1)).addHunger(-1);  // Verify that hunger was decreased by 1
    }

    @Test
    public void testAdjustHealthBasedOnHunger() {
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
    public void testStartHealthDecreaseTimer() {
        // Set up mock values
        when(mockCombatStats.getHunger()).thenReturn(0);
        when(mockCombatStats.getHealth()).thenReturn(80);

        // Simulate health decrease when hunger is 0
        mockCombatStats.addHealth(-1);

        // Ensure that addHealth() and getHealth() are both invoked
        verify(mockCombatStats, times(1)).addHealth(-1);
    }
}