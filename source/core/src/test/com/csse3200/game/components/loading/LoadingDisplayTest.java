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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
public class LoadingDisplayTest {

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

}
