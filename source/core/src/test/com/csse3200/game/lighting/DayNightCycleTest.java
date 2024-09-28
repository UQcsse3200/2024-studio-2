package com.csse3200.game.lighting;

import box2dLight.RayHandler;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        when(ServiceLocator.getTimeSource().getTime()).thenReturn(0L);
        DayNightCycle cycle = new DayNightCycle(rayHandler);
        cycle.update();
        verify(rayHandler).setAmbientLight(DayNightCycle.keyTimes[0]);
    }
}
