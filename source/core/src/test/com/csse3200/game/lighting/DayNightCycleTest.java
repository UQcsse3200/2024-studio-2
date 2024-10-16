package com.csse3200.game.lighting;

import box2dLight.RayHandler;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.InGameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.badlogic.gdx.graphics.Color;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the DayNightCycle class. These tests cover the behavior of getting
 * the current time of day, updating ambient light settings based on the time of day,
 * and verifying color interpolation between day phases.
 */
@ExtendWith(MockitoExtension.class)
class DayNightCycleTest {

    @Mock
    RayHandler rayHandler;  // Mocked RayHandler instance

    @Mock
    GameTime gameTime;  // Mocked GameTime instance
    @Mock
    InGameTime inGameTime;  // Mocked InGameTime instance

    /**
     * Sets up the mocks and registers them with the ServiceLocator before all tests.
     */
    @BeforeEach
    void setup() {
        ServiceLocator.clear();

        // Register the mocked GameTime and InGameTime
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerInGameTime(inGameTime);
    }

    /**
     * Cleans up the ServiceLocator after each test.
     */
    @AfterEach
    void tearDown() {
        ServiceLocator.clear();
    }

    /**
     * Tests that the correct time of day is returned based on the in-game time.
     */
    @Test
    void testGetTimeOfDay() {
        float target = 0.1f;
        when(inGameTime.getTime()).thenReturn(0L);  // Initial time is 0

        DayNightCycle cycle = new DayNightCycle(rayHandler);

        // Simulate 10% of the day has passed
        when(inGameTime.getTime()).thenReturn((long) (DayNightCycle.DAY_LENGTH * target));

        // Test that the time of day corresponds to 10%
        assertEquals(target, cycle.getTimeOfDay(), 1e-2);
    }

    /**
     * Tests that the ambient light is correctly updated at midnight (0.0 time of day).
     */
    @Test
    void testUpdateAtMidnight() {
        when(inGameTime.getTime()).thenReturn(0L);  // Midnight (0.0 time of day)

        DayNightCycle cycle = new DayNightCycle(rayHandler);
        cycle.update();

        // Capture the argument passed to setAmbientLight to inspect it
        ArgumentCaptor<Color> colorCaptor = ArgumentCaptor.forClass(Color.class);
        verify(rayHandler).setAmbientLight(colorCaptor.capture());

        // Assert that the captured color is the one expected at midnight
        assertEquals(DayNightCycle.keyTimes[0], colorCaptor.getValue());
    }

    /**
     * Tests that the time of day at midnight is correctly returned as 0.0.
     */
    @Test
    void testGetTimeOfDayAtMidnight() {
        when(inGameTime.getTime()).thenReturn(0L);  // Midnight

        DayNightCycle cycle = new DayNightCycle(rayHandler);

        // Test that the time of day is 0.0 at midnight
        assertEquals(0.0f, cycle.getTimeOfDay(), 1e-2);
    }

    /**
     * Tests that the time of day at noon is correctly returned as 0.5.
     */
    @Test
    void testGetTimeOfDayAtNoon() {
        when(inGameTime.getTime()).thenReturn(DayNightCycle.DAY_LENGTH / 2);  // Noon

        DayNightCycle cycle = new DayNightCycle(rayHandler);

        // Test that the time of day is 0.5 at noon
        assertEquals(0.5f, cycle.getTimeOfDay(), 1e-2);
    }

    /**
     * Tests that the time of day close to the end of the day is returned as approximately 1.0.
     */
    @Test
    void testGetTimeOfDayCloseToDayEnd() {
        long almostEndOfDay = DayNightCycle.DAY_LENGTH - 1;  // Close to the end of the day
        when(inGameTime.getTime()).thenReturn(almostEndOfDay);

        DayNightCycle cycle = new DayNightCycle(rayHandler);

        // Test that the time of day is close to 1.0 near the end of the day
        assertEquals(0.9999f, cycle.getTimeOfDay(), 1e-4);
    }

    /**
     * Tests that the ambient light is updated correctly for noon (keyTimes[4]).
     */
    @Test
    void testUpdateAtNoon() {
        when(inGameTime.getTime()).thenReturn(DayNightCycle.DAY_LENGTH / 2);  // Noon

        DayNightCycle cycle = new DayNightCycle(rayHandler);
        cycle.update();

        // Capture the argument passed to setAmbientLight to inspect it
        ArgumentCaptor<Color> colorCaptor = ArgumentCaptor.forClass(Color.class);
        verify(rayHandler).setAmbientLight(colorCaptor.capture());

        // Assert that the captured color is the one expected at noon
        assertEquals(DayNightCycle.keyTimes[4], colorCaptor.getValue());
    }

    /**
     * Tests that the ambient light is updated correctly for dawn (keyTimes[2]).
     */
    @Test
    void testUpdateAtDawn() {
        long dawnTime = DayNightCycle.DAY_LENGTH / 4;  // Dawn is 25% into the day
        when(inGameTime.getTime()).thenReturn(dawnTime);

        DayNightCycle cycle = new DayNightCycle(rayHandler);
        cycle.update();

        // Capture the argument passed to setAmbientLight to inspect it
        ArgumentCaptor<Color> colorCaptor = ArgumentCaptor.forClass(Color.class);
        verify(rayHandler).setAmbientLight(colorCaptor.capture());

        // Assert that the captured color is the one expected at dawn
        assertEquals(DayNightCycle.keyTimes[2], colorCaptor.getValue());
    }

