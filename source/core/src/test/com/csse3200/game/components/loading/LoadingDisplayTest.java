package com.csse3200.game.components.loading;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.rendering.RenderService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class LoadingDisplayTest {

    @Mock
    private ResourceService resourceService;

    @Mock
    private Stage stage;

    @Mock
    private Batch batch;

    @Mock
    private RenderService renderService;  // Mock the RenderService

    private GameTime gameTime;
    private LoadingDisplay loadingDisplay;

    @BeforeAll
    static void beforeAll() {
        // Mock Gdx.graphics so that getDeltaTime() can be controlled
        Gdx.graphics = mock(Graphics.class);
        when(Gdx.graphics.getDeltaTime()).thenReturn(10f);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize GameTime and register it with the ServiceLocator
        gameTime = new GameTime();
        ServiceLocator.registerTimeSource(gameTime);

        // Ensure RenderService returns the mocked stage
        when(renderService.getStage()).thenReturn(stage);

        // Register the mock ResourceService and RenderService
        ServiceLocator.registerResourceService(resourceService);
        ServiceLocator.registerRenderService(renderService);  // Register the RenderService

        // Create and set up LoadingDisplay
        loadingDisplay = new LoadingDisplay();
        loadingDisplay.create();
    }

    @Test
    void shouldDisplayLoadingMessages() {
        // Get the list of loading messages from the LoadingDisplay instance
        List<String> loadingMessages = loadingDisplay.getAllMessages();

        // Set the mock delta time
        when(Gdx.graphics.getDeltaTime()).thenReturn(1f);

        // Update the loading display and get the current message
        loadingDisplay.update();
        String currentMessage = loadingDisplay.getCurrentMessage();

        // Assert that the current message is in the list of loading messages
        assertTrue(loadingMessages.contains(currentMessage), "The current message should be in the list of loading messages");
    }



    @Test
    void shouldUpdateMoonProgress() {
        // Set up mock progress for ResourceService (assuming 100% resource progress)
        when(resourceService.getProgress()).thenReturn((int) 100f);  // Mock full resource progress

        // Simulate time passing for 3 frames (3 seconds)
        when(Gdx.graphics.getDeltaTime()).thenReturn(1f);  // Each update represents 1 second
        for (int i = 0; i < 3; i++) {
            loadingDisplay.update();  // Call update three times to simulate 3 seconds
        }

        // Check that the moon actor's progress is updated
        MoonActor moonActor = loadingDisplay.getMoonActor();
        assertEquals(0.5f, moonActor.getProgress(), 0.01f);  // After 3/6 seconds, moon should be 50% filled
    }




    @Test
    void shouldFinishLoadingAfterDuration() {
        // Set mock delta time for multiple updates
        when(Gdx.graphics.getDeltaTime()).thenReturn(1f);  // Each update simulates 1 second passing

        // Simulate time passing over multiple frames
        for (int i = 0; i < 6; i++) {
            loadingDisplay.update();
        }

        // After the duration, loading should be finished
        assertTrue(loadingDisplay.isLoadingFinished());
    }

    @Test
    void shouldBlendMoonIntoBackground() {
        // Test the moon actor's opacity
        MoonActor moonActor = loadingDisplay.getMoonActor();

        // Set opacity and verify it's applied correctly
        moonActor.setOpacity(0.7f);
        assertEquals(0.7f, moonActor.getOpacity());

        // Verify moon actor is added to the stage with the correct opacity
        verify(stage, atLeastOnce()).addActor(moonActor);
    }
    @Test
    void shouldNotRepeatMessagesConsecutively() {
        when(Gdx.graphics.getDeltaTime()).thenReturn(2f);  // Each update represents 2 seconds
        String firstMessage = loadingDisplay.getCurrentMessage();

        for (int i = 0; i < 5; i++) {  // Update multiple times to check message cycling
            loadingDisplay.update();
            String newMessage = loadingDisplay.getCurrentMessage();
            assertNotEquals(firstMessage, newMessage, "Loading messages should not repeat consecutively");
            firstMessage = newMessage;  // Update the reference for the next iteration
        }
    }

    @Test
    void shouldDisplayAllLoadingMessagesOverTime() {
        // Ensure that all loading messages are eventually displayed
        Set<String> displayedMessages = new HashSet<>();

        // Simulate enough time passing for all messages to potentially change
        int totalMessages = loadingDisplay.getAllMessages().size();
        int maxIterations = totalMessages * 2;  // Ensure enough iterations for message changes

        // Simulate time passing
        when(Gdx.graphics.getDeltaTime()).thenReturn(2f);  // Each update simulates 2 seconds

        for (int i = 0; i < maxIterations; i++) {
            loadingDisplay.update();
            String currentMessage = loadingDisplay.getCurrentMessage();
            displayedMessages.add(currentMessage);

            // Stop early if all messages have been displayed
            if (displayedMessages.size() == totalMessages) {
                break;
            }
        }

        // Ensure all messages have been displayed at least once
        assertEquals(totalMessages, displayedMessages.size(),
                "All messages should be displayed at least once");
    }

    @Test
    void shouldNotExceedFullProgress() {
        // Simulate maximum progress
        when(resourceService.getProgress()).thenReturn((int) 100f);  // Mock progress at 100%

        // Simulate time passing beyond the loading duration
        when(Gdx.graphics.getDeltaTime()).thenReturn(1f);  // Simulate 1 second per frame
        for (int i = 0; i < 10; i++) {
            loadingDisplay.update();
        }

        // Verify moon progress doesn't exceed 100%
        assertEquals(1f, loadingDisplay.getMoonActor().getProgress(), 0.01f, "Progress should not exceed 100%");
    }

    @Test
    void shouldStartWithZeroProgress() {
        // Simulate initial state with 0 progress
        when(resourceService.getProgress()).thenReturn((int) 0f);  // Mock progress at 0%

        // No time has passed
        when(Gdx.graphics.getDeltaTime()).thenReturn(0f);
        loadingDisplay.update();

        // Check that the progress starts at 0
        assertEquals(0f, loadingDisplay.getMoonActor().getProgress(), "Progress should start at 0%");
    }

    @Test
    void moonActorShouldDisplayFullWhenProgressIsComplete() {
        MoonActor moonActor = loadingDisplay.getMoonActor();
        moonActor.setProgress(1f);  // Set moon to full progress

        // Simulate a frame
        moonActor.act(1f);

        // Verify that the moon's progress is 100%
        assertEquals(1f, moonActor.getProgress(), "Moon should be fully visible when progress is complete");
    }

    @Test
    void moonActorShouldClampProgressAtZero() {
        MoonActor moonActor = loadingDisplay.getMoonActor();
        moonActor.setProgress(-0.5f);  // Set negative progress

        // Simulate a frame
        moonActor.act(1f);

        // Verify that the progress is clamped at 0
        assertEquals(0f, moonActor.getProgress(), "Moon progress should not be negative");
    }

    @Test
    void moonActorShouldClampProgressAtMax() {
        MoonActor moonActor = loadingDisplay.getMoonActor();
        moonActor.setProgress(1.5f);  // Set progress greater than 100%

        // Simulate a frame
        moonActor.act(1f);

        // Verify that the progress is clamped at 100%
        assertEquals(1f, moonActor.getProgress(), "Moon progress should not exceed 100%");
    }

    @Test
    void moonActorShouldClampOpacityBetweenZeroAndOne() {
        MoonActor moonActor = loadingDisplay.getMoonActor();

        // Set opacity below 0 and check
        moonActor.setOpacity(-1f);
        assertEquals(0f, moonActor.getOpacity(), "Opacity should be clamped to 0 when set below 0");

        // Set opacity above 1 and check
        moonActor.setOpacity(1.5f);
        assertEquals(1f, moonActor.getOpacity(), "Opacity should be clamped to 1 when set above 1");
    }

    @Test
    void shouldReflectResourceServiceProgressInLoadingCompletion() {
        when(Gdx.graphics.getDeltaTime()).thenReturn(3f);  // Each update simulates 3 seconds
        when(resourceService.getProgress()).thenReturn(50);  // 50% resource load progress

        loadingDisplay.update();  // Simulate one update cycle

        assertFalse(loadingDisplay.isLoadingFinished(), "Loading should not be finished at 50% resource progress");
        assertEquals(0.5f, loadingDisplay.getMoonActor().getProgress(), 0.01f, "Moon actor progress should match resource progress");
    }
    ///@Test
    //void shouldDisposeOfAllResourcesAndActors() {
    //    loadingDisplay.dispose();
    //
    //    verify(stage, times(1)).clear();  // Check that stage.clear() is called
    //}
    @Test
    void shouldHandleNullMoonActor() {
        // Set moon actor to null and call update
        loadingDisplay = new LoadingDisplay() {
            @Override
            public MoonActor getMoonActor() {
                return null;  // Simulate a scenario where the MoonActor is null
            }
        };
        loadingDisplay.create();

        // Ensure update does not throw an exception
        assertDoesNotThrow(() -> loadingDisplay.update(), "Updating should not throw an exception even if MoonActor is null");
    }

    @Test
    void shouldStopLoadingWhenFinished() {
        // Set mock delta time for multiple updates
        when(Gdx.graphics.getDeltaTime()).thenReturn(1f);  // Each update simulates 1 second passing

        // Simulate time passing over multiple frames
        for (int i = 0; i < 6; i++) {
            loadingDisplay.update();
        }

        // After the duration, loading should be finished
        assertTrue(loadingDisplay.isLoadingFinished(), "Loading should be marked as finished after duration");

        // Verify that moon actor does not update beyond finished state
        float progressBefore = loadingDisplay.getMoonActor().getProgress();
        loadingDisplay.update();
        float progressAfter = loadingDisplay.getMoonActor().getProgress();

        assertEquals(progressBefore, progressAfter, "Progress should not change after loading is finished");
    }


    @Test
    void shouldStopMessageCyclingAfterLoadingFinishes() {
        // Simulate maximum progress
        when(resourceService.getProgress()).thenReturn((int) 100f);  // Mock progress at 100%

        // Simulate time passing beyond the loading duration
        when(Gdx.graphics.getDeltaTime()).thenReturn(1f);  // Simulate 1 second per frame
        for (int i = 0; i < 10; i++) {
            loadingDisplay.update();
        }

        // Check that loading messages no longer change after loading finishes
        String messageBefore = loadingDisplay.getCurrentMessage();
        loadingDisplay.update();
        String messageAfter = loadingDisplay.getCurrentMessage();

        assertEquals(messageBefore, messageAfter, "Loading messages should stop changing after loading finishes");
    }

    @Test
    void shouldMaintainCorrectProgressWhenResourceServiceProgressIsLow() {
        // Simulate very low progress from the resource service
        when(resourceService.getProgress()).thenReturn((int) 10f);  // 10% resource load progress

        // Simulate time passing
        when(Gdx.graphics.getDeltaTime()).thenReturn(1f);
        loadingDisplay.update();

        // Verify the moon actor's progress reflects resource progress
        assertEquals(0.1f, loadingDisplay.getMoonActor().getProgress(), 0.01f, "Moon actor progress should reflect 10% resource progress");
    }

    @Test
    void shouldReshuffleMessagesWhenEndOfListIsReached() {
        // Set the mock delta time to 2 seconds to trigger message change
        when(Gdx.graphics.getDeltaTime()).thenReturn(2f);

        // Get the original message order
        List<String> originalMessages = new LinkedList<>(loadingDisplay.getAllMessages());

        // Simulate enough time to reach the end of the message list
        int totalMessages = originalMessages.size();
        for (int i = 0; i < totalMessages; i++) {
            loadingDisplay.update();
        }

        List<String> shuffledMessages = new LinkedList<>();
        // After going through all messages, the message list should be reshuffled
        for (int i = 0; i < totalMessages; i++) {
            shuffledMessages.add(loadingDisplay.getCurrentMessage());
            loadingDisplay.update();
        }
        assertNotEquals(originalMessages, shuffledMessages, "Messages should be reshuffled after reaching the end of the list");
    }

    @Test
    void shouldClampProgressAtZeroWhenElapsedTimeIsNegative() {
        // Simulate negative elapsed time
        when(Gdx.graphics.getDeltaTime()).thenReturn(-1f);

        loadingDisplay.update();

        // Check that the progress is clamped at 0
        assertEquals(0f, loadingDisplay.getMoonActor().getProgress(), "Progress should be clamped to 0 when elapsed time is negative");
    }

    @Test
    void shouldDisplayMessageEvenWhenProgressIsZero() {
        // Simulate zero progress
        when(resourceService.getProgress()).thenReturn(0);

        // Update loading display
        loadingDisplay.update();

        // Assert that a message is still being displayed
        assertNotNull(loadingDisplay.getCurrentMessage(), "A message should be displayed even when progress is zero");
    }

    @Test
    void shouldUpdateMoonActorWhenLoadingStarts() {
        // Set initial state with no time passed
        when(Gdx.graphics.getDeltaTime()).thenReturn(0f);
        loadingDisplay.update();

        // Check that the moon actor starts with zero progress
        assertEquals(0f, loadingDisplay.getMoonActor().getProgress(), "Moon actor should start with zero progress when loading starts");
    }

    @Test
    void shouldCycleThroughMessagesInCorrectInterval() {
        // Simulate delta time to trigger message updates
        when(Gdx.graphics.getDeltaTime()).thenReturn(2f);

        // Track the first message
        String firstMessage = loadingDisplay.getCurrentMessage();

        // Update the loading display to move to the next message
        loadingDisplay.update();
        String secondMessage = loadingDisplay.getCurrentMessage();

        // Assert that the second message is different from the first
        assertNotEquals(firstMessage, secondMessage, "Loading display should cycle to a different message after the interval");
    }

    @Test
    void shouldNotExceedOpacityLimitsForMoonActor() {
        MoonActor moonActor = loadingDisplay.getMoonActor();

        // Set and assert opacity below 0
        moonActor.setOpacity(-0.5f);
        assertEquals(0f, moonActor.getOpacity(), "Opacity should not be less than 0");

        // Set and assert opacity above 1
        moonActor.setOpacity(1.5f);
        assertEquals(1f, moonActor.getOpacity(), "Opacity should not exceed 1");
    }

    @Test
    void shouldNotExceedFullProgressEvenAfterMaxDuration() {
        // Simulate time passing beyond the loading duration
        when(Gdx.graphics.getDeltaTime()).thenReturn(7f);  // Simulate 7 seconds, more than loading duration

        // Simulate resource progress is also at 100%
        when(resourceService.getProgress()).thenReturn(100);

        loadingDisplay.update();

        // Assert that progress does not exceed 100%
        assertEquals(1f, loadingDisplay.getMoonActor().getProgress(), 0.01f,
                "Progress should not exceed 100%");
    }

    @Test
    void shouldReflectResourceProgressInMoonActor() {
        // Simulate resource loading progress at 50%
        when(resourceService.getProgress()).thenReturn(50);  // 50% resource progress

        // Simulate 1 second of delta time (out of 6 seconds total loading duration)
        when(Gdx.graphics.getDeltaTime()).thenReturn(1f);

        // Simulate the update for a few frames to allow both time and resource progress to factor in
        for (int i = 0; i < 3; i++) {
            loadingDisplay.update();
        }

        // Assert that the moon actor reflects 50% resource progress
        assertEquals(0.5f, loadingDisplay.getMoonActor().getProgress(), 0.01f,
                "Moon actor progress should reflect resource loading progress");
    }

    @Test
    void shouldCycleMessagesEvenWhenNoResourcesLoaded() {
        // Simulate zero resource loading progress
        when(resourceService.getProgress()).thenReturn(0);

        // Simulate delta time to trigger message updates
        when(Gdx.graphics.getDeltaTime()).thenReturn(2f);

        // Track the first message
        String firstMessage = loadingDisplay.getCurrentMessage();

        // Update the loading display to move to the next message
        loadingDisplay.update();
        String secondMessage = loadingDisplay.getCurrentMessage();

        // Assert that the second message is different from the first
        assertNotEquals(firstMessage, secondMessage, "Loading display should cycle to a different message even when no resources are loaded");
    }

}

