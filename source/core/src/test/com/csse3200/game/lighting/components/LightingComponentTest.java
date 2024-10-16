package com.csse3200.game.lighting.components;

import box2dLight.PositionalLight;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the LightingComponent class. These tests cover the behavior of attaching,
 * detaching, and updating light sources on an entity, as well as handling offsets and
 * multiple lights.
 */
@ExtendWith(MockitoExtension.class)
class LightingComponentTest {
    private Entity entity;

    private LightingComponent component;
    @Mock
    PositionalLight light;
    private static final Vector2 offset = new Vector2(1.3f, 0.2f);


    /**
     * Setup method that runs before each test. Initializes the entity and attaches a light
     * with an offset to the component.
     */
    @BeforeEach
    void setup() {
        entity = new Entity();
        entity.setPosition(0,0);
        component = new LightingComponent().attach(light, offset);
        entity.addComponent(component);
        entity.create();

    }

    /**
     * Tests that the attached light is correctly retrieved from the component.
     */
    @Test
    void testGetLights() {
        assertEquals(Collections.singletonList(light), component.getLights());
    }
    /**
     * Tests that the attached offset is correctly retrieved from the component.
     */
    @Test
    void testGetOffsets() {
        assertEquals(Collections.singletonList(offset), component.getOffsets());
    }

    /**
     * Tests that the light's position is updated correctly when the entity's position changes.
     */
    @Test
    void testUpdateLightPosition() {
        entity.setPosition(1,1);
        entity.update();
        verify(light).setPosition(entity.getCenterPosition().add(offset));
    }

    /**
     * Tests that the light is removed when the component is disposed.
     */
    @Test
    void testDisposeLight() {
        component.dispose();
        verify(light).remove(true);
    }

    /**
     * Tests attaching a light without an offset. The default offset should be Vector2.Zero.
     */
    @Test
    void testAttachLightWithoutOffset() {
        PositionalLight newLight = mock(PositionalLight.class);
        component.attach(newLight);  // No offset
        assertEquals(2, component.getLights().size());
        assertEquals(Vector2.Zero, component.getOffsets().get(1));
    }

    /**
     * Tests detaching a light and ensures that the light is removed from the component.
     */
    @Test
    void testDetachLight() {
        boolean result = component.detach(light);
        assertTrue(result, "Light should be detached successfully.");
        assertEquals(0, component.getLights().size(), "Lights list should be empty after detachment.");
    }

    /**
     * Tests attempting to detach a light that was never attached.
     * Ensures that the component's state remains unchanged.
     */
    @Test
    void testDetachNonexistentLight() {
        PositionalLight nonExistentLight = mock(PositionalLight.class);
        boolean result = component.detach(nonExistentLight);
        assertFalse(result, "Detaching a nonexistent light should return false.");
        assertEquals(1, component.getLights().size(), "Lights list should remain unchanged.");
    }

    /**
     * Tests attaching multiple lights with different offsets and ensures that both lights are
     * attached and stored correctly.
     */
    @Test
    void testAttachMultipleLights() {
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

    /**
     * Tests updating the positions of multiple attached lights to ensure their positions
     * update correctly with the entity's position changes.
     */
    @Test
    void testUpdateMultipleLightPositions() {
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

    /**
     * Tests attaching a light with a null offset. The offset should default to Vector2.Zero.
     */
    @Test
    void testAttachNullOffset() {
        PositionalLight newLight = mock(PositionalLight.class);
        component.attach(newLight, null);  // Null offset should default to zero offset
        assertEquals(2, component.getLights().size());
        assertNull(component.getOffsets().get(1));
    }

    /**
     * Tests that a LightingComponent with no attached lights or offsets behaves as expected.
     */
    @Test
    void testAttachWithoutLights() {
        LightingComponent emptyComponent = new LightingComponent();
        entity.addComponent(emptyComponent);
        entity.create();

        assertEquals(0, emptyComponent.getLights().size(), "No lights should be attached.");
        assertEquals(0, emptyComponent.getOffsets().size(), "No offsets should be attached.");
    }

    /**
     * Tests that updating an entity with no lights attached does not result in any actions.
     */
    @Test
    void testUpdateWithoutLights() {
        LightingComponent emptyComponent = new LightingComponent();
        entity.addComponent(emptyComponent);
        entity.create();

        // Ensure no update is called on lights since there are no lights attached
        entity.update();
    }

    /**
     * Tests detaching all attached lights and verifies that no lights remain after detachment.
     */
    @Test
    void testDetachAllLights() {
        PositionalLight newLight = mock(PositionalLight.class);
        component.attach(newLight);

        assertEquals(2, component.getLights().size(), "Both lights should be attached.");
        assertTrue(component.detach(light), "First light should be detached.");
        assertTrue(component.detach(newLight), "Second light should be detached.");
        assertEquals(0, component.getLights().size(), "No lights should remain after detachment.");
    }

    /**
     * Tests updating light positions after detaching some lights. Verifies that only
     * the remaining lights are updated.
     */
    @Test
    void testUpdateAfterLightDetachment() {
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

    /**
     * Tests detaching a light that has already been detached to ensure it behaves as expected.
     */
    @Test
    void testDetachLightAlreadyDetached() {
        PositionalLight newLight = mock(PositionalLight.class);
        component.attach(newLight);

        // Detach the light once
        component.detach(newLight);
        // Attempt to detach it again
        boolean result = component.detach(newLight);

        assertFalse(result, "Detaching a light that has already been detached should return false.");
        assertEquals(1, component.getLights().size(), "The original light should still be in the list.");
    }

    /**
     * Tests attaching the same light multiple times with different offsets.
     */
    @Test
    void testAddSameLightMultipleTimes() {
        PositionalLight duplicateLight = mock(PositionalLight.class);

        // Attach the same light twice with different offsets
        component.attach(duplicateLight, new Vector2(0, 0));
        component.attach(duplicateLight, new Vector2(2, 2));

        // Verify that both instances were added
        assertEquals(3, component.getLights().size(), "Three lights should be attached (including the original).");
        assertEquals(new Vector2(2, 2), component.getOffsets().get(2), "The last attached offset should be (2, 2).");
    }

    /**
     * Tests attaching and detaching all lights and verifies no lights remain afterward.
     */
    @Test
    void testAttachAndDetachAllLights() {
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

    /**
     * Tests that an empty component has no attached offsets.
     */
    @Test
    void testEmptyOffsetsList() {
        LightingComponent emptyComponent = new LightingComponent();
        entity.addComponent(emptyComponent);
        entity.create();

        // Ensure no lights or offsets are attached initially
        assertEquals(0, emptyComponent.getOffsets().size(), "No offsets should be attached.");
        assertEquals(0, emptyComponent.getLights().size(), "No lights should be attached.");
    }

    /**
     * Tests that a light with a zero offset is updated to match the entity's position exactly.
     */
    @Test
    void testAttachAndUpdateWithZeroOffset() {
        PositionalLight newLight = mock(PositionalLight.class);

        // Attach a light with zero offset
        component.attach(newLight, Vector2.Zero);

        entity.setPosition(5, 5);
        entity.update();

        // Verify the light position is updated to the entity's center without offset
        verify(newLight).setPosition(entity.getCenterPosition());
    }

    /**
     * Tests multiple updates of light positions as the entity's position changes.
     */
    @Test
    void testMultipleUpdates() {
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