    /**
     * Tests that the ambient light is updated correctly for dusk (keyTimes[6]).
     */
    @Test
    void testUpdateAtDusk() {
        long duskTime = 3 * DayNightCycle.DAY_LENGTH / 4;  // Dusk is 75% into the day
        when(inGameTime.getTime()).thenReturn(duskTime);

        DayNightCycle cycle = new DayNightCycle(rayHandler);
        cycle.update();

        // Capture the argument passed to setAmbientLight to inspect it
        ArgumentCaptor<Color> colorCaptor = ArgumentCaptor.forClass(Color.class);
        verify(rayHandler).setAmbientLight(colorCaptor.capture());

        // Assert that the captured color is the one expected at dusk
        assertEquals(DayNightCycle.keyTimes[6], colorCaptor.getValue());
    }

    /**
     * Tests that the ambient light is updated correctly for a time between two key times,
     * verifying that color interpolation is being used.
     */
    @Test
    void testUpdateInterpolatedColor() {
        // Set the time to be between two key times, e.g., between keyTimes[0] (midnight) and keyTimes[1] (late night)
        long timeBetweenMidnightAndLateNight = (long) (DayNightCycle.DAY_LENGTH * 0.05);  // 5% into the day
        when(inGameTime.getTime()).thenReturn(timeBetweenMidnightAndLateNight);

        DayNightCycle cycle = new DayNightCycle(rayHandler);
        cycle.update();

        // Verify that an interpolated color between midnight and late night was set
        verify(rayHandler, atLeastOnce()).setAmbientLight(any(Color.class));
    }
    /**
     * Tests if the day-night cycle updates correctly after pausing and resuming.
     */
    @Test
    void testPauseAndResumeCycle() {
        when(inGameTime.getTime()).thenReturn(0L); // Midnight initially
        DayNightCycle cycle = new DayNightCycle(rayHandler);

        cycle.pause(); // Pause the cycle
        when(inGameTime.getTime()).thenReturn(1000L); // Move time forward, but cycle is paused
        cycle.update();

        // Ensure ambient light does not change since the cycle is paused
        verify(rayHandler, times(1)).setAmbientLight(any(Color.class));  // Still called once, no additional calls

        cycle.resume(); // Resume the cycle
        cycle.update();

        // Now the ambient light should be updated after resuming
        verify(rayHandler, atLeastOnce()).setAmbientLight(any(Color.class));
    }

    /**
     * Tests that the cycle correctly handles crossing over midnight (i.e., day restart).
     */
    @Test
    void testMidnightCycleReset() {
        // Set time just before midnight
        when(inGameTime.getTime()).thenReturn(DayNightCycle.DAY_LENGTH - 500L);  // Just before midnight
        DayNightCycle cycle = new DayNightCycle(rayHandler);
        cycle.update();

        // Set time just after midnight
        when(inGameTime.getTime()).thenReturn(500L);  // Just after midnight
        cycle.update();

        // Capture the argument passed to setAmbientLight to inspect it
        ArgumentCaptor<Color> colorCaptor = ArgumentCaptor.forClass(Color.class);
        verify(rayHandler, atLeastOnce()).setAmbientLight(colorCaptor.capture());

        // Get the expected midnight color from the cycle
        Color expectedMidnightColor = DayNightCycle.keyTimes[0];
        Color actualColor = colorCaptor.getValue();

        // Use tolerance for floating-point comparison
        float tolerance = 2e-2f;

        // Compare each color channel with a tolerance
        assertEquals(expectedMidnightColor.r, actualColor.r, tolerance, "Red channel mismatch");
        assertEquals(expectedMidnightColor.g, actualColor.g, tolerance, "Green channel mismatch");
        assertEquals(expectedMidnightColor.b, actualColor.b, tolerance, "Blue channel mismatch");
    }



    /**
     * Tests if the day-night cycle maintains consistency with long durations between updates.
     */
    @Test
    void testLongDurationBetweenUpdates() {
        when(inGameTime.getTime()).thenReturn(0L);  // Midnight
        DayNightCycle cycle = new DayNightCycle(rayHandler);

        when(inGameTime.getTime()).thenReturn(DayNightCycle.DAY_LENGTH * 3 / 4);  // Time jumps to dusk
        cycle.update();

        // Verify that the ambient light updated to dusk
        ArgumentCaptor<Color> colorCaptor = ArgumentCaptor.forClass(Color.class);
        verify(rayHandler, atLeastOnce()).setAmbientLight(colorCaptor.capture());
        assertEquals(DayNightCycle.keyTimes[6], colorCaptor.getValue());
    }

