package com.csse3200.game.lighting;

import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
public class LightingServiceTest {
    @Test
    void shouldGetSetEngine() {
        LightingEngine engine = mock(LightingEngine.class);
        LightingService service = new LightingService(engine);
        assertEquals(engine, service.getLighting());
    }
}