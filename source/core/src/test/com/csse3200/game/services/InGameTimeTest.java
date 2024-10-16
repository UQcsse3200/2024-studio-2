package com.csse3200.game.services;

import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class InGameTimeTest {

    private InGameTime inGameTime;

    @BeforeEach
    void setUp() {
        inGameTime = new InGameTime();
    }

    @Test
    void shouldStartWithZeroTime() {
        // Ensure that in-game time starts at 0 or greater
        long startTime = inGameTime.getTime();
        assertTrue(startTime >= 0, "In-game time should start at 0 or greater");
    }

    @Test
    void shouldPauseAndResumeTime() throws InterruptedException {
        // Let some time pass before pausing
        Thread.sleep(100);

        // Pause the in-game time
        inGameTime.pause();
        long pausedTime = inGameTime.getTime();

        // Simulate passage of real time while paused
        Thread.sleep(100); // In-game time should not advance during this period

        // Check that time hasn't changed while paused
        assertEquals(pausedTime, inGameTime.getTime(), "In-game time should not change while paused");

        // Resume the in-game time and let some more time pass
        inGameTime.resume();
        Thread.sleep(100);
        long resumedTime = inGameTime.getTime();

        // Ensure the time has advanced after resuming
        assertTrue(resumedTime > pausedTime, "In-game time should advance after resuming");
    }

    @Test
    void shouldNotAdvanceTimeWhenPaused() throws InterruptedException {
        inGameTime.pause();
        long timeWhenPaused = inGameTime.getTime();

        // Simulate the passage of time while paused
        Thread.sleep(200);

        // Ensure that the time does not advance while paused
        assertEquals(timeWhenPaused, inGameTime.getTime(), "Time should not advance when paused");
    }

    @Test
    void shouldAdvanceTimeWhenResumed() throws InterruptedException {
        inGameTime.pause();
        long pausedTime = inGameTime.getTime();

        // Resume and let some time pass
        inGameTime.resume();
        Thread.sleep(100);
        long resumedTime = inGameTime.getTime();

        // Ensure the time has advanced after resuming
        assertTrue(resumedTime > pausedTime, "In-game time should advance after resuming");
    }

    @Test
    void shouldHandleMultiplePausesAndResumes() throws InterruptedException {
        // Let some time pass and pause
        Thread.sleep(100);
        inGameTime.pause();
        long firstPausedTime = inGameTime.getTime();

        // Simulate more real-time passage
        Thread.sleep(100);

        // Resume and pause again
        inGameTime.resume();
        Thread.sleep(50);
        inGameTime.pause();
        long secondPausedTime = inGameTime.getTime();

        // Check that time advanced between the first and second pause
        assertTrue(secondPausedTime > firstPausedTime, "In-game time should advance between pauses");

        // Simulate real-time passage during the second pause
        Thread.sleep(100);
        assertEquals(secondPausedTime, inGameTime.getTime(), "Time should not advance during the second pause");
    }

    @Test
    void shouldProperlyCalculateElapsedTime() throws InterruptedException {
        // Let some time pass
        long initialTime = inGameTime.getTime();
        Thread.sleep(100);

        // Check that the elapsed time is increasing
        long elapsedTime = inGameTime.getTime();
        assertTrue(elapsedTime > initialTime, "Elapsed in-game time should be greater than the initial time");
    }

    @Test
    void shouldCorrectlyReportDeltaTimeAsZero() {
        // Since delta time is not managed by the InGameTime, it should return 0
        long deltaTime = inGameTime.getDeltaTime();
        assertEquals(0, deltaTime, "Delta time should always be 0");
    }

    @Test
    void shouldPauseMultipleTimesWithoutAdvancing() throws InterruptedException {
        inGameTime.pause();
        long timePausedFirst = inGameTime.getTime();

        // Simulate time passing
        Thread.sleep(100);

        // Pause again (without resuming)
        inGameTime.pause();
        long timePausedSecond = inGameTime.getTime();

        // Time should not advance
        assertEquals(timePausedFirst, timePausedSecond, "Time should not advance between consecutive pauses without resuming");
    }

    @Test
    void shouldHandleEdgeCaseOfImmediatePauseAndResume() throws InterruptedException {
        inGameTime.pause();
        long pausedTime = inGameTime.getTime();

        // Resume immediately after pausing
        inGameTime.resume();
        Thread.sleep(50);
        long resumedTime = inGameTime.getTime();

        // Ensure time advances slightly after an immediate resume
        assertTrue(resumedTime > pausedTime, "In-game time should advance slightly after immediate resume");
    }
    @Test
    void shouldHandleVeryQuickPauseAndResume() {
        inGameTime.pause();
        long pausedTime = inGameTime.getTime();

        // Resume immediately after pausing
        inGameTime.resume();
        long resumedTime = inGameTime.getTime();

        // Ensure that the time hasn't progressed significantly
        assertTrue(resumedTime >= pausedTime, "Time should not significantly progress after an immediate resume");
    }

    @Test
    void shouldNotDoubleResume() throws InterruptedException {
        inGameTime.pause();
        long pausedTime = inGameTime.getTime();

        // Resume twice and check if time progresses normally
        inGameTime.resume();
        inGameTime.resume(); // Second resume should have no effect
        Thread.sleep(100);
        long resumedTime = inGameTime.getTime();

        assertTrue(resumedTime > pausedTime, "Time should progress after resuming once");
    }

    @Test
    void shouldHandleRapidPauseResumeCycles() throws InterruptedException {
        long startTime = inGameTime.getTime();

        // Rapidly pause and resume multiple times
        for (int i = 0; i < 10; i++) {
            inGameTime.pause();
            inGameTime.resume();
        }

        Thread.sleep(100);
        long endTime = inGameTime.getTime();

        // Ensure that time progressed despite rapid pauses and resumes
        assertTrue(endTime > startTime, "Time should advance after rapid pause and resume cycles");
    }

    @Test
    void shouldAccumulatePausedTimeCorrectly() throws InterruptedException {
        long startTime = inGameTime.getTime();

        // Pause the in-game time
        inGameTime.pause();
        Thread.sleep(100); // Simulate 100ms of real-time passage

        // Resume and pause again
        inGameTime.resume();
        Thread.sleep(100); // Simulate additional 100ms of real-time passage
        inGameTime.pause();

        // Ensure that time progressed while the game was resumed
        long pausedTime = inGameTime.getTime();
        assertTrue(pausedTime > startTime, "Time should progress between resumes and pauses");
    }

    @Test
    void shouldResumeFromZeroTime() throws InterruptedException {
        // Immediately pause after starting
        inGameTime.pause();
        long initialTime = inGameTime.getTime();

        // Let some real time pass while paused
        Thread.sleep(100);

        // Resume and check if time starts advancing
        inGameTime.resume();
        Thread.sleep(100);
        long resumedTime = inGameTime.getTime();

        // Ensure the time progresses after resuming
        assertTrue(resumedTime > initialTime, "Time should progress after resuming from zero time");
    }

    @Test
    void shouldReturnToCorrectTimeAfterResume() throws InterruptedException {
        // Let some time pass and then pause
        Thread.sleep(100);
        inGameTime.pause();
        long pausedTime = inGameTime.getTime();

        // Let more real time pass
        Thread.sleep(100);

        // Resume and allow time to pass
        inGameTime.resume();
        Thread.sleep(100);
        long resumedTime = inGameTime.getTime();

        // Ensure that resumed time is correct after resuming
        assertTrue(resumedTime > pausedTime, "Time should continue after resuming");
    }

    @Test
    void shouldHandleLongPauseDurations() throws InterruptedException {
        // Pause the game for a long duration (e.g., 500ms)
        inGameTime.pause();
        long pausedTime = inGameTime.getTime();
        Thread.sleep(500);

        // Resume and let time pass
        inGameTime.resume();
        Thread.sleep(100);
        long resumedTime = inGameTime.getTime();

        // Ensure time advances correctly after long pause
        assertTrue(resumedTime > pausedTime, "Time should advance after a long pause");
    }

    @Test
    void shouldAccumulateTimeAfterMultiplePausesAndResumes() throws InterruptedException {
        // Let some time pass and pause
        Thread.sleep(100);
        inGameTime.pause();
        long firstPausedTime = inGameTime.getTime();

        // Let some real time pass, resume and pause again
        Thread.sleep(100);
        inGameTime.resume();
        Thread.sleep(100);
        inGameTime.pause();
        long secondPausedTime = inGameTime.getTime();

        // Ensure that time accumulated between pauses
        assertTrue(secondPausedTime > firstPausedTime, "Time should accumulate after multiple pauses and resumes");
    }

    @Test
    void shouldCorrectlyHandlePauseDuringRealTimeSimulatedDelay() throws InterruptedException {
        long startTime = inGameTime.getTime();

        // Simulate delay during the game while paused
        inGameTime.pause();
        Thread.sleep(200);  // Simulate real time delay

        // Resume and let some time pass
        inGameTime.resume();
        Thread.sleep(100);
        long finalTime = inGameTime.getTime();

        // Ensure the time progressed correctly after delay
        assertTrue(finalTime > startTime, "Time should correctly progress after delay");
    }

    @Test
    void shouldNotCrashWhenResumingWithoutPausing() throws InterruptedException {
        // Resume without pausing and ensure no crash
        inGameTime.resume();
        long resumedTime = inGameTime.getTime();

        // Let some time pass and check if time progressed
        Thread.sleep(100);
        long newTime = inGameTime.getTime();

        assertTrue(newTime > resumedTime, "Time should progress even after resuming without pausing first");
    }
}
