package com.csse3200.game.components.loading;

import static org.junit.jupiter.api.Assertions.*;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
public class LoadingDisplayTest {
    private LoadingDisplay loadingDisplay;

    @Test
    public void testInitialProgress() {
        loadingDisplay = new LoadingDisplay();
        // Update the progress and verify the progress bar is updated
        assertEquals(0f, loadingDisplay.progressBar.getValue(), 0f);
    }

    @Test
    public void testUpdateProgress() {
        // Update the progress and verify the progress bar is updated
        loadingDisplay = new LoadingDisplay();
        loadingDisplay.update();
        assertEquals(0.01f, loadingDisplay.progressBar.getValue(), 0.01f);
    }

    @Test
    public void testGetZIndex() {
        // Ensure the Z index is correct
        loadingDisplay = new LoadingDisplay();
        assertEquals(2f, loadingDisplay.getZIndex(), 0.0f);
    }

}
