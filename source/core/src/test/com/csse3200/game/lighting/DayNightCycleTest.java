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
@ExtendWith(MockitoExtension.class)
public class DayNightCycleTest {
    @Mock
    RayHandler rayHandler;

    @BeforeAll
    public static void setup() {
        GameTime gameTime = mock(GameTime.class);
        ServiceLocator.registerTimeSource(gameTime);
    }

    @BeforeEach
    public void resetMocks() {
        reset(rayHandler);
    }

    @Test
    public void testGetTimeOfDay() {
        float target = 0.1f;
        when(ServiceLocator.getTimeSource().getTime()).thenReturn(0L);

        DayNightCycle cycle = new DayNightCycle(rayHandler);

        when(ServiceLocator.getTimeSource().getTime()).thenReturn((long) (DayNightCycle.DAY_LENGTH * target));

        assertEquals(target, cycle.getTimeOfDay(), 1e-2);
    }

    @Test
    public void testUpdate() {
        GameTime gameTime = mock(GameTime.class);
        ServiceLocator.registerTimeSource(gameTime);

        when(ServiceLocator.getTimeSource().getTime()).thenReturn(0L);
        DayNightCycle cycle = new DayNightCycle(rayHandler);
        cycle.update();
        verify(rayHandler).setAmbientLight(DayNightCycle.keyTimes[0]);
    }

    @Test
    public void testGetTimeOfDayAtMidnight() {
        when(ServiceLocator.getTimeSource().getTime()).thenReturn(0L);

        DayNightCycle cycle = new DayNightCycle(rayHandler);

        assertEquals(0.0f, DayNightCycle.getTimeOfDay(), 1e-2);
    }

    @Test
    public void testGetTimeOfDayAtNoon() {
        when(ServiceLocator.getTimeSource().getTime()).thenReturn(DayNightCycle.DAY_LENGTH / 2);

        DayNightCycle cycle = new DayNightCycle(rayHandler);

        assertEquals(0.5f, DayNightCycle.getTimeOfDay(), 1e-2);
    }

    @Test
    public void testGetTimeOfDayCloseToDayEnd() {
        long almostEndOfDay = DayNightCycle.DAY_LENGTH - 1;
        when(ServiceLocator.getTimeSource().getTime()).thenReturn(almostEndOfDay);

        DayNightCycle cycle = new DayNightCycle(rayHandler);

        assertEquals(0.9999f, DayNightCycle.getTimeOfDay(), 1e-4);
    }

    @Test
    public void testUpdateAtMidnight() {
        when(ServiceLocator.getTimeSource().getTime()).thenReturn(0L);
        DayNightCycle cycle = new DayNightCycle(rayHandler);
        cycle.update();

        // Verifying the ambient light for midnight (first color in keyTimes)
        verify(rayHandler).setAmbientLight(DayNightCycle.keyTimes[0]);
    }

    @Test
    public void testUpdateAtNoon() {
        when(ServiceLocator.getTimeSource().getTime()).thenReturn(DayNightCycle.DAY_LENGTH / 2);
        DayNightCycle cycle = new DayNightCycle(rayHandler);
        cycle.update();

        // Verifying the ambient light at noon (middle color in keyTimes, keyTimes[4])
        verify(rayHandler).setAmbientLight(DayNightCycle.keyTimes[4]);
    }

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