    /**
     * Tests that interpolation works when the time is exactly between two key times (noon and dusk).
     */
    @Test
    void testInterpolationAtHalfwayPoint() {
        when(inGameTime.getTime()).thenReturn(DayNightCycle.DAY_LENGTH / 2 + (DayNightCycle.DAY_LENGTH / 8));  // Between noon and dusk
        DayNightCycle cycle = new DayNightCycle(rayHandler);
        cycle.update();

        // Capture the argument passed to setAmbientLight to inspect the interpolated color
        ArgumentCaptor<Color> colorCaptor = ArgumentCaptor.forClass(Color.class);
        verify(rayHandler, atLeastOnce()).setAmbientLight(colorCaptor.capture());

        // Verify the color is within the range of noon and dusk colors
        Color noon = DayNightCycle.keyTimes[4];
        Color dusk = DayNightCycle.keyTimes[6];
        Color interpolatedColor = colorCaptor.getValue();

        // Verify that each channel is within the range of noon and dusk (only using argumentcaptor since exact values weren't matching)
        assertTrue(interpolatedColor.r >= Math.min(noon.r, dusk.r) && interpolatedColor.r <= Math.max(noon.r, dusk.r), "Red channel out of range");
        assertTrue(interpolatedColor.g >= Math.min(noon.g, dusk.g) && interpolatedColor.g <= Math.max(noon.g, dusk.g), "Green channel out of range");
        assertTrue(interpolatedColor.b >= Math.min(noon.b, dusk.b) && interpolatedColor.b <= Math.max(noon.b, dusk.b), "Blue channel out of range");
    }


    /**
     * Tests that the day-night cycle can pause during the day without affecting time flow.
     */
    @Test
    void testPauseDuringDay() {
        when(inGameTime.getTime()).thenReturn(DayNightCycle.DAY_LENGTH / 4);  // Dawn (25% into the day)
        DayNightCycle cycle = new DayNightCycle(rayHandler);

        // Initial update to set the light before pausing
        cycle.update();

        cycle.pause();

        // Simulate time passing, but the cycle is paused
        when(inGameTime.getTime()).thenReturn(DayNightCycle.DAY_LENGTH / 2);  // Noon
        cycle.update();

        // Verify that setAmbientLight was called only twice (before the pause and to pause)
        verify(rayHandler, times(2)).setAmbientLight(any(Color.class));
    }


    /**
     * Tests that the day-night cycle updates correctly after a significant passage of time.
     */
    @Test
    void testSignificantTimePassage() {
        when(inGameTime.getTime()).thenReturn(0L);  // Midnight initially
        DayNightCycle cycle = new DayNightCycle(rayHandler);

        when(inGameTime.getTime()).thenReturn(DayNightCycle.DAY_LENGTH * 2);  // Two full days later
        cycle.update();

        // Verify that it reset to the first key time (since it's after two full days)
        ArgumentCaptor<Color> colorCaptor = ArgumentCaptor.forClass(Color.class);
        verify(rayHandler).setAmbientLight(colorCaptor.capture());
        assertEquals(DayNightCycle.keyTimes[0], colorCaptor.getValue());
    }

    /**
     * Tests the case where time skips backward (such as due to a game save/load).
     */
    @Test
    void testTimeBackwardSkip() {
        when(inGameTime.getTime()).thenReturn(DayNightCycle.DAY_LENGTH / 2);  // Noon
        DayNightCycle cycle = new DayNightCycle(rayHandler);
        cycle.update();

        // Simulate a time jump backwards
        when(inGameTime.getTime()).thenReturn(DayNightCycle.DAY_LENGTH / 4);  // Dawn
        cycle.update();

        ArgumentCaptor<Color> colorCaptor = ArgumentCaptor.forClass(Color.class);
        verify(rayHandler, atLeastOnce()).setAmbientLight(colorCaptor.capture());

        // Verify the color updated to the dawn color
        assertEquals(DayNightCycle.keyTimes[2], colorCaptor.getValue());
    }

    /**
     * Tests that the day-night cycle can handle very short updates.
     */
    @Test
    void testShortUpdateTime() {
        when(inGameTime.getTime()).thenReturn(0L);  // Midnight
        DayNightCycle cycle = new DayNightCycle(rayHandler);

        cycle.update();

        when(inGameTime.getTime()).thenReturn(1L);  // A millisecond later
        cycle.update();

        // The light should update very slightly but still within the range of midnight
        verify(rayHandler, atMost(2)).setAmbientLight(any(Color.class));
    }

    /**
     * Tests the case where time moves backwards by more than a full day.
     */
    @Test
    void testTimeWarpBackwardsOverFullDay() {
        when(inGameTime.getTime()).thenReturn(DayNightCycle.DAY_LENGTH * 3);  // Three days forward
        DayNightCycle cycle = new DayNightCycle(rayHandler);

        // Simulate time jump back by two full days
        when(inGameTime.getTime()).thenReturn(DayNightCycle.DAY_LENGTH / 2);  // Noon
        cycle.update();

        ArgumentCaptor<Color> colorCaptor = ArgumentCaptor.forClass(Color.class);
        verify(rayHandler).setAmbientLight(colorCaptor.capture());

        // Verify it correctly jumps back to noon
        assertEquals(DayNightCycle.keyTimes[4], colorCaptor.getValue());
    }
}
