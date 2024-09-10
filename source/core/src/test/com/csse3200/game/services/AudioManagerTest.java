package com.csse3200.game.services;

import static org.mockito.Mockito.*;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.ResourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(GameExtension.class)
class AudioManagerTest {

    private ResourceService resourceService;

    @BeforeEach
    void setUp() {
        // Mock the ResourceService to return mock Sound and Music assets
        resourceService = mock(ResourceService.class);
        ServiceLocator.registerResourceService(resourceService);  // Assuming ServiceLocator is a singleton
    }

    @Test
    void shouldPlaySound() {
        // Mock sound
        Sound sound = mock(Sound.class);
        when(resourceService.getAsset("soundPath", Sound.class)).thenReturn(sound);

        // Call playSound method
        AudioManager.playSound("soundPath");

        // Verify sound is played with the current sound volume
        verify(sound).play(anyFloat());
    }
}