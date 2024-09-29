package com.csse3200.game.lighting.components;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.PositionalLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.lighting.LightingEngine;
import com.csse3200.game.lighting.LightingService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LightingComponentTest {
    private Entity entity;

    private LightingComponent component;
    @Mock
    PositionalLight light;

    @Mock
    LightingService lightingService;

    @Mock
    LightingEngine lightingEngine;

    private final static Vector2 offset = new Vector2(1.3f, 0.2f);

    @BeforeEach
    public void setup() {
        entity = new Entity();
        entity.setPosition(0,0);
        component = new LightingComponent().attach(light, offset);
        entity.addComponent(component);
        entity.create();

    }

    @Test
    public void testGetLights() {
        assertEquals(Collections.singletonList(light), component.getLights());
    }

    @Test
    public void testGetOffsets() {
        assertEquals(Collections.singletonList(offset), component.getOffsets());
    }

    @Test
    public void testUpdateLightPosition() {
        entity.setPosition(1,1);
        entity.update();
        verify(light).setPosition(entity.getCenterPosition().add(offset));
    }


    @Test
    public void testDisposeLight() {
        component.dispose();
        verify(light).remove(true);
    }

    @Test
    public void testAttachLightWithoutOffset() {
        PositionalLight newLight = mock(PositionalLight.class);
        component.attach(newLight);  // No offset
        assertEquals(2, component.getLights().size());
        assertEquals(Vector2.Zero, component.getOffsets().get(1));
    }

    @Test
    public void testDetachLight() {
        boolean result = component.detach(light);
        assertTrue(result, "Light should be detached successfully.");
        assertEquals(0, component.getLights().size(), "Lights list should be empty after detachment.");
    }

    @Test
    public void testDetachNonexistentLight() {
        PositionalLight nonExistentLight = mock(PositionalLight.class);
        boolean result = component.detach(nonExistentLight);
        assertFalse(result, "Detaching a nonexistent light should return false.");
        assertEquals(1, component.getLights().size(), "Lights list should remain unchanged.");
    }
    @Test
    public void testAttachMultipleLights() {
        PositionalLight light1 = mock(PositionalLight.class);
        PositionalLight light2 = mock(PositionalLight.class);

        Vector2 offset1 = new Vector2(1, 0);
        Vector2 offset2 = new Vector2(0, 1);

        component.attach(light1, offset1);
        component.attach(light2, offset2);

        assertEquals(3, component.getLights().size()); // Including the initially attached light
        assertEquals(offset1, component.getOffsets().get(1));
        assertEquals(offset2, component.getOffsets().get(2));
    }

    @Test
    public void testUpdateMultipleLightPositions() {
        PositionalLight light1 = mock(PositionalLight.class);
        PositionalLight light2 = mock(PositionalLight.class);

        Vector2 offset1 = new Vector2(1, 0);
        Vector2 offset2 = new Vector2(0, 1);

        component.attach(light1, offset1);
        component.attach(light2, offset2);

        entity.setPosition(2, 2);
        entity.update();

        verify(light1).setPosition(entity.getCenterPosition().add(offset1));
        verify(light2).setPosition(entity.getCenterPosition().add(offset2));
    }


    @Test
    public void testAttachNullOffset() {
        PositionalLight newLight = mock(PositionalLight.class);
        component.attach(newLight, null);  // Null offset should default to zero offset
        assertEquals(2, component.getLights().size());
        assertEquals(null, component.getOffsets().get(1));
    }

    @Test
    public void testAttachWithoutLights() {
        LightingComponent emptyComponent = new LightingComponent();
        entity.addComponent(emptyComponent);
        entity.create();

        assertEquals(0, emptyComponent.getLights().size(), "No lights should be attached.");
        assertEquals(0, emptyComponent.getOffsets().size(), "No offsets should be attached.");
    }

    @Test
    public void testUpdateWithoutLights() {
        LightingComponent emptyComponent = new LightingComponent();
        entity.addComponent(emptyComponent);
        entity.create();

        // Ensure no update is called on lights since there are no lights attached
        entity.update();
    }

    @Test
    public void testDetachAllLights() {
        PositionalLight newLight = mock(PositionalLight.class);
        component.attach(newLight);

        assertEquals(2, component.getLights().size(), "Both lights should be attached.");
        assertTrue(component.detach(light), "First light should be detached.");
        assertTrue(component.detach(newLight), "Second light should be detached.");
        assertEquals(0, component.getLights().size(), "No lights should remain after detachment.");
    }

    @Test
    public void testUpdateAfterLightDetachment() {
        PositionalLight newLight = mock(PositionalLight.class);
        component.attach(newLight, offset);

        entity.setPosition(1, 1);
        entity.update();
        verify(light).setPosition(entity.getCenterPosition().add(offset));
        verify(newLight).setPosition(entity.getCenterPosition().add(offset));

        component.detach(light);
        entity.setPosition(2, 2);
        entity.update();

        // Ensure only the remaining light is updated
        verify(newLight, times(1)).setPosition(entity.getCenterPosition().add(offset));
        verifyNoMoreInteractions(light);
    }

    @Test
    public void testDetachLightAlreadyDetached() {
        PositionalLight newLight = mock(PositionalLight.class);
        component.attach(newLight);

        // Detach the light once
        component.detach(newLight);
        // Attempt to detach it again
        boolean result = component.detach(newLight);

        assertFalse(result, "Detaching a light that has already been detached should return false.");
        assertEquals(1, component.getLights().size(), "The original light should still be in the list.");
    }

    @Test
    public void testAddSameLightMultipleTimes() {
        PositionalLight duplicateLight = mock(PositionalLight.class);

        // Attach the same light twice with different offsets
        component.attach(duplicateLight, new Vector2(0, 0));
        component.attach(duplicateLight, new Vector2(2, 2));

        // Verify that both instances were added
        assertEquals(3, component.getLights().size(), "Three lights should be attached (including the original).");
        assertEquals(new Vector2(2, 2), component.getOffsets().get(2), "The last attached offset should be (2, 2).");
    }

    @Test
    public void testAttachAndDetachAllLights() {
        PositionalLight light1 = mock(PositionalLight.class);
        PositionalLight light2 = mock(PositionalLight.class);

        component.attach(light1, new Vector2(1, 1));
        component.attach(light2, new Vector2(2, 2));

        // Detach all lights
        component.detach(light);
        component.detach(light1);
        component.detach(light2);

        // Verify no lights remain
        assertEquals(0, component.getLights().size(), "No lights should remain after detaching all lights.");
    }

    @Test
    public void testEmptyOffsetsList() {
        LightingComponent emptyComponent = new LightingComponent();
        entity.addComponent(emptyComponent);
        entity.create();

        // Ensure no lights or offsets are attached initially
        assertEquals(0, emptyComponent.getOffsets().size(), "No offsets should be attached.");
        assertEquals(0, emptyComponent.getLights().size(), "No lights should be attached.");
    }

    @Test
    public void testAttachAndUpdateWithZeroOffset() {
        PositionalLight newLight = mock(PositionalLight.class);

        // Attach a light with zero offset
        component.attach(newLight, Vector2.Zero);

        entity.setPosition(5, 5);
        entity.update();

        // Verify the light position is updated to the entity's center without offset
        verify(newLight).setPosition(entity.getCenterPosition());
    }

    @Test
    public void testMultipleUpdates() {
        PositionalLight light1 = mock(PositionalLight.class);
        Vector2 offset1 = new Vector2(1, 1);

        component.attach(light1, offset1);

        // Move the entity multiple times and check if the light's position is updated accordingly
        entity.setPosition(3, 3);
        entity.update();
        verify(light1).setPosition(entity.getCenterPosition().add(offset1));

        entity.setPosition(6, 6);
        entity.update();
        // verify once only because it verifies the first one above
        verify(light1, times(1)).setPosition(entity.getCenterPosition().add(offset1));
    }


}
