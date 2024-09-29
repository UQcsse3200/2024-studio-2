package com.csse3200.game.lighting;

import box2dLight.RayHandler;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.badlogic.gdx.graphics.Color;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the DayNightCycle class. These tests cover the behavior of getting
 * the current time of day, updating ambient light settings based on the time of day,
 * and verifying color interpolation between day phases.
 */
@ExtendWith(MockitoExtension.class)
public class DayNightCycleTest {

    @Mock
    RayHandler rayHandler;

    /**
     * Initializes the GameTime mock and registers it with the ServiceLocator before all tests.
     */
    @BeforeAll
    public static void setup() {
        GameTime gameTime = mock(GameTime.class);
        ServiceLocator.registerTimeSource(gameTime);
    }

    /**
     * Resets the RayHandler mock before each test to ensure a clean state.
     */
    @BeforeEach
    public void resetMocks() {
        reset(rayHandler);
    }

    /**
     * Tests that the correct time of day is returned based on the in-game time.
     */
    @Test
    public void testGetTimeOfDay() {
        float target = 0.1f;
        when(ServiceLocator.getTimeSource().getTime()).thenReturn(0L);

        DayNightCycle cycle = new DayNightCycle(rayHandler);

        when(ServiceLocator.getTimeSource().getTime()).thenReturn((long) (DayNightCycle.DAY_LENGTH * target));

        assertEquals(target, cycle.getTimeOfDay(), 1e-2);
    }

    /**
     * Tests that the ambient light is correctly updated at midnight (0.0 time of day).
     */
    @Test
    public void testUpdate() {
        GameTime gameTime = mock(GameTime.class);
        ServiceLocator.registerTimeSource(gameTime);

        when(ServiceLocator.getTimeSource().getTime()).thenReturn(0L);
        DayNightCycle cycle = new DayNightCycle(rayHandler);
        cycle.update();
        verify(rayHandler).setAmbientLight(DayNightCycle.keyTimes[0]);
    }

    /**
     * Tests that the time of day at midnight is correctly returned as 0.0.
     */
    @Test
    public void testGetTimeOfDayAtMidnight() {
        when(ServiceLocator.getTimeSource().getTime()).thenReturn(0L);

        DayNightCycle cycle = new DayNightCycle(rayHandler);

        assertEquals(0.0f, DayNightCycle.getTimeOfDay(), 1e-2);
    }

    /**
     * Tests that the time of day at noon is correctly returned as 0.5.
     */
    @Test
    public void testGetTimeOfDayAtNoon() {
        when(ServiceLocator.getTimeSource().getTime()).thenReturn(DayNightCycle.DAY_LENGTH / 2);

        DayNightCycle cycle = new DayNightCycle(rayHandler);

        assertEquals(0.5f, DayNightCycle.getTimeOfDay(), 1e-2);
    }

    /**
     * Tests that the time of day close to the end of the day is returned as approximately 1.0.
     */
    @Test
    public void testGetTimeOfDayCloseToDayEnd() {
        long almostEndOfDay = DayNightCycle.DAY_LENGTH - 1;
        when(ServiceLocator.getTimeSource().getTime()).thenReturn(almostEndOfDay);

        DayNightCycle cycle = new DayNightCycle(rayHandler);

        assertEquals(0.9999f, DayNightCycle.getTimeOfDay(), 1e-4);
    }

    /**
     * Tests that the ambient light is updated correctly for midnight (keyTimes[0]).
     */
    @Test
    public void testUpdateAtMidnight() {
        when(ServiceLocator.getTimeSource().getTime()).thenReturn(0L);
        DayNightCycle cycle = new DayNightCycle(rayHandler);
        cycle.update();

        // Verifying the ambient light for midnight (first color in keyTimes)
        verify(rayHandler).setAmbientLight(DayNightCycle.keyTimes[0]);
    }

    /**
     * Tests that the ambient light is updated correctly for noon (keyTimes[4]).
     */
    @Test
    public void testUpdateAtNoon() {
        when(ServiceLocator.getTimeSource().getTime()).thenReturn(DayNightCycle.DAY_LENGTH / 2);
        DayNightCycle cycle = new DayNightCycle(rayHandler);
        cycle.update();

        // Verifying the ambient light at noon (middle color in keyTimes, keyTimes[4])
        verify(rayHandler).setAmbientLight(DayNightCycle.keyTimes[4]);
    }

    /**
     * Tests that the ambient light is updated correctly for dawn (keyTimes[2]).
     */
    @Test
    public void testUpdateAtDawn() {
        // Dawn is 25% into the day
        long dawnTime = DayNightCycle.DAY_LENGTH / 4;
        when(ServiceLocator.getTimeSource().getTime()).thenReturn(dawnTime);
        DayNightCycle cycle = new DayNightCycle(rayHandler);
        cycle.update();

        // Verifying the ambient light at dawn (keyTimes[2])
        verify(rayHandler).setAmbientLight(DayNightCycle.keyTimes[2]);
    }

    /**
     * Tests that the ambient light is updated correctly for dusk (keyTimes[6]).
     */
    @Test
    public void testUpdateAtDusk() {
        // Dusk is approximately 75% into the day
        long duskTime = 3 * DayNightCycle.DAY_LENGTH / 4;
        when(ServiceLocator.getTimeSource().getTime()).thenReturn(duskTime);
        DayNightCycle cycle = new DayNightCycle(rayHandler);
        cycle.update();

        // Verifying the ambient light at dusk (keyTimes[6])
        verify(rayHandler).setAmbientLight(DayNightCycle.keyTimes[6]);
    }

    /**
     * Tests that the ambient light is updated correctly for a time between two key times,
     * verifying that color interpolation is being used.
     */
    @Test
    public void testUpdateInterpolatedColor() {
        // Set the time between two key times, say between keyTimes[0] (midnight) and keyTimes[1] (late night)
        long timeBetweenMidnightAndLateNight = (long) (DayNightCycle.DAY_LENGTH * 0.05); // 5% into the day
        when(ServiceLocator.getTimeSource().getTime()).thenReturn(timeBetweenMidnightAndLateNight);

        DayNightCycle cycle = new DayNightCycle(rayHandler);
        cycle.update();

        // Verifying the ambient light is set using an interpolated color between midnight and late night
        // No exact verification of the color, but ensure setAmbientLight was called
        verify(rayHandler, atLeastOnce()).setAmbientLight(any(Color.class));
    }
}
